package seanata.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import seanata.data.Course
import seanata.data.Training
import seanata.data.dong
import seanata.data.usd
import seanata.theme.Sea

/** A course in a list: photo, name, how long and from how much. */
@Composable
fun CourseCard(
    course: Course,
    onClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().tap(onClick).card(radius = 18.dp).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Photo(course.photo, Modifier.size(width = 92.dp, height = 88.dp).clip(RoundedCornerShape(12.dp)))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Txt(course.title, size = 17, weight = FontWeight.Bold, tracking = -0.3, maxLines = 1)
            Txt(course.subtitle, size = 12, color = Sea.Muted, maxLines = 1)
            Row(Modifier.padding(top = 3.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Txt(course.price.usd(), size = 16, weight = FontWeight.Bold, color = Sea.Blue)
                Txt(course.price.dong(), size = 12, color = Sea.Muted, maxLines = 1)
            }
        }
        Ico(AppIcons.ChevronRight, size = 22.dp)
    }
}

/** A training in the schedule: the yacht's photo, what and when, and how many of the crew are in. */
@Composable
fun TrainingCard(
    training: Training,
    taken: Int,
    booked: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().tap(onClick).card(radius = 18.dp).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(Modifier.size(width = 108.dp, height = 104.dp).clip(RoundedCornerShape(12.dp))) {
            Photo(training.photo, Modifier.matchParentSize())
            Box(
                Modifier.align(Alignment.BottomStart).padding(6.dp).clip(RoundedCornerShape(7.dp))
                    .background(Color(0xB3071A36)).padding(horizontal = 7.dp, vertical = 3.dp),
            ) {
                Txt(training.yacht, size = 11, weight = FontWeight.Medium, color = Color.White)
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Tag(training.tag)
                if (booked) Tag("Вы записаны", solid = true)
            }
            Txt(training.title, size = 16, weight = FontWeight.Bold, tracking = -0.3, maxLines = 2, lineHeight = 20)
            Meta(AppIcons.Clock, training.time, color = Sea.Ink)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Meta(AppIcons.Level, training.level, Modifier.weight(1f))
                Seats(taken, training.seats)
            }
        }
        Ico(AppIcons.ChevronRight, size = 22.dp)
    }
}

/** «3 / 6»: places taken out of the crew's size. */
@Composable
fun Seats(
    taken: Int,
    seats: Int,
) {
    val full = taken >= seats
    Row(
        Modifier.clip(RoundedCornerShape(10.dp)).background(if (full) Sea.Line else Sea.Tint).padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Ico(AppIcons.Users, size = 15.dp, tint = if (full) Sea.Muted else Sea.Blue)
        Txt("$taken / $seats", size = 13, weight = FontWeight.SemiBold, color = if (full) Sea.Muted else Sea.Blue)
    }
}

/** A wide photo with words on its darkened left side and «further» on the right. */
@Composable
fun Banner(
    photo: DrawableResource,
    title: String,
    text: String,
    onClick: () -> Unit,
    shade: Color = Sea.Navy,
) {
    Box(Modifier.fillMaxWidth().height(124.dp).tap(onClick).card(radius = 20.dp)) {
        Photo(photo, Modifier.matchParentSize())
        Box(Modifier.matchParentSize().background(Brush.horizontalGradient(0f to shade, 0.55f to shade.copy(alpha = 0.75f), 1f to Color.Transparent)))
        Row(Modifier.matchParentSize().padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Txt(title, size = 19, weight = FontWeight.Bold, color = Color.White, lineHeight = 23, tracking = -0.3)
                Txt(text, size = 13, color = Color(0xE6FFFFFF), lineHeight = 18)
            }
            RoundChevron(Modifier.padding(start = 12.dp))
        }
    }
}

/** A row on a vertical line with a ring: a day of a course, a step of a plan. */
@Composable
fun TimelineRow(
    first: Boolean,
    last: Boolean,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().drawBehind {
            val x = 9.dp.toPx()
            drawLine(
                Sea.Blue,
                Offset(x, if (first) size.height / 2 else 0f),
                Offset(x, if (last) size.height / 2 else size.height),
                strokeWidth = 2.dp.toPx(),
            )
        }.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(Modifier.width(18.dp), contentAlignment = Alignment.Center) {
            Box(Modifier.size(14.dp).clip(CircleShape).background(Color.White).border(3.dp, Sea.Blue, CircleShape))
        }
        content()
    }
}
