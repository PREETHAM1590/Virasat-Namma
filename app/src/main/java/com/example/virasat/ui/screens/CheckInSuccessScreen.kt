package com.example.virasat.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.data.source.KarnatakaSites
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatGold
import com.example.virasat.ui.theme.VirasatMaroon
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInSuccessScreen(
    siteId: String,
    onViewSite: () -> Unit,
    onViewPassport: () -> Unit,
    onShare: () -> Unit
) {
    val site = KarnatakaSites.allSites.find { it.id == siteId } ?: return

    var showConfetti by remember { mutableStateOf(false) }
    var showBadge by remember { mutableStateOf(false) }
    val scaleAnim = remember { Animatable(0f) }
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        showConfetti = true
        scaleAnim.animateTo(1f, spring(dampingRatio = 0.5f, stiffness = 300f))
        alphaAnim.animateTo(1f, tween(800))
        delay(600)
        showBadge = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Check-In Successful") },
                navigationIcon = {
                    IconButton(onClick = onViewSite) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = VirasatCream)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VirasatCream)
                .padding(padding)
        ) {
            if (showConfetti) {
                ConfettiOverlay()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .scale(scaleAnim.value)
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        null,
                        modifier = Modifier.size(80.dp),
                        tint = Color(0xFF2E7D32)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Checked In!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = VirasatMaroon
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "You have successfully checked in at ${site.name}.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = alphaAnim.value > 0.5f,
                    enter = fadeIn(tween(600)) + slideInVertically(tween(600)) { it / 2 }
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(VirasatGold.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.EmojiEvents,
                                    null,
                                    tint = VirasatGold,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    "+250 XP Earned!",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VirasatMaroon
                                )
                                Text(
                                    "Keep exploring to level up",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = showBadge,
                    enter = fadeIn(tween(500)) + expandVertically(tween(500))
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(VirasatMaroon.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.LockOpen,
                                    null,
                                    tint = VirasatMaroon,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    "Badge Unlocked!",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VirasatMaroon
                                )
                                Text(
                                    "Heritage Hunter — Visit 5 different sites",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                if (site.facts.any { it.isUnlocked }) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LockOpen, null, tint = VirasatGold)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Hidden fact unlocked!",
                                color = VirasatMaroon,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onViewPassport,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VirasatMaroon),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Bookmark, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Passport", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onShare,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Share, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share Check-In")
                }
            }
        }
    }
}

@Composable
fun ConfettiOverlay() {
    val colors = listOf(VirasatGold, VirasatMaroon, Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFE91E63))
    val particles = remember {
        List(30) {
            ConfettiParticle(
                x = (0..100).random() / 100f,
                delay = (0..800).random(),
                color = colors.random(),
                size = (6..14).random().dp,
                speed = (2..5).random()
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val infiniteTransition = rememberInfiniteTransition(label = "confetti")
            val yOffset by infiniteTransition.animateFloat(
                initialValue = -0.1f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 3000 / particle.speed,
                        delayMillis = particle.delay,
                        easing = LinearEasing
                    )
                ),
                label = "y"
            )
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing)
                ),
                label = "rot"
            )
            val xDrift by infiniteTransition.animateFloat(
                initialValue = -20f,
                targetValue = 20f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "drift"
            )

            Box(
                modifier = Modifier
                    .offset(
                        x = (particle.x * 360).dp + xDrift.dp,
                        y = (yOffset * 700).dp
                    )
                    .size(particle.size)
                    .background(particle.color, RoundedCornerShape(2.dp))
            )
        }
    }
}

data class ConfettiParticle(
    val x: Float,
    val delay: Int,
    val color: Color,
    val size: androidx.compose.ui.unit.Dp,
    val speed: Int
)
