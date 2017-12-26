package com.sdirin.games.testingsnake

/*
* A game of SnakeGame to test TDD approach
*/

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View.OnTouchListener
import com.sdirin.games.testingsnake.model.Direction
import com.sdirin.games.testingsnake.model.SnakeGame
import com.sdirin.games.testingsnake.view.SnakeView
import java.util.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    private lateinit var game: SnakeGame
    private lateinit var game_view: SnakeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        game = SnakeGame(17,17)
        game.createSnake(3,3)
        game.createFood(4,3)
        game.snakeDirection = Direction.RIGHT

        game_view = findViewById<SnakeView>(R.id.game_view)
        game_view.game = game

        val gdt = GestureDetector(GestureListener())
        game_view.setOnTouchListener(OnTouchListener { view, event ->
            gdt.onTouchEvent(event)
            true
        })

        timer("GameLoop",
            false,
            Date(),
            800,
            {
                runOnUiThread { update() }
            }
        )
    }

    fun update() {
        game.tick()
        game_view.invalidate()
    }


    private val SWIPE_MIN_DISTANCE = 120
    private val SWIPE_THRESHOLD_VELOCITY = 200

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (e1.x - e2.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                game.snakeDirection = Direction.LEFT
                return false // Right to left
            } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                game.snakeDirection = Direction.RIGHT
                return false // Left to right
            }

            if (e1.y - e2.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                game.snakeDirection = Direction.TOP
                return false // Bottom to top
            } else if (e2.y - e1.y > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                game.snakeDirection = Direction.DOWN
                return false // Top to bottom
            }
            return false
        }
    }
}
