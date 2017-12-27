package com.sdirin.games.testingsnake.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.sdirin.games.testingsnake.model.CellType
import com.sdirin.games.testingsnake.model.Direction
import com.sdirin.games.testingsnake.model.SnakeGame



/**
 * Created by SDirin on 26-Dec-17.
 */
class SnakeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    lateinit var game: SnakeGame
    var width = 0f
    var height = 0f
    val cellSize = 50

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        val paint = Paint()

        paint.color = Color.BLUE
        canvas?.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

        val cols = game.field.size
        val rows = game.field[0].size
        val gap = 1f
        width = canvas!!.width.toFloat()
        height = canvas.height.toFloat()
        val cell_width = cellSize//min(width / cols, height / rows)
        val cell_height = cellSize//min(width / cols, height / rows)
        val offset = (width - cell_width * cols) / 2

        for ((col, line) in game.field.withIndex()) {
            for ((row, cell) in line.withIndex()) {
                when (cell) {
                    CellType.EMPTY -> {
                        paint.color = Color.WHITE
                    }
                    CellType.SNAKE_BODY -> {
                        paint.color = Color.BLACK
                    }
                    CellType.FOOD -> {
                        paint.color = Color.GREEN
                    }
                    CellType.DEAD_BODY -> {
                        paint.color = Color.RED
                    }
                }

                canvas.drawRect(
                        col * cell_width + gap + offset,
                        row * cell_height + gap,
                        (col + 1) * cell_width - gap + offset,
                        (row + 1) * cell_height - gap,
                        paint)

            }
        }
        paint.color = Color.RED
        paint.alpha = 30
//        canvas.drawRect(
//                width * 0.2f,
//                0f,
//                width*0.8f,
//                height*0.2f,
//                paint)
//        canvas.drawRect(
//                width * 0.8f,
//                height*0.2f,
//                width,
//                height*0.8f,
//                paint)
    }

    override fun callOnClick(): Boolean {
        val result = super.callOnClick()

//        if ()

        return result
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                //top area
                if (inRect(x,y,0f, width*0.8f, height*0.2f, width * 0.2f)){
                    game.snakeDirection = Direction.TOP
                }
                //right area
                if (inRect(x,y,height*0.2f, width, height*0.8f, width * 0.8f)){
                    game.snakeDirection = Direction.RIGHT
                }
                //bottom area
                if (inRect(x,y,height*0.8f, width*0.8f, height, width * 0.2f)){
                    game.snakeDirection = Direction.DOWN
                }
                //left area
                if (inRect(x,y,height*0.2f, width*0.2f, height*0.8f, 0f)){
                    game.snakeDirection = Direction.LEFT
                }
            }
        }
        return false
    }

    private fun inRect(x:Float,y:Float,top:Float,right:Float,bottom:Float,left:Float) = (x in left..right && y in top..bottom)
}