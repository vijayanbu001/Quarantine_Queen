package viewModel

import android.arch.lifecycle.ViewModel
import listeners.Game

class BoardViewModel: ViewModel() {

    var game: Game = Game()
}