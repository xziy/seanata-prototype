package seanata.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/** Lucide icons (24×24, stroke 2) from their SVG path data; tinted where they are drawn. */
object AppIcons {
    val Home = icon("M15 21v-8a1 1 0 0 0-1-1h-4a1 1 0 0 0-1 1v8", "M3 10a2 2 0 0 1 .709-1.528l7-5.999a2 2 0 0 1 2.582 0l7 5.999A2 2 0 0 1 21 10v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z")
    val Book = icon("M12 7v14", "M3 18a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1h5a4 4 0 0 1 4 4 4 4 0 0 1 4-4h5a1 1 0 0 1 1 1v13a1 1 0 0 1-1 1h-6a3 3 0 0 0-3 3 3 3 0 0 0-3-3z")
    val Calendar = icon("M8 2v4", "M16 2v4", "M5 4h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2z", "M3 10h18", "M8 14h.01", "M12 14h.01", "M16 14h.01", "M8 18h.01", "M12 18h.01", "M16 18h.01")
    val Logbook = icon("M10 2v8l3-3 3 3V2", "M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H19a1 1 0 0 1 1 1v18a1 1 0 0 1-1 1H6.5a1 1 0 0 1 0-5H20")
    val User = icon("M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2", "M16 7a4 4 0 1 1-8 0a4 4 0 0 1 8 0z")
    val Bell = icon("M10.268 21a2 2 0 0 0 3.464 0", "M3.262 15.326A1 1 0 0 0 4 17h16a1 1 0 0 0 .74-1.673C19.41 13.956 18 12.499 18 8A6 6 0 0 0 6 8c0 4.499-1.411 5.956-2.738 7.326")
    val ChevronRight = icon("m9 18 6-6-6-6")
    val ChevronLeft = icon("m15 18-6-6 6-6")
    val ChevronDown = icon("m6 9 6 6 6-6")
    val ArrowRight = icon("M5 12h14", "m12 5 7 7-7 7")
    val Share = icon("M12 2v13", "m16 6-4-4-4 4", "M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8")
    val Clock = icon("M22 12a10 10 0 1 1-20 0a10 10 0 0 1 20 0z", "M12 6v6l4 2")
    val Pin = icon("M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0", "M15 10a3 3 0 1 1-6 0a3 3 0 0 1 6 0z")
    val Users = icon("M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2", "M13 7a4 4 0 1 1-8 0a4 4 0 0 1 8 0z", "M22 21v-2a4 4 0 0 0-3-3.87", "M16 3.13a4 4 0 0 1 0 7.75")
    val Cap = icon("M21.42 10.922a1 1 0 0 0-.019-1.838L12.83 5.18a2 2 0 0 0-1.66 0L2.6 9.08a1 1 0 0 0 0 1.832l8.57 3.908a2 2 0 0 0 1.66 0z", "M22 10v6", "M6 12.5V16a6 3 0 0 0 12 0v-3.5")
    val Award = icon("m15.477 12.89 1.515 8.526a.5.5 0 0 1-.81.47l-3.58-2.687a1 1 0 0 0-1.197 0l-3.586 2.686a.5.5 0 0 1-.81-.469l1.514-8.526", "M18 8a6 6 0 1 1-12 0a6 6 0 0 1 12 0z")
    val Sailboat = icon("M22 18H2a4 4 0 0 0 4 4h12a4 4 0 0 0 4-4z", "M21 14 10 2 3 14h18z", "M10 2v16")
    val Anchor = icon("M12 22V8", "M5 12H2a10 10 0 0 0 20 0h-3", "M15 5a3 3 0 1 1-6 0a3 3 0 0 1 6 0z")
    val Level = icon("M12 20V10", "M18 20V4", "M6 20v-4")
    val Filter = icon("M22 3H2l8 9.46V19l4 2v-8.54L22 3z")
    val Flag = icon("M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z", "M4 22v-7")
    val Wheel = icon("M20 12a8 8 0 1 1-16 0a8 8 0 0 1 16 0z", "M14.5 12a2.5 2.5 0 1 1-5 0a2.5 2.5 0 0 1 5 0z", "M12 2v7.5", "M12 14.5V22", "M2 12h7.5", "M14.5 12H22", "m19 5-5.23 5.23", "m5 19 5.23-5.23", "m5 5 5.23 5.23", "m19 19-5.23-5.23")
    val Check = icon("M20 6 9 17l-5-5")
    val Compass = icon("M22 12a10 10 0 1 1-20 0a10 10 0 0 1 20 0z", "m16.24 7.76-1.804 5.411a2 2 0 0 1-1.265 1.265L7.76 16.24l1.804-5.411a2 2 0 0 1 1.265-1.265z")
    val LogOut = icon("m16 17 5-5-5-5", "M21 12H9", "M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4")

    private fun icon(vararg paths: String): ImageVector =
        ImageVector.Builder("icon", 24.dp, 24.dp, 24f, 24f).apply {
            paths.forEach { d ->
                addPath(
                    pathData = addPathNodes(d),
                    fill = null,
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                )
            }
        }.build()
}
