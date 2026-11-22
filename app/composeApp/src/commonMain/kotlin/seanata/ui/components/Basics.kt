package seanata.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import seanata.theme.LocalAppFont
import seanata.theme.Sea

/** All text of the app: Inter, a size and a weight. */
@Composable
fun Txt(
    text: String,
    modifier: Modifier = Modifier,
    size: Int = 15,
    weight: FontWeight = FontWeight.Normal,
    color: Color = Sea.Ink,
    lineHeight: Int = 0,
    maxLines: Int = Int.MAX_VALUE,
    align: TextAlign = TextAlign.Start,
    tracking: Double = 0.0,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(
            color = color,
            fontSize = size.sp,
            fontWeight = weight,
            fontFamily = LocalAppFont.current,
            lineHeight = if (lineHeight > 0) lineHeight.sp else TextUnit.Unspecified,
            textAlign = align,
            letterSpacing = tracking.sp,
        ),
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun Ico(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    tint: Color = Sea.Ink,
) {
    Image(rememberVectorPainter(icon), contentDescription = null, modifier = modifier.size(size), colorFilter = ColorFilter.tint(tint))
}

/** A photo that fills its box. */
@Composable
fun Photo(
    photo: DrawableResource,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
) {
    Image(painterResource(photo), contentDescription = null, modifier = modifier, contentScale = ContentScale.Crop, alignment = alignment)
}

/** A press without a ripple: what is pressed sinks a little and springs back. Goes first in a chain, so the whole thing moves. */
@Composable
fun Modifier.tap(onClick: () -> Unit): Modifier {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, spring(stiffness = Spring.StiffnessMediumLow))
    return graphicsLayer { scaleX = scale; scaleY = scale }.clickable(interactionSource = source, indication = null, onClick = onClick)
}

/** How one view replaces another: the new one fades in sliding from the side it lies on. */
fun slideFade(forward: Boolean, shift: Int = 6): ContentTransform {
    val dir = if (forward) 1 else -1
    return ContentTransform(
        targetContentEnter = fadeIn(tween(260, delayMillis = 70)) + slideInHorizontally(tween(330)) { dir * it / shift },
        initialContentExit = fadeOut(tween(150)) + slideOutHorizontally(tween(330)) { -dir * it / shift },
        sizeTransform = SizeTransform(clip = false),
    )
}

/**
 * How one screen replaces another. Nothing turns transparent on the way, so the page never shows through
 * as a flash: a deeper screen rides in from the right over the one it leaves, and rides back out on return;
 * a neighbouring section settles in on top of the one still standing there.
 */
fun screenChange(depth: Int, layer: Float): ContentTransform {
    val move = tween<IntOffset>(360, easing = FastOutSlowInEasing)
    return when {
        depth > 0 -> ContentTransform(slideInHorizontally(move) { it }, slideOutHorizontally(move) { -it / 4 }, layer)
        depth < 0 -> ContentTransform(slideInHorizontally(move) { -it / 4 }, slideOutHorizontally(move) { it }, layer)
        else -> ContentTransform(fadeIn(tween(240, easing = LinearOutSlowInEasing)), fadeOut(snap(delayMillis = 240)), layer)
    }
}

/** The white raised surface every block sits on. */
fun Modifier.card(
    radius: Dp = 20.dp,
    color: Color = Sea.Card,
    elevation: Dp = 10.dp,
): Modifier {
    val shape = RoundedCornerShape(radius)
    return shadow(elevation, shape, clip = false, ambientColor = Color(0x400A2850), spotColor = Color(0x400A2850))
        .clip(shape)
        .background(color)
}

/** A darkening over a photo, top to bottom. */
@Composable
fun Scrim(
    vararg stops: Pair<Float, Color>,
    modifier: Modifier = Modifier,
) {
    Box(modifier.background(Brush.verticalGradient(*stops)))
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
) {
    Box(
        modifier.tap(onClick).height(height).shadow(8.dp, RoundedCornerShape(14.dp), ambientColor = Sea.Blue, spotColor = Sea.Blue)
            .clip(RoundedCornerShape(14.dp)).background(Sea.Action),
        contentAlignment = Alignment.Center,
    ) {
        Txt(text, size = 16, weight = FontWeight.SemiBold, color = Color.White)
    }
}

/** The quiet second action: white with a line. */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Sea.Blue,
    icon: ImageVector? = null,
) {
    Row(
        modifier.tap(onClick).height(52.dp).clip(RoundedCornerShape(14.dp)).background(Sea.Card)
            .border(1.dp, Sea.Line, RoundedCornerShape(14.dp)),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) Ico(icon, size = 18.dp, tint = color)
        Txt(text, size = 16, weight = FontWeight.SemiBold, color = color)
    }
}

/** The kind of a training: «Практика», «Навигация». */
@Composable
fun Tag(
    text: String,
    solid: Boolean = false,
) {
    Box(
        Modifier.clip(RoundedCornerShape(8.dp)).background(if (solid) Sea.BlueBright else Sea.Tint)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Txt(text, size = 12, weight = FontWeight.SemiBold, color = if (solid) Color.White else Sea.Blue)
    }
}

/** A small icon with a caption: time, place, seats. */
@Composable
fun Meta(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Sea.Muted,
    size: Int = 13,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Ico(icon, size = (size + 2).dp, tint = color)
        Txt(text, size = size, color = color, maxLines = 1)
    }
}

/** A fact of a course on a white pill: «5 дней». */
@Composable
fun Pill(
    icon: ImageVector,
    text: String,
) {
    Row(
        Modifier.clip(RoundedCornerShape(12.dp)).background(Color.White).padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Ico(icon, size = 16.dp)
        Txt(text, size = 13, weight = FontWeight.Medium)
    }
}

/** The round «further» button on a banner. */
@Composable
fun RoundChevron(modifier: Modifier = Modifier) {
    Box(modifier.size(40.dp).clip(CircleShape).background(Color(0x55FFFFFF)), contentAlignment = Alignment.Center) {
        Ico(AppIcons.ChevronRight, size = 20.dp, tint = Color.White)
    }
}

@Composable
fun VLine(height: Dp = 36.dp) {
    Box(Modifier.width(1.dp).height(height).background(Sea.Line))
}

/** The heading of a group, with an optional link on the right. */
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    action: String? = null,
    onAction: () -> Unit = {},
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Txt(title, Modifier.weight(1f), size = 20, weight = FontWeight.Bold, tracking = -0.3)
        if (action != null) Txt(action, Modifier.tap(onAction), size = 14, weight = FontWeight.Medium, color = Sea.Blue)
    }
}
