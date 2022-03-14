package cn.syutung.subscribe.activitys

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import cn.syutung.subscribe.R
import cn.syutung.subscribe.datebase.SubscribeDatebase
import cn.syutung.subscribe.empty.Subscribe
import cn.syutung.subscribe.utils.ModeList
import cn.syutung.subscribe.utils.Nums
import cn.syutung.subscribe.utils.Utils
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.activity_add_subscribe.*
import kotlinx.android.synthetic.main.activity_add_subscribe.view.*
import java.util.*
import java.util.zip.Inflater
import kotlin.concurrent.thread

class AddSubscribeActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {
    var cycleType = 0
    var modes= 0
    var switch = false
    private lateinit var mode : ModeList //编辑模式还是添加模式
    private lateinit var myinflater : LayoutInflater
    private lateinit var editEmpty : Subscribe
    private val TAG = "ADDPAGE"
    //                 var description :String,
    //                var mode : Int ,
    //                // 0 or 1 0 instand of cycle Subscribe 1 instand of once
    //                var cycleType:Int,
    //                // 0 - 4 , 1 day ,2 month , 3 year , 0 no
    //                var cycleTime:Int = 0,
    //                var firstSubscribe: Calendar,
    //                var payMode : String
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subscribe)
        myinflater = LayoutInflater.from(this)
        Utils.setOrdinaryToolBar(this)

        val id = intent.getLongExtra("id",-1L)
        if (id == -1L){
            mode = ModeList.ADD
        }else{
            mode = ModeList.EDIT
            thread {
                editEmpty = SubscribeDatebase.getInstance(this)?.subsrcbleDao?.getbyId(id)!!
                runOnUiThread {
                    if (editEmpty.cycleType == 0){
                        subscribe_isCycle.isChecked = false
                        cycleType = 0
                        switch = false
                    }else{
                        cycleType = editEmpty.cycleType
                        subscribe_isCycle.isChecked = true
                        switch = true
                        subscribe_cycle.visibility = View.VISIBLE
                        cycle_picker.setSelection(editEmpty.cycleType-1)
                        subscribe_date.setText(editEmpty.cycleTime.toString())
                    }
                    subscribe_name.setText(editEmpty.name)
                    subscribe_money.setText(editEmpty.money.toString())
                    subscribe_decp.setText(editEmpty.description)
                    subscribe_choosedate.text =  "${editEmpty.firstSubscribe.get(Calendar.YEAR)}年" + editEmpty.firstSubscribe.get(Calendar.MONTH) + "月" + editEmpty.firstSubscribe.get(Calendar.DAY_OF_MONTH) + "日"
                    subscribe_mode.setText(editEmpty.payMode)
                    modes = editEmpty.mode
                }
            }

        }

        // 是否周期订阅
        subscribe_isCycle.setOnClickListener {
            if (!switch){
                switch = true
                subscribe_cycle.visibility = View.VISIBLE
                cycleType = 1
                modes = 1
            }else{
                switch = false
                subscribe_cycle.visibility = View.GONE
                cycleType = 0
                modes = 0
            }
        }
        // 日期选择
        subscribe_choosedate.setOnClickListener {
            datepicker.visibility = View.VISIBLE
        }
        var my = Calendar.getInstance()
        datepicker.init(
            Nums.calendar.get(Calendar.YEAR),
            Nums.calendar.get(Calendar.MONTH),
            Nums.calendar.get(
            Calendar.DAY_OF_MONTH),
            DatePicker.OnDateChangedListener { datePicker, i, i2, i3 ->
                run {
                    subscribe_choosedate.text =  "${i}年" + i2 + "月" + i3 + "日"
                    my.set(i,i2,i3)
                    datepicker.visibility = View.GONE

                }
            })

        cycle_picker.onItemSelectedListener = this

        subscribe_save.setOnClickListener {
            val m = dataCheck()
            Log.d(TAG,m.toString())
            if (m){
                if (mode == ModeList.ADD){
                    var oo = 0
                    if (subscribe_date.text.toString() != ""){
                        oo =  subscribe_date.text.toString().toInt()
                    }

                    val subscribe = Subscribe(
                        name = subscribe_name.text.toString(),
                        money = subscribe_money.text.toString().toDouble(),
                        description = subscribe_decp.text.toString(),
                        cycleType = cycleType,
                        mode = modes,
                        firstSubscribe = my,
                        payMode = subscribe_mode.text.toString(),
                        cycleTime = oo,
                        id = Utils.dateToTimestamp(Calendar.getInstance())
                    )
                    thread {
                        SubscribeDatebase.getInstance(this)?.subsrcbleDao?.insert(subscribe)
                        runOnUiThread { finish() }
                    }

                }else{
                    var oo = 0
                    if (subscribe_date.text.toString() != ""){
                        oo =  subscribe_date.text.toString().toInt()
                    }
                    editEmpty.name = subscribe_name.text.toString()
                    editEmpty.money = subscribe_money.text.toString().toDouble()
                    editEmpty.description = subscribe_decp.text.toString()
                    editEmpty.cycleType = cycleType
                    editEmpty.mode = modes
                    editEmpty.firstSubscribe = my
                    editEmpty.payMode = subscribe_mode.text.toString()
                    editEmpty.cycleTime = oo
                    thread {
                        SubscribeDatebase.getInstance(this)?.subsrcbleDao?.update(editEmpty)
                        runOnUiThread { finish() }

                    }
                }
            }else{
                Toast.makeText(this,"你好像还有一些东西没有输入哦",Toast.LENGTH_SHORT).show()
            }

        }



    }

    /***
     *   校验输入数据
     *   @return 数据校验结果
     */
    private fun dataCheck():Boolean{
        if (subscribe_money.text.toString() == ""){
            return false
        }
        if (subscribe_name.text.toString() == ""){
            return false
        }
        if (switch){
            if (subscribe_date.text.toString() == ""){
                return false
            }
        }

        if (subscribe_mode.text.toString() == ""){
            return false
        }
        if (subscribe_choosedate.text == "选择日期"){
            return false
        }
        return true
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        println(p1.toString())
        cycleType = p2+1
        println(cycleType)


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}