package com.reactnative.quarantine_queen

//import listeners.QueenListener

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import listeners.QueenListener
import kotlin.math.min


class BoardView(context: Context?, attributeSet: AttributeSet) : View(context, attributeSet) {


    private lateinit var grid: Array<Array<String>>;
    private lateinit var conflictedMap: HashMap<String, ArrayList<String>>;
    private var listener: QueenListener? = null
    private val count: Int = 8
    private var availableQueen = 8
    private val hint: Boolean = true
    private var cellPixel: Float = 1f
    private var selectedRow = -1
    private var selectedColumn = -1
    private var textValue = "Q"
    private val gridLine = Paint().apply {
        color = Color.BLACK
        strokeWidth = 2f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private val selectedCell = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        strokeWidth = 1f
        style = Paint.Style.FILL

    }
    private val wrongCell: Paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeWidth = 1f
        style = Paint.Style.FILL

    }
    private val conflictedCell = Paint().apply {
        color = if (hint) Color.LTGRAY else Color.WHITE
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private val cellText = Paint().apply {
        color = Color.BLUE
        strokeWidth = 3f
        textSize = 50f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouch(event.x, event.y)
                true
            }
            else -> false

        }
    }

    private fun handleTouch(row: Float, column: Float) {

        val touchedrow = (row / cellPixel).toInt()
        val touchedCol = (column / cellPixel).toInt()
        listener?.selectCell(touchedrow, touchedCol)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val squareSize = min(widthMeasureSpec, heightMeasureSpec)
        println(squareSize)
        setMeasuredDimension(squareSize, squareSize)
    }

    override fun onDraw(canvas: Canvas) {
        cellPixel = (width / count).toFloat()

        fillGridCell(canvas)

        drawGridLine(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), width.toFloat(), gridLine)
    }

    private fun addQueen(row: Int, column: Int): Boolean {
        val status = false
        selectedRow = row
        selectedColumn = column
        invalidate()
        return status
    }

    private fun trackBot(r: Int, c: Int) {
        var i = r
        var j = c
        if (i < grid.size) {
            if (j < grid.size) {
                if ((i > -1 && j > -1) && addQueen(i, j)) {
                    i++
                    trackBot(i, 0)

                } else {
                    if (i > -1 && j > -1) {

                        grid[i][j] = ""
                        j++
                        if (j == grid.size) {
                            i--
                            j = 0
                        }
                        trackBot(i, j)
                    }
                }
            }
        }
    }

    private fun drawHints(canvas: Canvas, row: Int, column: Int) {
        if (grid[row][column] !== grid[selectedRow][selectedColumn]
        ) {
            canvas.drawRect(
                row * cellPixel,
                column * cellPixel,
                (row + 1) * cellPixel,
                (column + 1) * cellPixel, conflictedCell
            )
        }
    }

    private fun placeQueen(canvas: Canvas, row: Int, column: Int) {
        if (grid[row][column] == textValue) {
            canvas.drawRect(
                row * cellPixel,
                column * cellPixel,
                (row + 1) * cellPixel,
                (column + 1) * cellPixel, if (hasConflict(row, column)) wrongCell else selectedCell
            )

            var bounds: Rect = Rect()
            cellText.getTextBounds(grid[row][column], 0, grid[row][column].length, bounds)
            var textWidth: Int = bounds.width()
            var textHeight: Int = bounds.height()
            canvas.drawText(
                grid[row][column],
                (row * cellPixel) + (cellPixel / 2) - (textWidth / 2),
                (column * cellPixel) + (cellPixel / 2) + (textHeight / 2),
                cellText
            )
        }
    }

    private fun hasConflict(row: Int, column: Int): Boolean {
        val key = row.toString().plus(column)
        return if (conflictedMap.containsKey(key)) {
            conflictedMap[key]!!.size > 0
        } else {
            false
        }
    }

    private fun fillGridCell(canvas: Canvas) {
        if (selectedColumn == -1 || selectedRow == -1) return
        if (grid[selectedRow][selectedColumn] == textValue) {
            grid[selectedRow][selectedColumn] = ""
            listener?.conflictUpdate(selectedRow, selectedColumn, 0)
            listener?.change(-1)
        } else {
            if (availableQueen > 0) {
                grid[selectedRow][selectedColumn] = textValue
                listener?.change(1)
            }
        }
        listener?.gridUpdate(grid);
        var status = true

        for (row in 0 until count) {
            for (column in 0 until count) {
                if (!isSelectedCell(row, column)) {

                    if (isConflictCell(row, column) && grid[selectedRow][selectedColumn] == textValue) {
                        if (grid[row][column] == textValue
                        ) {
                            listener?.conflictUpdate(row, column, 1)

                            status = false
                        }
                        drawHints(canvas, row, column)
                    }
                }
                placeQueen(canvas, row, column)
                placeQueen(canvas, selectedRow, selectedColumn)
//                status = true;
            }
        }
    }

    private fun isSelectedCell(row: Int, column: Int): Boolean {
        return (selectedColumn == column && selectedRow == row)
    }

    private fun isConflictCell(row: Int, column: Int): Boolean {
        return (selectedColumn == column || selectedRow == row) || (selectedColumn + selectedRow == column + row) || (selectedColumn - selectedRow) == (column - row)
    }

    private fun drawGridLine(canvas: Canvas) {
        for (i in 1 until count) {
            canvas.drawLine(i * cellPixel, 0F, i * cellPixel, width.toFloat(), gridLine)
            canvas.drawLine(0F, i * cellPixel, width.toFloat(), i * cellPixel, gridLine)
        }
    }


    fun registerListener(listener: QueenListener) {
        this.listener = listener
    }

    fun addSelectedCell(row: Int, col: Int) {
        addQueen(row, col)
    }

    fun updateQueenCount(value: Int) {
        availableQueen = value
    }

    fun updateGrid(grid: Array<Array<String>>) {
        this.grid = grid
    }

    fun updateConflictMap(conflictMap: HashMap<String, ArrayList<String>>) {
        this.conflictedMap = conflictMap
    }

}