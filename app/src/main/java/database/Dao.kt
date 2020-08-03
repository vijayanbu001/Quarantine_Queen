package database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllGridDetails(gridDetails:List<GridDetail>)

    @Query("SELECT * FROM GridDetail")
    fun getAllGridDetails() : LiveData<List<GridDetail>>

    @Query("SELECT * FROM GridDetail where qCount = :qCount")
    suspend fun getSelectedGridDetail(qCount:Int):List<GridDetail>
}