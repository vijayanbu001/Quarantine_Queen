package com.boardGame.quarantine_queen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import listeners.QueenListener
import viewModel.Game
import viewModel.GameViewModel

class GameFragment : Fragment(), QueenListener {

    private lateinit var viewModel: GameViewModel
    private lateinit var game: Game

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
//        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        boardView.registerListener(this)
        game = viewModel.game
        updateBoardSize(GameFragmentArgs.fromBundle(requireArguments()).gridSize)

        game.selectedCellLiveData.observe(viewLifecycleOwner, Observer { updateSelectedUI(it) })
        game.availableQueens.observe(viewLifecycleOwner, Observer { updateMappedQueenUI(it) })
        game.grid.observe(viewLifecycleOwner, Observer { updateGridCellsUI(it) })
        game.conflictLiveMap.observe(viewLifecycleOwner, Observer { updateConflictCellUI(it) })
        game.countGridLiveData.observe(
            viewLifecycleOwner,
            Observer { updateAvailableQueenGrid(it) })
    }

    private fun updateBoardSize(size: Int) {
        game.updateBoardSize(size)
        boardView.updateBoardSize(size)
        countView.updateBoardSize(size)
    }

    private fun updateAvailableQueenGrid(availableQueenGrid: ArrayList<String>?) =
        availableQueenGrid?.let {
            countView.updateQueenGrid(availableQueenGrid)
            println("observer1")
        }

    private fun updateConflictCellUI(conflictMap: HashMap<String, ArrayList<String>>?) =
        conflictMap?.let {
            boardView.updateConflictMap(conflictMap)
            println("observer2")

        }

    private fun updateMappedQueenUI(value: Int?) = value?.let {
        boardView.updateQueenCount(value)
        println("observer3")
    }

    private fun updateGridCellsUI(grid: Array<Array<String>>?) = grid?.let {
        boardView.updateGrid(grid)
        println("observer4")
    }

    private fun updateSelectedUI(cell: Pair<Int, Int>?) = cell?.let {
        boardView.addSelectedCell(cell.first, cell.second)
        println("observer5")
    }

    override fun change(value: Int) {
        println("change function")
        game.updateAvailableQueenCount(value)
    }

    override fun selectCell(row: Int, col: Int) {
        println("selectCell function")
        game.updateSelectedCell(row, col)
    }

    override fun gridUpdate(grid: Array<Array<String>>) {
        println("gridUpdate function")
        game.updateGrid(grid)
    }

    override fun conflictUpdate(row: Int, column: Int, operation: Int) {
        println("conflictUpdate function")
        game.updateConflictMap(row, column, operation)
    }

}