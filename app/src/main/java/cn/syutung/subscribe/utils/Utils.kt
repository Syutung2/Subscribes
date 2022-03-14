package cn.syutung.subscribe.utils

import androidx.room.TypeConverter
import java.util.*

class Utils {
    companion object{
        fun fromTimestamp(value: Long?): Calendar? {
            val calendar = Calendar.getInstance()
            value?.let {
                calendar.timeInMillis = it * 1000
            }
            return calendar
        }

        fun dateToTimestamp(calendar: Calendar): Long {
            return calendar.timeInMillis.div(1000)
        }

    }
}