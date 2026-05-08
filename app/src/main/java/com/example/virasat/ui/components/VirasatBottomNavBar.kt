package com.example.virasat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.ui.theme.NavBackground
import com.example.virasat.ui.theme.NavOnBackground
import com.example.virasat.ui.theme.OnSecondaryFixedVariant
import com.example.virasat.ui.theme.PrimaryFixed

enum class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("home", "Explore", Icons.Default.Home, Icons.Outlined.Home),
    MAP("map", "Heritage", Icons.Default.Star, Icons.Outlined.Star),
    SEARCH("search", "Saved", Icons.Default.Search, Icons.Outlined.Search),
    PROFILE("profile", "Profile", Icons.Default.Person, Icons.Outlined.Person)
}

@Composable
fun VirasatBottomNavBar(
    currentRoute: String,
    onItemClick: (BottomNavItem) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(NavBackground)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem.entries.forEach { item ->
                val selected = currentRoute == item.route
                val bgColor = if (selected) OnSecondaryFixedVariant.copy(alpha = 0.2f) else Color.Transparent
                val tint = if (selected) PrimaryFixed else NavOnBackground.copy(alpha = 0.6f)
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onItemClick(item) }
                        .then(
                            if (selected) Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(bgColor)
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                            else Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                ) {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = item.label,
                        color = tint,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                    )
                }
            }
        }
    }
}