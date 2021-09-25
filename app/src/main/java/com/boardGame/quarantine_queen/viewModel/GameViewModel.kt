package com.boardGame.quarantine_queen.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class GameViewModel(application: Application) : AndroidViewModel(application) {
    var game: Game = Game(application)

}