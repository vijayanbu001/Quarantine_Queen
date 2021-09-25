package com.boardGame.quarantine_queen.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.boardGame.quarantine_queen.listeners.BoardListener
import com.boardGame.quarantine_queen.model.Cell
import com.boardGame.quarantine_queen.utils.GridTheme
import com.boardGame.quarantine_queen.utils.GridTheme.*
import com.boardGame.quarantine_queen.utils.drawCellWithDimension

class BoardView(context: Context?, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var grid: Array<Array<Cell>>? = null
    private var conflictedMap: HashMap<String, ArrayList<String>> = HashMap()
    private var listener: BoardListener? = null
    private var count: Int = 4
    private var availableQueen = 4
    private val hint: Boolean = true
    private var cellPixel: Float = 1f
    private var selectedRow = -1
    private var selectedColumn = -1
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

        val squareSize =
            if (widthMeasureSpec < heightMeasureSpec) widthMeasureSpec else heightMeasureSpec
        println("squareSize ,${squareSize}")
        setMeasuredDimension(squareSize, squareSize)
    }

    private fun handleTouch(row: Float, column: Float) {
        val touchedRow = (row / cellPixel).toInt()
        val touchedCol = (column / cellPixel).toInt()
        println("touchedRow $touchedRow,touchedCol $touchedCol")
        if (!readOnly) {
            listener?.selectCell(touchedRow, touchedCol)
        }
    }


    override fun onDraw(canvas: Canvas) {
        cellPixel = (width / count).toFloat().minus(1f)
        drawGrid(canvas)
        drawCells(canvas)
        drawGridBorder(canvas)
//        fillGridCell(canvas)
    }

    private fun addQueen(row: Int, column: Int) {
        selectedRow = row
        selectedColumn = column
//        invalidate()
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

    /*  private fun drawHints(canvas: Canvas, row: Int, column: Int) {
          if (grid[row][column] !== grid[selectedRow][selectedColumn]
          ) {
              drawCell(canvas, row, column, hintCell)
          }
      }*/

    private fun placeQueen(canvas: Canvas, row: Int, column: Int) {
        drawCell(
            canvas,
            row,
            column,
            if (this.grid?.get(row)?.get(column)
                    ?.isSafe() == true
            ) SELECTED_CELL.paint else CONFLICT_CELL.paint
        )
        val bounds = Rect()
        TEXT_CELL.paint.getTextBounds("Q", 0, 1, bounds)
        val textWidth: Int = bounds.width()
        val textHeight: Int = bounds.height()
        canvas.drawText(
            "Q",
            (row * cellPixel) + (cellPixel / 2) - (textWidth / 2),
            (column * cellPixel) + (cellPixel / 2) + (textHeight / 2),
            TEXT_CELL.paint
        )
    }


    /* private fun fillGridCell(canvas: Canvas) {
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
                             println("i am in conflicted cell $grid[$row][$column],col $column ,row $row  ")
                             listener?.conflictUpdate(row, column, 1)
                         }
                         drawHints(canvas, row, column)
                     }
                 }
                 placeQueen(canvas, row, column)
                 placeQueen(canvas, selectedRow, selectedColumn)
             }
         }
     }*/

    private fun isSelectedCell(row: Int, column: Int): Boolean {
        return (selectedColumn == column && selectedRow == row)
    }

    private fun isConflictCell(row: Int, column: Int): Boolean {
        return (selectedColumn == column || selectedRow == row) || (selectedColumn + selectedRow == column + row) || (selectedColumn - selectedRow) == (column - row)
    }

    private fun drawCell(canvas: Canvas, row: Int, col: Int, paint: Paint) {
        drawCellWithDimension(canvas, row * 1f, col * 1f, paint, cellPixel, cellPixel)
    }

    private fun drawGridBorder(canvas: Canvas) {
        drawCellWithDimension(canvas, 0f, 0f, GridTheme.LINE.paint, width.toFloat(), cellPixel)
    }

    private fun drawCells(canvas: Canvas) {
        for (row in 0 until count) {
            for (col in 0 until count) {
                println("row $row column $col")
                if (!grid.isNullOrEmpty() && grid?.get(row)?.get(col)?.hasQueen() == true) {

                    placeQueen(canvas, row, col)
                } else {
                    drawCell(canvas, row, col, BLANK_CELL.paint)
                }
            }
        }
    }

    private fun drawGrid(canvas: Canvas) {
        drawCellWithDimension(
            canvas,
            0f,
            0f,
            BACK_GROUND.paint,
            width.toFloat(),
            width.toFloat()
        )
    }

    fun registerListener(listener: BoardListener) {
        this.listener = listener
    }

    fun addSelectedCell(row: Int, col: Int) {
        addQueen(row, col)
    }

    fun updateQueenCount(value: Int) {
        availableQueen = value
//        listener?.gameOver()
    }

    fun updateGrid(grid: Array<Array<Cell>>) {
        this.grid = grid
        invalidate()
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