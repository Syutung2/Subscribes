package cn.syutung.subscribe.dao

import androidx.room.*
import cn.syutung.subscribe.empty.Subscribe
import cn.syutung.subscribe.empty.Tag
@Dao
interface TagDao {
    @Insert
    fun insert(vararg todo: Tag)

    @Delete
    fun delete(tag : Tag)

    @Update
    fun update(vararg tag: Tag)


    @Query("SELECT * FROM tags where tid = :tid")
    fun getbyId(
        tid:Long
    ): List<Tag>
}