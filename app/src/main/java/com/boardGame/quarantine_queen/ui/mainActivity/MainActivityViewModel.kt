package com.boardGame.quarantine_queen.ui.mainActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import database.GridDetail
import database.QueenDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import repository.QueenRepository

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QueenRepository
    val gridDetails:LiveData<List<GridDetail>>
//    val sharedPrefFile = "qQueenSharedPref"
//    val sharedPreferences: SharedPreferences = application.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

    init{
        val dao = QueenDatabase.getDatabase(application).dao()
        repository = QueenRepository(dao)
        gridDetails = repository.getAllGridDetails
    }

    fun insertDetails(gridDetail: ArrayList<GridDetail>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertGridDetails(gridDetail)
//        val editor:SharedPreferences.Editor =  sharedPreferences.edit();
//        editor.putStringSet()
    }
}
