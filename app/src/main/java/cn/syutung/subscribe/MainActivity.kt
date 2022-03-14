package cn.syutung.subscribe

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.syutung.subscribe.activitys.WebActivity
import cn.syutung.subscribe.fragment.HomeFragment
import cn.syutung.subscribe.fragment.MineFragment
import cn.syutung.subscribe.utils.Nums
import cn.syutung.subscribe.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences:SharedPreferences
    var isFirstYinsi = false
    var isFirstUser = false
    private fun showAlertDialog(){
        var builder= MaterialAlertDialogBuilder(this)

        builder.setTitle("提示")
        builder.setMessage("您还没有同意隐私协议和用户协议哦！")
        if (isFirstUser){
            builder.setPositiveButton("您已同意用户协议"){dialog, which ->

            }
        }else{
            builder.setPositiveButton("阅读用户协议"){dialog, which ->
                val intent = Intent(this,WebActivity::class.java)
                intent.putExtra("mode",1)
                startActivity(intent)
            }
        }

        if (isFirstYinsi){
            builder.setNegativeButton("您已同意隐私协议"){dialog, which ->

            }
        }else{
            builder.setNegativeButton("阅读隐私协议"){dialog, which ->
                val intent = Intent(this,WebActivity::class.java)
                intent.putExtra("mode",2)
                startActivity(intent)
            }
        }

        var dialog: AlertDialog =builder.create()
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    override fun onResume() {

        super.onResume()
        sharedPreferences = getSharedPreferences(Nums.PACKAGENAME,0)

        isFirstYinsi = sharedPreferences.getBoolean("isFirstYinsi",false)
        isFirstUser = sharedPreferences.getBoolean("isFirstUser",false)

        if (!isFirstYinsi || !isFirstUser){
            showAlertDialog()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        val permissions = arrayOf<String>(
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions(permissions,1)
        Utils.setOrdinaryToolBar(this)

        Nums.calendar = Calendar.getInstance()
        replaceFragment(HomeFragment())
        bottom_navi.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.mine -> {
                    replaceFragment(MineFragment())

                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment,fragment)
        transaction.commit()
    }
}