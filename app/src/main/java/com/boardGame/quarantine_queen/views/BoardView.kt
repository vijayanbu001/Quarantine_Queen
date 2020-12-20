package com.boardGame.quarantine_queen.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.boardGame.quarantine_queen.listeners.QueenListener
import com.boardGame.quarantine_queen.utils.GridColor
import com.boardGame.quarantine_queen.utils.ThemeUtils.getBackgroundColor
import com.boardGame.quarantine_queen.utils.drawCellWithDimension
import kotlin.math.min


class BoardView(context: Context?, attributeSet: AttributeSet) : View(context, attributeSet) {


    private lateinit var grid: Array<Array<String>>
    private var conflictedMap: HashMap<String, ArrayList<String>> = HashMap()
    private var listener: QueenListener? = null
    private var count: Int = 4
    private var availableQueen = 4
    private val hint: Boolean = true
    private var cellPixel: Float = 1f
    private var selectedRow = -1
    private var selectedColumn = -1
    private var textValue = "Q"
    /*private val gridLine = Paint().apply {
        color = Color.BLACK
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }*/
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
    private val hintCell = Paint().apply {
        color = if (hint) Color.LTGRAY else Color.WHITE
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }

    private val blankCell = Paint().apply {
        color = getBackgroundColor()
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }

    private val cellText = Paint().apply {
        color = Color.BLUE
        strokeWidth = 3f
        textSize = 50f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }
    private var readOnly = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    handleTouch(event.x, event.y)
                    true
                }
                else -> false
            }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val squareSize = min(widthMeasureSpec, heightMeasureSpec)
        println(squareSize)
        setMeasuredDimension(squareSize, squareSize)
    }

    private fun handleTouch(row: Float, column: Float) {
        val touchedRow = (row / cellPixel).toInt()
        val touchedCol = (column / cellPixel).toInt()
        println("$touchedRow, $touchedCol")
        if (!readOnly) {
            listener?.selectCell(touchedRow, touchedCol)
        }
    }


    override fun onDraw(canvas: Canvas) {
        cellPixel = (width / count).toFloat()
        drawGrid(canvas)
        drawCells(canvas)
        fillGridCell(canvas)
    }

    private fun addQueen(row: Int, column: Int) {
        selectedRow = row
        selectedColumn = column
        invalidate()
    }

    /*  private fun trackBot(r: Int, c: Int) {
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
      }*/

    private fun drawHints(canvas: Canvas, row: Int, column: Int) {
        if (grid[row][column] !== grid[selectedRow][selectedColumn]
        ) {
            drawCell(canvas, row, column, hintCell)
        }
    }

    private fun placeQueen(canvas: Canvas, row: Int, column: Int) {
        if (this.grid[row][column] == textValue) {
            drawCell(canvas, row, column, if (hasConflict(row, column)) wrongCell else selectedCell)
            val bounds = Rect()
            cellText.getTextBounds(this.grid[row][column], 0, this.grid[row][column].length, bounds)
            val textWidth: Int = bounds.width()
            val textHeight: Int = bounds.height()
            canvas.drawText(
                this.grid[row][column],
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
        for (column in 0 until count) {
            for (row in 0 until count) {
                if (!isSelectedCell(row, column)) {
                    if (isConflictCell(
                            row,
                            column
                        ) && grid[selectedRow][selectedColumn] == textValue
                    ) {
                        if (grid[row][column] == textValue
                        ) {
                            listener?.conflictUpdate(row, column, 1)
                        }
                        drawHints(canvas, row, column)
                    }
                }
                placeQueen(canvas, row, column)
                placeQueen(canvas, selectedRow, selectedColumn)
            }
        }
    }

    private fun isSelectedCell(row: Int, column: Int): Boolean {
        return (selectedColumn == column && selectedRow == row)
    }

    private fun isConflictCell(row: Int, column: Int): Boolean {
        return (selectedColumn == column || selectedRow == row) || (selectedColumn + selectedRow == column + row) || (selectedColumn - selectedRow) == (column - row)
    }

    private fun drawCell(canvas: Canvas, row: Int, col: Int, paint: Paint) {
        drawCellWithDimension(canvas, row, col, paint, cellPixel, cellPixel)
    }

    private fun drawCells(canvas: Canvas) {
        for (row in 0 until count) {
            for (col in 0 until count) {
                drawCell(canvas, row, col, blankCell)
//                drawCell(canvas, row, col, gridLine)
            }
        }
    }

    private fun drawGrid(canvas: Canvas) {
        drawCellWithDimension(canvas, 0, 0, GridColor.GRID_FILL.paint, width.toFloat(), width.toFloat())
    }

    fun registerListener(listener: QueenListener) {
        this.listener = listener
    }

    fun addSelectedCell(row: Int, col: Int) {
        addQueen(row, col)
    }

    fun updateQueenCount(value: Int) {
        availableQueen = value
//        listener?.gameOver()
    }

    fun updateGrid(grid: Array<Array<String>>) {
        this.grid = grid
    }

    fun updateConflictMap(conflictMap: HashMap<String, ArrayList<String>>) {
        this.conflictedMap = conflictMap
    }

    fun updateBoardSize(size: Int) {
        count = size
        availableQueen = size
    }

    fun gameCompleted(): Boolean {
        return (availableQueen == 0)
    }

    fun setReadonly() {
        readOnly = true
    }

}