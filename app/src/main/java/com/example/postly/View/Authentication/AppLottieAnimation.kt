
package com.example.postly.View.Authentication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation  // ← ADD THIS IMPORT
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun AppLottieAnimation(
    assetPath: String,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    iterations: Int = Int.MAX_VALUE,
    speed: Float = 1f
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset(assetPath)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        speed = speed
    )

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = modifier
    )
}