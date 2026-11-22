package seanata

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import seanata.data.SampleData
import seanata.theme.AppTheme
import seanata.theme.Sea
import seanata.ui.components.BottomNav
import seanata.ui.components.Tab
import seanata.ui.components.LocalSafe
import seanata.ui.components.safeArea
import seanata.ui.components.screenChange
import seanata.ui.screens.BookedScreen
import seanata.ui.screens.CourseScreen
import seanata.ui.screens.CoursesScreen
import seanata.ui.screens.HomeScreen
import seanata.ui.screens.LogbookScreen
import seanata.ui.screens.ProfileScreen
import seanata.ui.screens.ScheduleScreen
import seanata.ui.screens.TrainingScreen
import seanata.ui.screens.WelcomeScreen

/** The screens of the prototype. [hash] is how each one is addressed in the URL. */
/** [depth]: how far a screen is from the tabs; going deeper slides forward, coming back slides backward. */
enum class Route(val hash: String, val depth: Int = 0) {
    Welcome("welcome"),
    Home("home"),
    Courses("courses"),
    Course("course", 1),
    Program("program", 1),
    Schedule("schedule"),
    Training("training", 1),
    Booked("booked", 2),
    Logbook("logbook"),
    Profile("profile"),
    ;

    companion object {
        fun fromHash(hash: String): Route = entries.firstOrNull { it.hash == hash.removePrefix("#") } ?: Welcome
    }
}

/** Root: the theme, the state of the prototype and the navigation between screens. */
@Composable
fun App(
    start: Route,
    onRouteChange: (Route) -> Unit,
) {
    // `#program` is the course screen opened on its second tab.
    var route by remember { mutableStateOf(if (start == Route.Program) Route.Course else start) }
    var guest by remember { mutableStateOf(start == Route.Welcome) }
    var courseId by remember { mutableStateOf(SampleData.courses.first().id) }
    var courseTab by remember { mutableStateOf(if (start == Route.Program) 1 else 0) }
    var courseFrom by remember { mutableStateOf(Route.Courses) }
    var day by remember { mutableStateOf(SampleData.today) }
    var trainingId by remember { mutableStateOf(5) }
    var trainingFrom by remember { mutableStateOf(Route.Schedule) }
    var layer by remember { mutableStateOf(0f) }
    var booked by remember { mutableStateOf(if (start == Route.Booked) setOf(5) else emptySet()) }

    fun go(to: Route) {
        // A screen we return to lies under the one that leaves; any other comes in on top.
        if (to != route) layer += if (to.depth < route.depth) -1f else 1f
        route = to
        onRouteChange(to)
    }

    fun home() = go(if (guest) Route.Welcome else Route.Home)

    fun tab(tab: Tab) = when (tab) {
        Tab.Home -> home()
        Tab.Courses -> go(Route.Courses)
        Tab.Schedule -> go(Route.Schedule)
        Tab.Logbook -> go(Route.Logbook)
        Tab.Profile -> go(Route.Profile)
    }

    fun openCourse(id: Int) {
        courseId = id
        courseTab = 0
        courseFrom = route
        go(Route.Course)
    }

    fun openTraining(id: Int) {
        trainingId = id
        trainingFrom = route
        go(Route.Training)
    }

    fun nav(current: Tab): @Composable () -> Unit = { BottomNav(current, ::tab) }

    val course = SampleData.courses.first { it.id == courseId }
    val training = SampleData.trainings.first { it.id == trainingId }
    val next = SampleData.nextTraining

    AppTheme {
        // On a wide window the app stays a phone's column in the middle.
        CompositionLocalProvider(LocalSafe provides safeArea()) {
        Box(Modifier.fillMaxSize().background(Sea.NavyDeep)) {
            Box(Modifier.align(Alignment.TopCenter).widthIn(max = 460.dp).fillMaxHeight().clipToBounds().background(Sea.Bg)) {
                AnimatedContent(
                    route,
                    transitionSpec = { screenChange(targetState.depth - initialState.depth, layer) },
                    label = "route",
                ) { shown ->
                    when (shown) {
                        Route.Welcome -> WelcomeScreen(
                            onStart = { guest = false; go(Route.Home) },
                            onLogin = { guest = false; go(Route.Home) },
                            onSchedule = { go(Route.Schedule) },
                            onCourse = { openCourse(it.id) },
                            bottom = nav(Tab.Home),
                        )

                        Route.Home -> HomeScreen(
                            next = next,
                            taken = next.taken + if (next.id in booked) 1 else 0,
                            booked = next.id in booked,
                            onCourse = { openCourse(SampleData.courses.first().id) },
                            onTraining = { openTraining(next.id) },
                            onTab = ::tab,
                            bottom = nav(Tab.Home),
                        )

                        Route.Courses -> CoursesScreen(
                            onCourse = { openCourse(it.id) },
                            onProfile = { go(Route.Profile) },
                            bottom = nav(Tab.Courses),
                        )

                        Route.Course, Route.Program -> CourseScreen(
                            course = course,
                            tab = courseTab,
                            onTab = { courseTab = it },
                            onBack = { go(courseFrom) },
                            onEnroll = { go(Route.Schedule) },
                        )

                        Route.Schedule -> ScheduleScreen(
                            day = day,
                            onDay = { day = it },
                            booked = booked,
                            onTraining = { openTraining(it.id) },
                            onProfile = { go(Route.Profile) },
                            bottom = nav(Tab.Schedule),
                        )

                        Route.Training -> TrainingScreen(
                            training = training,
                            booked = training.id in booked,
                            onBack = { go(trainingFrom) },
                            onBook = { booked = booked + training.id; go(Route.Booked) },
                            onCancel = { booked = booked - training.id },
                        )

                        Route.Booked -> BookedScreen(
                            training = training,
                            onSchedule = { day = training.day; go(Route.Schedule) },
                            onHome = { home() },
                        )

                        Route.Logbook -> LogbookScreen(onProfile = { go(Route.Profile) }, bottom = nav(Tab.Logbook))

                        Route.Profile -> ProfileScreen(onLogout = { guest = true; go(Route.Welcome) }, bottom = nav(Tab.Profile))
                    }
                }
            }
        }
        }
    }
}
