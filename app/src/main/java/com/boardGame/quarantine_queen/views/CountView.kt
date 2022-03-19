package com.boardGame.quarantine_queen.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.boardGame.quarantine_queen.model.Cell
import com.boardGame.quarantine_queen.utils.GridTheme
import com.boardGame.quarantine_queen.utils.GridTheme.BACK_GROUND
import com.boardGame.quarantine_queen.utils.drawCellWithDimension
import java.util.*

class CountView(context: Context?, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var count: Int = 4
    private var availableQueenStack: Stack<Cell>? = null
    private var cellPixel: Float = 0F
    private val queenText = "Q"


    private val cellText = Paint().apply {
        color = Color.RED
        strokeWidth = 3f
        textSize = 50f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthPixels = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val actualCellPixel = widthPixels.toFloat() / count
        cellPixel = (widthPixels.toFloat() / count).minus(1f)
        val newHeightSpec = MeasureSpec.makeMeasureSpec(actualCellPixel.toInt(), widthMode)
        setMeasuredDimension(widthMeasureSpec, newHeightSpec)
        super.onMeasure(widthMeasureSpec, newHeightSpec)
    }

    override fun onDraw(canvas: Canvas) {
        drawGrid(canvas)
        fillGridCell(canvas)
        drawGridBorder(canvas)
    }

    private fun fillGridCell(canvas: Canvas) {
        val col = 0
        for (row in 0 until count) {
            drawCell(canvas, row, col, GridTheme.BLANK_CELL.paint)
            if (!availableQueenStack.isNullOrEmpty() && availableQueenStack?.size!! > row && availableQueenStack?.get(row)
                    ?.hasQueen() == true
            ) {
                placeQueen(canvas, row, col)
            }
        }
    }

    private fun drawCell(canvas: Canvas, row: Int, col: Int, paint: Paint) {
        drawCellWithDimension(
            canvas,
            row * 1f,
            col * 1f,
            paint,
            cellPixel,
            cellPixel,count
        )
    }

    private fun drawGrid(canvas: Canvas) {
        drawCellWithDimension(canvas, 0f, 0f, BACK_GROUND.paint, width.toFloat(), cellPixel,count)
    }

    private fun drawGridBorder(canvas: Canvas) {
        drawCellWithDimension(canvas, 0f, 0f, GridTheme.LINE.paint, width.toFloat(), cellPixel,count)
    }

    private fun placeQueen(canvas: Canvas, row: Int, column: Int) {
        with(canvas) {
            drawText(
                queenText,
                (row * cellPixel) + (cellPixel / 2) - 20f,
                (column * cellPixel) + (cellPixel / 2) + 15f,
                cellText
            )
        }
    }

    fun updateQueenGrid(availableQueenStack: Stack<Cell>) {
        this.availableQueenStack = availableQueenStack
        invalidate()
    }

    fun updateBoardSize(size: Int) {
        count = size
//        invalidate()
    }
}