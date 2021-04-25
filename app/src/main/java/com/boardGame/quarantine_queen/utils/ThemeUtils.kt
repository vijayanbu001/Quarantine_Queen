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

    fun getOnBackgroundColor(): Int {
        return currentTheme
    }

    fun getBackgroundColor(): Int {
        if (currentTheme == R.style.DarkTheme) {
            return Color.LTGRAY
        } else {
            return Color.WHITE
        }
    }
}
