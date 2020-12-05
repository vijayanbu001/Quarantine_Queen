package com.boardGame.quarantine_queen.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.boardGame.quarantine_queen.database.entity.GridDetail
import com.boardGame.quarantine_queen.database.entity.GridSolutionDetail
import com.boardGame.quarantine_queen.database.entity.ProgressDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllGridDetails(gridDetails: List<GridDetail>)

    @Query("SELECT * FROM GridDetail")
    fun getAllGridDetails(): LiveData<List<GridDetail>>

    @Query("SELECT * FROM GridDetail where qCount = :qCount")
    suspend fun getSelectedGridDetail(qCount: Int): List<GridDetail>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllGridSolutionDetails(gridSolutionDetail: List<GridSolutionDetail>)

    @Query("SELECT * FROM GridSolutionDetail order by size asc, status desc")
    fun getAllGridSolutionDetails(): Flow<List<GridSolutionDetail>>

    @Query("SELECT * FROM GridSolutionDetail where size= :size order by status_Order")
    fun getGridSolutionDetailsBySize(size: Int): LiveData<List<GridSolutionDetail>>

    // need to add update query for status
    @Update
    suspend fun updateStatus(gridSolutionDetail: GridSolutionDetail)

//    @Update
//    suspend fun updateProgressDetails(progressDetails: List<ProgressDetail>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProgressDetails(progressDetails: List<ProgressDetail>)

    @Query("SELECT * FROM ProgressDetail where size= :size")
    fun getProgressDetailsBySize(size: Int): LiveData<List<ProgressDetail>>

    @Update
    suspend fun updateProgressDetail(progressDetail: ProgressDetail)
}