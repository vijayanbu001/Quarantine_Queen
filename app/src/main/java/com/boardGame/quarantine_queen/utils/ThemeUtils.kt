package com.boardGame.quarantine_queen.utils

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.boardGame.quarantine_queen.R


object ThemeUtils {
    private var defaultThemeId = R.style.LightTheme
    private var currentTheme = defaultThemeId;
    fun setCurrentTheme(activity: Activity, themeId: Int, recreate: Boolean = false) {
        currentTheme = themeId
        with(activity.getPreferences(AppCompatActivity.MODE_PRIVATE).edit()) {
            putInt("themeId", themeId)
            apply()
        }
        activity.setTheme(themeId)
        if (recreate) {
            activity.recreate()
        }
    }

    fun getCurrentTheme(activity: Activity): Int {

        currentTheme = with(activity.getPreferences(AppCompatActivity.MODE_PRIVATE)) {
            getInt("themeId", defaultThemeId)
        }
        return currentTheme
    }

    var attrs = intArrayOf(android.R.attr.background)

    fun getBackgroundColor(): Int {
        if (currentTheme == R.style.LightTheme) {
            return Color.WHITE
        } else {
            return Color.BLACK
        }

    }

    fun getOnBackgroundColor(): Int {
        println("currentTheme $currentTheme")
        if (currentTheme == R.style.LightTheme) {
            return Color.BLACK
        } else {
            return Color.WHITE
        }

    }
}