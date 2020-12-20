package com.boardGame.quarantine_queen.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import com.boardGame.quarantine_queen.utils.getAlternateTheme
import com.boardGame.quarantine_queen.utils.getCurrentTheme
import com.boardGame.quarantine_queen.utils.setCurrentTheme
import com.boardGame.quarantine_queen.viewModel.GameLevelViewModel
import kotlinx.android.synthetic.main.action_bar.view.*
import java.lang.Math.abs


class MainActivityFragment : Fragment() {
    private var themeId: Int = 0;

    private val viewModel by activityViewModels<GameLevelViewModel>()
    private lateinit var binding: MainActivityFragmentBinding
    private lateinit var viewpager: ViewPager2
    private lateinit var imageList: List<Pair<Int, Int>>
    private lateinit var viewGroup: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        themeId = getCurrentTheme(requireActivity(), R.style.LightTheme)
        setCurrentTheme(requireActivity(), themeId)
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        context.theme.applyStyle(themeId, true); //blue ripple color
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.gridDetails.observe(viewLifecycleOwner, Observer {
            println("grid details $it")
            setViewPager(it)
        })

        val updatedInflater =
            inflater.cloneInContext(ContextThemeWrapper(activity, themeId))

        container.let {
            if (it != null) {
                viewGroup = it
            }
        }
        binding =
            DataBindingUtil.inflate(
                updatedInflater,
                R.layout.main_activity_fragment,
                container,
                false
            )

        val view = binding.root
        val toolbar: Toolbar = view.toolBar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPlay.setOnClickListener {
            val bundle = Bundle()
            val selectedGridSize = imageList[viewpager.currentItem].first
            bundle.putInt("gridSize", selectedGridSize)
            findNavController().navigate(
                R.id.action_mainActivityFragment_to_gameLevelFragment,
                bundle
            )
        }
    }

    private fun setTheme(resId: Int) {
        themeId = resId;
        setCurrentTheme(requireActivity(), themeId, true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println("onOptionsItemSelected $item");
        return when (item?.itemId) {
            R.id.theme -> {
                setTheme(getAlternateTheme(themeId))
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
        if (themeId == R.style.LightTheme) {
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

    private fun setViewPager(it: List<GridDetail>) {
        viewpager = binding.viewPager
        imageList = getImageList(it)
        viewpager.adapter = GameBoardAdapter(imageList)
        viewpager.offscreenPageLimit = 3
        viewpager.clipToPadding = false
        viewpager.clipChildren = false


        val pageMargin = resources.getDimension(R.dimen.pageMargin)
        val pageOffset = resources.getDimension(R.dimen.pagerOffset)
        viewpager.setPageTransformer { page, position ->
            val myOffset: Float = position * -(2 * pageOffset + pageMargin)
            if (viewpager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                println("abs(position) ${abs(position)} , $position , $myOffset ${viewpager.currentItem}")
                page.translationY = abs(position) * pageOffset
                page.alpha = 0.7f.coerceAtLeast(1 - abs(position))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    page.translationZ = 10f
                }
                if (ViewCompat.getLayoutDirection(viewpager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -myOffset
                } else {
                    page.translationX = myOffset
                }
            }
        }
    }

    private fun getImageList(it: List<GridDetail>): List<Pair<Int, Int>> {
        val imageIdMap: LinkedHashMap<Int, Int> = LinkedHashMap()
        it.forEach { gridDetail -> imageIdMap[gridDetail.qCount] = R.mipmap.ic_launcher }
        return imageIdMap.toList()
    }
}
