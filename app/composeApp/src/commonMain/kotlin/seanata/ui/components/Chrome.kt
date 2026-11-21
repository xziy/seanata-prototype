package seanata.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import seanata.res.Res
import seanata.res.avatar
import seanata.res.logo_color
import seanata.res.logo_white
import seanata.theme.Sea

/** The five sections of the bottom bar. */
enum class Tab(val label: String, val icon: ImageVector) {
    Home("Главная", AppIcons.Home),
    Courses("Курсы", AppIcons.Book),
    Schedule("Расписание", AppIcons.Calendar),
    Logbook("Логбук", AppIcons.Logbook),
    Profile("Профиль", AppIcons.User),
}

/** Seanata's logo: white over photos, in its own colours on a light surface. */
@Composable
fun Logo(
    onDark: Boolean = true,
    height: Dp = 40.dp,
) {
    Image(
        painterResource(if (onDark) Res.drawable.logo_white else Res.drawable.logo_color),
        contentDescription = "Seanata",
        modifier = Modifier.size(width = height * 307 / 140, height = height),
        contentScale = ContentScale.Fit,
    )
}

/** The top of a section screen: logo, then notifications and the student (or «Войти» for a guest). */
@Composable
fun BrandBar(
    guest: Boolean = false,
    onLogin: () -> Unit = {},
    onProfile: () -> Unit = {},
) {
    Row(Modifier.fillMaxWidth().height(48.dp), verticalAlignment = Alignment.CenterVertically) {
        Logo()
        Column(Modifier.padding(start = 10.dp).weight(1f)) {
            Txt("SAILING", size = 10, weight = FontWeight.SemiBold, color = Color(0xCCFFFFFF), tracking = 1.6)
            Txt("ACADEMY", size = 10, weight = FontWeight.SemiBold, color = Color(0xCCFFFFFF), tracking = 1.6)
        }
        if (guest) {
            Box(
                Modifier.tap(onLogin).clip(RoundedCornerShape(14.dp)).background(Color(0x33FFFFFF))
                    .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(14.dp))
                    .padding(horizontal = 18.dp, vertical = 10.dp),
            ) {
                Txt("Войти", size = 15, weight = FontWeight.SemiBold, color = Color.White)
            }
        } else {
            Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                Ico(AppIcons.Bell, size = 26.dp, tint = Color.White)
                Box(Modifier.align(Alignment.TopEnd).offset(x = (-7).dp, y = 6.dp).size(9.dp).clip(CircleShape).background(Sea.Alert))
            }
            Spacer(Modifier.size(10.dp))
            Photo(
                Res.drawable.avatar,
                Modifier.tap(onProfile).size(44.dp).clip(CircleShape).border(2.dp, Color.White, CircleShape),
            )
        }
    }
}

/** The top of an inner screen: back, a centred title, share. */
@Composable
fun TopBar(
    title: String,
    onBack: () -> Unit,
) {
    Row(Modifier.fillMaxWidth().height(48.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(40.dp).tap(onBack), contentAlignment = Alignment.CenterStart) {
            Ico(AppIcons.ChevronLeft, size = 28.dp, tint = Color.White)
        }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Txt(title, size = 19, weight = FontWeight.SemiBold, color = Color.White)
        }
        Box(Modifier.size(40.dp), contentAlignment = Alignment.CenterEnd) {
            Ico(AppIcons.Share, size = 24.dp, tint = Color.White)
        }
    }
}

@Composable
fun BottomNav(
    current: Tab,
    onSelect: (Tab) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), ambientColor = Sea.Navy, spotColor = Sea.Navy)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)).background(Sea.Card).padding(top = 10.dp, bottom = 12.dp),
    ) {
        Tab.entries.forEach { tab ->
            val color by animateColorAsState(if (tab == current) Sea.Blue else Sea.Muted, tween(220))
            Column(
                Modifier.weight(1f).tap { onSelect(tab) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Ico(tab.icon, size = 25.dp, tint = color)
                Txt(tab.label, size = 11, weight = if (tab == current) FontWeight.SemiBold else FontWeight.Medium, color = color, maxLines = 1)
            }
        }
    }
}

/** The pinned bottom of an inner screen: a price and the main action. */
@Composable
fun ActionBar(
    price: String?,
    dong: String? = null,
    content: @Composable (Modifier) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), ambientColor = Sea.Navy, spotColor = Sea.Navy)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)).background(Sea.Card)
            .padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (price != null) {
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Txt(price, size = 24, weight = FontWeight.Bold, tracking = -0.5)
                if (dong != null) Txt(dong, size = 12, color = Sea.Muted)
            }
            VLine()
        }
        content(Modifier.weight(1f))
    }
}

/** Two or three views of one screen. */
@Composable
fun Segmented(
    items: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier.card(radius = 16.dp, elevation = 6.dp).padding(5.dp)) {
        items.forEachIndexed { i, label ->
            val on = i == selected
            val glow by animateFloatAsState(if (on) 1f else 0f, tween(240))
            val color by animateColorAsState(if (on) Color.White else Sea.Muted, tween(240))
            Box(Modifier.weight(1f).height(40.dp).tap { onSelect(i) }, contentAlignment = Alignment.Center) {
                Box(Modifier.matchParentSize().graphicsLayer { alpha = glow }.clip(RoundedCornerShape(12.dp)).background(Sea.Action))
                Txt(label, size = 14, weight = FontWeight.SemiBold, color = color)
            }
        }
    }
}

/**
 * The frame of a screen: the page scrolls as a whole, with [backdrop] (a photo header) behind its top,
 * and [bottom] (the navigation or an action) stays pinned.
 */
@Composable
fun Page(
    bottom: @Composable () -> Unit,
    backdrop: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(Modifier.fillMaxSize().background(Sea.Bg)) {
        Box(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
            backdrop()
            Column(Modifier.fillMaxWidth(), content = content)
        }
        bottom()
    }
}
