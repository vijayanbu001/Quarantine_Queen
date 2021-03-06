package com.boardGame.quarantine_queen.viewModel

import androidx.lifecycle.MutableLiveData

class Game {
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var availableQueens = MutableLiveData<Int>()
    var grid = MutableLiveData<Array<Array<String>>>()
    var countGridLiveData = MutableLiveData<ArrayList<String>>()
    var conflictLiveMap = MutableLiveData<HashMap<String, ArrayList<String>>>()
    var progressList = MutableLiveData<ArrayList<String>>()
    var boardSize: Int = 4
    private var selectedRow = -1
    private var selectedCol = -1
    private lateinit var countArray: ArrayList<String>
    private var textValue = "Q"


    private fun initialSetup(size: Int) {
        boardSize = size
        countArray = ArrayList(boardSize)
        countArray.addAll(Array(boardSize) { "Q" })
        println("${countArray[3]} ====>")
        countGridLiveData.value = (countArray)
        grid.value = (Array(boardSize) { Array(boardSize) { c -> c.toString() } })
        selectedCellLiveData.value = Pair(selectedRow, selectedCol)
        availableQueens.value = boardSize
        conflictLiveMap.value = HashMap()
//        progressList.value = getDefaultList(boardSize)
    }


    fun updateSelectedCell(row: Int, col: Int, userAction: Boolean = true) {
        if (row > -1 && col > -1 && boardSize > row && boardSize > col) {
            selectedCellLiveData.value = (Pair(row, col))
            updateGridCell(row, col, userAction)
        }
    }

    fun updateBoardSize(size: Int) {
        initialSetup(size)
    }

    fun updateAvailableQueenCount(value: Int) {
        val countGrid = countGridLiveData.value!!
        if (countGrid.size > 0) {
            if (value == 1) {
                countGrid.remove("Q")
            }
        }
        if (countGrid.size <= boardSize) {
            if (value == -1) {
                countGrid.add("Q")
            }
        }
        countGridLiveData.value = (countGrid)
        availableQueens.value = (countGrid.size)

    }

    fun updateGrid(grid: Array<Array<String>>) {
        this.grid.value = (grid)
    }

    private fun updateGridCell(row: Int, col: Int, userAction: Boolean) {
        if (this.grid.value!![row][col] == textValue) {
            grid.value!![row][col] = ""
            updateConflictMap(row, col, 0)
            updateAvailableQueenCount(-1)
            if (userAction) {
                updateProgressList(row, col, 0)
            }
        } else {
            if (availableQueens.value!! > 0) {
                grid.value!![row][col] = textValue
                updateAvailableQueenCount(1)
                if (userAction) {
                    updateProgressList(row, col, 1)
                }
            }
        }
    }

    private fun updateProgressList(row: Int, column: Int, operation: Int) {
        val updatedList = ArrayList(progressList.value)
        if (operation == 1) {
            updatedList[column] = row.toString()
            progressList.value = updatedList
        } else {
            updatedList[column] = "-1"
            progressList.value = updatedList
        }
    }

    fun updateConflictMap(row: Int, column: Int, operation: Int) {
        val conflictedMap = conflictLiveMap.value!!
        val key =
            selectedCellLiveData.value!!.first.toString().plus(selectedCellLiveData.value!!.second)
        val value = row.toString().plus(column)
        if (operation == 1) {
            if (conflictedMap.containsKey(key)) {
                conflictedMap[key]!!.add(value)
            } else {
                val valueList = ArrayList<String>()
                valueList.add(value)
                conflictedMap[key] = valueList
            }

            if (conflictedMap.containsKey(value)) {
                conflictedMap[value]!!.add(key)
            } else {
                val valueList = ArrayList<String>()
                valueList.add(key)
                conflictedMap[value] = valueList
            }
        } else {
            val valueList = conflictedMap[key]
            valueList?.forEach { element -> conflictedMap[element]?.remove(key) }
            conflictedMap.remove(key)
        }

        conflictLiveMap.value = (conflictedMap)

    }

}
