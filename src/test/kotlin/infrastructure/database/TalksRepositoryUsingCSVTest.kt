package com.iodigital.infrastructure.database

import domain.talks.TedTalk
import infrastructure.database.TalksRepositoryUsingCSV
import toMonthYear
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class TalksRepositoryUsingCSVTest {
    private val csv = File("src/test/kotlin/test-talks.csv")
    private val backup = File("src/test/kotlin/test-talks-backup.csv")

    @Test
    fun `should paginate properly when using close to list size indexes`() {
        // Given
        val talksRepository = TalksRepositoryUsingCSV(csv)

        val countPerPage = 3
        val pageNumber = 4

        // When
        val talks = talksRepository.getTalks(countPerPage, pageNumber)

        // Then
        assertEquals(1, talks.size)
    }

    @Test
    fun `should search talks by title or author`() {
        // Given
        val talksRepository = TalksRepositoryUsingCSV(csv)

        val searchTerm = "deserve the right to repair"
        val count = 3

        // When
        val talks = talksRepository.searchTalks(searchTerm, count)

        // Then
        assertEquals(1, talks.size)
    }

    @Test
    fun `should add a talk`() {
        // Given
        val talksRepository = TalksRepositoryUsingCSV(csv)

        val talk = TedTalk(
            title = "A second brAIn",
            author = "Pim van Gurp",
            views = 100,
            likes = 50,
            date = "december 2022".toMonthYear(),
            link = "https://pim.fm/talks/a-second-brain"
        )

        // Assert
        assertEquals(10, talksRepository.getTalks(100, 1).size)

        // When
        talksRepository.addTalk(talk)

        // Then
        assertEquals(11, talksRepository.getTalks(100, 1).size)

        // Clean up
        csv.writeText(backup.readText())
    }

    @Test
    fun `should get the most influential talks by year`() {
        // Given
        val talksRepository = TalksRepositoryUsingCSV(csv)

        // When
        val influentialTalks = talksRepository.getInfluentialTalksByYear()

        // Then
        assertEquals(3, influentialTalks.size)
    }
}