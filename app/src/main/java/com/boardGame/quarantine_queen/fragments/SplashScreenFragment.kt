package com.boardGame.quarantine_queen.fragments

import TrackBot
import android.content.Context
import android.os.Bundle
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
import com.boardGame.quarantine_queen.viewModel.GameLevelViewModel
import kotlinx.coroutines.*

/**
 * A splash screen fragment which used to show the splash screen before main activity fragment.
 */
class SplashScreenFragment : Fragment() {

    val viewModel by activityViewModels<GameLevelViewModel>()
    val job = SupervisorJob();
    val scope = CoroutineScope(Dispatchers.Main + job)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("attaching splash fragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("creating splash fragment")
        viewModel.gridDetails.observe(viewLifecycleOwner, Observer { updateGameBoardSizeLimit(it) })
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("created splash fragment")

        scope.launch { openMainScreen() }

    }

    private fun updateGameBoardSizeLimit(it: List<GridDetail>?) {
        println("=======>$it")
        if (it.isNullOrEmpty()) {
            print("sample initialising")
//            initialDataSetter()
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
        var gridDetails = ArrayList<GridDetail>();
        var gridSolutionDetails = ArrayList<GridSolutionDetail>()
        var progressDetails = ArrayList<ProgressDetail>()
        for ((size, solutionList) in gridSolutionIndexMap) {
            var gridDetail = GridDetail(size, "$size Queen", solutionList.size)
            solutionList.forEachIndexed { solutionIndex, solution ->
                var gridSolutionDetail = GridSolutionDetail()
                var progressDetail = ProgressDetail("${size}_${solutionIndex + 1}")
                gridSolutionDetail.size = size
                gridSolutionDetail.solutionList = solution.toList() as ArrayList<String>
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
//        viewModel.insertSolutionDetails(gridSolutionDetails)
    }

    private fun getDefaultList(size: Int): ArrayList<String> {
        var emptyList = ArrayList<String>(size);
        for (i in 0 until size) {
            emptyList.add("-1")
        }
        return emptyList;
    }
    private suspend fun openMainScreen() {
        delay(3000)
        print("opening main screen")
        findNavController()
            .navigate(R.id.action_splashScreenFragment_to_mainActivityFragment)
    }
}