package cn.syutung.subscribe.activitys

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.syutung.subscribe.R
import cn.syutung.subscribe.datebase.BackupDatabase
import cn.syutung.subscribe.datebase.SubscribeDatebase
import cn.syutung.subscribe.datebase.TagDatabase
import cn.syutung.subscribe.empty.Backup
import cn.syutung.subscribe.empty.Subscribe
import cn.syutung.subscribe.empty.Tag
import cn.syutung.subscribe.utils.Nums
import cn.syutung.subscribe.utils.Utils
import com.thegrizzlylabs.sardineandroid.Sardine
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import kotlinx.android.synthetic.main.activity_add_subscribe.*
import kotlinx.android.synthetic.main.activity_back_upby_jian_guo_yun.*
import java.io.File
import java.util.*
import kotlin.concurrent.thread


class BackUpbyJianGuoYunActivity : AppCompatActivity() {
    var mode = 0;
    var SERVICE_URL = ""

    var USERNAME = ""
    var PASSWORD = ""
    var PACKAGE_NAME = "MySubscribe/"
    lateinit var sardine : Sardine
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_upby_jian_guo_yun)
        mode = intent.getIntExtra("mode",-1)
        val sharedPreferences = getSharedPreferences(Nums.PACKAGENAME,0)
        Utils.setOrdinaryToolBar(this)

        SERVICE_URL = sharedPreferences.getString("urls","").toString()
        USERNAME = sharedPreferences.getString("username","").toString()
        PASSWORD = sharedPreferences.getString("password","").toString()
        if (SERVICE_URL == ""){
            no_set_webdav.visibility = View.VISIBLE
            set_webdav.visibility = View.GONE
        }else{
            set_webdav.visibility = View.VISIBLE
            no_set_webdav.visibility = View.GONE
            sardine= OkHttpSardine()
            sardine.setCredentials(USERNAME, PASSWORD)
            val r = sharedPreferences.getString("lastBackup","无")
            backup_last.text = "上次备份：${r}"
        }
        val inflater = LayoutInflater.from(this)
        changeInformation.setOnClickListener {
            no_set_webdav.visibility = View.VISIBLE
            set_webdav.visibility = View.GONE
            webdav_urls.setText(SERVICE_URL)
            webdav_username.setText(USERNAME)
            webdav_password.setText(PASSWORD)

        }

            webdav_save.setOnClickListener {
                val isTrue = dataCheck()
                if (isTrue){
                    sharedPreferences.edit()
                        .putString("urls",webdav_urls.text.toString())
                        .putString("username",webdav_username.text.toString())
                        .putString("password",webdav_password.text.toString()).apply()
                    no_set_webdav.visibility = View.GONE
                    set_webdav.visibility = View.VISIBLE
                    sardine= OkHttpSardine()
                    sardine.setCredentials(webdav_username.text.toString(), webdav_password.text.toString())
                    SERVICE_URL = webdav_urls.text.toString()

                }else{
                    Toast.makeText(this,"你好像还有一些东西没有输入哦", Toast.LENGTH_SHORT).show()
                }
            }
            huanyuan.setOnClickListener {
                load(inflater)
            }
            backup_button.setOnClickListener {
                Backup()
                val r = sharedPreferences.getString("lastBackup","")
                backup_last.text = "上次备份：${r}"
            }

    }
    @SuppressLint("SetTextI18n")
    fun load(inflater:LayoutInflater){
        thread {
            var editEmpty = BackupDatabase.getInstance(this)?.backupDao?.getAllBackups()!!
            runOnUiThread {
                backups.removeAllViews()
                for (i in editEmpty){
                    val view2 = inflater.inflate(
                        R.layout.backupsitem,
                        backups, false)
                    view2.findViewById<TextView>(R.id.backup_name).text = "备份日期${i.backupname}"
                    view2.findViewById<TextView>(R.id.backup_date).text = "备份名称${i.date}"

                    view2.findViewById<ImageButton>(R.id.backup_huanyuan).setOnClickListener  {
                        recoverCard
                            .visibility = View.VISIBLE
                        recoverFromWebDev(i)
                    }
                    view2.findViewById<ImageButton>(R.id.backup_delete).setOnClickListener  {
                        deleteFile(i,view2)
                        if (editEmpty.indexOf(i)==editEmpty.size-1){
                            val sharedPreferences = getSharedPreferences(Nums.PACKAGENAME,0)
                            if (editEmpty.indexOf(i)-1==-1){
                                sharedPreferences.edit().putString("lastBackup","").apply()
                                backup_last.text = "上次备份：无"
                            }


                        }
                    }
                    backups.addView(view2)
                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    fun recoverFromWebDev(backup: Backup){
        val inflater = LayoutInflater.from(this)
        recovers.removeAllViews()
        thread {
            val textnn = sardine.get(SERVICE_URL+PACKAGE_NAME+backup.date)
            val m = String(textnn.readBytes()).split("\n")
            for (i in m){
                if(i!=""){
                    val q = i.split("|||")
                    println(q.toString())
                    val subscribe = Subscribe(
                        name = q[1],
                        money = q[3].toDouble(),
                        description = q[6],
                        cycleType = q[5].toInt(),
                        mode = q[7].toInt(),
                        firstSubscribe = fromTimestamp(q[2].toLong())!!,
                        payMode = q[8].toString(),
                        cycleTime = q[4].toInt(),
                        id = q[0].toLong(),
                    )

                    if (
                        SubscribeDatebase.getInstance(this)?.subsrcbleDao?.getbyId(
                            id = q[0].toLong(),
                        )==null
                    ){
                        SubscribeDatebase.getInstance(this)?.subsrcbleDao?.insert(
                            subscribe
                        )
                        runOnUiThread {
                            val view2 = inflater.inflate(
                                R.layout.recoveritem,
                                backups, false)
                            view2.findViewById<TextView>(R.id.recover_date).text = "${subscribe.name} 订阅未存在，已经恢复成功了"
                            recovers.addView(view2)

                        }
                    }else{
                        runOnUiThread {
                            val view2 = inflater.inflate(
                                R.layout.recoveritem,
                                backups, false)
                            view2.findViewById<TextView>(R.id.recover_date).text = "${subscribe.name} 订阅已经存在，暂不替换"
                            recovers.addView(view2)
                        }
                    }
                }
            }
        }
        recoverTagFromWebDev(backup.backupname)

    }

    @SuppressLint("SetTextI18n")
    private fun recoverTagFromWebDev(backupname: String) {
        val inflater = LayoutInflater.from(this)
        thread {
            val textnn = sardine.get(SERVICE_URL+PACKAGE_NAME+backupname+"-TAG.txt")
            val m = String(textnn.readBytes()).split("\n")
            for (i in m){
                if(i!=""){
                    val q = i.split("|||")
                    println(q.toString())
                    val tag = Tag(
                      tid = q[2].toLong(),tag = q[1]
                    )

                    if (
                        TagDatabase.getInstance(this)?.tagDao?.getbyIdAndName(
                            tid = q[2].toLong(),tag = q[1]
                        )==null
                    ){
                        TagDatabase.getInstance(this)?.tagDao?.insert(
                            tag
                        )
                        runOnUiThread {
                            val view2 = inflater.inflate(
                                R.layout.recoveritem,
                                backups, false)
                            view2.findViewById<TextView>(R.id.recover_date).text = "${tag.tag} + ${tag.tid} 订阅标签未存在，已经恢复成功了"
                            recovers.addView(view2)

                        }
                    }else{
                        runOnUiThread {
                            val view2 = inflater.inflate(
                                R.layout.recoveritem,
                                backups, false)
                            view2.findViewById<TextView>(R.id.recover_date).text = "${tag.tag} + ${tag.tid} 订阅标签已经存在，暂不替换"
                            recovers.addView(view2)
                        }
                    }
                }
            }
        }
    }

    fun deleteFile(backup: Backup,view: View){
        val dirConf = this.getDir("backups",Context.MODE_PRIVATE)
        val conf = File(dirConf, backup.date)
        conf.delete()
        thread {
            sardine.delete(SERVICE_URL+PACKAGE_NAME+backup.date);
            sardine.delete(SERVICE_URL+PACKAGE_NAME+backup.backupname+"-TAG.txt");

            BackupDatabase.getInstance(this)?.backupDao?.delete(
                backup
            )
            runOnUiThread {
                backups.removeView(view)
                Toast.makeText(this,"删除成功", Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun dataCheck():Boolean{
        if (webdav_urls.text.toString() == "" || webdav_urls.text.toString()[webdav_urls.text.toString().length-1]!='/'){
            return false
        }
        if (webdav_username.text.toString() == ""){
            return false
        }
        if (webdav_password.text.toString() == ""){
            return false
        }

        return true
    }
    fun fromTimestamp(value: Long?): Calendar? {
        val calendar = Calendar.getInstance()
        value?.let {
            calendar.timeInMillis = it * 1000
        }
        return calendar
    }
    fun dateToTimestamp(calendar: Calendar?): Long? {
        return calendar?.timeInMillis?.div(1000)
    }
    fun FileIsExist() : Boolean {
        return sardine.exists(SERVICE_URL+PACKAGE_NAME)
    }
    @SuppressLint("SetTextI18n")
    fun Backup(){
        recoverCard.visibility = View.VISIBLE
        recovers.removeAllViews()
        val inflater = LayoutInflater.from(this)

        val dirConf = this.getDir("backups",Context.MODE_PRIVATE)
        val calendar = Calendar.getInstance()
        val time = "${calendar.get(Calendar.YEAR)}" +
                "-${calendar.get(Calendar.MONTH)}" +
                "-${calendar.get(Calendar.DAY_OF_MONTH)}" +
                "-${calendar.get(Calendar.HOUR_OF_DAY)}" +
                "-${calendar.get(Calendar.MINUTE)}" +
                "-${calendar.get(Calendar.SECOND)}"

        val conf = File(dirConf, "${time}.txt")
        var text = ""
        thread {
            val mm = SubscribeDatebase.getInstance(this)?.subsrcbleDao?.getAllbyMoneyASC()!!
            for (i in mm){
                text += "${i.id}|||${i.name}|||${dateToTimestamp(i.firstSubscribe)}|||${i.money}|||${i.cycleTime}|||${i.cycleType}|||${i.description}|||${i.mode}|||${i.payMode}\n"
                runOnUiThread {
                    val view2 = inflater.inflate(
                        R.layout.recoveritem,
                        backups, false)
                    view2.findViewById<TextView>(R.id.recover_date).text = "${i.name} 订阅记录 备份 成功"
                    recovers.addView(view2)
                }
            }
            runOnUiThread {
                conf.writeText(text)
            }
            if (FileIsExist()){
                sardine.put(SERVICE_URL+PACKAGE_NAME+"${time}.txt", conf.readBytes())
                BackupDatabase.getInstance(this)?.backupDao?.insert(
                    cn.syutung.subscribe.empty.Backup(time,"${time}.txt")
                )
                runOnUiThread {
                    Toast.makeText(this,"备份成功", Toast.LENGTH_SHORT).show()
                    val sharedPreferences = getSharedPreferences(Nums.PACKAGENAME,0)
                    sharedPreferences.edit().putString("lastBackup",time).apply()
                    val view2 = inflater.inflate(
                        R.layout.recoveritem,
                        backups, false)
                    view2.findViewById<TextView>(R.id.recover_date).text = "订阅记录 备份 成功"
                    recovers.addView(view2)
                }

            }else{
                sardine.createDirectory(SERVICE_URL+PACKAGE_NAME);
                sardine.put(SERVICE_URL+PACKAGE_NAME+"${time}.txt", conf.readBytes())
                BackupDatabase.getInstance(this)?.backupDao?.insert(
                    cn.syutung.subscribe.empty.Backup(time,"${time}.txt")
                )
                runOnUiThread {
                    Toast.makeText(this,"备份成功", Toast.LENGTH_SHORT).show()
                    val sharedPreferences = getSharedPreferences(Nums.PACKAGENAME,0)
                    sharedPreferences.edit().putString("lastBackup",time).apply()
                    val view2 = inflater.inflate(
                        R.layout.recoveritem,
                        backups, false)
                    view2.findViewById<TextView>(R.id.recover_date).text = "订阅记录 备份 成功"
                    recovers.addView(view2)
                }

            }
        }
        BackupTags(time)


    }

    @SuppressLint("SetTextI18n")
    fun BackupTags(time:String){
        val inflater = LayoutInflater.from(this)

        val dirConf = this.getDir("backups",Context.MODE_PRIVATE)
        val conf = File(dirConf, "${time}-TAG.txt")
        var text = ""
        thread {
            val mm = TagDatabase.getInstance(this)?.tagDao?.getAll()!!
            for (i in mm){
                text += "${i.id}|||${i.tag}|||${i.tid}\n"
                runOnUiThread {
                    val view2 = inflater.inflate(
                        R.layout.recoveritem,
                        backups, false)
                    view2.findViewById<TextView>(R.id.recover_date).text = "${i.tag} - ${i.tid} 标签记录 备份 成功"
                    recovers.addView(view2)
                }
            }
            runOnUiThread {
                conf.writeText(text)
            }
            if (FileIsExist()){
                sardine.put(SERVICE_URL+PACKAGE_NAME+"${time}-TAG.txt", conf.readBytes())
                runOnUiThread {
                    Toast.makeText(this,"备份成功", Toast.LENGTH_SHORT).show()
                    val view2 = inflater.inflate(
                        R.layout.recoveritem,
                        backups, false)
                    view2.findViewById<TextView>(R.id.recover_date).text = "标签记录 备份 成功"
                    recovers.addView(view2)
                }

            }else{
                sardine.createDirectory(SERVICE_URL+PACKAGE_NAME);
                sardine.put(SERVICE_URL+PACKAGE_NAME+"${time}-TAG.txt", conf.readBytes())
                runOnUiThread {
                    Toast.makeText(this,"备份成功", Toast.LENGTH_SHORT).show()
                    val view2 = inflater.inflate(
                        R.layout.recoveritem,
                        backups, false)
                    view2.findViewById<TextView>(R.id.recover_date).text = "标签记录 备份 成功"
                    recovers.addView(view2)
                }

            }
        }


    }
}