package seanata.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import seanata.data.*
import seanata.res.*
import seanata.theme.Sea
import seanata.ui.components.*

/** A training: where and when, who is in, what will happen, and the place in the crew. */
@Composable
fun TrainingScreen(
    training: Training,
    booked: Boolean,
    onBack: () -> Unit,
    onBook: () -> Unit,
    onCancel: () -> Unit,
) {
    val day = SampleData.week[training.day]
    val taken = training.taken + if (booked) 1 else 0
    val free = training.seats - taken
    Page(
        bottom = {
            ActionBar(training.price.usd(), training.price.dong()) {
                when {
                    booked -> SecondaryButton("Отменить запись", onCancel, it, color = Sea.Alert)
                    free <= 0 -> SecondaryButton("Мест нет", {}, it, color = Sea.Muted)
                    else -> PrimaryButton("Записаться", onBook, it)
                }
            }
        },
        backdrop = { Backdrop(training.hero, 390, fade = false) },
    ) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp)) {
            TopBar("Тренировка", onBack)
            Spacer(Modifier.height(170.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Tag(training.tag, solid = true, glass = true)
                if (booked) Tag("Вы записаны", glass = true)
            }
            Spacer(Modifier.height(10.dp))
            Txt(training.title, size = 28, weight = FontWeight.Bold, color = Color.White, tracking = -0.6, lineHeight = 32)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Meta(AppIcons.Pin, SampleData.marina, Modifier.weight(1f), color = Color.White, size = 15)
                Box(Modifier.glass(9.dp, GlassTone.Clear).padding(horizontal = 10.dp, vertical = 4.dp)) {
                    Txt(training.yacht, size = 13, weight = FontWeight.Medium, color = Color.White)
                }
            }
            Spacer(Modifier.height(18.dp))
        }
        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)).background(Sea.Card)
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 26.dp),
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Fact(AppIcons.Calendar, "${day.short}, ${day.number} ${SampleData.monthShort}", training.time, Modifier.weight(1f))
                VLine(56.dp)
                Fact(AppIcons.Users, "$taken / ${training.seats}", if (free > 0) "свободно: $free" else "мест нет", Modifier.weight(1f))
                VLine(56.dp)
                Fact(AppIcons.Level, training.level, "уровень", Modifier.weight(1f))
            }
            Box(Modifier.padding(vertical = 18.dp).fillMaxWidth().height(1.dp).background(Sea.Line))
            Txt("О тренировке", size = 20, weight = FontWeight.Bold, tracking = -0.3)
            Spacer(Modifier.height(8.dp))
            Txt(training.about, size = 15, lineHeight = 22)
            Spacer(Modifier.height(22.dp))
            Txt("План занятия", size = 20, weight = FontWeight.Bold, tracking = -0.3)
            Spacer(Modifier.height(10.dp))
            SampleData.plan.forEach { step ->
                Row(Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Ico(step.icon, size = 24.dp, tint = Sea.Blue)
                    Txt(step.text, Modifier.weight(1f), size = 15)
                }
            }
        }
    }
}

@Composable
private fun Fact(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    caption: String,
    modifier: Modifier,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Ico(icon, size = 26.dp, tint = Sea.Blue)
        Txt(value, size = 15, weight = FontWeight.Bold, maxLines = 1)
        Txt(caption, size = 12, color = Sea.Muted, maxLines = 1)
    }
}
