package cn.syutung.subscribe.dao

import androidx.room.*
import cn.syutung.subscribe.empty.Backup
import cn.syutung.subscribe.empty.Tag
@Dao
interface BackupDao {
    @Insert
    fun insert(vararg backup: Backup)

    @Delete
    fun delete(backup: Backup)

    @Update
    fun update(vararg backup: Backup)

    @Query("SELECT * FROM backup")
    fun getAllBackups(): List<Backup>

    @Query("SELECT * FROM backup where date = :date")
    fun getbyDate(
        date:String
    ): List<Backup>


}