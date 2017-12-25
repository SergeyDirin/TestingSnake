package com.sdirin.games.testingsnake

/*
* A game of SnakeGame to test TDD approach
*/

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sdirin.games.testingsnake.model.Direction
import com.sdirin.games.testingsnake.model.SnakeGame

class MainActivity : AppCompatActivity() {

    private lateinit var game: SnakeGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        game = SnakeGame(5,7)
        game.createSnake(6,3)
        game.snakeDirection = Direction.RIGHT
        game.tick()
    }
}
