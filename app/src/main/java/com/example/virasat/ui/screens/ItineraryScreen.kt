package com.example.virasat.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virasat.domain.ItineraryPlanner
import com.example.virasat.viewmodel.ItineraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(
    onBack: () -> Unit = {},
    onSiteClick: (String) -> Unit = {},
    itineraryId: String? = null,
    viewModel: ItineraryViewModel = viewModel()
) {
    val context = LocalContext.current
    val orderedSites by viewModel.orderedSites.collectAsState()
    val savedItineraries by viewModel.savedItineraries.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showSaveDialog by remember { mutableStateOf(false) }
    var itineraryName by remember { mutableStateOf("") }

    LaunchedEffect(itineraryId) {
        itineraryId?.let { viewModel.loadItinerary(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Itinerary Planner") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    if (orderedSites.isNotEmpty()) {
                        IconButton(onClick = { showSaveDialog = true }) {
                            Icon(Icons.Default.Save, "Save")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Search sites to add...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true
            )

            if (savedItineraries.isNotEmpty()) {
                Text("Saved Itineraries", Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.labelLarge)
                Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    savedItineraries.take(5).forEach { itinerary ->
                        FilterChip(
                            selected = false,
                            onClick = { viewModel.loadItinerary(itinerary.id) },
                            label = { Text(itinerary.name) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }

            if (orderedSites.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Add sites to plan your itinerary", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(orderedSites) { index, site ->
                        Card(Modifier.fillMaxWidth()) {
                            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("${index + 1}", fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(32.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(site.name, fontWeight = FontWeight.Bold)
                                    Text(site.location, style = MaterialTheme.typography.bodySmall)
                                    if (index < orderedSites.size - 1) {
                                        val next = orderedSites[index + 1]
                                        val dist = ItineraryPlanner.haversineDistance(
                                            site.latitude, site.longitude, next.latitude, next.longitude
                                        )
                                        Text("→ %.1f km".format(dist),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                IconButton(onClick = { viewModel.removeSite(site.id) }) {
                                    Icon(Icons.Default.Close, "Remove")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Itinerary") },
            text = {
                OutlinedTextField(
                    value = itineraryName,
                    onValueChange = { if (it.length <= 100) itineraryName = it },
                    label = { Text("Itinerary name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (itineraryName.isNotBlank()) {
                        viewModel.saveItinerary(itineraryName)
                        showSaveDialog = false
                        itineraryName = ""
                    }
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) { Text("Cancel") }
            }
        )
    }
}
