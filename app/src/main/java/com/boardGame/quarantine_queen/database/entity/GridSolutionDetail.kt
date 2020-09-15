package com.boardGame.quarantine_queen.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.boardGame.quarantine_queen.Status
import java.util.*
import kotlin.collections.ArrayList

@Entity
data class GridSolutionDetail(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ForeignKey(
        entity = GridDetail::class,
        parentColumns = ["size"],
        childColumns = ["size"],
        onDelete = ForeignKey.CASCADE
    ) var size: Int = 0,
    @ColumnInfo(name = "solution_list") var solutionList: ArrayList<String> = ArrayList(),
    @ColumnInfo(name = "user_solution_list") var userSolutionList: ArrayList<String>? = ArrayList(),
    @ColumnInfo var status: Int = Status.START.value,
    @ColumnInfo(name = "status_order") var statusOrder: Int = 0,
    @ColumnInfo(name = "hint_value") var hintValue: Int = 0,
    @ColumnInfo(name = "created_date") var createdDate: Date = Date(),
    @ColumnInfo(name = "modified_date") var modifiedDate: Date = Date()
)