package com.boardGame.quarantine_queen.views

import android.graphics.Color
import android.graphics.Paint
import com.boardGame.quarantine_queen.utils.ThemeUtils

object CellThemes {


    val selectedCell = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        strokeWidth = 1f
        style = Paint.Style.FILL

    }
    val gridLine = Paint().apply {
        color = ThemeUtils.getBackgroundColor()
        strokeWidth = 2f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }

    val blankCell = Paint().apply {
        color = ThemeUtils.getBackgroundColor()
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }
    val conflictedCell: Paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeWidth = 1f
        style = Paint.Style.FILL

    }
    val hintCell = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 1f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }

    val cellText = Paint().apply {
        color = Color.BLUE
        strokeWidth = 3f
        textSize = 50f
        isAntiAlias = true
        style = Paint.Style.FILL_AND_STROKE
    }

/*private val gridLine = Paint().apply {
    color = Color.BLACK
    strokeWidth = 1f
    isAntiAlias = true
    style = Paint.Style.STROKE
}*/
}
