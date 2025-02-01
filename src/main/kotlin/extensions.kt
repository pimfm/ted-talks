import domain.talks.TedTalk
import kotlinx.serialization.Serializable

@Serializable
data class MonthYear(val month: String, val year: Int) {
    fun format() = "$month $year"
}

fun String.toMonthYear() = split(" ").let { (month, year) -> MonthYear(month, year.toInt()) }

fun TedTalk.toCSVLine() = "${title.replace(",", ";;;;;")},${author.replace(",", ";;;;;")},${date.format().replace(",", ";;;;;")},${views},${likes},${link.replace(",", ";;;;;")}"
