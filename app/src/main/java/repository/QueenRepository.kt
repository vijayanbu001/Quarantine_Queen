package repository

import androidx.lifecycle.LiveData
import database.Dao
import database.GridDetail

class QueenRepository(val dao: Dao){
        var getAllGridDetails: LiveData<List<GridDetail>> = dao.getAllGridDetails()

        suspend fun insertGridDetails(gridDetails: ArrayList<GridDetail>) {
                dao.insertAllGridDetails(gridDetails)
        }
}
