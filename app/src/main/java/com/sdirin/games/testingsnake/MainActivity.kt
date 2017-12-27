package com.sdirin.games.testingsnake

/*
* A game of SnakeGame to test TDD approach
*/

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.sdirin.games.testingsnake.model.Direction
import com.sdirin.games.testingsnake.model.GameState
import com.sdirin.games.testingsnake.model.SnakeGame
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {

    private lateinit var game: SnakeGame
    private lateinit var game_timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newGame()

        game_view.setOnClickListener {
            if (game.state == GameState.GAME_OVER){
                newGame()
            }
        }
        tv_main.setOnClickListener {
            if (game.state == GameState.GAME_OVER){
                newGame()
            }
        }


        //todo full screen with buttons

        //todo speed increase
        //todo score
        //todo border
        //todo ui testing
        //todo beautify
        //todo optimize drawing redraw only changed cells
        //todo publish
    }

    fun newGame() {
        game = SnakeGame(17,17)
        game.createSnake(3,3)
        game.createFood(4,3)
        game.snakeDirection = Direction.RIGHT

        game_timer = timer("GameLoop",
                false,
                Date(),
                500,
                {
                    runOnUiThread { update() }
                }
        )
        game_view.game = game
        tv_main.visibility = View.GONE

        game.onEndGame = {
            game_timer.cancel()
            game_view.invalidate()
            Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
            tv_main.visibility = View.VISIBLE
            //todo show new game screen
        }
    }

    fun update() {
        if (game.state == GameState.RUNNING){
            game.tick()
            game_view.invalidate()
        }
    }

}
