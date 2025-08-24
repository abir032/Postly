
package com.example.postly.View.Authentication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.postly.Utils.AppText

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppLottieAnimation(
                    assetPath = "BlogPost.json",
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(24.dp))
                )

                Text(
                    text = AppText.WELCOME_TITLE,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
                )

                Text(
                    text = AppText.WELCOME_SUBTITLE,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GlassButton(
                    onClick = onNavigateToLogin,
                    text = "Login",
                    modifier = Modifier.weight(1f),
                    colors = listOf(
                        Color(0xFFE3F2FD).copy(alpha = 0.3f),  // Light blue
                        Color(0xFFBBDEFB).copy(alpha = 0.2f),  // Medium blue
                        Color(0xFF90CAF9).copy(alpha = 0.1f)   // Dark blue
                    )
                )
                GlassButton(
                    onClick = onNavigateToRegister,
                    text = "Sign Up",
                    modifier = Modifier.weight(1f),
                    colors = listOf(
                        Color(0xFFF3E5F5).copy(alpha = 0.3f),
                        Color(0xFFE1BEE7).copy(alpha = 0.2f),
                        Color(0xFFCE93D8).copy(alpha = 0.1f)
                    )
                )

            }
        }
    }
}

@Composable
fun Modifier.glassMorphismEffect(
    cornerRadius: Dp = 16.dp,
    colors: List<Color>
): Modifier = this
    .clip(RoundedCornerShape(cornerRadius))
    .graphicsLayer {
        compositingStrategy = CompositingStrategy.Offscreen
    }
    .drawWithCache {
        onDrawWithContent {
            drawRect(
                brush = Brush.verticalGradient(colors),
                blendMode = BlendMode.Overlay
            )
            drawContent()
        }
    }
    .background(
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
        shape = RoundedCornerShape(cornerRadius)
    )


@Composable
fun GlassButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    colors: List<Color>,
    height: Dp = 56.dp,
    cornerRadius: Dp = 16.dp
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .height(height) // Fixed height
            .widthIn(min = 120.dp), // Minimum width
        shape = RoundedCornerShape(cornerRadius),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .glassMorphismEffect(cornerRadius = cornerRadius, colors),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}
