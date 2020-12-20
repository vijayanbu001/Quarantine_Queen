package com.boardGame.quarantine_queen.utils

import android.graphics.Paint
import com.boardGame.quarantine_queen.utils.ThemeUtils.getOnBackgroundColor

/*val cellSpace = Paint().apply {
    color = Color.BLACK
    strokeWidth = 1f
    isAntiAlias = true
    style = Paint.Style.FILL_AND_STROKE

}*/
val backgroundColor = getOnBackgroundColor()
enum class GridColor(val paint: Paint) {
    GRID_FILL(Paint().apply {
        color = backgroundColor
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    })

}

