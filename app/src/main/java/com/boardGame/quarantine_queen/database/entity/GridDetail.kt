package com.boardGame.quarantine_queen.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GridDetail(
    @PrimaryKey val qCount: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "solution_count") val solutionCount: Int
)
