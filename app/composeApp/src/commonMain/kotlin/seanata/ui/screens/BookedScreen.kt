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

/** The end of the booking scenario: the place is taken, here is when and where to come. */
@Composable
fun BookedScreen(
    training: Training,
    onSchedule: () -> Unit,
    onHome: () -> Unit,
) {
    val day = SampleData.week[training.day]
    val safe = LocalSafe.current
    Box(Modifier.fillMaxSize()) {
        Photo(Res.drawable.hero_deck, Modifier.matchParentSize())
        Box(Modifier.matchParentSize().background(Sea.NavyDeep.copy(alpha = 0.78f)))
        Column(Modifier.fillMaxSize().padding(start = 22.dp, end = 22.dp, top = 28.dp + safe.top, bottom = 28.dp + safe.bottom), horizontalAlignment = Alignment.CenterHorizontally) {
            Logo(height = 36.dp)
            Spacer(Modifier.weight(1f))
            Box(Modifier.size(84.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                Ico(AppIcons.Check, size = 44.dp, tint = Sea.Blue)
            }
            Spacer(Modifier.height(22.dp))
            Txt("Вы в экипаже!", size = 32, weight = FontWeight.Bold, color = Color.White, tracking = -0.6)
            Spacer(Modifier.height(8.dp))
            Txt("Место забронировано. Напомним о тренировке\nза день и за два часа до выхода.", size = 15, color = Color(0xE6FFFFFF), align = TextAlign.Center, lineHeight = 22)
            Spacer(Modifier.height(26.dp))
            Column(Modifier.fillMaxWidth().card().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Tag(training.tag) }
                Txt(training.title, size = 20, weight = FontWeight.Bold, tracking = -0.3)
                Box(Modifier.fillMaxWidth().height(1.dp).background(Sea.Line))
                Meta(AppIcons.Calendar, "${day.short}, ${day.number} ${SampleData.monthGenitive}  •  ${training.time}", color = Sea.Ink, size = 15)
                Meta(AppIcons.Pin, "${SampleData.marina}, причал B", color = Sea.Ink, size = 15)
                Meta(AppIcons.Sailboat, "${training.yacht}  •  место ${training.taken + 1} из ${training.seats}", color = Sea.Ink, size = 15)
                Meta(AppIcons.Clock, "Сбор экипажа за 15 минут до выхода", color = Sea.Ink, size = 15)
            }
            Spacer(Modifier.weight(1f))
            PrimaryButton("К расписанию", onSchedule, Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            Box(Modifier.fillMaxWidth().height(48.dp).tap(onHome), contentAlignment = Alignment.Center) {
                Txt("На главную", size = 16, weight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}
