package com.boardGame.quarantine_queen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.boardGame.quarantine_queen.R
import com.boardGame.quarantine_queen.Status
import com.boardGame.quarantine_queen.adapters.GameLevelGridAdapter
import com.boardGame.quarantine_queen.database.entity.GridSolutionDetail
import com.boardGame.quarantine_queen.databinding.GameLevelFragmentBinding
import com.boardGame.quarantine_queen.viewModel.GameLevelViewModel
import kotlinx.android.synthetic.main.game_level_fragment.view.*
import kotlin.properties.Delegates

class GameLevelFragment : Fragment() {
    private val viewModel by activityViewModels<GameLevelViewModel>()
    private lateinit var binding: GameLevelFragmentBinding
    private lateinit var gridView: GridView
    private lateinit var levelList: List<Pair<Int, Int>>
    private var gridSize by Delegates.notNull<Int>()
    private var progressIndex = 0
    private var isProgress = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.gridSolutionDetails.observe(viewLifecycleOwner, Observer {
            updateGameLevel(it)
        })

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_level_fragment, container, false
        )
        gridSize = GameLevelFragmentArgs.fromBundle(
            requireArguments()
        ).gridSize
        gridView = binding.gameLevelGrid

        return binding.root
    }

    private fun updateGameLevel(it: List<GridSolutionDetail>) = it?.let {

        levelList = getLevelList(gridSize, it)
        gridView.adapter =
            GameLevelGridAdapter(
                levelList
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController: NavController = view.findNavController()
        view.game_level_grid.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            if (position <= progressIndex) {
                val bundle = Bundle()
                bundle.putInt("gridSize", gridSize)
                bundle.putInt("position", position)
                viewModel.fetchGridDetailBySize(gridSize)
                navController.navigate(R.id.action_gameLevelFragment_to_gameFragment, bundle)
            }
        }
    }

    private fun getLevelList(
        gridSize: Int,
        gridSolutionDetails: List<GridSolutionDetail>
    ): List<Pair<Int, Int>> {
        var imageIdMap: HashMap<Int, Int> = HashMap()
        gridSolutionDetails?.let { gridSolutionDetails ->
            if (gridSolutionDetails.isNotEmpty()) {
                gridSolutionDetails.filter { gridSolutionDetail -> gridSolutionDetail.size == gridSize }
                    .forEachIndexed { index, filteredGridSolutionDetail ->
                        if (filteredGridSolutionDetail.status == Status.PROGRESS.value || (filteredGridSolutionDetail.status === Status.START.value && !isProgress)) {
                            progressIndex = index
                            isProgress = true
                            imageIdMap[index] = R.drawable.ic_launcher_foreground
                        } else if (filteredGridSolutionDetail.status === Status.COMPLETED.value) {
                            imageIdMap[index] = R.mipmap.ic_launcher
                        } else {
                            imageIdMap[index] = R.drawable.ic_launcher_background
                        }
                    }
                isProgress = false
            }
        }

        return imageIdMap.toList()
    }

}