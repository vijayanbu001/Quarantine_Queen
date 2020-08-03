package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(GridDetail::class),version = 1)
abstract class QueenDatabase:RoomDatabase() {
    abstract fun dao():Dao

    companion object {
        // Singleton prevents multiple instances of database opening at the
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
    AppDatabase::class.java, "database-name"
).build()*/
