package com.boardGame.quarantine_queen.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.boardGame.quarantine_queen.R
import com.boardGame.quarantine_queen.Status
import com.boardGame.quarantine_queen.database.entity.GridSolutionDetail
import com.boardGame.quarantine_queen.database.entity.ProgressDetail
import com.boardGame.quarantine_queen.databinding.GameFragmentBinding
import com.boardGame.quarantine_queen.listeners.BoardListener
import com.boardGame.quarantine_queen.model.Cell
import com.boardGame.quarantine_queen.utils.ThemeUtils
import com.boardGame.quarantine_queen.utils.getAlternateTheme
import com.boardGame.quarantine_queen.viewModel.BoardViewModel
import com.boardGame.quarantine_queen.viewModel.Game
import com.boardGame.quarantine_queen.viewModel.GameLevelViewModel
import kotlinx.android.synthetic.main.action_bar.view.*
import kotlinx.android.synthetic.main.game_fragment.*
import java.util.*
import kotlin.collections.ArrayList

class GameFragment : Fragment(), BoardListener {

    private val gameLevelViewModel by activityViewModels<GameLevelViewModel>()
    private val boardViewModel by activityViewModels<BoardViewModel>()
    private lateinit var binding: GameFragmentBinding
    private lateinit var game: Game
    private lateinit var selectedSolutionDetail: ProgressDetail
    private lateinit var solutionList: ArrayList<ProgressDetail>
    private var existingSolutionList: ArrayList<GridSolutionDetail> =
        ArrayList()
    private val dialog = GameOverDialogFragment()
    private lateinit var args: GameFragmentArgs


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("i am in GameFragment onCreate")
        game = boardViewModel.game
        args = GameFragmentArgs.fromBundle(
            requireArguments()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("i am in GameFragment onCreateView")
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment, container, false
        )
        val view = binding.root
        val toolbar: Toolbar = view.toolBar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        setHasOptionsMenu(true)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        return view
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("i am in GameFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        boardView.registerListener(this)
//        game = gameLevelViewModel.game
        /*  args = GameFragmentArgs.fromBundle(
              requireArguments()
          )*/
        game.progressDetailBySize.observeOnce(
            viewLifecycleOwner,
            Observer { optionalProgressDetails ->
                optionalProgressDetails?.let { progressDetails ->

                    game.loadBoard(args.position, progressDetails)


                }
            })
        game.gridLiveData.observe(
            viewLifecycleOwner,
            Observer { grid -> updateGridCellsUI(grid) })
        game.queenStackLiveData.observe(
            viewLifecycleOwner,
            Observer { queenStack -> updateAvailableQueenGrid(queenStack) })
        game.gameOverLiveData.observe(
            viewLifecycleOwner,
            Observer { gameOver ->
                if (gameOver == true) {
                    showGameOverModal(gameOver)
                }
            })
     /*   game.gridSolutionDetailBySize.observe(viewLifecycleOwner, Observer { solutionDetail ->
            println("solutionDetail $solutionDetail")
        })*/
    }

    private fun showGameOverModal(gameOver: Boolean?) {
        dialog.show(
            requireActivity().supportFragmentManager,
            "GameOverDialogFragment"
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.theme -> {
                ThemeUtils.setCurrentTheme(
                    requireActivity(),
                    getAlternateTheme(ThemeUtils.getCurrentTheme(requireActivity())),
                    true
                )
                true
            }
            else -> {
                true
            }
        }


//        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        var themeIcon = menu.findItem(R.id.theme)

        if (ThemeUtils.getCurrentTheme(requireActivity()) == R.style.LightTheme) {
            themeIcon?.setIcon(R.drawable.ic_twotone_bedtime_24)
        } else {
            themeIcon?.setIcon(R.drawable.ic_twotone_wb_sunny_24)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }


    private fun updateUserSolution(progressList: ArrayList<String>?, size: Int) =
        progressList?.let {
            println("updating user solution list $progressList")
            val progressDetail = ProgressDetail(selectedSolutionDetail.id)
            progressDetail.size = size
            progressDetail.createdDate = selectedSolutionDetail.createdDate
            progressDetail.userSolutionList = it
            if (gameOver(it)) {
                progressDetail.status = Status.COMPLETED.value
            } else {
                progressDetail.status = when (selectedSolutionDetail.status == 0) {
                    true -> Status.PROGRESS.value
                    else -> selectedSolutionDetail.status
                }
            }
            gameLevelViewModel.updateUserSolution(progressDetail)
        }

    private fun updateAvailableQueenGrid(availableQueen: Stack<Cell>) =
        availableQueen?.let {
            countView.updateBoardSize(args.gridSize)
            countView.updateQueenGrid(availableQueen)
            println("count view updateAvailableQueenGrid")
        }


    private fun updateGridCellsUI(grid: Array<Array<Cell>>) {
        println("observer4")
        boardView.updateBoardSize(grid.size)
        boardView.updateGrid(grid)
    }


    override fun selectCell(row: Int, col: Int, userAction: Boolean) {
        println("selectCell function")
        game.updateSelectedCell(row, col, userAction)
    }

    private fun gameOver(progressList: ArrayList<String>): Boolean {
        println("userSolutionList $solutionList $existingSolutionList")
        val currentSolution = progressList.joinToString("")
        if (boardView.gameCompleted()) {
            existingSolutionList.forEach { gridSolutionDetail ->
                println("gridSolutionDetail ${gridSolutionDetail.solutionList}")
                if (gridSolutionDetail.status != Status.COMPLETED.value && selectedSolutionDetail.status != Status.COMPLETED.value) {
                    if (gridSolutionDetail.solutionList.joinToString("") == (currentSolution)) {
                        dialog.show(
                            requireActivity().supportFragmentManager,
                            "GameOverDialogFragment"
                        )
                        gridSolutionDetail.userSolutionList = gridSolutionDetail.solutionList
                        gridSolutionDetail.status = Status.COMPLETED.value
                        println("selectedSolutionDetail $selectedSolutionDetail")
                        gameLevelViewModel.updateStatus(gridSolutionDetail)
                        return true
                    }
                }
            }

        }
        return false
    }

}