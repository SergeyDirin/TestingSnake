package com.sdirin.games.testingsnake

import com.sdirin.games.testingsnake.utils.TopScores
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by SDirin on 28-Dec-17.
 */
class TopScoresTest {

    lateinit var topScores: TopScores

    @Before
    fun setUp() {
    }

    @Test
    fun addScore() {
        topScores.safeScore(123)
        val score = topScores.getTop().get(0).score
        assertEquals(123, score)
    }
}