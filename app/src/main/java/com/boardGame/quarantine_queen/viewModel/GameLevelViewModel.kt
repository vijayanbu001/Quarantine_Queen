package com.boardGame.quarantine_queen.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.boardGame.quarantine_queen.database.QueenDatabase
import com.boardGame.quarantine_queen.database.entity.GridDetail
import com.boardGame.quarantine_queen.database.entity.GridSolutionDetail
import com.boardGame.quarantine_queen.database.entity.ProgressDetail
import com.boardGame.quarantine_queen.repository.QueenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class GameLevelViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QueenRepository
    var gridDetails: LiveData<List<GridDetail>>
    var gridSolutionDetails: LiveData<List<GridSolutionDetail>>
    lateinit var progressDetailBySize: LiveData<List<ProgressDetail>>
    lateinit var gridSolutionDetailBySize: LiveData<List<GridSolutionDetail>>
    var game: Game = Game()

    init {
        val dao = QueenDatabase.getDatabase(application).dao()
        repository = QueenRepository(dao)
        gridDetails = repository.getAllGridDetails
        gridSolutionDetails =
            repository.getGridSolutionDetails.asLiveData(EmptyCoroutineContext, 1000)
    }


    fun fetchGridDetailBySize(gridSize: Int) {
        gridSolutionDetailBySize = repository.getGridSolutionDetailBySize(gridSize)
        progressDetailBySize = repository.getProgressDetailsBySize(gridSize)
    }

    fun initializeData(
        _gridDetails: ArrayList<GridDetail>,
        _gridSolutionDetails: ArrayList<GridSolutionDetail>,
        _progressDetails: ArrayList<ProgressDetail>
    ) {
        gridDetails =
            MutableLiveData<ArrayList<GridDetail>>(_gridDetails) as LiveData<List<GridDetail>>
        gridSolutionDetails =
            MutableLiveData<ArrayList<GridSolutionDetail>>(_gridSolutionDetails) as LiveData<List<GridSolutionDetail>>
        insertDetails(_gridDetails)
        insertGridSolutionDetails(_gridSolutionDetails)
        insertProgressDetails(_progressDetails)

    }

    private fun insertDetails(gridDetails: ArrayList<GridDetail>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertGridDetails(gridDetails)
        }

    private fun insertGridSolutionDetails(gridSolutionDetails: ArrayList<GridSolutionDetail>) =
        viewModelScope.launch {
            repository.insertGridSolutionDetails(gridSolutionDetails)
        }

    private fun insertProgressDetails(progressDetails: ArrayList<ProgressDetail>) =
        viewModelScope.launch {
            repository.insertProgressDetails(progressDetails)
        }

    fun updateUserSolution(progressDetail: ProgressDetail) =
        viewModelScope.launch {
            repository.updateProgressDetail(progressDetail)
        }


    fun setProgressList(userSolutionList: ArrayList<String>?) = userSolutionList?.let {
        println("setting progress list $it")
        game.progressList.value = it
    }

}


