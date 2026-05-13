package com.example.virasat.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Design System — Indian Temple Architecture Shapes
// Inspired by stepped gopuram profiles and carved stone edges
// Mix of cut corners (shikhara steps) and rounded (dome arches)

val VirasatShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp),
    medium = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = CutCornerShape(topStart = 24.dp, bottomEnd = 24.dp)
)
