package com.boardGame.quarantine_queen.model

class Cell(private var row: Int, private var column: Int) {

    private var queen: Boolean = false
    private var safe: Boolean = true
    private var readOnly: Boolean = false
    private var lastChanged: Boolean = false
    private var conflictedPeers: HashSet<String> = HashSet()

    fun hasQueen(): Boolean {
        return this.queen
    }

    fun addQueen() {
        this.queen = true
    }

    fun removeQueen() {
        this.queen = false
    }

    fun setLastChanged(lastChanged: Boolean) {
        this.lastChanged = lastChanged
    }

    private fun setSafe() {
        this.safe = this.conflictedPeers.size == 0
    }

    fun isSafe(): Boolean {
        return this.safe
    }

    fun setReadOnly() {
        this.readOnly = true
    }

    fun equals(cell: Cell): Boolean {
        return this.row == cell.row && this.column == cell.column
    }

    fun getIndex(): String {
        return this.row.toString().plus("_").plus(this.column)
    }

    fun hasConflictWith(cell: Cell): Boolean {
        val conflictCell =
            ((this.column == cell.column || this.row == cell.row) || (this.column + this.row == cell.column + cell.row) || (this.column - this.row) == (cell.column - cell.row)) && cell.hasQueen() && this.hasQueen()
        if (conflictCell) {
            this.conflictedPeers.add(cell.getIndex())
            cell.conflictedPeers.add(this.getIndex())
        } else {
            this.conflictedPeers.remove(cell.getIndex())
            cell.conflictedPeers.remove(this.getIndex())
        }
        this.setSafe()
        cell.setSafe()
        return conflictCell;
    }
}

