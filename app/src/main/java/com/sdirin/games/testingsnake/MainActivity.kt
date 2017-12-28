package com.sdirin.games.testingsnake

/*
* A game of SnakeGame to test TDD approach
*/

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewTreeObserver
import com.sdirin.games.testingsnake.model.CellType
import com.sdirin.games.testingsnake.model.Direction
import com.sdirin.games.testingsnake.model.GameState
import com.sdirin.games.testingsnake.model.SnakeGame
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer




const val TAG = "SnakeApp"
class MainActivity : AppCompatActivity() {

    private lateinit var game: SnakeGame
    private lateinit var game_timer: Timer
    var width = 0
    var height = 0
    var gameSpeed = 0
    val maxSpeed = 300
    var skipFirst = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        game_view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                game_view.viewTreeObserver.removeOnPreDrawListener(this)

                width = game_view.getMeasuredWidth()
                height = game_view.getMeasuredHeight()

                newGame()

                val prefs = getSharedPreferences("game", Context.MODE_PRIVATE)
                if (prefs.contains("snake_game")){
                    gameSpeed = game.resume(prefs.getString("snake_game","{}"))
                    tv_score.text = (game.foods*game.scorePerFood).toString()
                }

                game_view.setOnClickListener {
                    if (game.state == GameState.GAME_OVER){
                        newGame()
                    }
                }
                tv_main.setOnClickListener {
                    when (game.state) {
                        GameState.GAME_OVER -> newGame()
                        GameState.PAUSED -> {
                            game.state = GameState.RUNNING
                            tv_main.visibility = View.GONE
                        }
                    }
                }

                return false
            }
        })

        //todo bug saving dead on restore continues
        //todo top scores local
        //todo optimize drawing redraw only changed cells
        //todo splash and pause screens
        //todo ui testing
        //todo publish
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
        tv_main.visibility = View.GONE

        game.onEndGame = {
            game_timer.cancel()
            game_view.invalidate()
            tv_main.visibility = View.VISIBLE
            tv_main.text = "Game Over"
            //todo show new game screen
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
        val prefs = getSharedPreferences("game", Context.MODE_PRIVATE)
        prefs.edit().putString("snake_game",game.getData(gameSpeed)).apply()
    }

    override fun onBackPressed() {
        if (game.state == GameState.GAME_OVER || game.state == GameState.PAUSED){
            super.onBackPressed()
        } else {
            game.state = GameState.PAUSED
            tv_main.visibility = View.VISIBLE
            tv_main.text = "PAUSED"
        }
    }

    fun update() {
        if (game.state == GameState.RUNNING){
            game.tick()
            game_view.invalidate()
        }
    }

}
