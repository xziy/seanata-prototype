package seanata.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import seanata.res.Res
import seanata.res.inter_bold
import seanata.res.inter_medium
import seanata.res.inter_regular
import seanata.res.inter_semi_bold
import zed.rainxch.rikkaui.foundation.RikkaAccentPreset
import zed.rainxch.rikkaui.foundation.RikkaPalette
import zed.rainxch.rikkaui.foundation.RikkaStylePreset
import zed.rainxch.rikkaui.foundation.RikkaTheme
import zed.rainxch.rikkaui.foundation.rikkaTypography

/**
 * The sea palette of the app. Rikka's theme stays underneath (its components keep working), but the look
 * of the references — navy photo headers, white cards, blue actions — is described by these tokens.
 */
object Sea {
    val NavyDeep = Color(0xFF071A36)
    val Navy = Color(0xFF0A2850)
    val Blue = Color(0xFF0B4AA2)
    val BlueBright = Color(0xFF1764C8)
    val Tint = Color(0xFFDCE8FA)
    val Bg = Color(0xFFF1F4F9)
    val Card = Color(0xFFFFFFFF)
    val Ink = Color(0xFF0D1A2D)
    val Muted = Color(0xFF64738A)
    val Line = Color(0xFFE1E7F0)
    val Success = Color(0xFF178A5B)
    val Alert = Color(0xFFE5484D)

    /** The fill of every primary action. */
    val Action = Brush.verticalGradient(listOf(BlueBright, Blue, Color(0xFF083A85)))
}

val LocalAppFont = staticCompositionLocalOf<FontFamily> { FontFamily.Default }

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    // Inter: the browser has no system fonts for Compose, and Cyrillic needs a font that has it.
    val inter = FontFamily(
        Font(Res.font.inter_regular, FontWeight.Normal),
        Font(Res.font.inter_medium, FontWeight.Medium),
        Font(Res.font.inter_semi_bold, FontWeight.SemiBold),
        Font(Res.font.inter_bold, FontWeight.Bold),
    )
    val style = RikkaStylePreset.Default.style
    RikkaTheme(
        colors = RikkaAccentPreset.Blue.applyTo(RikkaPalette.Slate.resolve(isDark = false), isDark = false),
        style = style,
        typography = rikkaTypography(fontFamily = inter, scale = style.typeScale),
    ) {
        CompositionLocalProvider(LocalAppFont provides inter, content = content)
    }
}
