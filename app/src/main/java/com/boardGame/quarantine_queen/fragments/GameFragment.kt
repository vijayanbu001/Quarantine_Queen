package com.boardGame.quarantine_queen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.boardGame.quarantine_queen.R
import com.boardGame.quarantine_queen.database.entity.ProgressDetail
import com.boardGame.quarantine_queen.listeners.QueenListener
import com.boardGame.quarantine_queen.viewModel.Game
import com.boardGame.quarantine_queen.viewModel.GameLevelViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class GameFragment : Fragment(), QueenListener {

    private val viewModel by activityViewModels<GameLevelViewModel>()
    private lateinit var game: Game
    private lateinit var selectedSolutionDetail: ProgressDetail

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boardView.registerListener(this)
        game = viewModel.game
        var args = GameFragmentArgs.fromBundle(
            requireArguments()
        )
        updateBoardSize(args.gridSize)

        /*  viewModel.gridSolutionDetailBySize.observe(viewLifecycleOwner, Observer {
              it?.let {
                  println("fetched grid details ====> $it, ${args.position}")
                  if (it.size > args.position) {
                      var selectedGridSolutionDetail = it[args.position]
                      println("fetched grid details $selectedGridSolutionDetail")
                      *//* selectedGridSolutionDetail?.userSolutionList?.forEachIndexed { index, item ->
                         selectCell(
                             item.toInt(),
                             index,
                             false
                         )
                     }
                 *//*
                }
            }
        })*/
        viewModel.progressDetailBySize.observeOnce(viewLifecycleOwner, Observer { progressDetails ->
            progressDetails?.let { progressDetails ->
                println("fetched grid details $progressDetails, ${args.position}")
                selectedSolutionDetail = progressDetails[args.position]
                if (selectedSolutionDetail.userSolutionList?.isNotEmpty()!!) {
                    viewModel.setProgressList(selectedSolutionDetail.userSolutionList)
                    println("fetched grid details $selectedSolutionDetail")
                    selectedSolutionDetail.userSolutionList?.forEachIndexed { index, item ->
                        selectCell(
                            item.toInt(),
                            index,
                            false
                        )
                    }

//                    statusUpdate()
                }
                game.selectedCellLiveData.observe(
                    viewLifecycleOwner,
                    Observer { updateSelectedUI(it) })
                game.availableQueens.observe(
                    viewLifecycleOwner,
                    Observer { updateMappedQueenUI(it) })
                game.grid.observe(viewLifecycleOwner, Observer { updateGridCellsUI(it) })
                game.conflictLiveMap.observe(
                    viewLifecycleOwner,
                    Observer { updateConflictCellUI(it) })
                game.countGridLiveData.observe(
                    viewLifecycleOwner,
                    Observer { updateAvailableQueenGrid(it) })
                var count = 0
                game.progressList.observe(viewLifecycleOwner, Observer {
                    println("updating user solution ${count++} ")
                    updateUserSolution(it, args.gridSize, args.position)
                })
            }
        })




    }


    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    private fun updateUserSolution(progressList: ArrayList<String>?, size: Int, position: Int) =
        progressList?.let {
            println("updating user solution list $progressList")
            var progressDetail = ProgressDetail(selectedSolutionDetail.id)
            progressDetail.size = size;
            progressDetail.createdDate = selectedSolutionDetail.createdDate;
            progressDetail.userSolutionList = it

            /*var progressDetails = ArrayList<ProgressDetail>()
            progressDetails.add(progressDetail)
            println("progressDetails $progressDetails")*/
            viewModel.updateUserSolution(progressDetail)
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

    override fun selectCell(row: Int, col: Int, userAction: Boolean) {
        println("selectCell function")
        game.updateSelectedCell(row, col, userAction)
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