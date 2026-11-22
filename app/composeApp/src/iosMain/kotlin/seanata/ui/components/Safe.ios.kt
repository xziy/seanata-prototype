package seanata.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

@Composable
actual fun safeArea(): Safe {
    val insets = WindowInsets.safeDrawing
    return with(LocalDensity.current) { Safe(insets.getTop(this).toDp(), insets.getBottom(this).toDp()) }
}
