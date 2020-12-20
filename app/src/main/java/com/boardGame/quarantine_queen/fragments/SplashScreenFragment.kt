package com.boardGame.quarantine_queen.fragments

import TrackBot
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.boardGame.quarantine_queen.R
import com.boardGame.quarantine_queen.Status
import com.boardGame.quarantine_queen.database.entity.GridDetail
import com.boardGame.quarantine_queen.database.entity.GridSolutionDetail
import com.boardGame.quarantine_queen.database.entity.ProgressDetail
import com.boardGame.quarantine_queen.utils.toStringList
import com.boardGame.quarantine_queen.viewModel.GameLevelViewModel
import kotlinx.coroutines.*

/**
 * A splash screen fragment which used to show the splash screen before main activity fragment.
 */
class SplashScreenFragment : Fragment() {

    private val viewModel by activityViewModels<GameLevelViewModel>()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.gridDetails.observe(viewLifecycleOwner, Observer { updateGameBoardSizeLimit(it) })
        val updatedInflater =
            inflater.cloneInContext(ContextThemeWrapper(activity, R.style.SplashTheme))

        return updatedInflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scope.launch { openMainScreen() }
    }

    private fun updateGameBoardSizeLimit(it: List<GridDetail>?) {
        if (it.isNullOrEmpty()) {
            scope.launch { initialDataSetter() }
        }

    }

    private fun initialDataSetter() {
        val trackBotInstance = TrackBot()
        val minSize = 4
        val maxSize = 10
        for (size in minSize..maxSize) {
            trackBotInstance.setSize(size)
            val grid = trackBotInstance.generateGrid()
            trackBotInstance.trackBot(grid)
        }
        val gridSolutionIndexMap = trackBotInstance.getSolutionIndex()
        val gridDetails = ArrayList<GridDetail>()
        val gridSolutionDetails = ArrayList<GridSolutionDetail>()
        val progressDetails = ArrayList<ProgressDetail>()
        for ((size, solutionList) in gridSolutionIndexMap) {
            val gridDetail = GridDetail(size, "$size Queen", solutionList.size)
            solutionList.forEachIndexed { solutionIndex, solution ->
                val gridSolutionDetail = GridSolutionDetail()
                val progressDetail = ProgressDetail("${size}_${solutionIndex + 1}")
                gridSolutionDetail.size = size
                gridSolutionDetail.solutionList = solution.toStringList() as ArrayList<String>
                progressDetail.userSolutionList= getDefaultList(size)
                if (solutionIndex == 0) {
                    progressDetail.status = Status.PROGRESS.value
                    gridSolutionDetail.status = Status.PROGRESS.value
                } else {
                    progressDetail.status = Status.START.value
                    gridSolutionDetail.status = Status.START.value
                }
                gridSolutionDetails.add(gridSolutionDetail)

                progressDetail.size = size

                progressDetails.add(progressDetail)

            }
            gridDetails.add(gridDetail)
        }

        viewModel.initializeData(gridDetails, gridSolutionDetails, progressDetails)
    }

    private fun getDefaultList(size: Int): ArrayList<String> {
        val emptyList = ArrayList<String>(size)
        for (i in 0 until size) {
            emptyList.add("-1")
        }
        return emptyList
    }
    private suspend fun openMainScreen() {
        delay(3000)
        findNavController()
            .navigate(R.id.action_splashScreenFragment_to_mainActivityFragment)
    }
}