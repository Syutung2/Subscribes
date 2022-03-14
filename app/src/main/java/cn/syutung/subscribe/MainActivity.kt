package cn.syutung.subscribe

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.syutung.subscribe.fragment.HomeFragment
import cn.syutung.subscribe.fragment.MineFragment
import cn.syutung.subscribe.utils.Nums
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

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