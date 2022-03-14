package cn.syutung.subscribe.empty

import android.accounts.AuthenticatorDescription
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "subscribes")
class Subscribe(
    var money: Double,
    var name: String,
    var description :String,
    var mode : Int ,
    // 0 or 1 0 instand of cycle Subscribe 1 instand of once
    var cycleType:Int,
    // 0 - 4 , 1 day ,2 month , 3 year , 0 no
    var cycleTime:Int,
    var firstSubscribe: Calendar,
    var payMode : String
    //Alipay or Wechat even Card
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}
