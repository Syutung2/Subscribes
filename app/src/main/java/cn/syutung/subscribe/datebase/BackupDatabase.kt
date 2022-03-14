package cn.syutung.subscribe.datebase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cn.syutung.subscribe.dao.BackupDao
import cn.syutung.subscribe.dao.SubsrcbleDao
import cn.syutung.subscribe.dao.TagDao
import cn.syutung.subscribe.empty.Backup
import cn.syutung.subscribe.empty.Converters
import cn.syutung.subscribe.empty.Subscribe
import cn.syutung.subscribe.empty.Tag

@Database(entities = [Backup::class], version = 1, exportSchema = false)
abstract class BackupDatabase() :RoomDatabase() {
    public  abstract val backupDao:BackupDao

    companion object {
        private const val DB_NAME = "backups.db"

        @Volatile
        private  var instance: BackupDatabase? = null
        @Synchronized
        fun getInstance(context: Context): BackupDatabase? {
            if (instance == null) {
                instance = create(context)
            }
            return instance
        }

        private fun create(context: Context): BackupDatabase {
            return Room.databaseBuilder(
                context,
                BackupDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}

