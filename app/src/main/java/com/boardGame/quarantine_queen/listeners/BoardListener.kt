package com.boardGame.quarantine_queen.listeners

interface BoardListener {
    fun selectCell(row: Int, col: Int, userAction: Boolean = true)
}

