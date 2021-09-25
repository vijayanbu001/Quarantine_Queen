package com.boardGame.quarantine_queen.repository

import androidx.lifecycle.LiveData
import com.boardGame.quarantine_queen.database.dao.Dao
import com.boardGame.quarantine_queen.database.entity.GridDetail
import com.boardGame.quarantine_queen.database.entity.GridSolutionDetail
import com.boardGame.quarantine_queen.database.entity.ProgressDetail
import java.util.*

class QueenRepository(private val dao: Dao) {
    var getAllGridDetails = dao.getAllGridDetails()
    var getGridSolutionDetails = dao.getAllGridSolutionDetails()

    fun getGridSolutionDetailBySize(gridSize: Int): LiveData<List<GridSolutionDetail>> {
        return dao.getGridSolutionDetailsBySize(gridSize)
    }

    fun insertGridSolutionDetails(gridSolutionDetails: ArrayList<GridSolutionDetail>) {
        dao.insertAllGridSolutionDetails(gridSolutionDetails)
    }

    suspend fun updateStatus(gridSolutionDetail: GridSolutionDetail){
        dao.updateStatus(gridSolutionDetail)
    }

    fun insertGridDetails(gridDetails: ArrayList<GridDetail>) {
        dao.insertAllGridDetails(gridDetails)
    }

    fun insertProgressDetails(progressDetails: ArrayList<ProgressDetail>) {
        dao.insertProgressDetails(progressDetails)

    }

    fun getProgressDetailsBySize(gridSize: Int): LiveData<List<ProgressDetail>> {
        return dao.getProgressDetailsBySize(gridSize)
    }

    suspend fun updateProgressDetail(progressDetail: ProgressDetail) {
        dao.updateProgressDetail(progressDetail)
    }
}
