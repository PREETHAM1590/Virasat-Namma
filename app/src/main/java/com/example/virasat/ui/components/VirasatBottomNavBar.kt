package com.example.virasat.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.virasat.ui.theme.VirasatCream
import com.example.virasat.ui.theme.VirasatMaroon

enum class BottomNavItem(val route: String, val label: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    HOME("home", "Home", Icons.Default.Home, Icons.Outlined.Home),
    MAP("map", "Map", Icons.Default.Map, Icons.Outlined.Map),
    SEARCH("search", "Search", Icons.Default.Search, Icons.Outlined.Search),
    NOTIFICATIONS("notifications", "Alerts", Icons.Default.Notifications, Icons.Outlined.Notifications),
    PROFILE("profile", "Profile", Icons.Default.Person, Icons.Outlined.Person)
}

@Composable
fun VirasatBottomNavBar(
    currentRoute: String,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = NavigationBarDefaults.Elevation
    ) {
        BottomNavItem.entries.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = selected,
                onClick = { onItemClick(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VirasatMaroon,
                    selectedTextColor = VirasatMaroon,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = VirasatMaroon.copy(alpha = 0.1f)
                )
            )
        }
    }
}