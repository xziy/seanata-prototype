package seanata.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import seanata.data.Fix
import seanata.data.Logbook
import seanata.theme.LocalAppFont
import seanata.theme.Sea

/**
 * The bay of Nha Trang as a sketch chart, in nautical miles from its corner (see [Fix]): the mainland on
 * the west, Hon Tre and the small islands south of it. Drawn by hand for the prototype, not to navigate by.
 */
private object Bay {
    val mainland = listOf(
        -8.0 to -6.0, 2.3 to -6.0, 2.1 to -1.0, 2.0 to 0.0, 1.65 to 1.2, 1.6 to 2.4, 1.75 to 3.2, 2.2 to 3.55, 2.75 to 3.85,
        2.55 to 4.3, 2.0 to 4.8, 1.9 to 6.0, 2.2 to 7.2, 2.6 to 8.4, 3.2 to 10.0, 3.5 to 16.0, -8.0 to 16.0,
    )
    val islands = listOf(
        listOf(3.9 to 3.4, 4.4 to 2.8, 5.3 to 2.5, 6.2 to 2.1, 7.2 to 2.3, 8.0 to 2.0, 8.7 to 2.5, 9.2 to 3.2, 9.1 to 4.1, 8.6 to 4.6, 8.2 to 5.2, 7.5 to 5.2, 6.8 to 4.5, 6.0 to 4.8, 5.2 to 4.4, 4.5 to 4.3, 4.0 to 3.9),
        listOf(2.95 to 4.7, 3.2 to 4.55, 3.5 to 4.7, 3.45 to 4.95, 3.15 to 5.05),
        listOf(4.15 to 5.6, 4.45 to 5.45, 4.75 to 5.65, 4.6 to 5.9, 4.25 to 5.9),
        listOf(5.85 to 5.9, 6.05 to 5.8, 6.2 to 5.95, 6.05 to 6.1),
        listOf(7.65 to 6.45, 7.9 to 6.3, 8.2 to 6.45, 8.1 to 6.7, 7.8 to 6.7),
    )
    val names = listOf(
        Triple("о. Хон Че", 6.5, 3.45), Triple("Хон Мун", 7.9, 7.0), Triple("Хон Там", 4.45, 6.2), Triple("Хон Мьеу", 3.2, 5.3),
        Triple("Нячанг", 0.6, 1.3), Triple("м. Кау Да", 1.55, 4.1),
    )
}

/**
 * A track on the chart of the bay. The view is fitted to the track, at least [span] miles wide, inside the
 * part of the box that [top] and [bottom] leave free (bars and panes lie over the rest).
 * [drawn] is how much of the track is laid, 0..1; [detailed] adds the grid, the names and the scale.
 */
@Composable
fun BayChart(
    track: List<Fix>,
    modifier: Modifier = Modifier,
    detailed: Boolean = false,
    drawn: () -> Float = { 1f },
    top: Dp = 0.dp,
    bottom: Dp = 0.dp,
    span: Double = 3.2,
) {
    val measurer = rememberTextMeasurer()
    val font = LocalAppFont.current
    Box(
        modifier.drawWithCache {
            val free = Size(size.width, size.height - top.toPx() - bottom.toPx())
            val pad = if (detailed) 0.7 else 0.45
            var miles = track.maxOf { it.x } - track.minOf { it.x } + pad * 2
            val tall = track.maxOf { it.y } - track.minOf { it.y } + pad * 2
            miles = maxOf(miles, tall * free.width / free.height, span)
            val scale = (free.width / miles).toFloat()
            val centre = Offset(((track.maxOf { it.x } + track.minOf { it.x }) / 2).toFloat(), ((track.maxOf { it.y } + track.minOf { it.y }) / 2).toFloat())
            val middle = Offset(size.width / 2, top.toPx() + free.height / 2)
            fun at(x: Double, y: Double) = Offset(middle.x + (x.toFloat() - centre.x) * scale, middle.y + (y.toFloat() - centre.y) * scale)

            val land = Path().apply {
                (Bay.islands + listOf(Bay.mainland)).forEach { shore ->
                    val points = shore.map { at(it.first, it.second) }
                    fun mid(i: Int) = (points[i % points.size] + points[(i + 1) % points.size]) / 2f
                    moveTo(mid(0).x, mid(0).y)
                    for (i in 1..points.size) quadraticTo(points[i % points.size].x, points[i % points.size].y, mid(i).x, mid(i).y)
                    close()
                }
            }
            val line = Path().apply {
                track.forEachIndexed { i, fix -> at(fix.x, fix.y).let { if (i == 0) moveTo(it.x, it.y) else lineTo(it.x, it.y) } }
            }
            val measure = PathMeasure().apply { setPath(line, false) }
            val laid = Path()
            val width = (if (detailed) 3.2.dp else 2.2.dp).toPx()
            val label = TextStyle(color = Color(0xFF55708C), fontSize = 10.sp, fontFamily = font, fontWeight = FontWeight.Medium)

            onDrawBehind {
                drawRect(Brush.verticalGradient(listOf(Color(0xFFCDE6F8), Color(0xFFB4D8F2))))
                if (detailed) {
                    // A line every mile: the lattice a chart is read against.
                    val first = at(0.0, 0.0)
                    var x = first.x % scale
                    while (x < size.width) { drawLine(Color(0x40FFFFFF), Offset(x, 0f), Offset(x, size.height), 1f); x += scale }
                    var y = first.y % scale
                    while (y < size.height) { drawLine(Color(0x40FFFFFF), Offset(0f, y), Offset(size.width, y), 1f); y += scale }
                }
                // The shoals: a paler band of water along every shore.
                drawPath(land, Color(0x80E6F4FD), style = Stroke(scale * 0.5f, join = StrokeJoin.Round))
                drawPath(land, Color(0xFFE9E7D2))
                drawPath(land, Color(0xFFBFC7A4), style = Stroke(1.dp.toPx()))
                if (detailed) {
                    Bay.names.forEach { (name, x, y) -> centred(measurer, name, at(x, y), label) }
                }

                val part = drawn().coerceIn(0f, 1f)
                laid.reset()
                measure.getSegment(0f, measure.length * part, laid, true)
                drawPath(laid, Color.White, style = Stroke(width + 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                drawPath(laid, Sea.BlueBright, style = Stroke(width, cap = StrokeCap.Round, join = StrokeJoin.Round))

                if (detailed) {
                    // The lines of the deck log lie on the track as rings.
                    var run = 0f
                    track.forEachIndexed { i, fix ->
                        if (i > 0) run += (at(fix.x, fix.y) - at(track[i - 1].x, track[i - 1].y)).getDistance()
                        if (fix.note != null && i in 1 until track.lastIndex && run <= measure.length * part) {
                            drawCircle(Color.White, 4.5.dp.toPx(), at(fix.x, fix.y))
                            drawCircle(if (fix.stop > 0) Sea.Alert else Sea.Blue, 2.6.dp.toPx(), at(fix.x, fix.y))
                        }
                    }
                }
                val home = at(Logbook.marina.x, Logbook.marina.y)
                val big = if (detailed) 1f else 0.7f
                drawCircle(Color.White, 7.dp.toPx() * big, home)
                drawCircle(Sea.Navy, 4.5.dp.toPx() * big, home)
                if (detailed) centred(measurer, "Marina", home + Offset(0f, 15.dp.toPx()), label.copy(color = Sea.Navy, fontWeight = FontWeight.Bold))
                if (part < 1f) {
                    val head = measure.getPosition(measure.length * part)
                    drawCircle(Sea.BlueBright.copy(alpha = 0.25f), 11.dp.toPx(), head)
                    drawCircle(Color.White, 6.dp.toPx(), head)
                    drawCircle(Sea.BlueBright, 4.dp.toPx(), head)
                }
                if (detailed) {
                    // One mile, to judge the rest by.
                    val from = Offset(20.dp.toPx(), top.toPx() + free.height - 10.dp.toPx())
                    drawLine(Sea.Navy, from, from + Offset(scale, 0f), 2.dp.toPx())
                    drawLine(Sea.Navy, from + Offset(0f, -4.dp.toPx()), from + Offset(0f, 4.dp.toPx()), 2.dp.toPx())
                    drawLine(Sea.Navy, from + Offset(scale, -4.dp.toPx()), from + Offset(scale, 4.dp.toPx()), 2.dp.toPx())
                    centred(measurer, "1 миля", from + Offset(scale / 2, -9.dp.toPx()), label.copy(color = Sea.Navy, fontWeight = FontWeight.SemiBold))
                }
            }
        },
    )
}

private fun DrawScope.centred(measurer: TextMeasurer, text: String, at: Offset, style: TextStyle) {
    val laid = measurer.measure(text, style)
    val corner = Offset(at.x - laid.size.width / 2f, at.y - laid.size.height / 2f)
    // Text that would start off the canvas cannot be drawn at all.
    if (corner.x < 0f || corner.y < 0f || corner.x + laid.size.width > size.width || corner.y + laid.size.height > size.height) return
    drawText(laid, topLeft = corner)
}
