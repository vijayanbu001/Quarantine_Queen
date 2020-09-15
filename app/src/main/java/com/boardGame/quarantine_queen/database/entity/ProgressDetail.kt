package com.boardGame.quarantine_queen.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.boardGame.quarantine_queen.Status
import java.util.*
import kotlin.collections.ArrayList

@Entity
data class ProgressDetail(
    @PrimaryKey var id: String,
    @ForeignKey(
        entity = GridDetail::class,
        parentColumns = ["size"],
        childColumns = ["size"],
        onDelete = ForeignKey.CASCADE
    ) var size: Int = 0,
    @ColumnInfo(name = "user_solution_list") var userSolutionList: ArrayList<String>? = ArrayList(),
    @ColumnInfo var status: Int = Status.START.value,
    @ColumnInfo(name = "created_date") var createdDate: Date = Date(),
    @ColumnInfo(name = "modified_date") var modifiedDate: Date = Date()
)