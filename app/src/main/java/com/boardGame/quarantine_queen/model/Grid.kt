package com.boardGame.quarantine_queen.model

import java.util.*

class Grid(private var size: Int) {
    private var grid = Array(size) { row -> Array(size) { column -> Cell(row, column) } }
    private var availableQueenCount: Int = 0
    private var maxQueenCount: Int = size
    private fun setAvailableQueenCount(queenCount: Int) {
        availableQueenCount = queenCount
    }

    fun getAvailableQueenCount(): Int {
        return availableQueenCount
    }

    private fun increaseAvailableQueenCount() {
        availableQueenCount++
    }

    fun getMaxQueenCount(): Int {
        return maxQueenCount
    }

    private fun reset() {
        grid = Array(size) { row -> Array(size) { column -> Cell(row, column) } }
        setAvailableQueenCount(0)
    }

    fun loadGrid(list: List<String>) {
        this.reset()
        list.forEach { item ->
            val row = item.split("_")[0].toInt()
            val column = item.split("_")[1].toInt()
            if (row > -1 && column > -1) {
                val selectedCell = getCell(row, column)
                selectedCell.addQueen()
                selectedCell.setLastChanged(true)
                calculateConflicts(selectedCell)
                increaseAvailableQueenCount()
            }
        }
    }


    fun getGrid(): Array<Array<Cell>> {
        return this.grid
    }

    fun getCell(row: Int, column: Int): Cell {
        return grid[row][column]
    }

    private fun getRow(index: Int): Array<Cell> {
        return grid[index]
    }

    /*fun getColumn(index: Int): Array<Cell> {
        return Array(size) { row ->
            getRow(row)[index]
        }
    }*/


    fun selectCell(rowIndex: Int, columnIndex: Int) {
        val selectedCell = getRow(rowIndex)[columnIndex]

        if (selectedCell.hasQueen()) {
            selectedCell.removeQueen()
            selectedCell.setLastChanged(false)
            setAvailableQueenCount(getAvailableQueenCount() - 1)

        } else {
            if (getAvailableQueenCount() < size) {
                selectedCell.addQueen()
                selectedCell.setLastChanged(true)
                setAvailableQueenCount(getAvailableQueenCount() + 1)
            }
        }

        calculateConflicts(selectedCell)
    }

    private fun calculateConflicts(selectedCell: Cell) {
        for (row in 0 until size) {
            for (column in 0 until size) {
                val cell = getCell(row, column)
                if (!selectedCell.equals(cell)) {
                    cell.setLastChanged(false)
                    selectedCell.hasConflictWith(cell)
                }
            }
        }
    }

    /*private fun Array<Cell>.getValidCell(): HashSet<String> =
        fold(hashSetOf<String>("-1_-1"), { acc: HashSet<String>, cell: Cell ->
            when (cell.hasQueen()) {
                true -> {
                    if (acc.contains("-1_-1")) {
                        acc.remove("-1_-1")
                    }
                    acc.add(cell.getIndex())
                    acc
                }
                else -> acc
            }
        })*/


    fun getQueensIndex(): List<String> {
        val queenIndexList = Array(size) { "-1_-1" }.toList() as ArrayList<String>
        this.grid.forEach { row ->
            row.forEach { cell ->
                if (cell.hasQueen()) {
                    queenIndexList.remove("-1_-1")
                    queenIndexList.add(cell.getIndex())
                }
            }
        }
        queenIndexList.trimToSize()
        return queenIndexList
    }

}






