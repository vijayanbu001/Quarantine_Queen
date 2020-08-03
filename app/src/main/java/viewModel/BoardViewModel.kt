package viewModel

import android.util.Log
import androidx.lifecycle.ViewModel

class BoardViewModel : ViewModel() {

    var game: Game = Game()

    init {
        Log.i("BoardViewModel", "BoardViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("BoardViewModel", "BoardViewModel destroyed!")
//        println(game.selectedCellLiveData.value?.first)
    }
}
