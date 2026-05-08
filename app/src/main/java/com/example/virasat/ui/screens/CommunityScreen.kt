package com.example.virasat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.virasat.data.source.ImageUrls

@Composable
fun CommunityScreen(onBack: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val posts = listOf(
        CommunityPost(
            author = "Priya Sharma",
            category = "Heritage Walk",
            title = "Restoring the Sacred Grove",
            body = "Join us this weekend as we gather to document the indigenous flora surrounding the 14th-century ruins. We have uncovered three new species of medicinal moss that haven't been recorded in this area for decades.",
            imageUrl = ImageUrls.HAMPI,
            time = "2h ago",
            likes = 24,
            actionLabel = "Join Event",
            actionIcon = Icons.Default.NaturePeople
        ),
        CommunityPost(
            author = "Ravi Kumar",
            category = "Craftsmanship",
            title = "The Terracotta Revival",
            body = "A beautiful deep dive into the traditional methods of clay sourcing along the riverbanks. The tactile connection between the maker and the earth is something we must fight to preserve in the digital age.",
            imageUrl = ImageUrls.BADAMI,
            time = "5h ago",
            likes = 18,
            actionLabel = "12 Comments",
            actionIcon = Icons.Default.Forum
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .statusBarsPadding()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = colorScheme.surfaceContainerLowest.copy(alpha = 0.9f),
                    tonalElevation = 1.dp,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(onClick = onBack)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Text(
                    text = "Virasat",
                    style = typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.primary
                )

                Surface(
                    shape = CircleShape,
                    color = colorScheme.surfaceContainerLowest.copy(alpha = 0.9f),
                    tonalElevation = 1.dp,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(onClick = { })
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Header Text
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                Text(
                    text = "Community",
                    style = typography.displayLarge.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Discover stories, heritage walks, and preservation efforts from local custodians.",
                    style = typography.bodyLarge,
                    color = colorScheme.onSurfaceVariant
                )
            }

            // Feed cards
            posts.forEach { post ->
                CommunityFeedCard(post = post)
                Spacer(modifier = Modifier.height(24.dp))
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun CommunityFeedCard(post: CommunityPost) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceContainerLowest),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image with overlapping avatar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
                // Bottom gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    androidx.compose.ui.graphics.Color.Transparent,
                                    androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.3f)
                                )
                            )
                        )
                )

                // Overlapping avatar (bottom-left)
                Surface(
                    shape = CircleShape,
                    color = colorScheme.surfaceContainerLowest,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.BottomStart)
                        .offset(x = 20.dp, y = 24.dp),
                    shadowElevation = 4.dp,
                    border = BorderStroke(
                        width = 4.dp,
                        color = colorScheme.surfaceContainerLowest
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.author.take(1).uppercase(),
                            style = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Card Content
            Column(modifier = Modifier.padding(20.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                // Category + time row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = post.category.uppercase(),
                        style = typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        ),
                        color = colorScheme.primary
                    )
                    Text(
                        text = post.time,
                        style = typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title
                Text(
                    text = post.title,
                    style = typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Body
                Text(
                    text = post.body,
                    style = typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val isFirst = post.actionLabel == "Join Event"
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFirst)
                                colorScheme.primaryContainer
                            else
                                colorScheme.surfaceContainer,
                            contentColor = if (isFirst)
                                colorScheme.onPrimaryContainer
                            else
                                colorScheme.onSurface
                        ),
                        modifier = Modifier.height(44.dp)
                    ) {
                        Icon(
                            imageVector = post.actionIcon ?: Icons.Default.NaturePeople,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            post.actionLabel,
                            style = typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Bookmark / Favorite button
                    Surface(
                        shape = CircleShape,
                        color = colorScheme.surfaceContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isFirst) Icons.Default.Bookmark else Icons.Default.Favorite,
                                contentDescription = null,
                                tint = colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

data class CommunityPost(
    val author: String,
    val category: String,
    val title: String,
    val body: String,
    val imageUrl: String,
    val time: String,
    val likes: Int,
    val actionLabel: String = "Join",
    val actionIcon: ImageVector? = null
)
