package com.boardGame.quarantine_queen.database.typeconverters

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {
    @TypeConverter
    fun dateToTimeStamp(date: Date): Long {
        return date?.time;
    }

    @TypeConverter
    fun timeStampToDate(timestamp: Long):Date {
        return timestamp?.let { Date(it) }
    }
}