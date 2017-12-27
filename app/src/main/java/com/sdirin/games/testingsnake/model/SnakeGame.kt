package com.sdirin.games.testingsnake.model

import java.util.*

/**
 * Created by SDirin on 22-Dec-17.
 */

const val TAG = "SnakeApp"
enum class Direction {TOP, RIGHT, DOWN, LEFT}
enum class CellType {
    EMPTY,
    SNAKE_BODY,
    OUT_OF_FIELD,
    FOOD,
    DEAD_BODY,
    OBSTACLE
}
enum class GameState {
    RUNNING,
    GAME_OVER
}
fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) +  start

class SnakeGame(val height: Int, val width: Int) {

    var snakeDirection = Direction.RIGHT

    private var snake: MutableList<Point> = mutableListOf()

    var state: GameState = GameState.RUNNING
    var foods = 0
    val scorePerFood = 13

    val foodToObstacle = 3

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
    var field:Array<Array<CellType>> = Array(width) { Array<CellType>(height,{ CellType.EMPTY }) }

    lateinit var onEndGame: ()->Unit
    lateinit var onEatFood: ()->Unit

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

        when (snakeDirection) {
            Direction.TOP -> y--
            Direction.RIGHT -> x++
            Direction.DOWN -> y++
            Direction.LEFT -> x--
        }
        val oldX = x
        val oldY = y
        x %= field.size
        y %= field[0].size
        if (x<0) x = field.size - 1
        if (y<0) y = field[0].size - 1
        snake[0].x = x
        snake[0].y = y

        when (field[x][y]) {
            CellType.EMPTY -> {
                field[snake.last().x][snake.last().y] = CellType.EMPTY
                snake.removeAt(snake.size-1)
                field[x][y] = CellType.SNAKE_BODY
            }
            CellType.FOOD -> {
                if (this::onEatFood.isInitialized) {
                    onEatFood()
                }
                foods ++
                if (foods % foodToObstacle == 0){
                    generateNew(CellType.OBSTACLE)
                }
                generateNew(CellType.FOOD)
                field[x][y] = CellType.SNAKE_BODY
            }
            CellType.SNAKE_BODY -> {
                //die
                field[x][y] = CellType.DEAD_BODY
                state = GameState.GAME_OVER
                if (this::onEndGame.isInitialized) {
                    onEndGame()
                }
            }
            CellType.OBSTACLE -> {
                //die
                field[x][y] = CellType.DEAD_BODY
                state = GameState.GAME_OVER
                if (this::onEndGame.isInitialized) {
                    onEndGame()
                }
            }
        }
    }
    fun generateNew(cellType: CellType) {
        var loop = true
        while(loop){
            var p = Point((0..field.size).random(),(0..field[0].size).random())
            if (field[p.x][p.y] == CellType.EMPTY){
                loop = false
                field[p.x][p.y] = cellType
            }
        }
    }

    fun createFood(x:Int, y:Int) {
        checkOutOfField(x,y)
        field[x][y] = CellType.FOOD
    }

    fun createObstacle(x:Int, y:Int) {
        checkOutOfField(x,y)
        field[x][y] = CellType.OBSTACLE
    }

    fun getCellTypeCount(cellType: CellType):Int {
        var foodCnt = 0
        for (line in field) {
            line
                .filter { it == cellType }
                .forEach { foodCnt++ }
        }
        return foodCnt
    }

}