package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.ui.theme.PlusJakartaSans
import com.example.virasat.ui.theme.BeVietnamPro
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DATE_FMT = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

@Composable
fun MyCheckInsScreen(
    onBack: () -> Unit,
    onSiteClick: (String) -> Unit
) {
    val ctx = LocalContext.current
    val repo = remember(ctx) { RepositoryProvider.getRepository(ctx) }
    val rawCheckIns by repo.getAllCheckIns().collectAsState(initial = emptyList())
    // Map Room CheckIn → display model. Most recent first.
    val checkIns = remember(rawCheckIns) {
        val sevenDaysMs = 7L * 24 * 3600 * 1000
        val now = System.currentTimeMillis()
        rawCheckIns
            .sortedByDescending { it.timestamp }
            .map { ci ->
                CheckInEntry(
                    name = ci.siteName,
                    location = ci.siteLocation,
                    date = DATE_FMT.format(Date(ci.timestamp)),
                    siteId = ci.siteId,
                    status = if (now - ci.timestamp < sevenDaysMs) CheckInStatus.RECENT else CheckInStatus.VERIFIED
                )
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLowest,
                        RoundedCornerShape(999.dp)
                    )
                    .clickable(onClick = onBack)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "My Check-Ins",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (checkIns.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No check-ins yet. Scan a QR code at a heritage site!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(checkIns) { entry ->
                    CheckInCard(
                        entry = entry,
                        onClick = { onSiteClick(entry.siteId) }
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
fun CheckInCard(entry: CheckInEntry, onClick: () -> Unit) {
    val badgeColor = when (entry.status) {
        CheckInStatus.VERIFIED -> MaterialTheme.colorScheme.primary
        CheckInStatus.RECENT -> MaterialTheme.colorScheme.tertiary
        CheckInStatus.PENDING -> MaterialTheme.colorScheme.outline
    }
    val badgeBg = when (entry.status) {
        CheckInStatus.VERIFIED -> MaterialTheme.colorScheme.primaryContainer
        CheckInStatus.RECENT -> MaterialTheme.colorScheme.tertiaryContainer
        CheckInStatus.PENDING -> MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val badgeLabel = when (entry.status) {
        CheckInStatus.VERIFIED -> "Verified"
        CheckInStatus.RECENT -> "Recent"
        CheckInStatus.PENDING -> "Pending"
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = entry.location,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = BeVietnamPro
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(badgeBg, RoundedCornerShape(999.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badgeLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = BeVietnamPro,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = badgeColor
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = entry.date,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = BeVietnamPro
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

enum class CheckInStatus { VERIFIED, RECENT, PENDING }

data class CheckInEntry(
    val name: String,
    val location: String,
    val date: String,
    val siteId: String = "",
    val status: CheckInStatus = CheckInStatus.VERIFIED
)
