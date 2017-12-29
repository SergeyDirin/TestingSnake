package com.sdirin.games.testingsnake.activities

/*
* A game of SnakeGame to test TDD approach
*/

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewTreeObserver
import com.sdirin.games.testingsnake.R
import com.sdirin.games.testingsnake.model.CellType
import com.sdirin.games.testingsnake.model.Direction
import com.sdirin.games.testingsnake.model.GameState
import com.sdirin.games.testingsnake.model.SnakeGame
import com.sdirin.games.testingsnake.utils.TopScores
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer






const val TAG = "SnakeApp"
const val SNAKE_GAME = "snake_game"
class MainActivity : AppCompatActivity() {

    private lateinit var game: SnakeGame
    private lateinit var game_timer: Timer
    var width = 0
    var height = 0
    var gameSpeed = 0
    val maxSpeed = 300
    var skipFirst = false
    val topScores = TopScores(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //todo bug pwoer off and on starts another loop
        //todo show controll arrows before start

        //todo publish

        //todo ui testing
        //todo sounds
        //todo in cell animation
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    fun newGame() {
        game = SnakeGame((height / game_view.cellSize).toInt(),(width / game_view.cellSize).toInt())
        game.createSnake(3,3)
        game.generateNew(CellType.FOOD)
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
        main_text_container.visibility = View.GONE
        tv_score.text = "0"

        game.onEndGame = {
            game_timer.cancel()
            game_view.invalidate()
            topScores.safeScore(game.foods * game.scorePerFood)
//            tv_main.visibility = View.VISIBLE
//            tv_main.text = "Game Over"
            val intent = Intent(this,GameOverActivity::class.java)
            this@MainActivity.startActivity(intent)
        }
        game.onEatFood = {
            game_timer.cancel()
            game_timer.purge()
            gameSpeed += 5
            tv_score.text = (game.foods * game.scorePerFood).toString()
            if (gameSpeed > maxSpeed) gameSpeed = maxSpeed
            skipFirst = true
            game_timer = timer("GameLoop",
                    false,
                    Date(),
                    (500 - gameSpeed).toLong(),
                    {
                        if (skipFirst) {
                            skipFirst = false
                        } else {
                            runOnUiThread { update() }
                        }
                    }
            )
        }
    }

    override fun onPause() {
        super.onPause()
        safeState()
    }

    private fun onGameResume(){

        game_view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                game_view.viewTreeObserver.removeOnPreDrawListener(this)

                width = game_view.getMeasuredWidth()
                height = game_view.getMeasuredHeight()

                newGame()

                val prefs = getSharedPreferences("game", Context.MODE_PRIVATE)
                if (prefs.contains(SNAKE_GAME)){
                    gameSpeed = game.resume(prefs.getString(SNAKE_GAME,"{}"))
                    tv_score.text = (game.foods*game.scorePerFood).toString()
                }

                controller_view.onDirectionChange = {
                    game.snakeDirection = it
                }

                game_view.setOnClickListener {
                    when (game.state) {
                        GameState.GAME_OVER -> newGame()
                        GameState.PAUSED -> {
                            game.state = GameState.RUNNING
                            main_text_container.visibility = View.GONE
                        }
                    }
                }
                main_text_container.setOnClickListener {
                    when (game.state) {
                        GameState.GAME_OVER -> newGame()
                        GameState.PAUSED -> {
                            game.state = GameState.RUNNING
                            main_text_container.visibility = View.GONE
                        }
                    }
                }

                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        onGameResume()
    }

    fun safeState() {
        val prefs = getSharedPreferences("game", Context.MODE_PRIVATE)
        if (game.state != GameState.GAME_OVER) {
            prefs.edit().putString(SNAKE_GAME, game.getData(gameSpeed)).apply()
        } else {
            prefs.edit().remove(SNAKE_GAME).apply()
        }
    }

    override fun onBackPressed() {
        if (game.state == GameState.GAME_OVER || game.state == GameState.PAUSED){
            super.onBackPressed()
        } else {
            game.state = GameState.PAUSED
            main_text_container.visibility = View.VISIBLE
            tv_main.text = "PAUSED"
            safeState()
        }
    }

    fun update() {
        if (game.state == GameState.RUNNING){
            game.tick()
            game_view.invalidate()
        }
    }

}
