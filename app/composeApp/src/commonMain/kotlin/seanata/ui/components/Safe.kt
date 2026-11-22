package seanata.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** What the phone keeps for itself: the notch and the clock above, the home bar below. */
data class Safe(val top: Dp = 0.dp, val bottom: Dp = 0.dp)

/** Photos and bars run under these edges; what is read or pressed stays clear of them. */
val LocalSafe = compositionLocalOf { Safe() }

@Composable
expect fun safeArea(): Safe
