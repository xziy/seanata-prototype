package seanata.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import seanata.data.*
import seanata.res.*
import seanata.theme.Sea
import seanata.ui.components.*

/** The logbook: the sea time the student has, and an entry for every passage. */
@Composable
fun LogbookScreen(
    onPassage: (Passage) -> Unit,
    onProfile: () -> Unit,
    bottom: @Composable () -> Unit,
) {
    Page(bottom = bottom, backdrop = { Backdrop(Res.drawable.sea_dusk, 350) }, sheet = 350) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            BrandBar(onProfile = onProfile)
            Txt("Логбук", Modifier.padding(top = 8.dp), size = 28, weight = FontWeight.Bold, color = Color.White, tracking = -0.6)
            Txt("Морской стаж и записи о каждом выходе", Modifier.padding(bottom = 6.dp), size = 15, color = Color(0xE6FFFFFF))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    Logbook.miles.one() to "миль пройдено",
                    (Logbook.minutes / 60.0).one() to "часов в море",
                    "${Logbook.passages.size}" to "выходов",
                ).forEach { (value, label) ->
                    Column(Modifier.weight(1f).glass(18.dp, GlassTone.Clear).padding(vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Txt(value, size = 26, weight = FontWeight.Bold, color = Color.White, tracking = -0.5)
                        Txt(label, size = 12, color = Color(0xE6FFFFFF))
                    }
                }
            }

            Column(Modifier.fillMaxWidth().card().padding(horizontal = 18.dp, vertical = 6.dp)) {
                listOf(
                    Triple(AppIcons.Sailboat, "Под парусом", "${Logbook.sailMiles.one()} мили"),
                    Triple(AppIcons.Wheel, "Под мотором", "${Logbook.motorMiles.one()} мили"),
                    Triple(AppIcons.Clock, "Ночные часы", span(Logbook.nightMinutes)),
                    Triple(AppIcons.Users, "В должности", "матрос, ${Logbook.passages.size} выходов"),
                ).forEachIndexed { i, (icon, label, value) ->
                    if (i > 0) Box(Modifier.fillMaxWidth().height(1.dp).background(Sea.Line))
                    Row(Modifier.padding(vertical = 13.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Ico(icon, size = 20.dp, tint = Sea.Blue)
                        Txt(label, Modifier.weight(1f), size = 15)
                        Txt(value, size = 15, weight = FontWeight.SemiBold)
                    }
                }
            }

            SectionTitle("Записи журнала", Modifier.padding(top = 10.dp))
            Logbook.passages.forEach { passage ->
                Row(
                    Modifier.fillMaxWidth().tap { onPassage(passage) }.card(radius = 18.dp).padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    BayChart(passage.track, Modifier.size(width = 84.dp, height = 84.dp).clip(RoundedCornerShape(12.dp)))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Txt(passage.title, size = 16, weight = FontWeight.Bold, tracking = -0.3, maxLines = 1)
                        Txt("${passage.day} ${SampleData.monthGenitive}  •  ${passage.boat.type}", size = 13, color = Sea.Muted, maxLines = 1)
                        Row(Modifier.padding(top = 2.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Txt("${passage.miles.one()} мили", size = 15, weight = FontWeight.Bold, color = Sea.Blue)
                            Txt(span(passage.minutes), size = 13, color = Sea.Muted)
                        }
                        Signature(passage.signed)
                    }
                    Ico(AppIcons.ChevronRight, size = 22.dp)
                }
            }
        }
    }
}

/** Whether the skipper has signed an entry: without the signature the miles do not count towards a certificate. */
@Composable
private fun Signature(signed: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Ico(if (signed) AppIcons.Check else AppIcons.Clock, size = 13.dp, tint = if (signed) Sea.Success else Sea.Muted)
        Txt(if (signed) "Подписано шкипером" else "Ждёт подписи шкипера", size = 12, weight = FontWeight.Medium, color = if (signed) Sea.Success else Sea.Muted)
    }
}

/** An entry of the logbook: the track on the chart, then everything a logbook asks about a passage. */
@Composable
fun PassageScreen(
    passage: Passage,
    onBack: () -> Unit,
) {
    val drawn = remember(passage.id) { Animatable(0f) }
    LaunchedEffect(passage.id) { drawn.animateTo(1f, tween(1800, delayMillis = 350, easing = FastOutSlowInEasing)) }
    val safe = LocalSafe.current
    Page(
        bottom = { ActionBar(null) { PrimaryButton(if (passage.signed) "Поделиться записью" else "Отправить на подпись", {}, it) } },
        backdrop = {
            Box(Modifier.fillMaxWidth().height(safe.top + 470.dp)) {
                BayChart(passage.track, Modifier.matchParentSize(), detailed = true, drawn = { drawn.value }, top = safe.top + 84.dp, bottom = 150.dp, span = 4.5)
                Scrim(0f to Sea.Navy.copy(alpha = 0.85f), 1f to Color.Transparent, modifier = Modifier.fillMaxWidth().height(safe.top + 110.dp))
            }
        },
    ) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp)) {
            TopBar("Запись № ${passage.id}", onBack)
            Spacer(Modifier.height(270.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    passage.miles.one() to "мили",
                    span(passage.minutes).removeSuffix(" мин").replace(" ч ", ":") to "в море, ч",
                    passage.average.one() to "узла, средняя",
                ).forEach { (value, label) ->
                    Column(Modifier.weight(1f).glass(18.dp, GlassTone.Smoke).padding(vertical = 11.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Txt(value, size = 23, weight = FontWeight.Bold, color = Color.White, tracking = -0.4)
                        Txt(label, size = 12, color = Color(0xE6FFFFFF))
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
        }
        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)).background(Sea.Bg)
                .padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Txt(passage.title, size = 24, weight = FontWeight.Bold, tracking = -0.5)
                Txt(passage.route, size = 14, color = Sea.Muted, lineHeight = 20)
            }

            Sheet("Переход") {
                Line("Дата", passage.date)
                Line("Яхта", "«${passage.boat.name}», ${passage.boat.type}")
                Line("Длина, номер на парусе", "${passage.boat.length}, ${passage.boat.sail}")
                Line("Порт отхода и прихода", Logbook.home)
                Line("Отход — приход", "${clock(passage.start)} — ${clock(passage.end)}")
                Line("Пройдено по лагу", "${passage.miles.one()} мили")
                Line("Под парусом / под мотором", "${passage.sailMiles.one()} / ${passage.motorMiles.one()} мили")
                Line("На ходу", span(passage.underway))
                Line("Ночные часы", if (passage.nightMinutes > 0) span(passage.nightMinutes) else "—")
                Line("Средняя / наибольшая скорость", "${passage.average.one()} / ${passage.fastest.one()} уз")
                Line("Должность на борту", Logbook.role, last = true)
            }

            Sheet("Погода") {
                Line("Ветер", passage.wind)
                Line("Море", passage.sea)
                Line("Погода", passage.weather)
                Line("Давление", passage.pressure, last = true)
            }

            Column(Modifier.fillMaxWidth().card().padding(18.dp)) {
                Txt("Вахтенный журнал", size = 19, weight = FontWeight.Bold, tracking = -0.3)
                Txt("Время  •  истинный курс  •  отсчёт лага  •  ход", Modifier.padding(top = 4.dp, bottom = 10.dp), size = 12, color = Sea.Muted)
                passage.lines.forEachIndexed { i, line ->
                    TimelineRow(first = i == 0, last = i == passage.lines.lastIndex) {
                        Column(Modifier.weight(1f).padding(vertical = 5.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Txt(line.time, size = 15, weight = FontWeight.Bold)
                                Txt(
                                    listOfNotNull(line.course?.let { "ИК $it" }, "лаг ${line.log.one()}", line.speed?.let { "${it.one()} уз" }).joinToString("  •  "),
                                    size = 12, weight = FontWeight.Medium, color = Sea.Blue, maxLines = 1,
                                )
                            }
                            Txt(line.note, size = 14, lineHeight = 19)
                        }
                    }
                }
            }

            Column(Modifier.fillMaxWidth().card().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Txt("Отработано", size = 19, weight = FontWeight.Bold, tracking = -0.3)
                passage.skills.forEach { skill ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(Modifier.size(26.dp).clip(CircleShape).background(Sea.Tint), contentAlignment = Alignment.Center) {
                            Ico(AppIcons.Check, size = 15.dp, tint = Sea.Blue)
                        }
                        Txt(skill, Modifier.weight(1f), size = 14, lineHeight = 19)
                    }
                }
            }

            Row(Modifier.fillMaxWidth().card().padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Photo(Res.drawable.instructor, Modifier.size(64.dp).clip(RoundedCornerShape(14.dp)))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Txt("Шкипер: ${Logbook.skipper}", size = 16, weight = FontWeight.Bold, tracking = -0.3)
                    Txt("Инструктор школы Seanata", size = 13, color = Sea.Muted)
                    Signature(passage.signed)
                }
            }
        }
    }
}

/** A titled card of a logbook's page: rows of what is asked and what was written. */
@Composable
private fun Sheet(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(Modifier.fillMaxWidth().card().padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 6.dp)) {
        Txt(title, Modifier.padding(bottom = 6.dp), size = 19, weight = FontWeight.Bold, tracking = -0.3)
        content()
    }
}

@Composable
private fun Line(
    label: String,
    value: String,
    last: Boolean = false,
) {
    Row(Modifier.fillMaxWidth().padding(vertical = 11.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Txt(label, Modifier.width(132.dp), size = 13, color = Sea.Muted, lineHeight = 18)
        Txt(value, Modifier.weight(1f), size = 14, weight = FontWeight.Medium, lineHeight = 18)
    }
    if (!last) Box(Modifier.fillMaxWidth().height(1.dp).background(Sea.Line))
}
