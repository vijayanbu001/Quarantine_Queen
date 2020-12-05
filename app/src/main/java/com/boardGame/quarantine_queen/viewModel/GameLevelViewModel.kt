package com.boardGame.quarantine_queen.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boardGame.quarantine_queen.database.QueenDatabase
import com.boardGame.quarantine_queen.database.entity.GridDetail
import com.boardGame.quarantine_queen.database.entity.GridSolutionDetail
import com.boardGame.quarantine_queen.database.entity.ProgressDetail
import com.boardGame.quarantine_queen.repository.QueenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GameLevelViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QueenRepository
    var gridDetails: LiveData<List<GridDetail>>
    var gridSolutionDetails: MutableLiveData<List<GridSolutionDetail>> =
        MutableLiveData()
    lateinit var progressDetailBySize: LiveData<List<ProgressDetail>>
    lateinit var gridSolutionDetailBySize: LiveData<List<GridSolutionDetail>>
    var game: Game = Game()

    init {
        val dao = QueenDatabase.getDatabase(application).dao()
        repository = QueenRepository(dao)
        gridDetails = repository.getAllGridDetails
        viewModelScope.launch {
            repository.getGridSolutionDetails.collect {
                gridSolutionDetails.value = it
            }
        }
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
            MutableLiveData(_gridSolutionDetails)
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

    fun updateStatus(gridSolutionDetail: GridSolutionDetail) {
        viewModelScope.launch { repository.updateStatus(gridSolutionDetail) }
    }

}


