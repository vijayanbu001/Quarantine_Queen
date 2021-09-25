package com.boardGame.quarantine_queen.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel

class BoardViewModel(application: Application) : AndroidViewModel(application) {

    var game: Game = Game(application)

    init {
        Log.i("BoardViewModel", "BoardViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BoardViewModel", "BoardViewModel destroyed!")
    }
}
