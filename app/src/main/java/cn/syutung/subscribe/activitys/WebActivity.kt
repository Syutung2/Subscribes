package cn.syutung.subscribe.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.syutung.subscribe.R
import cn.syutung.subscribe.utils.Nums
import cn.syutung.subscribe.utils.Utils
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {
    val URL1 = "https://we-chat.cn/dy/ysxy.html"
    val URL2 = "https://we-chat.cn/dy/user.html"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val mode = intent.getIntExtra("mode",1)
        val sharedPreferences = getSharedPreferences(Nums.PACKAGENAME,0)
        if (mode == 1){
            val isFirstUser = sharedPreferences.getBoolean("isFirstUser",false)
            web.loadUrl(URL2)
            if (isFirstUser){
                webview_nav.visibility = View.GONE
            }else{
                tongyi.setOnClickListener {
                    sharedPreferences.edit().putBoolean("isFirstUser",true).apply()

                    finish()
                }
                jujue.setOnClickListener {
                    finish()
                }
            }
        }else{
            val isFirstYinsi = sharedPreferences.getBoolean("isFirstYinsi",false)
            web.loadUrl(URL1)
            if (isFirstYinsi){
                webview_nav.visibility = View.GONE
            }else{
                tongyi.setOnClickListener {
                    sharedPreferences.edit().putBoolean("isFirstYinsi",true).apply()

                    finish()
                }
                jujue.setOnClickListener {
                    finish()
                }
            }
        }
    }
}