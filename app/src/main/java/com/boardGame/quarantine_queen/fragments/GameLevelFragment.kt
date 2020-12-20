 package com.boardGame.quarantine_queen.fragments

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import com.boardGame.quarantine_queen.utils.ThemeUtils
import com.boardGame.quarantine_queen.utils.getAlternateTheme
import com.boardGame.quarantine_queen.viewModel.GameLevelViewModel
import kotlinx.android.synthetic.main.action_bar.view.*
import kotlinx.android.synthetic.main.game_level_fragment.view.*
import kotlin.properties.Delegates


class GameLevelFragment : Fragment() {
    private val viewModel by activityViewModels<GameLevelViewModel>()
    private lateinit var binding: GameLevelFragmentBinding
    private lateinit var gridView: GridView
    private lateinit var levelList: List<Pair<Int, Int>>
    private var gridSize by Delegates.notNull<Int>()
    private var progressIndex = -1
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

    private fun updateGameLevel(it: List<GridSolutionDetail>?) = it?.let {
        levelList = getLevelList(gridSize, it)
        gridView.adapter =
            GameLevelGridAdapter(
                levelList
            )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println("onOptionsItemSelected $item");
        return when (item?.itemId) {
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
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        var themeIcon = menu?.findItem(R.id.theme)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController: NavController = view.findNavController()
        view.game_level_grid.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            println("position ==> $position,$progressIndex")
            if (progressIndex == -1 || position <= progressIndex) {
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
        val imageIdMap: LinkedHashMap<Int, Int> = LinkedHashMap()
        if (gridSolutionDetails.isNotEmpty()) {
            gridSolutionDetails.filter { gridSolutionDetail -> gridSolutionDetail.size == gridSize }
                .forEachIndexed { index, filteredGridSolutionDetail ->
                    if (filteredGridSolutionDetail.status == Status.PROGRESS.value || (filteredGridSolutionDetail.status == Status.START.value && !isProgress)) {
                        progressIndex = index
                        isProgress = true
                        imageIdMap[index] = R.drawable.ic_launcher_foreground
                    } else if (filteredGridSolutionDetail.status == Status.COMPLETED.value) {
                        imageIdMap[index] = R.mipmap.ic_launcher
                    } else {
                        imageIdMap[index] = R.drawable.ic_launcher_background
                    }
                }
            isProgress = false
        }
        return imageIdMap.toList()
    }

}