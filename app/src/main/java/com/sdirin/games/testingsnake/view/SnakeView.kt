package com.sdirin.games.testingsnake.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.sdirin.games.testingsnake.model.CellType
import com.sdirin.games.testingsnake.model.ClickableArea
import com.sdirin.games.testingsnake.model.Direction
import com.sdirin.games.testingsnake.model.SnakeGame


/**
 * Created by SDirin on 26-Dec-17.
 */
class SnakeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    lateinit var game: SnakeGame
    private var width = 0f
    private var height = 0f
    val cellSize = 50

    lateinit var topClickable : ClickableArea
    lateinit var rightClickable : ClickableArea
    lateinit var bottomClickable : ClickableArea
    lateinit var leftClickable : ClickableArea

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        val paint = Paint()
        paint.alpha = 0xFF
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
                    CellType.OBSTACLE -> {
                        paint.color = Color.BLUE
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
        paint.alpha = 0x60
        if (topClickable.isVisible){
            canvas.drawPath(topClickable.getPath(), paint)
        }
        if (rightClickable.isVisible){
            canvas.drawPath(rightClickable.getPath(), paint)
        }
        if (bottomClickable.isVisible){
            canvas.drawPath(bottomClickable.getPath(), paint)
        }
        if (leftClickable.isVisible){
            canvas.drawPath(leftClickable.getPath(), paint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != 0 && h != 0){
            topClickable = ClickableArea(h,w,Direction.TOP)
            rightClickable = ClickableArea(h,w,Direction.RIGHT)
            bottomClickable = ClickableArea(h,w,Direction.DOWN)
            leftClickable = ClickableArea(h,w, Direction.LEFT)
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                //top area
                if (topClickable.inRect(x,y)){
                    game.snakeDirection = Direction.TOP
                    topClickable.isVisible = true
                    invalidate()
                }
                //right area
                if (rightClickable.inRect(x,y)){
                    game.snakeDirection = Direction.RIGHT
                    rightClickable.isVisible = true
                    invalidate()
                }
                //bottom area
                if (bottomClickable.inRect(x,y)){
                    game.snakeDirection = Direction.DOWN
                    bottomClickable.isVisible = true
                    invalidate()
                }
                //left area
                if (leftClickable.inRect(x,y)){
                    game.snakeDirection = Direction.LEFT
                    leftClickable.isVisible = true
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                topClickable.isVisible = false
                rightClickable.isVisible = false
                bottomClickable.isVisible = false
                leftClickable.isVisible = false
                invalidate()
            }
        }
        return true
    }

}