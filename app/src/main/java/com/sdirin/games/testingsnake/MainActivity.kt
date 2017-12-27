package com.sdirin.games.testingsnake

/*
* A game of SnakeGame to test TDD approach
*/

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import com.sdirin.games.testingsnake.model.Direction
import com.sdirin.games.testingsnake.model.GameState
import com.sdirin.games.testingsnake.model.SnakeGame
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer




class MainActivity : AppCompatActivity() {

    private lateinit var game: SnakeGame
    private lateinit var game_timer: Timer
    var width = 0
    var height = 0
    var gameSpeed = 0
    val maxSpeed = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        game_view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                game_view.viewTreeObserver.removeOnPreDrawListener(this)

                width = game_view.getMeasuredWidth()
                height = game_view.getMeasuredHeight()
                Log.d(com.sdirin.games.testingsnake.model.TAG,"width=$width height=$height")

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

                return false
            }
        })

        //todo ui redo
        //todo ui testing
        //todo safe state
        //todo top scores
        //todo optimize drawing redraw only changed cells
        //todo publish
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    fun newGame() {
        game = SnakeGame((height / game_view.cellSize).toInt(),(width / game_view.cellSize).toInt())
        game.createSnake(3,3)
        game.createFood(4,3)
        game.snakeDirection = Direction.RIGHT
        gameSpeed = 0
        game_timer = timer("GameLoop",
                false,
                Date(),
                (500 - gameSpeed).toLong(),
                {
                    runOnUiThread { update() }
                }
        )
        game_view.game = game
        tv_main.visibility = View.GONE

        game.onEndGame = {
            game_timer.cancel()
            game_view.invalidate()
            tv_main.visibility = View.VISIBLE
            //todo show new game screen
        }
        game.onEatFood = {
            game_timer.cancel()
            game_timer.purge()
            gameSpeed += 5
            tv_score.text = (game.foods * game.scorePerFood).toString()
            if (gameSpeed > maxSpeed) gameSpeed = maxSpeed
            game_timer = timer("GameLoop",
                    false,
                    Date(),
                    (500 - gameSpeed).toLong(),
                    {
                        runOnUiThread { update() }
                    }
            )
        }
    }

    fun update() {
        if (game.state == GameState.RUNNING){
            game.tick()
            game_view.invalidate()
        }
    }

}
