package com.example.virasat.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virasat.viewmodel.QrScannerViewModel
import com.example.virasat.R
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.ExperimentalMaterial3Api
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(
    onBack: () -> Unit,
    onNavigateToSite: (String) -> Unit,
    viewModel: QrScannerViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = context as androidx.lifecycle.LifecycleOwner
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    val scannedSite by viewModel.scannedSite.collectAsState()
    val hasCheckedIn by viewModel.hasCheckedIn.collectAsState()
    val isCheckingIn by viewModel.isCheckingIn.collectAsState()
    val unlockedFact by viewModel.unlockedFact.collectAsState()
    val qrError by viewModel.qrError.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    // Show error snackbar on unrecognized/invalid QR; clear after shown so same error re-fires
    LaunchedEffect(qrError) {
        if (qrError != null) {
            snackbarHostState.showSnackbar(qrError!!)
            viewModel.clearQrError()
        }
    }

    // Navigate to site detail as soon as a site is scanned (no check-in tap required).
    // Check-in happens automatically in the background.
    LaunchedEffect(scannedSite) {
        val site = scannedSite ?: return@LaunchedEffect
        // Auto check-in
        viewModel.checkIn()
    }

    LaunchedEffect(hasCheckedIn, isCheckingIn) {
        if (hasCheckedIn && !isCheckingIn && scannedSite != null) {
            onNavigateToSite(scannedSite!!.id)
        }
    }

    val cs = MaterialTheme.colorScheme
    val type = MaterialTheme.typography

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = cs.inverseSurface
    ) { innerPadding ->
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.inverseSurface)
            .padding(innerPadding)
    ) {
        if (hasCameraPermission) {
            CameraPreviewWithScanner(
                lifecycleOwner = lifecycleOwner,
                // analysisActive is true only while no site has been scanned yet;
                // setting it to false after a scan and back to true on reset lets
                // the analyser resume without duplicate detections.
                analysisActive = scannedSite == null,
                onQrDetected = { qrValue ->
                    viewModel.processQrCode(qrValue)
                }
            )

            // Blurred overlay with cutout (simulated via layered boxes)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(cs.inverseSurface.copy(alpha = 0.4f))
            ) {
                // Transparent center cutout - achieved by covering edges only
                // Top bar area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(cs.inverseSurface.copy(alpha = 0.4f))
                )
                // Bottom area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .align(Alignment.BottomCenter)
                        .background(cs.inverseSurface.copy(alpha = 0.4f))
                )
                // Left area
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(48.dp)
                        .align(Alignment.CenterStart)
                        .padding(top = 120.dp, bottom = 120.dp)
                        .background(cs.inverseSurface.copy(alpha = 0.4f))
                )
                // Right area
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(48.dp)
                        .align(Alignment.CenterEnd)
                        .padding(top = 120.dp, bottom = 120.dp)
                        .background(cs.inverseSurface.copy(alpha = 0.4f))
                )
            }

            // Viewfinder reticle frame
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.size(280.dp)) {
                    // Top left bracket
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(topStart = 32.dp))
                            .background(Color.Transparent)
                            .padding(top = 0.dp, start = 0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(4.dp)
                                .background(cs.primary)
                                .align(Alignment.TopStart)
                        )
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .fillMaxHeight(0.7f)
                                .background(cs.primary)
                                .align(Alignment.TopStart)
                        )
                    }
                    // Top right bracket
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(4.dp)
                                .background(cs.primary)
                                .align(Alignment.TopEnd)
                        )
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .fillMaxHeight(0.7f)
                                .background(cs.primary)
                                .align(Alignment.TopEnd)
                        )
                    }
                    // Bottom left bracket
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(4.dp)
                                .background(cs.primary)
                                .align(Alignment.BottomStart)
                        )
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .fillMaxHeight(0.7f)
                                .background(cs.primary)
                                .align(Alignment.BottomStart)
                        )
                    }
                    // Bottom right bracket
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(4.dp)
                                .background(cs.primary)
                                .align(Alignment.BottomEnd)
                        )
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .fillMaxHeight(0.7f)
                                .background(cs.primary)
                                .align(Alignment.BottomEnd)
                        )
                    }
                    // Scanning line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(2.dp)
                            .background(cs.primary.copy(alpha = 0.8f))
                            .align(Alignment.Center)
                    )
                }
            }

            // TopAppBar overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(cs.surfaceContainerLowest.copy(alpha = 0.8f))
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = cs.primary
                    )
                }
                Text(
                    "Virasat",
                    style = type.headlineLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = cs.surfaceContainerLowest
                )
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(cs.surfaceContainerLowest.copy(alpha = 0.8f))
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "QR",
                        tint = cs.primary
                    )
                }
            }

            // Floating bottom pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = 24.dp, end = 24.dp, bottom = 64.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(999.dp))
                        .background(cs.surfaceContainerLowest)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(cs.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = cs.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        "Scan QR to unlock history",
                        style = type.labelMedium.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 0.05.sp),
                        color = cs.onSurface
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                    Text(stringResource(R.string.camera_permission_required),
                        style = type.bodyLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { launcher.launch(Manifest.permission.CAMERA) },
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primaryContainer,
                            contentColor = cs.onPrimaryContainer
                        )
                    ) {
                        Text(stringResource(R.string.grant_permission), style = type.labelLarge)
                    }
            }
        }

        // Result Bottom Sheet
        scannedSite?.let { site ->
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(cs.surfaceContainerLowest)
                    .padding(28.dp)
            ) {
                Text(
                    site.name,
                    style = type.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = cs.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    site.location,
                    style = type.bodyLarge,
                    color = cs.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (hasCheckedIn) {
                    Button(
                        onClick = { onNavigateToSite(site.id) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primaryContainer,
                            contentColor = cs.onPrimaryContainer
                        )
                    ) {
                        Text(stringResource(R.string.view_site_details), style = type.labelLarge)
                    }
                } else {
                    Button(
                        onClick = { viewModel.checkIn() },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primaryContainer,
                            contentColor = cs.onPrimaryContainer
                        ),
                        enabled = !isCheckingIn
                    ) {
                        if (isCheckingIn) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = cs.onPrimaryContainer
                            )
                        } else {
                            Text(stringResource(R.string.check_in_here), style = type.labelLarge)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = { viewModel.reset() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Scan Another QR",
                        style = type.labelLarge,
                        color = cs.onSurfaceVariant
                    )
                }
            }
        }
    } // end Box
    } // end Scaffold
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun CameraPreviewWithScanner(
    lifecycleOwner: LifecycleOwner,
    analysisActive: Boolean = true,
    onQrDetected: (String) -> Unit
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    DisposableEffect(Unit) {
        onDispose { cameraExecutor.shutdown() }
    }
    val barcodeScanner = remember {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )
    }
    // AtomicBoolean flag: set to true after the first valid barcode is detected
    // to prevent duplicate processQrCode() calls from subsequent frames.
    val analysisPaused = remember { AtomicBoolean(false) }

    // Reset the pause flag whenever the caller signals analysis should resume
    // (e.g. after the user taps "Scan Another QR" and scannedSite is cleared).
    LaunchedEffect(analysisActive) {
        if (analysisActive) analysisPaused.set(false)
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )

    LaunchedEffect(previewView) {
        val cameraProvider = withContext(Dispatchers.IO) {
            ProcessCameraProvider.getInstance(context).get()
        }

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysis ->
                analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                    // Skip processing if analysis has been paused after first detection
                    if (analysisPaused.get()) {
                        imageProxy.close()
                        return@setAnalyzer
                    }
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )
                        barcodeScanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                barcodes.firstOrNull()?.rawValue?.let { value ->
                                    // Pause analysis before calling back to prevent
                                    // duplicate scans from subsequent frames.
                                    if (analysisPaused.compareAndSet(false, true)) {
                                        onQrDetected(value)
                                    }
                                }
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    } else {
                        imageProxy.close()
                    }
                }
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (_: Exception) { }
    }
}
