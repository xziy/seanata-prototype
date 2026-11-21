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

/** The logbook, as far as the prototype goes: the totals and the last outings. */
@Composable
fun LogbookScreen(
    onProfile: () -> Unit,
    bottom: @Composable () -> Unit,
) {
    Page(bottom = bottom, backdrop = { Backdrop(Res.drawable.sea_dusk, 250) }) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            BrandBar(onProfile = onProfile)
            Txt("Логбук", Modifier.padding(top = 8.dp), size = 28, weight = FontWeight.Bold, color = Color.White, tracking = -0.6)
            Txt("Часы практики, мили и освоенные навыки", Modifier.padding(bottom = 8.dp), size = 15, color = Color(0xE6FFFFFF))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("18" to "часов", "64" to "мили", "6" to "выходов").forEach { (value, label) ->
                    Column(Modifier.weight(1f).card(radius = 16.dp).padding(vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Txt(value, size = 26, weight = FontWeight.Bold, color = Sea.Blue)
                        Txt(label, size = 13, color = Sea.Muted)
                    }
                }
            }
            SectionTitle("Последние выходы", Modifier.padding(top = 10.dp))
            listOf(
                Triple("Манёвры под парусом", "19 ноября  •  Fareast 19  •  3 ч", Res.drawable.yacht_keelboat),
                Triple("Выход в залив", "15 ноября  •  Bavaria 40  •  7 ч", Res.drawable.yacht_cruiser),
                Triple("Работа с парусами", "12 ноября  •  Fareast 19  •  3 ч", Res.drawable.sails_sunset),
            ).forEach { (title, text, photo) ->
                Row(Modifier.fillMaxWidth().card(radius = 18.dp).padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Photo(photo, Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Txt(title, size = 16, weight = FontWeight.Bold)
                        Txt(text, size = 13, color = Sea.Muted, maxLines = 1)
                    }
                    Box(Modifier.size(26.dp).clip(CircleShape).background(Sea.Tint), contentAlignment = Alignment.Center) {
                        Ico(AppIcons.Check, size = 15.dp, tint = Sea.Blue)
                    }
                }
            }
        }
    }
}

/** The profile, as far as the prototype goes: who the student is, and the way out. */
@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    bottom: @Composable () -> Unit,
) {
    Page(bottom = bottom, backdrop = { Backdrop(Res.drawable.sea_dusk, 400) }) {
        Column(Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Photo(Res.drawable.avatar, Modifier.size(104.dp).clip(CircleShape).border(3.dp, Color.White, CircleShape))
            Spacer(Modifier.height(14.dp))
            Txt("${SampleData.student} Морозов", size = 24, weight = FontWeight.Bold, color = Color.White, tracking = -0.4)
            Spacer(Modifier.height(4.dp))
            Txt("Ученик  •  ${SampleData.courses.first().title}", size = 14, color = Color(0xE6FFFFFF))
            Spacer(Modifier.height(26.dp))
            Column(Modifier.fillMaxWidth().card().padding(horizontal = 18.dp, vertical = 6.dp)) {
                listOf(
                    AppIcons.User to "Личные данные и документы",
                    AppIcons.Award to "Достижения и сертификаты",
                    AppIcons.Compass to "Уровень подготовки",
                    AppIcons.Bell to "Уведомления",
                ).forEachIndexed { i, (icon, label) ->
                    if (i > 0) Box(Modifier.fillMaxWidth().height(1.dp).background(Sea.Line))
                    Row(Modifier.padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Ico(icon, size = 22.dp, tint = Sea.Blue)
                        Txt(label, Modifier.weight(1f), size = 15, weight = FontWeight.Medium)
                        Ico(AppIcons.ChevronRight, size = 20.dp, tint = Sea.Muted)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            SecondaryButton("Выйти", onLogout, Modifier.fillMaxWidth(), color = Sea.Alert, icon = AppIcons.LogOut)
        }
    }
}
