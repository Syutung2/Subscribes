package cn.syutung.subscribe.empty

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity(tableName = "tags")
class Tag(
    var tid: Int,
    var tag: String,

){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
