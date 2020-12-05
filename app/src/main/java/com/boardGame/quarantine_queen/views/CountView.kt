package com.boardGame.quarantine_queen.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.boardGame.quarantine_queen.utils.drawCellWithDimension


class CountView(context: Context?, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var count: Int = 4
    private var availableQueenList: ArrayList<String> = ArrayList()
    private var cellPixel: Float = 0F

    private val gridLine = Paint().apply {
        color = Color.BLACK
        strokeWidth = 2f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private val cellText = Paint().apply {
        color = Color.BLUE
        strokeWidth = 3f
        textSize = 50f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthPixels = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        cellPixel = widthPixels.toFloat() / count
        val newHeightSpec = MeasureSpec.makeMeasureSpec(cellPixel.toInt(), widthMode)
        setMeasuredDimension(widthMeasureSpec, newHeightSpec)
        super.onMeasure(widthMeasureSpec, newHeightSpec)
    }

    override fun onDraw(canvas: Canvas) {
        fillGridCell(canvas)
        drawGrid(canvas)
//        canvas.drawRect(0f, 0f, width.toFloat(), cellPixel, gridLine)
    }

    private fun fillGridCell(canvas: Canvas) {
        val row = 0
        for (col in 0 until count) {
            placeQueen(canvas, row, col)
        }
    }

    private fun drawGrid(canvas: Canvas) {
        drawCellWithDimension(canvas, 0, 0, gridLine, width.toFloat(), cellPixel)
    }

    private fun placeQueen(canvas: Canvas, row: Int, column: Int) {
        val text = if (availableQueenList.size > column) availableQueenList[column] else ""
        canvas.drawRect(
            column * cellPixel,
            row * cellPixel,
            (column + 1) * cellPixel,
            (row + 1) * cellPixel, gridLine
        )
        with(canvas) {
            drawText(
                text,
                (column * cellPixel) + (cellPixel / 2) - 20f,
                (row * cellPixel) + (cellPixel / 2) + 15f,
                cellText
            )
        }
    }

    fun updateQueenGrid(availableQueenList: ArrayList<String>) {
        this.availableQueenList = availableQueenList
        invalidate()
    }

    fun updateBoardSize(size: Int) {
        count = size
    }
}