package com.boardGame.quarantine_queen.utils

import android.graphics.Canvas
import android.graphics.Paint

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
