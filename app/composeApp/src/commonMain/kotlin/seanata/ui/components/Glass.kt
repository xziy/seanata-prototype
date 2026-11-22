package seanata.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.FilterTileMode
import org.jetbrains.skia.ImageFilter
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder
import seanata.theme.Sea

/**
 * What a pane of glass looks through: a picture of what lies under it, and where that lies on the screen.
 * [moving] reads whatever state moves the picture (a scroll), so the pane is redrawn along with it.
 */
class GlassSource(val layer: GraphicsLayer, val moving: () -> Unit) {
    var position by mutableStateOf(Offset.Zero)
}

/** The picture the glass of this part of the screen looks through; without one a pane is only tinted. */
val LocalGlassSource = compositionLocalOf<GlassSource?> { null }

@Composable
fun rememberGlassSource(moving: () -> Unit = {}): GlassSource {
    val layer = rememberGraphicsLayer()
    return remember { GlassSource(layer, moving) }
}

/** Draws this content as usual and keeps its picture in [source] for the glass above it. */
fun Modifier.glassSource(source: GlassSource): Modifier =
    onGloballyPositioned { source.position = it.positionInRoot() }.drawWithContent {
        source.moving()
        source.layer.record { this@drawWithContent.drawContent() }
        drawLayer(source.layer)
    }

/** The look of a pane: what it is tinted with, and how bright its rim is. */
class GlassTone(val tint: Color, val sheen: Float, val rim: Float) {
    companion object {
        /** A light pane with dark words on it: the bars of the app. */
        val Light = GlassTone(Color(0xFFF3F6FB).copy(alpha = 0.52f), sheen = 0.5f, rim = 0.95f)

        /** A clear pane over a photo, with white words on it. */
        val Clear = GlassTone(Color.White.copy(alpha = 0.16f), sheen = 0.2f, rim = 0.7f)

        /** A darker pane over a photo, where white words need more under them. */
        val Smoke = GlassTone(Sea.NavyDeep.copy(alpha = 0.34f), sheen = 0.14f, rim = 0.5f)

        /** The same in the app's blue: what is chosen or switched on. */
        val Blue = GlassTone(Sea.BlueBright.copy(alpha = 0.62f), sheen = 0.22f, rim = 0.6f)
    }
}

/**
 * Liquid glass: what lies under the pane shows through blurred, bends at the rounded edge as in a lens,
 * and the pane itself is a tint with a light rim. Goes where `clip().background()` would.
 *
 * It is drawn by Compose on every platform (iOS 26 keeps its own glass for native views only).
 */
@Composable
fun Modifier.glass(
    radius: Dp,
    tone: GlassTone = GlassTone.Light,
    blur: Dp = 14.dp,
    lens: Boolean = true,
): Modifier {
    val source = LocalGlassSource.current
    val pane = rememberGraphicsLayer()
    var position by remember { mutableStateOf(Offset.Zero) }
    return onGloballyPositioned { position = it.positionInRoot() }.drawWithCache {
        val corner = radius.toPx().coerceAtMost(size.minDimension / 2)
        val outline = RoundedCornerShape(corner).createOutline(size, layoutDirection, this)
        val path = Path().apply { addOutline(outline) }
        val effect = glassEffect(size, corner, blur.toPx(), bend = if (lens && corner > 0f) (corner * 0.9f).coerceAtMost(16.dp.toPx()) else 0f)
        val sheen = Brush.verticalGradient(0f to Color.White.copy(alpha = tone.sheen), 0.6f to Color.Transparent)
        val rim = Brush.verticalGradient(
            0f to Color.White.copy(alpha = tone.rim),
            0.5f to Color.White.copy(alpha = tone.rim * 0.25f),
            1f to Color.White.copy(alpha = tone.rim * 0.55f),
        )
        onDrawWithContent {
            clipPath(path) {
                if (source != null) {
                    source.moving()
                    val shift = source.position - position
                    pane.renderEffect = effect
                    pane.record { translate(shift.x, shift.y) { drawLayer(source.layer) } }
                    drawLayer(pane)
                }
                drawRect(tone.tint)
                drawRect(sheen)
            }
            drawContent()
            if (corner > 0f) drawOutline(outline, rim, style = Stroke(1.dp.toPx()))
        }
    }
}

/** Something (a photo, a map) with panes of glass over it: [content] looks through to [under]. */
@Composable
fun GlassOver(
    modifier: Modifier = Modifier,
    under: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val source = rememberGlassSource()
    Box(modifier) {
        Box(Modifier.matchParentSize().glassSource(source), content = under)
        CompositionLocalProvider(LocalGlassSource provides source) { content() }
    }
}

/**
 * The optics of a pane: a blur, then the bend. Within [bend] of the rounded edge every point shows what
 * lies further inside, the more so the closer to the edge: the picture is squeezed along the rim.
 */
private fun glassEffect(size: Size, corner: Float, blur: Float, bend: Float): RenderEffect {
    val blurred = ImageFilter.makeBlur(blur / 2, blur / 2, FilterTileMode.CLAMP)
    if (bend <= 0f) return blurred.asComposeRenderEffect()
    val lens = RuntimeShaderBuilder(lensEffect).apply {
        uniform("size", size.width, size.height)
        uniform("corner", corner)
        uniform("bend", bend)
    }
    return ImageFilter.makeRuntimeShader(lens, "content", blurred).asComposeRenderEffect()
}

private val lensEffect by lazy {
    RuntimeEffect.makeForShader(
        """
        uniform shader content;
        uniform float2 size;
        uniform float corner;
        uniform float bend;

        float edge(float2 p) {
            float2 q = abs(p) - size * 0.5 + corner;
            return length(max(q, 0.0)) + min(max(q.x, q.y), 0.0) - corner;
        }

        half4 main(float2 xy) {
            float2 p = xy - size * 0.5;
            float depth = -edge(p);
            float t = clamp(1.0 - depth / bend, 0.0, 1.0);
            float2 out_ = float2(edge(p + float2(1.0, 0.0)) - edge(p - float2(1.0, 0.0)), edge(p + float2(0.0, 1.0)) - edge(p - float2(0.0, 1.0)));
            float len = length(out_);
            if (len < 0.0001 || t <= 0.0) return content.eval(xy);
            return content.eval(xy - out_ / len * t * t * bend * 1.4);
        }
        """.trimIndent(),
    )
}
