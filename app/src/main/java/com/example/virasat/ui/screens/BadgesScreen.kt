package com.example.virasat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virasat.data.model.BadgeResult
import com.example.virasat.viewmodel.BadgesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgesScreen(
    onBack: () -> Unit = {},
    viewModel: BadgesViewModel = viewModel()
) {
    val badges by viewModel.badges.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var previousUnlocked by remember { mutableStateOf<Set<String>>(emptySet()) }

    LaunchedEffect(badges) {
        val currentUnlocked = badges.filter { it.isUnlocked }.map { it.definition.id }.toSet()
        val newlyUnlocked = currentUnlocked - previousUnlocked
        if (newlyUnlocked.isNotEmpty() && previousUnlocked.isNotEmpty()) {
            val badge = badges.first { it.definition.id in newlyUnlocked }
            snackbarHostState.showSnackbar("Congratulations! You earned: ${badge.definition.name}")
        }
        previousUnlocked = currentUnlocked
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(com.example.virasat.R.string.badges_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (badges.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(badges) { badge -> BadgeCard(badge) }
            }
        }
    }
}

@Composable
private fun BadgeCard(badge: BadgeResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = badge.definition.icon,
                contentDescription = badge.definition.name,
                modifier = Modifier.size(48.dp),
                tint = if (badge.isUnlocked)
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    badge.definition.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    badge.definition.description,
                    style = MaterialTheme.typography.bodySmall
                )
                if (!badge.isUnlocked) {
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { badge.progress.toFloat() / badge.target },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "${badge.progress} / ${badge.target}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
