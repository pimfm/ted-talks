package domain.contracts

import domain.talks.TedTalk

interface TalksRepository {
    /**
     * Get a page of talks.
     *
     * @param countPerPage The number of talks to return per page. Must be greater than 0 and less than 500
     * @param pageNumber The page number to return. Must be greater than 0
     *
     * @return A list of talks
     */
    fun getTalks(countPerPage: Int, pageNumber: Int): List<TedTalk>

    /**
     * Search talks by title or author.
     *
     * @param searchTerm Used as a case-insensitive string partial to match anywhere in the String
     *                   A maximum of [count] or 500 talks will be returned
     * @return A list of talks that match the search term
     */
    fun searchTalks(searchTerm: String, count: Int): List<TedTalk>

    /**
     * Create a new talk and store it. Duplicates by author + title will not be added.
     *
     * @param talk The talk to add
     */
    fun addTalk(talk: TedTalk)

    /**
     * Rank talks by views and likes and return the top 3 talks, grouped by year.
     *
     * @return The top 3 talks per year.
     */
    fun getInfluentialTalksByYear(): Map<Int, Set<TedTalk>>
}