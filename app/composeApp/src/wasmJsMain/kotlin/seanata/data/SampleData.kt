package seanata.data

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource
import seanata.res.Res
import seanata.res.coast
import seanata.res.deck_day
import seanata.res.deck_sunset
import seanata.res.knot
import seanata.res.sails_sunset
import seanata.res.winch
import seanata.res.yacht_cruiser
import seanata.res.yacht_day
import seanata.res.yacht_dinghy
import seanata.res.yacht_keelboat
import seanata.res.yacht_sunset
import seanata.ui.components.AppIcons

data class ProgramDay(val title: String, val text: String, val photo: DrawableResource)

data class Course(
    val id: Int,
    val title: String,
    val subtitle: String,
    val days: String,
    val entry: String,
    val price: Int,
    val photo: DrawableResource,
    val about: String,
)

data class PlanStep(val time: String, val text: String, val icon: ImageVector)

data class Training(
    val id: Int,
    /** Index into [SampleData.week]. */
    val day: Int,
    val tag: String,
    val title: String,
    val time: String,
    val yacht: String,
    val taken: Int,
    val seats: Int,
    val level: String,
    val price: Int,
    val photo: DrawableResource,
    val hero: DrawableResource,
    val about: String,
)

data class Day(val weekday: String, val short: String, val number: Int)

/** The school sells in dollars and takes dong: every price shows both. */
const val dongPerDollar = 26_000

fun Int.usd(): String = "$this $"

/** «≈ 6 500 000 ₫»: the same sum in dong, at [dongPerDollar]. */
fun Int.dong(): String = "≈ " + (this * dongPerDollar).toString().reversed().chunked(3).joinToString(" ").reversed() + " ₫"

/** «1 занятие», «3 занятия», «5 занятий». */
fun lessons(n: Int): String = when {
    n % 10 == 1 && n % 100 != 11 -> "$n занятие"
    n % 10 in 2..4 && n % 100 !in 12..14 -> "$n занятия"
    else -> "$n занятий"
}

/** Demo data: everything the screens show. */
object SampleData {
    const val student = "Алексей"
    const val marina = "Marina Nha Trang"
    const val month = "Ноябрь 2026"
    const val monthGenitive = "ноября"
    const val monthShort = "ноя"
    const val theoryProgress = 0.68f

    val week = listOf(
        Day("Пн", "Пн", 23), Day("Вт", "Вт", 24), Day("Ср", "Ср", 25), Day("Чт", "Чт", 26),
        Day("Пт", "Пт", 27), Day("Сб", "Сб", 28), Day("Вс", "Вс", 29),
    )
    const val today = 3

    val courses = listOf(
        Course(
            1, "Sailing Crew", "Двухдневный курс для новичков", "2 дня", "С нуля", 250, Res.drawable.yacht_day,
            "Первый шаг в мир яхтинга. За два дня на воде вы освоите жизнь на борту, узлы, работу с парусами " +
                "и станете полноценным членом экипажа.",
        ),
        Course(
            2, "Sailing 101", "Базовый курс управления яхтой", "2 дня", "С нуля", 250, Res.drawable.yacht_keelboat,
            "Основы самостоятельного управления килевой яхтой: руль и паруса, повороты, правила расхождения " +
                "и подход к причалу. С этого курса начинается путь шкипера.",
        ),
    )

    val includes = listOf(
        AppIcons.Book to "Теория: основы яхтинга, безопасность, навигация",
        AppIcons.Sailboat to "Практика на яхте: управление, манёвры, работа в экипаже",
        AppIcons.Users to "Ежедневные брифинги и разборы на борту",
        AppIcons.Award to "Сертификат школы по итогам курса",
    )

    val requirements = listOf(
        "Возраст от 16 лет, опыт не нужен",
        "Умение плавать и базовая физическая форма",
        "Пройденная бесплатная теория в приложении",
        "Непромокаемая куртка, обувь со светлой подошвой",
    )

    val program = listOf(
        ProgramDay("День 1 · утро", "Устройство яхты и безопасность", Res.drawable.winch),
        ProgramDay("День 1 · после обеда", "Узлы и работа с парусами", Res.drawable.knot),
        ProgramDay("День 2 · утро", "Манёвры в экипаже", Res.drawable.sails_sunset),
        ProgramDay("День 2 · после обеда", "Выход в залив и итоговая практика", Res.drawable.coast),
    )

    val plan = listOf(
        PlanStep("09:00", "Брифинг и подготовка яхты", AppIcons.Sailboat),
        PlanStep("09:20", "Выход в залив, манёвры под парусом", AppIcons.Wheel),
        PlanStep("11:00", "Работа в команде и ролевые задачи", AppIcons.Users),
        PlanStep("11:40", "Разбор и обратная связь", AppIcons.Flag),
    )

    private const val manoeuvres = "Отработка основных манёвров: повороты оверштаг и фордевинд, работа с гротом " +
        "и стакселем, координация экипажа."
    private const val crew = "Практическое занятие в составе экипажа: базовые навыки управления яхтой, работа " +
        "с парусами и слаженные действия на борту."
    private const val bay = "Выход в залив Нячанга на весь день: прокладка курса, вахты на руле, постановка на якорь " +
        "у острова и возвращение под парусами."

    val trainings = listOf(
        Training(1, 0, "Практика", "Практика в экипаже", "09:00 – 12:00", "Fareast 19", 5, 6, "Базовый", 45, Res.drawable.yacht_keelboat, Res.drawable.deck_day, crew),
        Training(2, 1, "Тренировка", "Узлы и швартовка", "15:00 – 18:00", "Bavaria 40", 2, 6, "Базовый", 45, Res.drawable.winch, Res.drawable.deck_day, crew),
        Training(3, 2, "Практика", "Работа с парусами", "09:00 – 12:00", "Fareast 19", 4, 6, "Базовый", 45, Res.drawable.sails_sunset, Res.drawable.deck_sunset, manoeuvres),
        Training(4, 2, "Навигация", "Вечерний выход в залив", "16:00 – 19:00", "Bavaria 40", 6, 6, "Средний", 60, Res.drawable.yacht_sunset, Res.drawable.deck_sunset, bay),
        Training(5, 3, "Практика", "Манёвры под парусом", "09:00 – 12:00", "Fareast 19", 3, 6, "Средний", 45, Res.drawable.yacht_keelboat, Res.drawable.deck_sunset, manoeuvres),
        Training(6, 3, "Тренировка", "Постановка и уборка парусов", "14:00 – 17:00", "RS Quest", 2, 3, "Базовый", 35, Res.drawable.yacht_dinghy, Res.drawable.deck_day, crew),
        Training(7, 3, "Навигация", "Выход в залив. Отработка навыков", "09:00 – 16:00", "Bavaria 40", 2, 4, "Средний", 90, Res.drawable.yacht_cruiser, Res.drawable.deck_day, bay),
        Training(8, 4, "Тренировка", "Манёвры и швартовка", "13:00 – 16:00", "Bavaria 40", 4, 6, "Средний", 60, Res.drawable.yacht_cruiser, Res.drawable.deck_day, manoeuvres),
        Training(9, 5, "Практика", "Практика в экипаже", "09:00 – 17:00", "Bavaria 40", 3, 6, "Базовый", 90, Res.drawable.yacht_day, Res.drawable.deck_day, crew),
        Training(10, 5, "Тренировка", "Гонка выходного дня", "10:00 – 13:00", "Fareast 19", 5, 6, "Средний", 45, Res.drawable.yacht_keelboat, Res.drawable.deck_sunset, manoeuvres),
    )

    /** The one the home screen offers. */
    val nextTraining get() = trainings.first { it.id == 9 }
}
