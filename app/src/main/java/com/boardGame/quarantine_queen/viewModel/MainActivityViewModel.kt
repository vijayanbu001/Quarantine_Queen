package com.boardGame.quarantine_queen.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.boardGame.quarantine_queen.database.QueenDatabase
import com.boardGame.quarantine_queen.database.entity.GridDetail
import com.boardGame.quarantine_queen.repository.QueenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QueenRepository
    val gridDetails:LiveData<List<GridDetail>>
//    val sharedPrefFile = "qQueenSharedPref"
//    val sharedPreferences: SharedPreferences = application.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

    init{
        val database = QueenDatabase.getDatabase(application)
        val dao = database.dao()
        repository = QueenRepository(dao)
        gridDetails = repository.getAllGridDetails
    }

    fun insertDetails(gridDetail: ArrayList<GridDetail>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertGridDetails(gridDetail)
//        val editor:SharedPreferences.Editor =  sharedPreferences.edit();
//        editor.putStringSet()
    }
}
