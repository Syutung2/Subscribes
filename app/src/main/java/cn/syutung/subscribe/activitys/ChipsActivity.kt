package cn.syutung.subscribe.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import cn.syutung.subscribe.R
import cn.syutung.subscribe.datebase.SubscribeDatebase
import cn.syutung.subscribe.datebase.TagDatabase
import cn.syutung.subscribe.empty.Tag
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_add_subscribe.*
import kotlinx.android.synthetic.main.activity_chips.*
import java.util.*
import kotlin.concurrent.thread

class ChipsActivity : AppCompatActivity() {
    var id = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chips)
        id = intent.getIntExtra("id",-1)
        val inflater = LayoutInflater.from(this)
        if (id!=-1){
            load(inflater)
        }
        label_button.setOnClickListener {
            val mmm = label_text.text.toString()
            if (mmm!=""){
                thread {
                    TagDatabase.getInstance(this)?.tagDao?.insert(Tag(
                        id,mmm
                    ))!!
                    runOnUiThread {
                        load(inflater)
                    }
                }
            }
        }
    }
    fun load(inflater:LayoutInflater){
        thread {
            var editEmpty = TagDatabase.getInstance(this)?.tagDao?.getbyId(id)!!
            runOnUiThread {
                labellistss.removeAllViews()
                for (i in editEmpty){
                    var view2 = inflater.inflate(
                        R.layout.chip,
                        labellistss, false)
                    view2.findViewById<Chip>(R.id.subscribe_label).text = i.tag
                    view2.findViewById<Chip>(R.id.subscribe_label).setOnCloseIconClickListener  {
                        thread {
                            TagDatabase.getInstance(this)?.tagDao?.delete(i)!!
                        }
                        labellistss.removeView(view2)

                    }
                    labellistss.addView(view2)
                }
            }
        }
    }
}