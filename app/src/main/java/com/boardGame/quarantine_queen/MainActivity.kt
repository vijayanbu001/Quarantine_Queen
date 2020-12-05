package com.boardGame.quarantine_queen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.LightTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }


    override fun onSupportNavigateUp() =
        findNavController(this, R.id.nav_host_fragment).navigateUp()


}
