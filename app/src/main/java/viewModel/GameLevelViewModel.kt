package viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import database.GridDetail
import database.QueenDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repository.QueenRepository

class GameLevelViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QueenRepository
    val gridDetails: LiveData<List<GridDetail>>
    init{
        val dao = QueenDatabase.getDatabase(application).dao()
        repository = QueenRepository(dao)
        gridDetails = repository.getAllGridDetails
    }

    fun insertDetails(gridDetails: ArrayList<GridDetail>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertGridDetails(gridDetails)
    }
}