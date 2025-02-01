package domain.talks

import MonthYear
import kotlinx.serialization.Serializable

@Serializable
data class TedTalk(
    val title: String,
    val author: String,
    val date: MonthYear,
    val views: Long,
    val likes: Long,
    val link: String
) {
    /**
     * Weighing algorithm to gather how much effect a talk has had.
     * Not everyone that views the content will like it, so we weigh likes more than views.
     * Some less-viewed talks have a high like ratio, which increases their influence.
     */
    val influence = likes * 10L + views
}
