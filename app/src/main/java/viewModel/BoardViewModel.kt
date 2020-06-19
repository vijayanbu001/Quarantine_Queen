package viewModel

import androidx.lifecycle.ViewModel
import android.util.Log
import listeners.Game

class BoardViewModel : ViewModel() {

    var game: Game = Game()

    init {
        Log.i("BoardViewModel", "BoardViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BoardViewModel", "BoardViewModel destroyed!")
    }
}
