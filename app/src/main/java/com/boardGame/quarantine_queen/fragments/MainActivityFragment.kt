package com.boardGame.quarantine_queen.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.boardGame.quarantine_queen.R
import com.boardGame.quarantine_queen.adapters.GameBoardAdapter
import com.boardGame.quarantine_queen.database.entity.GridDetail
import com.boardGame.quarantine_queen.databinding.MainActivityFragmentBinding
import com.boardGame.quarantine_queen.viewModel.GameLevelViewModel
import kotlin.math.abs


class MainActivityFragment : Fragment() {

    private val viewModel by activityViewModels<GameLevelViewModel>()
    private lateinit var binding: MainActivityFragmentBinding
    private lateinit var viewpager: ViewPager2
    private lateinit var imageList: List<Pair<Int, Int>>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        print("onAttach")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("fragment creation process")
        binding =
            DataBindingUtil.inflate(inflater, R.layout.main_activity_fragment, container, false)
        viewModel.gridDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                setViewPager(it)
            }
        })
        viewModel.gridSolutionDetails.observe(viewLifecycleOwner, Observer { })
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPlay.setOnClickListener {
            var bundle: Bundle = Bundle()
            val selectedGridSize = imageList[viewpager.currentItem].first
            bundle.putInt("gridSize", selectedGridSize)
            findNavController().navigate(
                R.id.action_mainActivityFragment_to_gameLevelFragment,
                bundle
            )
        }
    }

    private fun setViewPager(it: List<GridDetail>) {
        viewpager = binding.viewPager
        imageList = getImageList(it)
        viewpager.adapter = GameBoardAdapter(imageList)
        viewpager.offscreenPageLimit = 3
        viewpager.clipToPadding = false
        viewpager.clipChildren = false


        val pageMargin = resources.getDimension(R.dimen.pageMargin).toFloat()
        val pageOffset = resources.getDimension(R.dimen.pagerOffset).toFloat()
        viewpager.setPageTransformer(ViewPager2.PageTransformer { page, position ->
            val myOffset: Float = position * -(2 * pageOffset + pageMargin)
            if (viewpager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                println("abs(position) ${abs(position)} , $position , $myOffset ${viewpager.currentItem}")
                page.translationY = abs(position) * pageOffset
                page.alpha = Math.max(0.7f, 1 - abs(position))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    page.translationZ = 10f
                }
                if (ViewCompat.getLayoutDirection(viewpager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -myOffset
                } else {
                    page.translationX = myOffset
                }
            }
        })
    }

    private fun getImageList(it: List<GridDetail>): List<Pair<Int, Int>> {
        var imageIdMap: HashMap<Int, Int> = HashMap()
        it?.forEach { gridDetail ->
            imageIdMap[gridDetail.qCount] = R.mipmap.ic_launcher
        }
        return imageIdMap.toList()
    }
}