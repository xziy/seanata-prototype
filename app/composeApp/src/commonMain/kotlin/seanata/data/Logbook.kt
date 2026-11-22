package seanata.data

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.roundToInt

/**
 * A point of a track on the chart of the bay: nautical miles east ([x]) and south ([y]) of its corner.
 * [kn] is the speed on the leg that ends here and [motor] says it was made under engine; a fix with
 * a [note] is a line of the deck log; [stop] is the minutes spent here before going on.
 */
data class Fix(
    val x: Double,
    val y: Double,
    val kn: Double = 4.5,
    val motor: Boolean = false,
    val note: String? = null,
    val stop: Int = 0,
)

data class Boat(val name: String, val type: String, val length: String, val sail: String)

/** A line of the deck log: when, the course steered from here, the log's reading, the speed, and what happened. */
data class LogLine(val time: String, val course: String?, val log: Double, val speed: Double?, val note: String)

/**
 * An entry of the logbook: one passage. Only the track and the departure are written down; the miles,
 * the times, the courses and the deck log follow from them, so the entry always agrees with its track.
 */
class Passage(
    val id: Int,
    /** Day of the month. */
    val day: Int,
    val title: String,
    val boat: Boat,
    val route: String,
    /** Departure, minutes from midnight. */
    val start: Int,
    val wind: String,
    val sea: String,
    val weather: String,
    val pressure: String,
    val skills: List<String>,
    /** The skipper has signed the entry. */
    val signed: Boolean,
    val track: List<Fix>,
) {
    private val legs = track.zipWithNext { a, b -> hypot(b.x - a.x, b.y - a.y) }

    /** When the yacht came to each fix, minutes from midnight. */
    private val times = track.indices.runningFold(start.toDouble()) { t, i ->
        if (i == track.lastIndex) t else t + track[i].stop + legs[i] / track[i + 1].kn * 60
    }.dropLast(1)

    val miles = legs.sum()
    val motorMiles = legs.indices.sumOf { if (track[it + 1].motor) legs[it] else 0.0 }
    val sailMiles = miles - motorMiles
    val end = times.last().roundToInt()

    /** From the lines cast off to the lines made fast. */
    val minutes = end - start

    /** The same without the stops. */
    val underway = minutes - track.sumOf { it.stop }
    val nightMinutes = (end - maxOf(start, nightfall)).coerceAtLeast(0)
    val average = miles / underway * 60
    val fastest = track.drop(1).maxOf { it.kn }

    val lines = track.indices.filter { track[it].note != null }.map { i ->
        val next = track.getOrNull(i + 1)
        LogLine(
            time = clock(times[i].roundToInt()),
            course = next?.let { course(track[i], it) },
            log = legs.take(i).sum(),
            speed = next?.kn,
            note = track[i].note!!,
        )
    }

    val date get() = "$day ${SampleData.monthGenitive} 2026"

    companion object {
        /** The end of the evening twilight in Nha Trang in November: from here the hours count as night ones. */
        const val nightfall = 17 * 60 + 40
    }
}

/** «09:05». */
fun clock(minutes: Int): String = "${(minutes / 60).toString().padStart(2, '0')}:${(minutes % 60).toString().padStart(2, '0')}"

/** «2 ч 15 мин». */
fun span(minutes: Int): String = if (minutes < 60) "$minutes мин" else "${minutes / 60} ч ${(minutes % 60).toString().padStart(2, '0')} мин"

/** A number with one decimal and a comma: «7,8». */
fun Double.one(): String = (this * 10).roundToInt().let { "${it / 10},${it % 10}" }

/** The true course from one fix to the next: «045°». */
private fun course(from: Fix, to: Fix): String {
    val degrees = (atan2(to.x - from.x, from.y - to.y) * 180 / PI).roundToInt()
    return "${((degrees + 360) % 360).toString().padStart(3, '0')}°"
}

/** The student's logbook. The yachts' names, the skipper and the tracks are made up for the prototype. */
object Logbook {
    const val skipper = "Андрей Соколов"
    const val role = "Матрос (член экипажа)"
    const val home = "Marina Nha Trang"

    val marina = Fix(1.75, 2.25)
    private val lotus = Boat("Lotus", "Fareast 19R", "5,8 м", "VIE 119")
    private val mistral = Boat("Mistral", "Bavaria 40 Cruiser", "12,4 м", "VIE 40")

    private fun cast(text: String = "Отдали швартовы, выход из марины под мотором") = marina.copy(note = text)
    private fun moor(text: String = "Ошвартовались лагом, причал B. Паруса убраны") = marina.copy(kn = 3.0, motor = true, note = text)

    val passages = listOf(
        Passage(
            6, 19, "Манёвры под парусом", lotus, "$home — залив Нячанг — $home", 9 * 60 + 10,
            wind = "СВ, 4 балла (12–15 уз)", sea = "0,5 м, 2 балла", weather = "Ясно, видимость 10 миль", pressure = "1014 гПа",
            skills = listOf("Поворот оверштаг", "Поворот фордевинд", "Работа на стаксель-шкотах", "Человек за бортом"),
            signed = false,
            track = listOf(
                cast(),
                Fix(2.4, 2.0, kn = 3.5, motor = true, note = "Поставили грот и стаксель, мотор заглушён. Бейдевинд левого галса"),
                Fix(2.45, 1.2, kn = 4.2),
                Fix(3.3, 1.25, kn = 4.4),
                Fix(3.35, 0.6, kn = 4.3, note = "Серия поворотов оверштаг, на руле по очереди"),
                Fix(4.3, 0.65, kn = 4.6),
                Fix(4.35, 0.15, kn = 4.4),
                Fix(5.2, 0.2, kn = 4.6, note = "Увалились до бакштага. Повороты фордевинд"),
                Fix(4.9, 1.2, kn = 5.4),
                Fix(4.1, 1.5, kn = 5.2),
                Fix(3.95, 2.3, kn = 5.5),
                Fix(3.3, 2.65, kn = 5.0, note = "Учебная тревога «человек за бортом»: подход к бую под парусами", stop = 14),
                Fix(2.4, 2.3, kn = 4.4, note = "Убрали паруса, запустили мотор"),
                moor(),
            ),
        ),
        Passage(
            5, 15, "Выход в залив", mistral, "$home — о. Хон Мун — вокруг о. Хон Че — $home", 9 * 60 + 20,
            wind = "СВ, 4–5 баллов (14–18 уз)", sea = "0,8 м, 3 балла", weather = "Переменная облачность, видимость 8 миль", pressure = "1012 гПа",
            skills = listOf("Вахта на руле", "Прокладка курса по карте", "Постановка на якорь", "Определение места по пеленгам"),
            signed = true,
            track = listOf(
                cast(),
                Fix(2.6, 2.85, kn = 5.0, motor = true),
                Fix(3.3, 3.7, kn = 5.0, motor = true, note = "Прошли пролив у мыса Кау Да. Поставили грот и геную"),
                Fix(3.8, 4.6, kn = 5.6),
                Fix(4.9, 5.1, kn = 5.8, note = "Траверз о. Хон Там. Смена вахты на руле"),
                Fix(6.0, 5.4, kn = 5.8),
                Fix(7.0, 5.8, kn = 5.6),
                Fix(7.6, 6.05, kn = 3.5, note = "Встали на якорь у о. Хон Мун, глубина 9 м, вытравили 35 м цепи. Обед", stop = 80),
                Fix(8.5, 5.9, kn = 4.0, motor = true, note = "Снялись с якоря, поставили паруса. Курс вокруг о. Хон Че"),
                Fix(9.2, 5.2, kn = 4.8),
                Fix(9.75, 4.2, kn = 4.6),
                Fix(9.8, 3.1, kn = 4.4, note = "Траверз восточного мыса. Взяли риф на гроте: ветер 18 уз"),
                Fix(9.3, 2.2, kn = 4.6),
                Fix(8.4, 1.4, kn = 5.2, note = "Увалились, курс на запад. Фордевинд, генуя на спинакер-гике"),
                Fix(7.0, 1.3, kn = 6.2),
                Fix(5.5, 1.5, kn = 6.4),
                Fix(4.0, 1.9, kn = 6.0),
                Fix(2.5, 2.2, kn = 5.6, note = "Убрали паруса, запустили мотор"),
                moor("Ошвартовались кормой, причал A. Яхта сдана"),
            ),
        ),
        Passage(
            4, 12, "Работа с парусами", lotus, "$home — залив Нячанг — $home", 9 * 60 + 15,
            wind = "С-СВ, 3 балла (8–10 уз)", sea = "0,3 м, 2 балла", weather = "Ясно, видимость 10 миль", pressure = "1015 гПа",
            skills = listOf("Постановка и уборка грота", "Рифление", "Настройка стакселя", "Постановка геннакера"),
            signed = true,
            track = listOf(
                cast(),
                Fix(2.4, 2.0, kn = 3.5, motor = true, note = "Поставили грот и стаксель"),
                Fix(2.7, 0.9, kn = 3.8),
                Fix(4.4, 1.0, kn = 4.0, note = "Взяли и отдали риф на ходу, по два раза каждый", stop = 10),
                Fix(5.5, 1.8, kn = 4.2),
                Fix(4.5, 2.5, kn = 4.4, note = "Поставили геннакер. Бакштаг правого галса"),
                Fix(3.4, 1.9, kn = 5.2),
                Fix(2.5, 2.2, kn = 4.8, note = "Убрали геннакер и паруса, запустили мотор"),
                moor(),
            ),
        ),
        Passage(
            3, 8, "Вечерний выход в залив", mistral, "$home — залив Нячанг — $home", 16 * 60 + 5,
            wind = "СВ, 3–4 балла (10–13 уз)", sea = "0,5 м, 2 балла", weather = "Ясно, после заката видимость 5 миль", pressure = "1013 гПа",
            skills = listOf("Вахта на руле", "Ходовые огни", "Подход к марине в темноте"),
            signed = true,
            track = listOf(
                cast(),
                Fix(2.5, 1.9, kn = 4.5, motor = true, note = "Поставили грот и геную"),
                Fix(3.0, 0.7, kn = 4.6),
                Fix(5.0, 0.6, kn = 5.0, note = "Поворот, галфвинд левого галса"),
                Fix(6.6, 1.3, kn = 5.2, note = "Закат 17:16. Включили ходовые огни, поворот на обратный курс"),
                Fix(5.2, 1.9, kn = 5.0),
                Fix(3.6, 2.2, kn = 4.8, note = "Наступила темнота. Идём на огни марины"),
                Fix(2.5, 2.25, kn = 4.4, note = "Убрали паруса, запустили мотор"),
                moor("Ошвартовались кормой в темноте, причал A"),
            ),
        ),
        Passage(
            2, 5, "Практика в экипаже", lotus, "$home — залив Нячанг — $home", 9 * 60 + 10,
            wind = "В-СВ, 3 балла (7–10 уз)", sea = "0,3 м, 1–2 балла", weather = "Облачно, видимость 8 миль", pressure = "1013 гПа",
            skills = listOf("Обязанности на борту", "Работа на шкотах", "Удержание курса на руле"),
            signed = true,
            track = listOf(
                cast(),
                Fix(2.4, 2.05, kn = 3.5, motor = true, note = "Поставили паруса. Распределили роли на борту"),
                Fix(3.2, 1.2, kn = 3.8),
                Fix(4.6, 1.5, kn = 4.0, note = "Смена ролей: рулевой, шкотовые, вперёдсмотрящий"),
                Fix(4.0, 2.5, kn = 4.2),
                Fix(3.0, 1.9, kn = 4.0),
                Fix(3.6, 0.9, kn = 3.8, note = "Вторая смена ролей. Галфвинд"),
                Fix(2.6, 1.3, kn = 4.2),
                Fix(2.4, 2.1, kn = 4.0, note = "Убрали паруса, запустили мотор"),
                moor(),
            ),
        ),
        Passage(
            1, 1, "Первый выход", mistral, "$home — о. Хон Там — $home", 10 * 60,
            wind = "СВ, 2–3 балла (6–9 уз)", sea = "0,3 м, 1 балл", weather = "Ясно, видимость 10 миль", pressure = "1014 гПа",
            skills = listOf("Инструктаж по безопасности", "Спасательный жилет и страховка", "Морские узлы", "Первая вахта на руле"),
            signed = true,
            track = listOf(
                cast("Инструктаж по безопасности. Отдали швартовы"),
                Fix(2.7, 2.9, kn = 4.5, motor = true),
                Fix(3.35, 3.7, kn = 4.5, motor = true, note = "Прошли пролив у мыса Кау Да. Поставили грот и геную"),
                Fix(3.9, 4.5, kn = 4.4),
                Fix(4.3, 5.25, kn = 4.0, note = "Встали на якорь у о. Хон Там, глубина 6 м. Вязали узлы", stop = 45),
                Fix(3.75, 4.5, kn = 3.8, note = "Снялись с якоря. Бейдевинд, на руле по очереди"),
                Fix(3.2, 3.65, kn = 3.6),
                Fix(2.75, 2.9, kn = 4.5, motor = true, note = "Убрали паруса, запустили мотор"),
                moor(),
            ),
        ),
    )

    val miles = passages.sumOf { it.miles }
    val sailMiles = passages.sumOf { it.sailMiles }
    val motorMiles = passages.sumOf { it.motorMiles }
    val minutes = passages.sumOf { it.minutes }
    val nightMinutes = passages.sumOf { it.nightMinutes }
}
