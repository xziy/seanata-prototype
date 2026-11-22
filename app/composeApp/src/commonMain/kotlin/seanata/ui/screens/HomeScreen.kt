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

/** The home of a student: where the course stands, the next training, and the shortcuts. */
@Composable
fun HomeScreen(
    next: Training,
    taken: Int,
    booked: Boolean,
    onCourse: () -> Unit,
    onTraining: () -> Unit,
    onTab: (Tab) -> Unit,
    bottom: @Composable () -> Unit,
) {
    val day = SampleData.week[next.day]
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { shown = true }
    val progress by animateFloatAsState(if (shown) SampleData.theoryProgress else 0f, tween(900, delayMillis = 300))
    Page(bottom = bottom, backdrop = { Backdrop(Res.drawable.sea_dusk, 330) }, sheet = 330) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 24.dp)) {
            BrandBar(onProfile = { onTab(Tab.Profile) })
            Spacer(Modifier.height(22.dp))
            Txt("Привет, ${SampleData.student}!", size = 30, weight = FontWeight.Bold, color = Color.White, tracking = -0.6)
            Spacer(Modifier.height(6.dp))
            Txt("Продолжай обучение и выходи в море", size = 16, color = Color(0xE6FFFFFF))
            Spacer(Modifier.height(20.dp))

            Row(
                Modifier.fillMaxWidth().tap(onCourse).card().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Photo(Res.drawable.yacht_day, Modifier.size(72.dp).clip(RoundedCornerShape(14.dp)))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Txt(SampleData.courses.first().title, size = 18, weight = FontWeight.Bold, tracking = -0.3)
                    Row {
                        Txt("Теория: ", size = 14, color = Sea.Muted)
                        Txt("${(SampleData.theoryProgress * 100).toInt()}%", size = 14, weight = FontWeight.Bold)
                    }
                    Box(Modifier.fillMaxWidth().height(7.dp).clip(CircleShape).background(Sea.Tint)) {
                        Box(Modifier.fillMaxWidth(progress).height(7.dp).clip(CircleShape).background(Sea.Action))
                    }
                }
                Ico(AppIcons.ChevronRight, size = 24.dp)
            }

            Spacer(Modifier.height(30.dp))
            SectionTitle("Ближайшие тренировки", action = "Смотреть все", onAction = { onTab(Tab.Schedule) })
            Spacer(Modifier.height(14.dp))
            Column(Modifier.fillMaxWidth().tap(onTraining).card()) {
                Box(Modifier.fillMaxWidth().height(150.dp)) {
                    Photo(next.hero, Modifier.matchParentSize())
                    Box(Modifier.matchParentSize().background(Sea.Navy.copy(alpha = 0.62f)))
                    Row(Modifier.matchParentSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(
                            Modifier.width(64.dp).clip(RoundedCornerShape(14.dp)).background(Color.White).padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Txt(day.short, size = 13, weight = FontWeight.SemiBold)
                            Txt("${day.number}", size = 28, weight = FontWeight.Bold, lineHeight = 32)
                            Txt(SampleData.monthShort.uppercase(), size = 11, weight = FontWeight.Bold)
                        }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Txt(next.title, size = 20, weight = FontWeight.Bold, color = Color.White, tracking = -0.3)
                            Txt("${SampleData.marina}  •  ${next.yacht}", size = 13, color = Color(0xE6FFFFFF), maxLines = 1)
                            Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Meta(AppIcons.Clock, next.time, Modifier.weight(1f), color = Color.White, size = 14)
                                Meta(AppIcons.Users, "$taken / ${next.seats}", color = Color.White, size = 14)
                            }
                        }
                    }
                }
                Box(Modifier.padding(12.dp)) {
                    if (booked) {
                        SecondaryButton("Вы записаны", onTraining, Modifier.fillMaxWidth(), color = Sea.Success, icon = AppIcons.Check)
                    } else {
                        PrimaryButton("Записаться", onTraining, Modifier.fillMaxWidth())
                    }
                }
            }

            Spacer(Modifier.height(30.dp))
            SectionTitle("Быстрый доступ")
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    Triple(AppIcons.Cap, "Курсы") { onTab(Tab.Courses) },
                    Triple(AppIcons.Calendar, "Расписание") { onTab(Tab.Schedule) },
                    Triple(AppIcons.Book, "Обучение", onCourse),
                    Triple(AppIcons.Anchor, "Логбук") { onTab(Tab.Logbook) },
                ).forEach { (icon, label, go) ->
                    Column(
                        Modifier.weight(1f).tap(go).card(radius = 16.dp, elevation = 6.dp).padding(vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Ico(icon, size = 28.dp, tint = Sea.Blue)
                        Txt(label, size = 12, weight = FontWeight.SemiBold, maxLines = 1)
                    }
                }
            }

            Spacer(Modifier.height(26.dp))
            Banner(Res.drawable.gift, "Подарочный\nсертификат", "Удиви близкого\nприключением под парусом", onClick = {}, shade = Color(0xFF1B2330))
        }
    }
}
