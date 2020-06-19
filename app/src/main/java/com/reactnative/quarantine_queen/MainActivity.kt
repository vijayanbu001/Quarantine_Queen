package com.reactnative.quarantine_queen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import listeners.QueenListener
import viewModel.BoardViewModel

class MainActivity : AppCompatActivity(), QueenListener {

    private lateinit var boardViewModel: BoardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        boardView.registerListener(this)
        boardViewModel = ViewModelProviders.of(this).get(BoardViewModel::class.java)
        boardViewModel.game.selectedCellLiveData.observe(this, Observer { updateSelectedUI(it) })
        boardViewModel.game.availableQueens.observe(this, Observer { updateMappedQueenUI(it) })
        boardViewModel.game.grid.observe(this, Observer { updateGridCellsUI(it) })
        boardViewModel.game.conflictLiveMap.observe(this, Observer { updateConflictCellUI(it) })
        boardViewModel.game.countGridLiveData.observe(this, Observer { updateAvailableQueenGrid(it) })
    }

    private fun updateAvailableQueenGrid(availableQueenGrid: ArrayList<String>?) = availableQueenGrid?.let {
        countView.updateQueenGrid(availableQueenGrid);
    }

    private fun updateConflictCellUI(conflictMap: HashMap<String, ArrayList<String>>?) = conflictMap?.let {
        boardView.updateConflictMap(conflictMap)
    }

    private fun updateMappedQueenUI(value: Int?) = value?.let {
        boardView.updateQueenCount(value)
    }

    private fun updateGridCellsUI(grid: Array<Array<String>>?) = grid?.let {
        boardView.updateGrid(grid)
    }

    private fun updateSelectedUI(cell: Pair<Int, Int>?) = cell?.let {
        boardView.addSelectedCell(cell.first, cell.second)
    }

    override fun change(value: Int) {
        boardViewModel.game.updateAvailableQueenCount(value)
    }

    override fun selectCell(row: Int, col: Int) {
        boardViewModel.game.updateSelectedCell(row, col)
    }

    override fun gridUpdate(grid: Array<Array<String>>) {
        boardViewModel.game.updateGrid(grid);
    }

    override fun conflictUpdate(row: Int, column: Int, operation: Int) {
        boardViewModel.game.updateConflictMap(row, column, operation)
    }


}
