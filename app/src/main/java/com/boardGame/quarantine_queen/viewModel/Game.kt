package com.boardGame.quarantine_queen.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.boardGame.quarantine_queen.Status
import com.boardGame.quarantine_queen.database.QueenDatabase
import com.boardGame.quarantine_queen.database.entity.GridSolutionDetail
import com.boardGame.quarantine_queen.database.entity.ProgressDetail
import com.boardGame.quarantine_queen.model.Cell
import com.boardGame.quarantine_queen.model.Grid
import com.boardGame.quarantine_queen.model.QueenStack
import com.boardGame.quarantine_queen.repository.QueenRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class Game(application: Application) {
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var gridLiveData = MutableLiveData<Array<Array<Cell>>>()
    var queenStackLiveData = MutableLiveData<Stack<Cell>>()
    var gameOverLiveData = MutableLiveData<Boolean>(false)
    private var boardSizeLiveData = MutableLiveData<Int>(4)
    private lateinit var queenStack: QueenStack
    private lateinit var grid: Grid
    lateinit var progressDetailBySize: LiveData<List<ProgressDetail>>
    lateinit var gridSolutionDetailBySize: /*LiveData<*/List<GridSolutionDetail>/*>*/
    private var currentSolutionIndex = 0;
    private var selectedRow = -1
    private var selectedCol = -1
    private val repository: QueenRepository

    init {
        val dao = QueenDatabase.getDatabase(application).dao()
        repository = QueenRepository(dao)
    }

    private fun initialSetup(size: Int) {
        boardSizeLiveData.postValue(size)
        queenStack = QueenStack(size)
        grid = Grid(size)
        gameOverLiveData.value = false
        queenStackLiveData.value = queenStack.getStack()
        gridLiveData.value = grid.getGrid()
        selectedCellLiveData.value = Pair(selectedRow, selectedCol)
    }

    fun fetchGridDetailBySize(gridSize: Int) {
        progressDetailBySize = repository.getProgressDetailsBySize(gridSize)
        repository.getGridSolutionDetailBySize(gridSize)
            .observeForever { solutionDetail -> gridSolutionDetailBySize = solutionDetail }
        initialSetup(gridSize)
        updateMutableData()
    }

    private fun getSize(): Int {
        return boardSizeLiveData.value!!
    }

    fun updateSelectedCell(row: Int, col: Int, userAction: Boolean = true) {
        if (row > -1 && col > -1 && getSize() > row && getSize() > col) {
            selectedCellLiveData.value = (Pair(row, col))
            selectProcess(row, col, userAction)
            updateMutableData()
            updateDatabase()
        }
    }

    private fun updateDatabase() {
        println("i am in update database ${grid.getQueensIndex()}")
        val updatedProgressDetail = progressDetailBySize.value?.get(currentSolutionIndex)

        updatedProgressDetail?.let {
            it.userSolutionList =
                grid.getQueensIndex() as ArrayList<String>
            gridSolutionDetailBySize?.forEach { gridSolutionDetail ->
                if (gridSolutionDetail.status != Status.COMPLETED.value && it.status != Status.COMPLETED.value) {
                    if (gridSolutionDetail.solutionList.joinToString("") == (it.userSolutionList?.joinToString(
                            ""
                        ))
                    ) {
                        /* dialog.show(
                             requireActivity().supportFragmentManager,
                             "GameOverDialogFragment"
                         )*/
                        gameOverLiveData.value = true

                        gridSolutionDetail.userSolutionList = gridSolutionDetail.solutionList
                        gridSolutionDetail.status = Status.COMPLETED.value
                        gridSolutionDetail.statusOrder = currentSolutionIndex
//                        println("selectedSolutionDetail $selectedSolutionDetail")
//                        val gameLevelViewModel by ViewModel<GameLevelViewModel>
//                        gameLevelViewModel.updateStatus(gridSolutionDetail)
//                        return true
                        GlobalScope.launch {
                            repository.updateStatus(gridSolutionDetail)
                        }
                    }
                }

            }

            if (it.status == Status.START.value) {
                it.status = Status.PROGRESS.value
            } else if (gameOverLiveData.value == true) {
                it.status = Status.COMPLETED.value
            }
            updateUserSolution(it)
        }
    }


    private fun selectProcess(row: Int, col: Int, _userAction: Boolean) {
        if (gameOverLiveData.value == false) {
            if (grid.getCell(row, col)
                    .hasQueen() && queenStack.getQueenCount() < queenStack.getMaxQueenCount() && queenStack.getQueenCount() >= 0
            ) {
                queenStack.addQueen()
                grid.selectCell(row, col)
            } else {
                if (grid.getAvailableQueenCount() < grid.getMaxQueenCount()
                ) {
                    queenStack.removeQueen()

                    grid.selectCell(row, col)
                }
            }
        }

    }

    private fun updateMutableData() {
        this.gridLiveData.value = grid.getGrid()
        this.queenStackLiveData.value = queenStack.getStack()
    }

    fun updateBoardSize(size: Int) {
        initialSetup(size)
    }

    fun loadBoard(position: Int, progressDetails: List<ProgressDetail>) {
        progressDetails.forEachIndexed { index, progressDetail ->
            if (position == index) {
                currentSolutionIndex = position
                val userSolution = ArrayList<String>()
                progressDetail.userSolutionList?.let { userSolution.addAll(it) }
                grid.loadGrid(userSolution)
                queenStack.loadQueen(userSolution)
                if (progressDetail.status == Status.COMPLETED.value) {
                    gameOverLiveData.value = true
                }
            }
        }
        updateMutableData()
    }

    private fun updateUserSolution(progressDetail: ProgressDetail) =
        GlobalScope.launch {
            repository.updateProgressDetail(progressDetail)
        }

    /* fun updateAvailableQueenCount(value: Int) {
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
 */
    /*  fun updateGrid(grid: Array<Array<String>>) {
          this.grid.value = (grid)
      }*/

    /* private fun updateGridCell(row: Int, col: Int, userAction: Boolean) {
         if (this.grid.value?.getCell(row, col)?.hasQueen() == true) {
             grid.value!![row][col] = ""
             updateConflictMap(row, col, 0)
             updateAvailableQueenCount(-1)
             if (userAction) {
                 updateProgressList(row, col, 1)
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
 */
    /* private fun updateProgressList(row: Int, column: Int, operation: Int) {
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

     }*/

}
