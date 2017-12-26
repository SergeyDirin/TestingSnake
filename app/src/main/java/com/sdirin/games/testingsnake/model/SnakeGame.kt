package com.sdirin.games.testingsnake.model

import android.util.Log
import java.util.*

/**
 * Created by SDirin on 22-Dec-17.
 */

val TAG = "SnakeApp"
enum class Direction {TOP, RIGHT, DOWN, LEFT}
enum class CellType {
    EMPTY,
    SNAKE_BODY,
    OUT_OF_FIELD,
    FOOD
}
enum class GameState {
    RUNNING,
    GAME_OVER
}
fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) +  start
interface IEndGame {
    fun onEnd(): Unit
}
class SnakeGame(val height: Int, val width: Int) {

    var snakeDirection = Direction.RIGHT

    private var snake: MutableList<Point> = mutableListOf()

    var state: GameState = GameState.RUNNING

    private var snakePos = Point(0,0)
    private var snakeEnd = Point(0, 0)
    class FieldSizeException(override var message:String): Exception()
    class OutOfFieldException(override var message:String): Exception()
    class GameOverException(override var message:String): Exception()
    init {
        if (height <= 0 || width <= 0) {
            throw FieldSizeException("Field size have to be more that 0")
        }
    }
    private var field:Array<Array<CellType>> = Array(width) { Array<CellType>(height,{ CellType.EMPTY }) }

    lateinit var onEndGame: ()->Unit

    private fun checkOutOfField(x: Int, y: Int){
        if (x < 0 || y < 0 || x >= field.size || y >= field[0].size){
            throw OutOfFieldException("Snake can not be created out of field")
        }
    }

    fun createSnake(x: Int, y: Int) {
        checkOutOfField(x,y)
        field[x][y] = CellType.SNAKE_BODY
        snake.add(Point(x,y))
    }
    fun findAt(x: Int, y: Int): CellType {
        if (x < 0 || y < 0 || x >= field.size || y >= field[0].size){
            return CellType.OUT_OF_FIELD
        }
        return field[x][y]
    }

    fun tick() {
        if (state == GameState.GAME_OVER){
            throw GameOverException("Can not tick during game over")
        }
        var x = snake[0].x
        var y = snake[0].y
        snake.add(0,Point(x,y))
        Log.d(TAG,"snakeDirection="+snakeDirection)
        Log.d(TAG,"snake[0]="+snake[0])
        when (snakeDirection) {
            Direction.TOP -> y--
            Direction.RIGHT -> x++
            Direction.DOWN -> y++
            Direction.LEFT -> x--
        }
        x %= field.size
        y %= field[0].size
        snake[0].x = x
        snake[0].y = y
//        Log.d(TAG,"snake[0]="+snake[0])

        if (field[x][y] == CellType.EMPTY) {
            field[snake.last().x][snake.last().y] = CellType.EMPTY
            snake.removeAt(snake.size-1)
        } else if (field[x][y] == CellType.FOOD) {
            generateNewFood()
        } else if (field[x][y] == CellType.SNAKE_BODY) {
            //die
            if (this::onEndGame.isInitialized) {
                onEndGame()
            }
            state = GameState.GAME_OVER
        }

        field[x][y] = CellType.SNAKE_BODY

        Log.d(TAG,"findAt(6,3)="+findAt(6,3))
        Log.d(TAG,"findAt(0,3)="+findAt(0,3))
        Log.d(TAG,"snake.size="+snake.size)
        Log.d(TAG,"snake[0]="+snake[0])
    }

    private fun generateNewFood() {
        var loop = true
        while(loop){
            var p = Point((0..field.size).random(),(0..field[0].size).random())
            if (field[p.x][p.y] == CellType.EMPTY){
                loop = false
                field[p.x][p.y] = CellType.FOOD
            }
        }
    }

    fun createFood(x:Int, y:Int) {
        checkOutOfField(x,y)
        field[x][y] = CellType.FOOD
    }

    fun getFoodCount():Int {
        var foodCnt = 0
        for (line in field) {
            line
                .filter { it == CellType.FOOD }
                .forEach { foodCnt++ }
        }
        return foodCnt
    }
}