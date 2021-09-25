package com.boardGame.quarantine_queen.utils

import android.graphics.Color
import android.graphics.Paint
import com.boardGame.quarantine_queen.utils.ThemeUtils.getBackgroundColor
import com.boardGame.quarantine_queen.utils.ThemeUtils.getOnBackgroundColor

/*val cellSpace = Paint().apply {
    color = Color.BLACK
    strokeWidth = 1f
    isAntiAlias = true
    style = Paint.Style.FILL_AND_STROKE

}*/
val onBackgroundColor = getOnBackgroundColor()
val backgroundColor = getBackgroundColor()

enum class GridTheme(val paint: Paint) {
    LINE(Paint().apply {
        color = onBackgroundColor
        strokeWidth = 4f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }),
    BACK_GROUND(Paint().apply {
        color = onBackgroundColor
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }),
    BLANK_CELL(Paint().apply {
        color = backgroundColor
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }),
    SELECTED_CELL(Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        strokeWidth = 1f
        style = Paint.Style.FILL

    }),
    CONFLICT_CELL(Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeWidth = 1f
        style = Paint.Style.FILL

    }),
    HINT_CELL(Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }),
    TEXT_CELL(Paint().apply {
        color = Color.BLUE
        strokeWidth = 3f
        textSize = 50f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    })

}

