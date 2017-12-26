package com.sdirin.games.testingsnake.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sdirin.games.testingsnake.model.CellType
import com.sdirin.games.testingsnake.model.SnakeGame
import kotlin.math.min

/**
 * Created by SDirin on 26-Dec-17.
 */
class SnakeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    lateinit var game: SnakeGame

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val paint = Paint()

        paint.color = Color.BLUE
        canvas?.drawRect(0f,0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

        val cols = game.field.size
        val rows = game.field[0].size
        val gap = 1f
        val cell_width = min(canvas!!.width.toFloat() / cols,canvas!!.height.toFloat() / rows)
        val cell_height = min(canvas!!.width.toFloat() / cols,canvas!!.height.toFloat() / rows)

        for ((col, line) in game.field.withIndex()){
            for ((row, cell) in line.withIndex()){
                when (cell) {
                    CellType.EMPTY -> {paint.color = Color.WHITE}
                    CellType.SNAKE_BODY -> {paint.color = Color.BLACK}
                    CellType.FOOD -> {paint.color = Color.GREEN}
                }

                canvas?.drawRect(
                        col * cell_width + gap,
                        row * cell_height + gap,
                        (col+1) * cell_width - gap,
                        (row+1) * cell_height - gap,
                        paint)

            }
        }
    }
}