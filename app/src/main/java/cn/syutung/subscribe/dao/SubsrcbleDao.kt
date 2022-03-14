package cn.syutung.subscribe.dao

import androidx.room.*
import cn.syutung.subscribe.empty.Subscribe
import java.sql.Date
import java.util.*

/***
 *  @author 汝阳县斯普锐思软件技术工作室
 *  @since 2022
 */
@Dao
interface SubsrcbleDao {
    @Insert
    fun insert(vararg todo: Subscribe)

    @Delete
    fun delete(subscribe : Subscribe)

    @Query("SELECT * FROM subscribes order by money asc")
    fun getAllbyMoneyASC(): List<Subscribe>
    @Query("SELECT * FROM subscribes order by money desc")
    fun getAllbyMoneyDESC(): List<Subscribe>

    @Query("SELECT * FROM subscribes order by firstSubscribe asc")
    fun getAllbyFirstSubscribeASC(): List<Subscribe>
    @Query("SELECT * FROM subscribes order by firstSubscribe desc")
    fun getAllbyFirstSubscribeDESC(): List<Subscribe>

    @Query("SELECT * FROM subscribes order by name asc")
    fun getAllbyNameASC(): List<Subscribe>
    @Query("SELECT * FROM subscribes order by name desc")
    fun getAllbyNameDESC(): List<Subscribe>

    @Update
    fun update(vararg subscribe: Subscribe)

    @Query("SELECT SUM(money) FROM subscribes")
    fun getAllMoney():Double

    @Query("SELECT * FROM subscribes where id = :id")
    fun getbyId(
        id:Int
    ): Subscribe

}