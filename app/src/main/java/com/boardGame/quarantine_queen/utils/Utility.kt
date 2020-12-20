package com.boardGame.quarantine_queen.utils

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import com.boardGame.quarantine_queen.R


fun getAlternateTheme(themeId: Int): Int {
    return when (themeId) {
        R.style.LightTheme -> R.style.DarkTheme
        else -> R.style.LightTheme
    }
}

fun setCurrentTheme(activity: Activity, themeId: Int, recreate: Boolean = false) {
    with(activity.getPreferences(AppCompatActivity.MODE_PRIVATE).edit()) {
        putInt("themeId", themeId)
        apply()
    }
    activity.setTheme(themeId)
    if (recreate) {
        activity.recreate()
//        activity.invalidateOptionsMenu()
    }
}

fun getCurrentTheme(activity: Activity, themeId: Int): Int {

    return with(activity.getPreferences(AppCompatActivity.MODE_PRIVATE)) {
        getInt("themeId", themeId)
    }
}

fun <T> Array<out T>.toStringList(): List<String> {
    return when (size) {
        0 -> emptyList()
        else -> {
            ArrayList(this.map {
                it.toString()
            })
        }
    }
}


fun drawCellWithDimension(
    canvas: Canvas,
    left: Int,
    top: Int,
    paint: Paint,
    width: Float,
    height: Float
) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        canvas.drawRoundRect(
            (left * width) + 5f,
            (top * height) + 5f,
            (left + 1) * width - 5f,
            (top + 1) * height - 5f,
            (100 / 4f), 100 / 4f, paint
        )
    } else {
        canvas.drawRect(
            (left * width) + 5f,
            (top * height) + 5f,
            (left + 1) * width,
            (top + 1) * height,
            paint
        )
    }


}
