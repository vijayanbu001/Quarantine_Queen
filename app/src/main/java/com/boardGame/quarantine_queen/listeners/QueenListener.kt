package com.boardGame.quarantine_queen.listeners

interface QueenListener {
    fun change(value: Int)
    fun selectCell(row: Int, col: Int,userAction: Boolean =true)
    fun gridUpdate(grid: Array<Array<String>>)
    fun conflictUpdate(row: Int, column: Int, operation: Int)
}

