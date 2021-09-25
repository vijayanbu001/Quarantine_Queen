package com.boardGame.quarantine_queen.fragments

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
import com.boardGame.quarantine_queen.database.entity.GridDetail
import com.boardGame.quarantine_queen.viewModel.GameLevelViewModel

/**
 * A splash screen fragment which used to show the splash screen before main activity fragment.
 */
class SplashScreenFragment : Fragment() {

    private val viewModel by activityViewModels<GameLevelViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.gridDetails.observe(viewLifecycleOwner, Observer { openMainScreen(it) })
        val updatedInflater =
            inflater.cloneInContext(ContextThemeWrapper(activity, R.style.SplashTheme))

        return updatedInflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    private fun openMainScreen(it: List<GridDetail>?) {
        println("open Main screen")
        it?.let {
            findNavController()
                .navigate(R.id.action_splashScreenFragment_to_mainActivityFragment)
        }
    }
}