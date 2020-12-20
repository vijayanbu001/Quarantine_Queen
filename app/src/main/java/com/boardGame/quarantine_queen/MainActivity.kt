package com.boardGame.quarantine_queen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import com.boardGame.quarantine_queen.utils.getCurrentTheme
import com.boardGame.quarantine_queen.utils.setCurrentTheme

class MainActivity : AppCompatActivity() {
    private var themeId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        themeId = getCurrentTheme(this, R.style.LightTheme)
        setCurrentTheme(this, themeId)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

    }

    override fun onSupportNavigateUp() =
        findNavController(this, R.id.nav_host_fragment).navigateUp()
}
