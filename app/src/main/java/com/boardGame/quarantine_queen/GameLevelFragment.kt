package com.boardGame.quarantine_queen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.boardGame.quarantine_queen.databinding.GameLevelFragmentBinding
import database.GridDetail
import kotlinx.android.synthetic.main.game_level_fragment.view.*
import viewModel.GameLevelViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.properties.Delegates

class GameLevelFragment : Fragment() {

    companion object {
        fun newInstance() = GameLevelFragment()
    }

    private lateinit var viewModel: GameLevelViewModel
    private lateinit var binding: GameLevelFragmentBinding
    private lateinit var gridView: GridView
    private lateinit var levelList: List<Pair<Int, Int>>
    private var gridSize by Delegates.notNull<Int>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(GameLevelViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_level_fragment, container, false)
        gridView = binding.gameLevelGrid
        gridSize = GameLevelFragmentArgs.fromBundle(requireArguments()).gridSize
        viewModel.gridDetails.observe(viewLifecycleOwner, Observer{

        levelList = getLevelList(gridSize)
        gridView.adapter = GameLevelGridAdapter(levelList)
        })
        return binding.root
    }

    private fun setGridDetails(): List<GridDetail> {
        val solutionMap: HashMap<Int, Int> = HashMap()
        solutionMap.set(4, 2)
        solutionMap.set(5, 10)
        solutionMap.set(6, 4)
        solutionMap.set(7, 40)
        solutionMap.set(8, 92)
        solutionMap.set(9, 110)
        solutionMap.set(10, 110)
        solutionMap.set(11, 110)
        solutionMap.set(12, 110)
        solutionMap.set(13, 110)

        val gridDetails: ArrayList<GridDetail> = ArrayList()
        for (i in 4..13) {
            gridDetails.add(GridDetail(i, "$i Queen", solutionMap.get(i)!!))
        }
        println("before inserting into database ${Date().time}")
        viewModel.insertDetails(gridDetails)
        println("after inserting into database ${Date().time}")

        return gridDetails

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController: NavController = view.findNavController()
        view.game_level_grid.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, position: Int, l: Long ->
            val bundle = Bundle()
            bundle.putInt("gridSize", gridSize)
            /*val action =
                MainActivity2FragmentDirections.actionMainActivity2FragmentToMainActivity( )
            println(action);
            */
            navController.navigate(R.id.action_gameLevelFragment_to_gameFragment, bundle)
        }
    }

    private fun getGridDetails(): List<GridDetail> {
        val details = viewModel.gridDetails.value
        return when (details != null && details.isNotEmpty()) {
            true -> {
//                println("fetching from database")
                details
            }
            else -> setGridDetails()
        }
    }

    private fun getGridSolutionCount(gridSize: Int): GridDetail {
        return getGridDetails().find { it.qCount == gridSize }!!
    }

    private fun getLevelList(gridSize: Int): List<Pair<Int, Int>> {

        var imageIdMap: HashMap<Int, Int> = HashMap()



        for (i in 0 until getGridSolutionCount(gridSize).solutionCount) {
            imageIdMap.set(i, R.mipmap.ic_launcher)
        }

        return imageIdMap.toList()
    }

}