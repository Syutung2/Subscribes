package cn.syutung.subscribe.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.syutung.subscribe.R
import cn.syutung.subscribe.utils.Utils

class DrawCakeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_cake)
        Utils.setOrdinaryToolBar(this)

    }
}