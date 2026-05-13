package com.example.virasat.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.virasat.data.service.WeatherCondition
import com.example.virasat.data.service.WeatherData
import com.example.virasat.ui.theme.OnSurface
import com.example.virasat.ui.theme.SurfaceContainerLowest

@Composable
fun AnimatedWeatherWidget(
    weather: WeatherData?,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "weather_anim")

    Row(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(999.dp), spotColor = Color.Black.copy(alpha = 0.05f))
            .clip(RoundedCornerShape(999.dp))
            .background(SurfaceContainerLowest)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        weather?.let { data ->
            when (data.condition) {
                WeatherCondition.CLEAR -> {
                    val rotation by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(12000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "sun_rotate"
                    )
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("☀️", fontSize = 20.sp, modifier = Modifier.rotate(rotation))
                    }
                }
                WeatherCondition.CLOUDY -> {
                    val offsetX by infiniteTransition.animateFloat(
                        initialValue = -2f,
                        targetValue = 2f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(3000, easing = EaseInOutSine),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "cloud_drift"
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .offset(x = offsetX.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("☁️", fontSize = 20.sp)
                    }
                }
                WeatherCondition.RAIN, WeatherCondition.DRIZZLE -> {
                    val offsetY by infiniteTransition.animateFloat(
                        initialValue = -3f,
                        targetValue = 3f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(800, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "rain_fall"
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .offset(y = offsetY.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🌧️", fontSize = 20.sp)
                    }
                }
                WeatherCondition.THUNDERSTORM -> {
                    val alpha by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 0.4f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(400, easing = FastOutLinearInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "thunder_flash"
                    )
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "⛈️",
                            fontSize = 20.sp,
                            modifier = Modifier.alpha(alpha)
                        )
                    }
                }
                WeatherCondition.SNOW -> {
                    val offsetY by infiniteTransition.animateFloat(
                        initialValue = -2f,
                        targetValue = 2f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "snow_fall"
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .offset(y = offsetY.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("❄️", fontSize = 20.sp)
                    }
                }
                WeatherCondition.FOG -> {
                    val alpha by infiniteTransition.animateFloat(
                        initialValue = 0.6f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(4000, easing = EaseInOutSine),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "fog_pulse"
                    )
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🌫️", fontSize = 20.sp, modifier = Modifier.alpha(alpha))
                    }
                }
            }
            Text(
                "${data.temperature.toInt()}°C",
                style = MaterialTheme.typography.labelLarge,
                color = OnSurface,
                fontWeight = FontWeight.SemiBold
            )
        } ?: run {
            Text("☀️", fontSize = 20.sp)
            Text(
                "--°C",
                style = MaterialTheme.typography.labelLarge,
                color = OnSurface
            )
        }
    }
}
