package cn.syutung.subscribe.utils

import android.app.Activity
import android.view.WindowManager
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
        fun setOrdinaryToolBar( activity : Activity){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }

    }

}