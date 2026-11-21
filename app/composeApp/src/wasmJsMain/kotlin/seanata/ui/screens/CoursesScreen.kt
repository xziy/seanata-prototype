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

@Composable
fun CoursesScreen(
    onCourse: (Course) -> Unit,
    onProfile: () -> Unit,
    bottom: @Composable () -> Unit,
) {
    Page(bottom = bottom, backdrop = { Backdrop(Res.drawable.sea_dusk, 250) }) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            BrandBar(onProfile = onProfile)
            Txt("Курсы", Modifier.padding(top = 8.dp), size = 28, weight = FontWeight.Bold, color = Color.White, tracking = -0.6)
            Txt("Два курса, с которых начинается парус", Modifier.padding(bottom = 8.dp), size = 15, color = Color(0xE6FFFFFF))
            SampleData.courses.forEach { CourseCard(it) { onCourse(it) } }
            Banner(Res.drawable.sails_sunset, "Бесплатная теория", "Первые уроки открыты сразу\nпосле регистрации", onClick = { onCourse(SampleData.courses.first()) })
        }
    }
}
