package com.boardGame.quarantine_queen.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boardGame.quarantine_queen.database.dao.Dao
import com.boardGame.quarantine_queen.database.entity.GridDetail
import com.boardGame.quarantine_queen.database.entity.GridSolutionDetail
import com.boardGame.quarantine_queen.database.entity.ProgressDetail
import com.boardGame.quarantine_queen.database.typeconverters.DateTypeConverter
import com.boardGame.quarantine_queen.database.typeconverters.ListTypeConverter

@Database(entities = [GridDetail::class, GridSolutionDetail::class,ProgressDetail::class], version = 2)
@TypeConverters(value = [DateTypeConverter::class, ListTypeConverter::class])
abstract class QueenDatabase : RoomDatabase() {
    abstract fun dao(): Dao

    companion object {
        // Singleton prevents multiple instances of com.boardGame.quarantine_queen.database opening at the
        // same time.
        @Volatile
        private var INSTANCE: QueenDatabase? = null

        fun getDatabase(context: Context): QueenDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QueenDatabase::class.java,
                    "queen_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
/*

val db = Room.databaseBuilder(
    applicationContext,
    AppDatabase::class.java, "com.boardGame.quarantine_queen.database-name"
).build()*/
