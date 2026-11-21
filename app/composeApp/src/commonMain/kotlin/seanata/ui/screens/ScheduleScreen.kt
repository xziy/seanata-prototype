package seanata.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
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

/** The week and its trainings: pick a day, see who sails what and how many places are left. */
@Composable
fun ScheduleScreen(
    day: Int,
    onDay: (Int) -> Unit,
    booked: Set<Int>,
    onTraining: (Training) -> Unit,
    onProfile: () -> Unit,
    bottom: @Composable () -> Unit,
) {
    Page(bottom = bottom, backdrop = { Backdrop(Res.drawable.sea_dusk, 330) }) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 24.dp)) {
            BrandBar(onProfile = onProfile)
            Spacer(Modifier.height(20.dp))
            Txt("Расписание тренировок", size = 28, weight = FontWeight.Bold, color = Color.White, tracking = -0.6)
            Spacer(Modifier.height(6.dp))
            Txt("Выбирай тренировку и бронируй место в экипаже", size = 14, color = Color(0xE6FFFFFF))
            Spacer(Modifier.height(20.dp))

            Column(Modifier.fillMaxWidth().card().padding(horizontal = 10.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Ico(AppIcons.ChevronLeft, size = 22.dp)
                    Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically) {
                        Txt(SampleData.month, size = 17, weight = FontWeight.Bold)
                        Ico(AppIcons.ChevronDown, size = 18.dp)
                    }
                    Ico(AppIcons.ChevronRight, size = 22.dp)
                }
                Row(Modifier.fillMaxWidth()) {
                    SampleData.week.forEachIndexed { i, d ->
                        val on = i == day
                        val has = SampleData.trainings.any { it.day == i }
                        val glow by animateFloatAsState(if (on) 1f else 0f, tween(240))
                        val ink by animateColorAsState(if (on) Color.White else Sea.Ink, tween(240))
                        val soft by animateColorAsState(if (on) Color.White else Sea.Muted, tween(240))
                        val dot by animateColorAsState(if (!has) Color.Transparent else if (on) Color.White else Sea.BlueBright, tween(240))
                        Box(Modifier.weight(1f).padding(horizontal = 3.dp).tap { onDay(i) }) {
                            Box(Modifier.matchParentSize().graphicsLayer { alpha = glow }.clip(RoundedCornerShape(14.dp)).background(Sea.Action))
                            Column(
                                Modifier.fillMaxWidth().padding(top = 9.dp, bottom = 7.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Txt(d.weekday, size = 13, color = soft)
                                Txt("${d.number}", size = 18, weight = FontWeight.Bold, color = ink)
                                Box(Modifier.size(5.dp).clip(CircleShape).background(dot))
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(AppIcons.Pin to "Нячанг", AppIcons.Sailboat to "Все яхты", AppIcons.Level to "Уровень").forEach { (icon, label) ->
                    Row(
                        Modifier.weight(1f).card(radius = 14.dp, elevation = 5.dp).padding(horizontal = 10.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterHorizontally),
                    ) {
                        Ico(icon, size = 17.dp, tint = Sea.Blue)
                        Txt(label, size = 13, weight = FontWeight.Medium, maxLines = 1)
                    }
                }
            }

            AnimatedContent(day, transitionSpec = { slideFade(targetState > initialState) }, label = "day") { shown ->
                val trainings = SampleData.trainings.filter { it.day == shown }
                Column {
                    Spacer(Modifier.height(24.dp))
                    SectionTitle("Тренировки ${SampleData.week[shown].number} ${SampleData.monthGenitive}", action = if (trainings.isEmpty()) null else lessons(trainings.size))
                    Spacer(Modifier.height(14.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        trainings.forEach { t ->
                            val mine = t.id in booked
                            TrainingCard(t, taken = t.taken + if (mine) 1 else 0, booked = mine) { onTraining(t) }
                        }
                        if (trainings.isEmpty()) {
                            Column(
                                Modifier.fillMaxWidth().card().padding(vertical = 34.dp, horizontal = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Ico(AppIcons.Anchor, size = 34.dp, tint = Sea.Blue)
                                Txt("В этот день яхты отдыхают", size = 17, weight = FontWeight.Bold)
                                Txt("Выберите другой день: точка под датой показывает, когда есть тренировки", size = 13, color = Sea.Muted, align = TextAlign.Center, lineHeight = 18)
                            }
                        }
                    }
                }
            }
        }
    }
}
