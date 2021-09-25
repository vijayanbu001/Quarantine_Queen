package com.boardGame.quarantine_queen.model

import java.util.*

class QueenStack(private var size: Int) {
    private var stack = Stack<Cell>()
    private var maxQueenCount = size;

    init {
        for (index in 0 until size) {
            var cell = Cell(0, index)
            cell.addQueen()
            stack.push(cell)
        }
    }

    fun getMaxQueenCount(): Int {
        return maxQueenCount
    }

    fun addQueen() {
        if (stack.size < size) {
            var cell = Cell(0, stack.size)
            cell.addQueen()
            stack.push(cell)
        }
    }

    fun removeQueen() {
        if (stack.size > 0) {
            stack.pop()
        }
    }

    fun getQueenCount(): Int {
        return stack.size
    }

    fun hasQueen(): Boolean {
        return stack.size > 0
    }

    fun getStack(): Stack<Cell> {
        return stack
    }

    fun reset() {
        stack.clear()
    }

    fun loadQueen(list: ArrayList<String>) {
        this.reset()
        list.forEach { item ->
            val row = item.split("_")[0].toInt()
            val column = item.split("_")[1].toInt()
            if (row == -1 && column == -1) {
                this.addQueen()
            }
        }
    }

}