package com.boardGame.quarantine_queen.views

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.boardGame.quarantine_queen.utils.GridTheme
import com.boardGame.quarantine_queen.utils.drawCellWithDimension

class CellView (private val canvas: Canvas, val width: Float, val height: Float) {

    fun drawCell(row: Int, col: Int, colour: Paint){
        drawCellWithDimension(canvas, row * 1f, col * 1f,  colour, width, height, 0)
    }

    fun placeQueen(row: Int, column: Int, cellPixel: Float, colour: Paint) {
        drawCell(
            row,
            column,
            colour
        )
        val bounds = Rect()
        GridTheme.TEXT_CELL.paint.getTextBounds("Q", 0, 1, bounds)
        val textWidth: Int = bounds.width()
        val textHeight: Int = bounds.height()

        canvas.drawText(
            "Q",
            (row * cellPixel) + (cellPixel / 2) - (textWidth / 2),
            (column * cellPixel) + (cellPixel / 2) + (textHeight / 2),
            GridTheme.TEXT_CELL.paint
        )

    }
}