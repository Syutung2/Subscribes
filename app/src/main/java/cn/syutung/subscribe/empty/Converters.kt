package cn.syutung.subscribe.empty

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? {
        val calendar = Calendar.getInstance()
        value?.let {
            calendar.timeInMillis = it * 1000
        }
        return calendar
    }

    @TypeConverter
    fun dateToTimestamp(calendar: Calendar?): Long? {
        return calendar?.timeInMillis?.div(1000)
    }


}