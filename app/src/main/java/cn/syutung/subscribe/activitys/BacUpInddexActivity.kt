package cn.syutung.subscribe.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.syutung.subscribe.R
import kotlinx.android.synthetic.main.activity_bac_up_inddex.*

class BacUpInddexActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bac_up_inddex)
        backupbyjianguoyun.setOnClickListener {
            val m = Intent(
                this,BackUpbyJianGuoYunActivity::class.java
            )
            m.putExtra("mode",1)
            startActivity(
                m
            )
        }
    }
}