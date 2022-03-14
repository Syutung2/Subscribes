package cn.syutung.subscribe.datebase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cn.syutung.subscribe.dao.SubsrcbleDao
import cn.syutung.subscribe.dao.TagDao
import cn.syutung.subscribe.empty.Converters
import cn.syutung.subscribe.empty.Subscribe
import cn.syutung.subscribe.empty.Tag

@Database(entities = [Tag::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TagDatabase() :RoomDatabase() {
    public  abstract val tagDao: TagDao

    companion object {
        private const val DB_NAME = "tags.db"

        @Volatile
        private  var instance: TagDatabase? = null
        @Synchronized
        fun getInstance(context: Context): TagDatabase? {
            if (instance == null) {
                instance = create(context)
            }
            return instance
        }

        private fun create(context: Context): TagDatabase {
            return Room.databaseBuilder(
                context,
                TagDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}

