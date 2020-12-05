package com.boardGame.quarantine_queen.utils

import android.graphics.Color
import android.graphics.Paint

/*val cellSpace = Paint().apply {
    color = Color.BLACK
    strokeWidth = 1f
    isAntiAlias = true
    style = Paint.Style.FILL_AND_STROKE

}*/

enum class GridColor(val paint: Paint) {
    GRID_FILL(Paint().apply {
        color = Color.BLACK
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    })

}

