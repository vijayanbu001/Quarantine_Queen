package com.boardGame.quarantine_queen.ui.mainActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.boardGame.quarantine_queen.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A splash screen fragment which used to show the splash screen before main activity fragment.
 */
class SplashScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch { openMainScreen() }
    }

    private suspend fun openMainScreen() {
        delay(2000)
        findNavController()
            .navigate(R.id.action_splashScreenFragment_to_mainActivityFragment)
    }
}