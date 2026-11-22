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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import org.jetbrains.compose.resources.DrawableResource
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.width
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
                Modifier.tap(onLogin).glass(14.dp, GlassTone.Clear).padding(horizontal = 18.dp, vertical = 10.dp),
            ) {
                Txt("Войти", size = 15, weight = FontWeight.SemiBold, color = Color.White)
            }
        } else {
            Box(Modifier.size(42.dp).glass(21.dp, GlassTone.Clear), contentAlignment = Alignment.Center) {
                Ico(AppIcons.Bell, size = 22.dp, tint = Color.White)
                Box(Modifier.align(Alignment.TopEnd).offset(x = (-9).dp, y = 8.dp).size(9.dp).clip(CircleShape).background(Sea.Alert))
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
        Box(Modifier.tap(onBack).size(42.dp).glass(21.dp, GlassTone.Clear), contentAlignment = Alignment.Center) {
            Ico(AppIcons.ChevronLeft, Modifier.offset(x = (-1).dp), size = 24.dp, tint = Color.White)
        }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Txt(title, size = 19, weight = FontWeight.SemiBold, color = Color.White)
        }
        Box(Modifier.size(42.dp).glass(21.dp, GlassTone.Clear), contentAlignment = Alignment.Center) {
            Ico(AppIcons.Share, Modifier.offset(y = (-1).dp), size = 20.dp, tint = Color.White)
        }
    }
}

/** How far a floating bar keeps from the sides of the screen. */
private val BarInset = 12.dp

/** A floating bar stands on the home bar's level on an iPhone, and a little above the edge elsewhere. */
@Composable
private fun barBottom(): Dp = (LocalSafe.current.bottom - 12.dp).coerceAtLeast(12.dp)

private val BarShadow = Color(0x590A2850)

/** The sections of the app: a floating capsule of glass; the page scrolls on under it. */
@Composable
fun BottomNav(
    current: Tab,
    onSelect: (Tab) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().padding(start = BarInset, end = BarInset, bottom = barBottom())
            .shadow(18.dp, CircleShape, clip = false, ambientColor = BarShadow, spotColor = BarShadow)
            .glass(34.dp).padding(5.dp),
    ) {
        Tab.entries.forEach { tab ->
            val on = tab == current
            val color by animateColorAsState(if (on) Sea.Blue else Sea.Ink.copy(alpha = 0.72f), tween(220))
            val lens by animateFloatAsState(if (on) 1f else 0f, tween(260))
            Box(Modifier.weight(1f).tap { onSelect(tab) }, contentAlignment = Alignment.Center) {
                // The section we are in lies under a denser drop of the same glass.
                Box(
                    Modifier.matchParentSize().graphicsLayer { alpha = lens }.clip(CircleShape)
                        .background(Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.85f), Color.White.copy(alpha = 0.5f))))
                        .border(1.dp, Color.White, CircleShape),
                )
                Column(
                    Modifier.padding(top = 8.dp, bottom = 7.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Ico(tab.icon, size = 24.dp, tint = color)
                    Txt(tab.label, size = 10, weight = if (on) FontWeight.SemiBold else FontWeight.Medium, color = color, maxLines = 1)
                }
            }
        }
    }
}

/** The pinned bottom of an inner screen: a price and the main action, on a floating pane of glass. */
@Composable
fun ActionBar(
    price: String?,
    dong: String? = null,
    content: @Composable (Modifier) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().padding(start = BarInset, end = BarInset, bottom = barBottom())
            .shadow(18.dp, RoundedCornerShape(26.dp), clip = false, ambientColor = BarShadow, spotColor = BarShadow)
            .glass(26.dp).padding(start = if (price != null) 18.dp else 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (price != null) {
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Txt(price, size = 24, weight = FontWeight.Bold, tracking = -0.5)
                if (dong != null) Txt(dong, size = 12, color = Sea.Ink.copy(alpha = 0.7f))
            }
            Box(Modifier.width(1.dp).height(36.dp).background(Sea.Ink.copy(alpha = 0.12f)))
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

/** How far the photo header moves for each pixel the page is scrolled. */
private const val BackdropDrift = 0.3f

/**
 * The frame of a screen: [backdrop] (a photo header) lies under the interface and follows the scroll at a
 * third of its speed, and [bottom] (the navigation or an action) floats over the page as glass.
 *
 * A screen whose photo fades into the page gives [sheet], the height of that photo: from there down the
 * content carries the page's colour with it, so what scrolls up is never dark text on the photo.
 */
@Composable
fun Page(
    bottom: @Composable () -> Unit,
    backdrop: @Composable () -> Unit,
    sheet: Int? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val safe = LocalSafe.current
    val scroll = rememberScrollState()
    // The glass in the content looks through to the photo; the bars look through to the whole page.
    val photo = rememberGlassSource { scroll.value }
    val page = rememberGlassSource { scroll.value }
    var bar by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Box(Modifier.fillMaxSize().clipToBounds()) {
        Box(Modifier.fillMaxSize().glassSource(page).background(Sea.Bg)) {
            Box(Modifier.glassSource(photo)) {
                Box(Modifier.graphicsLayer { translationY = -scroll.value * BackdropDrift }) { backdrop() }
            }
            CompositionLocalProvider(LocalGlassSource provides photo) {
                Column(Modifier.fillMaxSize().verticalScroll(scroll)) {
                    Column(
                        Modifier.fillMaxWidth().drawBehind {
                            if (sheet == null) return@drawBehind
                            val solid = (safe.top + sheet.dp - 45.dp).toPx()
                            val soft = solid - 50.dp.toPx()
                            drawRect(Brush.verticalGradient(listOf(Color.Transparent, Sea.Bg), startY = soft, endY = solid), Offset(0f, soft), Size(size.width, solid - soft))
                            drawRect(Sea.Bg, Offset(0f, solid), Size(size.width, (size.height - solid).coerceAtLeast(0f)))
                        }.padding(top = safe.top, bottom = bar),
                        content = content,
                    )
                }
            }
        }
        CompositionLocalProvider(LocalGlassSource provides page) {
            // What scrolls up must not run under the clock: a band of dark glass comes in with the first of the scroll.
            if (safe.top > 0.dp) {
                Box(
                    Modifier.fillMaxWidth().height(safe.top).graphicsLayer { alpha = (scroll.value / 40.dp.toPx()).coerceIn(0f, 1f) }
                        .glass(0.dp, GlassTone(Sea.Navy.copy(alpha = 0.78f), sheen = 0f, rim = 0f), blur = 20.dp),
                )
            }
            Box(Modifier.align(Alignment.BottomCenter).onSizeChanged { bar = with(density) { it.height.toDp() } }) { bottom() }
        }
    }
}

/** The photo behind the top of a screen: navy at the top for the bar, fading into the page below. */
@Composable
fun Backdrop(
    photo: DrawableResource,
    height: Int,
    fade: Boolean = true,
    alignment: Alignment = Alignment.Center,
) {
    Box(Modifier.fillMaxWidth().height(LocalSafe.current.top + height.dp)) {
        Photo(photo, Modifier.matchParentSize(), alignment)
        if (fade) {
            Scrim(0f to Sea.Navy.copy(alpha = 0.92f), 0.45f to Sea.Navy.copy(alpha = 0.6f), 0.8f to Sea.Bg.copy(alpha = 0.85f), 1f to Sea.Bg, modifier = Modifier.matchParentSize())
        } else {
            Scrim(0f to Sea.Navy.copy(alpha = 0.9f), 0.3f to Sea.Navy.copy(alpha = 0.15f), 0.48f to Sea.Navy.copy(alpha = 0.3f), 0.75f to Sea.NavyDeep.copy(alpha = 0.78f), 1f to Sea.NavyDeep.copy(alpha = 0.92f), modifier = Modifier.matchParentSize())
        }
    }
}
