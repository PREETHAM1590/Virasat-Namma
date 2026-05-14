package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.virasat.R
import com.example.virasat.data.di.RepositoryProvider
import com.example.virasat.ui.theme.PlusJakartaSans

@Composable
fun ReviewsScreen(
    siteId: String,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current
    val repo = remember(ctx) { RepositoryProvider.getRepository(ctx) }
    val site by produceState<com.example.virasat.data.model.HeritageSite?>(null, siteId) {
        value = try { repo.getSiteById(siteId) } catch (_: Exception) { null }
    }
    val reviews = listOf(
        ReviewItem("Heritage Explorer", "Absolutely stunning architecture. The stone carvings are breathtaking!", 5, "2 days ago"),
        ReviewItem("TravelBug", "Great experience but gets crowded on weekends. Visit early morning.", 4, "1 week ago"),
        ReviewItem("HistoryBuff", "A must-visit for anyone interested in South Indian history.", 5, "2 weeks ago"),
        ReviewItem("WeekendWanderer", "Good maintenance and helpful audio guide.", 4, "3 weeks ago")
    )

    var userRating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    val scheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(scheme.background)
    ) {
        // TopAppBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(scheme.surfaceContainerLow, RoundedCornerShape(999.dp))
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = scheme.onSurface
                )
            }
            Text(
                text = stringResource(R.string.reviews_share_journey),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = PlusJakartaSans,
                    fontWeight = FontWeight.SemiBold
                ),
                color = scheme.onSurface
            )
            // Spacer for alignment
            Box(modifier = Modifier.size(48.dp))
        }

        // Ambient background blobs
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .size(400.dp)
                    .background(scheme.secondaryContainer.copy(alpha = 0.4f), CircleShape)
                    .align(Alignment.TopEnd)
                    .padding(top = 24.dp, end = 24.dp)
            )
            Box(
                modifier = Modifier
                    .size(500.dp)
                    .background(scheme.primaryContainer.copy(alpha = 0.2f), CircleShape)
                    .align(Alignment.BottomStart)
                    .padding(bottom = 24.dp, start = 24.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Site rating summary
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${site?.rating ?: 4.5f}",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.Bold
                        ),
                        color = scheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row {
                            repeat(5) { i ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (i < (site?.rating?.toInt() ?: 4))
                                        scheme.primary
                                    else
                                        scheme.onSurfaceVariant.copy(alpha = 0.3f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(
                            text = stringResource(R.string.reviews_count, site?.reviews ?: 0),
                            style = MaterialTheme.typography.bodySmall,
                            color = scheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Write Review Card - styled from submit_review HTML
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = scheme.surfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Rating section
                        Text(
                            text = stringResource(R.string.reviews_rate_experience),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = scheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) { index ->
                                val starFilled = index < userRating
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star ${index + 1}",
                                    tint = if (starFilled)
                                        scheme.primaryContainer
                                    else
                                        scheme.surfaceContainerHighest,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable { userRating = index + 1 }
                                )
                            }
                        }

                        // Add Photos button with dashed border
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .border(
                                    width = 2.dp,
                                    color = scheme.outlineVariant,
                                    shape = RoundedCornerShape(
                                        topStart = 48.dp,
                                        topEnd = 24.dp,
                                        bottomEnd = 40.dp,
                                        bottomStart = 16.dp
                                    )
                                )
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 48.dp,
                                        topEnd = 24.dp,
                                        bottomEnd = 40.dp,
                                        bottomStart = 16.dp
                                    )
                                )
                                .background(scheme.surfaceBright)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(scheme.surfaceContainer, RoundedCornerShape(24.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = stringResource(R.string.reviews_add_photos),
                                        tint = scheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.reviews_add_photos),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = scheme.onSurfaceVariant
                                )
                            }
                        }

                        // Review text area - inset/recessed style
                        OutlinedTextField(
                            value = reviewText,
                            onValueChange = { reviewText = it },
                            placeholder = {
                                Text(
                                    stringResource(R.string.reviews_write_placeholder),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = scheme.background,
                                unfocusedContainerColor = scheme.background,
                                focusedBorderColor = scheme.primaryContainer.copy(alpha = 0.5f),
                                unfocusedBorderColor = scheme.outlineVariant,
                                focusedTextColor = scheme.onSurface,
                                unfocusedTextColor = scheme.onSurface
                            ),
                            textStyle = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Post Review pill button
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = scheme.primary,
                        contentColor = scheme.onPrimary
                    ),
                    shape = RoundedCornerShape(999.dp),
                    enabled = userRating > 0
                ) {
                    Text(
                        text = stringResource(R.string.reviews_post),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Existing reviews header
            item {
                Text(
                    text = stringResource(R.string.reviews_recent),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = PlusJakartaSans,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = scheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Existing reviews list
            items(reviews.size) { index ->
                ReviewCard(review = reviews[index])
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ReviewCard(review: ReviewItem) {
    val scheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = scheme.surfaceContainerLowest
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(scheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.author.take(1).uppercase(),
                        color = scheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.author,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = PlusJakartaSans,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = scheme.onSurface
                    )
                    Text(
                        text = review.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = scheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
                Row {
                    repeat(5) { i ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (i < review.rating)
                                scheme.primary
                            else
                                scheme.onSurfaceVariant.copy(alpha = 0.3f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.body,
                style = MaterialTheme.typography.bodyMedium,
                color = scheme.onSurfaceVariant
            )
        }
    }
}

data class ReviewItem(val author: String, val body: String, val rating: Int, val time: String)
