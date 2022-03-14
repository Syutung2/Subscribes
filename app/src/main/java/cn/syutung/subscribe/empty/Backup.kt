package cn.syutung.subscribe.empty

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "backup")
class Backup (
    var backupname: String,
    var date: String,

    ){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

