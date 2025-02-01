package infrastructure.database

import domain.contracts.TalksRepository
import domain.talks.TedTalk
import toCSVLine
import toMonthYear
import java.io.File

class TalksRepositoryUsingCSV(val csv: File) : TalksRepository {
    private val talks by lazy { loadTalksFromCSV() }

    /**
     * Get a page of talks.
     *
     * @param countPerPage The number of talks to return per page. Must be greater than 0 and less than 500
     * @param pageNumber The page number to return. Must be greater than 0
     *
     * @return A list of talks
     */
    override fun getTalks(
        countPerPage: Int,
        pageNumber: Int
    ): List<TedTalk> {
        val page = (pageNumber - 1).coerceIn(0, talks.size / countPerPage)
        val count = countPerPage.coerceIn(1, 500)

        return talks.subList(
            fromIndex = page * count,
            toIndex = (page * count + count).coerceAtMost(talks.size)
        )
    }


    /**
     * Search talks by title or author.
     *
     * @param searchTerm Used as a case-insensitive string partial to match anywhere in the String
     *                   A maximum of [count] or 500 talks will be returned
     * @return A list of talks that match the search term
     */
    override fun searchTalks(searchTerm: String, count: Int): List<TedTalk> {
        val nTalks = count.coerceIn(1, 500)

        return talks.filter {
            it.title.contains(searchTerm, ignoreCase = true)
            || it.author.contains(searchTerm, ignoreCase = true)
        }.take(nTalks)
    }

    /**
     * Create a new talk and store it. Duplicates by author + title will not be added.
     *
     * @param talk The talk to add
     */
    override fun addTalk(talk: TedTalk) {
        if (talks.find { it.title == talk.title && it.author == talk.author } != null) {
            return
        }

        talks.add(talk)
            .also {
                csv.appendText("\n" + talk.toCSVLine())
            }
    }

    /**
     * Rank talks by views and likes and return the top 3 talks, grouped by year.
     *
     * @return The top 3 talks per year.
     */
    override fun getInfluentialTalksByYear(): Map<Int, Set<TedTalk>> {
        return talks
            .groupBy { it.date.year }
            .mapValues { (_, talks) -> talks
                .sortedByDescending { it.influence }
                .take(3)
                .toSet()
            }
    }

    /**
     * Load the talks into memory the first time the repository is accessed. (by lazy)
     * The talks in memory are written to the CSV file when the server is stopped, see shutdown hook.
     */
    private fun loadTalksFromCSV(): MutableList<TedTalk> {
        return csv.readLines().drop(1).map { line ->
            line.split(",").map { it.replace(";;;;;", ",") }.let { (title, author, date, views, likes, link) ->
                TedTalk(title, author, date.toMonthYear(), views.toLongOrNull() ?: 0L, likes.toLongOrNull() ?: 0L, link)
            }
        }.toMutableList()
    }

    /**
     * Save the talks to the CSV file when the server is stopped.
     * See Ktor shutdown hook in [infrastructure.ktor.Application.kt]
     *
     * Note: Tried to set this up as a shutdown hook using Arrow ResourceScope, but had to fall back to
     *       directly appending on saving a new talk.
     */
    fun saveTalksToCSVOnShutdownOfApplication() {
        csv.writeText("title,author,date,views,likes,link\n")
        csv.appendText(
            talks.joinToString("\n") { it.toCSVLine() }
        )
    }
}

/**
 * Allow full destructuring of the CSV line. Component 1 through 5 are provided out of the box, this adds the 6th.
 */
private operator fun List<String>.component6() = get(5)