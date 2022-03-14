package cn.syutung.subscribe.datebase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cn.syutung.subscribe.dao.SubsrcbleDao
import cn.syutung.subscribe.empty.Converters
import cn.syutung.subscribe.empty.Subscribe


@Database(entities = [Subscribe::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract  class SubscribeDatebase :RoomDatabase() {
    public  abstract val subsrcbleDao: SubsrcbleDao

    companion object {
        private const val DB_NAME = "sub.db"

        @Volatile
        private  var instance: SubscribeDatebase? = null
        @Synchronized
        fun getInstance(context: Context): SubscribeDatebase? {
            if (instance == null) {
                instance = create(context)
            }
            return instance
        }

        private fun create(context: Context): SubscribeDatebase {
            return Room.databaseBuilder(
                context,
                SubscribeDatebase::class.java,
                DB_NAME
            ).build()
        }
    }
}