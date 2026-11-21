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

/** A course: the photo and the price first, then what it is, its days, and what it asks for. */
@Composable
fun CourseScreen(
    course: Course,
    tab: Int,
    onTab: (Int) -> Unit,
    onBack: () -> Unit,
    onEnroll: () -> Unit,
) {
    Page(
        bottom = { ActionBar(course.price.usd(), course.price.dong()) { PrimaryButton("Купить курс", onEnroll, it) } },
        backdrop = { Backdrop(course.photo, 400, fade = false) },
    ) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp)) {
            TopBar("Курс", onBack)
            Spacer(Modifier.height(168.dp))
            Txt(course.title, size = 30, weight = FontWeight.Bold, color = Color.White, tracking = -0.6)
            Spacer(Modifier.height(4.dp))
            Txt(course.subtitle, size = 16, color = Color(0xE6FFFFFF))
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill(AppIcons.Clock, course.days)
                Pill(AppIcons.Cap, course.entry)
                Pill(AppIcons.Award, "Сертификат")
            }
            Spacer(Modifier.height(18.dp))
        }
        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)).background(Sea.Bg)
                .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Segmented(listOf("О курсе", "Программа", "Требования"), tab, onTab, Modifier.fillMaxWidth())
            AnimatedContent(tab, transitionSpec = { slideFade(targetState > initialState) }, label = "tab") { shown ->
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    when (shown) {
                        0 -> {
                            TextCard("О курсе", course.about)
                            Column(Modifier.fillMaxWidth().card().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Txt("Что входит", size = 19, weight = FontWeight.Bold, tracking = -0.3)
                                SampleData.includes.forEach { (icon, text) ->
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                                        Ico(icon, size = 24.dp, tint = Sea.Blue)
                                        Txt(text, Modifier.weight(1f), size = 14, lineHeight = 19)
                                    }
                                }
                            }
                            Row(
                                Modifier.fillMaxWidth().card().padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                            ) {
                                Photo(Res.drawable.instructor, Modifier.size(76.dp).clip(RoundedCornerShape(14.dp)))
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                    Txt("Инструктор: Андрей", size = 17, weight = FontWeight.Bold, tracking = -0.3)
                                    Txt("Сертифицированный инструктор с 10+ годами опыта", size = 13, color = Sea.Muted, lineHeight = 18)
                                }
                                Ico(AppIcons.ChevronRight, size = 22.dp)
                            }
                        }

                        1 -> {
                            Txt("Программа курса", Modifier.padding(top = 6.dp), size = 20, weight = FontWeight.Bold, tracking = -0.3)
                            Column {
                                SampleData.program.forEachIndexed { i, day ->
                                    TimelineRow(first = i == 0, last = i == SampleData.program.lastIndex) {
                                        Row(
                                            Modifier.weight(1f).card(radius = 16.dp, elevation = 6.dp).padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                                        ) {
                                            Photo(day.photo, Modifier.size(width = 104.dp, height = 64.dp).clip(RoundedCornerShape(11.dp)))
                                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                                Txt(day.title, size = 16, weight = FontWeight.Bold)
                                                Txt(day.text, size = 13, lineHeight = 17)
                                            }
                                            Ico(AppIcons.ChevronRight, size = 20.dp)
                                        }
                                    }
                                }
                            }
                        }

                        else -> Column(Modifier.fillMaxWidth().card().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Txt("Что нужно для старта", size = 19, weight = FontWeight.Bold, tracking = -0.3)
                            SampleData.requirements.forEach { text ->
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Box(Modifier.size(26.dp).clip(CircleShape).background(Sea.Tint), contentAlignment = Alignment.Center) {
                                        Ico(AppIcons.Check, size = 15.dp, tint = Sea.Blue)
                                    }
                                    Txt(text, Modifier.weight(1f), size = 14, lineHeight = 19)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
