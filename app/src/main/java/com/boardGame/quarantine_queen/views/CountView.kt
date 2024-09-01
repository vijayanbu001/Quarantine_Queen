package com.boardGame.quarantine_queen.views

import android.R
import android.content.Context
import android.graphics.*
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



//        val squareSize =
//            if (widthMeasureSpec < heightMeasureSpec) widthMeasureSpec else heightMeasureSpec
//        println("squareSize ===> ,${squareSize}")
//        setMeasuredDimension(squareSize, squareSize)

        val widthPixels = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val actualCellPixel = widthPixels.toFloat() / count
        cellPixel = (actualCellPixel)
        val newHeightSpec = MeasureSpec.makeMeasureSpec(actualCellPixel.toInt(), widthMode)
        setMeasuredDimension(widthMeasureSpec, newHeightSpec)
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
            val bounds = Rect()
            GridTheme.TEXT_CELL.paint.getTextBounds("Q", 0, 1, bounds)
            val textWidth: Int = bounds.width()
            val textHeight: Int = bounds.height()

            drawText(
                queenText,
                (row * cellPixel) + (cellPixel / 2) - (textWidth / 2),
                (column * cellPixel) + (cellPixel / 2) + (textHeight / 2),
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