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

/** The home of a guest: what the school is, and the way in. */
@Composable
fun WelcomeScreen(
    onStart: () -> Unit,
    onLogin: () -> Unit,
    onSchedule: () -> Unit,
    onCourse: (Course) -> Unit,
    bottom: @Composable () -> Unit,
) {
    Page(bottom = bottom, backdrop = { Backdrop(Res.drawable.hero_deck, 570, fade = false, alignment = Alignment.BottomCenter) }) {
        Column(Modifier.fillMaxWidth().height(520.dp).padding(start = 22.dp, end = 22.dp, top = 18.dp)) {
            BrandBar(guest = true, onLogin = onLogin)
            Spacer(Modifier.height(46.dp))
            Txt("Яхтенная\nшкола", size = 46, weight = FontWeight.Bold, color = Color.White, lineHeight = 50, tracking = -1.0)
            Spacer(Modifier.height(12.dp))
            Txt("Обучение, практика и\nсертификаты яхтсмена в Нячанге", size = 16, color = Color(0xE6FFFFFF), lineHeight = 23)
            Spacer(Modifier.weight(1f))
            Row(Modifier.fillMaxWidth()) {
                listOf(AppIcons.Cap to "Обучение\nс нуля", AppIcons.Users to "Практика\nв экипаже", AppIcons.Award to "Сертификат\nпо итогам").forEach { (icon, label) ->
                    Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Ico(icon, size = 32.dp, tint = Color.White)
                        Txt(label, size = 13, weight = FontWeight.Medium, color = Color.White, align = TextAlign.Center, lineHeight = 17)
                    }
                }
            }
            Spacer(Modifier.height(22.dp))
            Row(
                Modifier.fillMaxWidth().height(56.dp).tap(onStart).card(radius = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Txt("Начать обучение", size = 17, weight = FontWeight.SemiBold)
                Ico(AppIcons.ArrowRight, size = 20.dp)
            }
            Spacer(Modifier.height(26.dp))
        }
        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)).background(Sea.Bg)
                .padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Banner(Res.drawable.coast, "Ближайшие тренировки", "Выходы в море, практика,\nотработка навыков", onSchedule)
            SectionTitle("Наши курсы", Modifier.padding(top = 6.dp))
            SampleData.courses.forEach { CourseCard(it) { onCourse(it) } }
        }
    }
}
