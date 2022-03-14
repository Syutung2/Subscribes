package cn.syutung.subscribe.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import cn.syutung.subscribe.R
import cn.syutung.subscribe.activitys.AddSubscribeActivity
import cn.syutung.subscribe.activitys.ChipsActivity
import cn.syutung.subscribe.datebase.SubscribeDatebase
import cn.syutung.subscribe.datebase.TagDatabase
import cn.syutung.subscribe.empty.Subscribe
import cn.syutung.subscribe.empty.Tag
import cn.syutung.subscribe.utils.Nums
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.util.*
import kotlin.concurrent.thread


class HomeFragment : Fragment() {
    private lateinit var myinflater : LayoutInflater
    private val arr = arrayOf("周付费", "月付费",  "年付费","天付费")
    private var currentIndex = 0;
    private var money_sum : Double = 0.0
    private lateinit var views : View
    private val day_month = arrayOf(
       0,31,28,31,30,31,30,31,31,30,31,30,31)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun isRun(year:Int):Boolean{
        var isLeapYear =
            year % 4 == 0
        isLeapYear = isLeapYear && (year% 100 != 0); // 年份或者能够被400整除
        isLeapYear = isLeapYear || (year % 400 ==0);
        return isLeapYear
    }
    fun show(){
        currentIndex++
        if (currentIndex > 3){
            currentIndex=0
        }
        loadSum()
        views.findViewById<TextView>(R.id.date_money_cycle).text = arr[currentIndex]

    }
    @SuppressLint("SetTextI18n")
    fun loadSum(){
        val year = Nums.calendar.get(Calendar.YEAR)
        val month = Nums.calendar.get(Calendar.MONTH)

        if (currentIndex == 0){
            var m = ((money_sum /4).toString())
            if (m.length>6){
                m = m.substring(0,6)
            }
            views.findViewById<TextView>(R.id.date_money_sum).text =m + "元"
        }else if (currentIndex == 1){
            var m = ((money_sum).toString())
            if (m.length>6){
                m = m.substring(0,6)
            }
            views.findViewById<TextView>(R.id.date_money_sum).text =m + "元"
        }else if (currentIndex == 2){
            var m = ((money_sum*12).toString())
            if (m.length>6){
                m = m.substring(0,6)
            }
            views.findViewById<TextView>(R.id.date_money_sum).text =m + "元"
        }else if (currentIndex == 3){
            if (month == 2){
                if (isRun(year)){
                    var m = ((money_sum/29).toString())
                    if (m.length>6){
                        m = m.substring(0,6)
                    }
                    views.findViewById<TextView>(R.id.date_money_sum).text =m + "元"
                }else{
                    var m = ((money_sum/28).toString())
                    if (m.length>6){
                        m = m.substring(0,6)
                    }
                    views.findViewById<TextView>(R.id.date_money_sum).text =m + "元"                }
            }else{
                var m = ((money_sum/day_month[month]).toString())
                if (m.length>6){
                    m = m.substring(0,6)
                }
                views.findViewById<TextView>(R.id.date_money_sum).text =m + "元"

            }
        }

    }

    @SuppressLint("SetTextI18n", "CutPasteId")
    fun loadList(linearLayout: LinearLayout, inflater: LayoutInflater, date:List<Subscribe>){
        linearLayout.removeAllViews()
        var my = Calendar.getInstance()

        for (p in date){
            var view = inflater.inflate(
                R.layout.subscribe,
                linearLayout, false)
            view.findViewById<TextView>(R.id.subscribe_name).text = p.name

            if (p.cycleType == 1){
                view.findViewById<TextView>(R.id.subscribe_type).visibility=View.VISIBLE
                view.findViewById<TextView>(R.id.subscribe_type).text = "${p.money}元/${p.cycleTime}天"
                view.findViewById<TextView>(R.id.subscribe_monty).text = (p.money/p.cycleTime).toString() + " 元/天"

                money_sum += p.money*30/p.cycleTime


            }else if (p.cycleType == 2){
                view.findViewById<TextView>(R.id.subscribe_type).visibility=View.VISIBLE
                view.findViewById<TextView>(R.id.subscribe_type).text = "${p.money}元/${p.cycleTime}月"
                view.findViewById<TextView>(R.id.subscribe_monty).text = (p.money/p.cycleTime).toString() + " 元/月"

                money_sum +=p.money/p.cycleTime



            }else if (p.cycleType == 3){
                view.findViewById<TextView>(R.id.subscribe_type).visibility=View.VISIBLE
                view.findViewById<TextView>(R.id.subscribe_type).text = "${p.money}元/${p.cycleTime}年"
                view.findViewById<TextView>(R.id.subscribe_monty).text = (p.money/(p.cycleTime)).toString() + " 元/年"

                money_sum += p.money/(12*p.cycleTime)


            }


            println(money_sum)
            var isZhankai = false
            view.setOnClickListener {
                if (!isZhankai){
                    loadChipList(p.id,view)
                    view.findViewById<ChipGroup>(R.id.labellist).visibility = View.VISIBLE
                    isZhankai = true
                }else{
                    view.findViewById<ChipGroup>(R.id.labellist).visibility = View.GONE
                    isZhankai = false
                }


            }



            view.setOnLongClickListener{
                val intent = Intent(requireContext(),AddSubscribeActivity::class.java)
                intent.putExtra("id",p.id)
                startActivity(intent)
                return@setOnLongClickListener true
            }
            linearLayout.addView(view)
        }

        var m = ((money_sum /4).toString())
        if (m.length>6){
            m = m.substring(0,6)
        }
        views.findViewById<TextView>(R.id.date_money_sum).text =m + "元"
    }
    fun loadDatabyMoneyDesc(){
        thread {
            val p = SubscribeDatebase.getInstance(requireContext())?.subsrcbleDao?.getAllbyMoneyDESC()!!
            // 0 - 4 , 1 day ,2 month , 3 year , 0 no
            activity?.runOnUiThread {
                loadSum()
                loadList( views.findViewById<LinearLayout>(R.id.subscribe_lists),myinflater,p)
            }
        }
    }
    fun loadDatabyMoneyAsc(){
        thread {
            val p = SubscribeDatebase.getInstance(requireContext())?.subsrcbleDao?.getAllbyFirstSubscribeASC()!!
            // 0 - 4 , 1 day ,2 month , 3 year , 0 no
            activity?.runOnUiThread {
                loadSum()
                loadList( views.findViewById<LinearLayout>(R.id.subscribe_lists),myinflater,p)
            }
        }
    }
    fun loadDatabyNameDesc(){
        thread {
            val p = SubscribeDatebase.getInstance(requireContext())?.subsrcbleDao?.getAllbyNameDESC()!!
            // 0 - 4 , 1 day ,2 month , 3 year , 0 no
            activity?.runOnUiThread {
                loadSum()
                loadList( views.findViewById<LinearLayout>(R.id.subscribe_lists),myinflater,p)
            }
        }
    }
    fun loadDatabyNameAsc(){
        thread {
            val p = SubscribeDatebase.getInstance(requireContext())?.subsrcbleDao?.getAllbyNameASC()!!
            // 0 - 4 , 1 day ,2 month , 3 year , 0 no
            activity?.runOnUiThread {
                loadSum()
                loadList( views.findViewById<LinearLayout>(R.id.subscribe_lists),myinflater,p)
            }
        }
    }
    fun loadDatabyTimeDesc(){
        thread {
            val p = SubscribeDatebase.getInstance(requireContext())?.subsrcbleDao?.getAllbyFirstSubscribeDESC()!!
            // 0 - 4 , 1 day ,2 month , 3 year , 0 no
            activity?.runOnUiThread {
                loadSum()
                loadList( views.findViewById<LinearLayout>(R.id.subscribe_lists),myinflater,p)
            }
        }
    }
    fun loadDatabyTimeAsc(){
        thread {
            val p = SubscribeDatebase.getInstance(requireContext())?.subsrcbleDao?.getAllbyFirstSubscribeASC()!!
            // 0 - 4 , 1 day ,2 month , 3 year , 0 no
            activity?.runOnUiThread {
                loadSum()
                loadList( views.findViewById<LinearLayout>(R.id.subscribe_lists),myinflater,p)
            }
        }
    }
    fun loadChipList(id:Long,viewss:View){
        thread {
            val p = TagDatabase.getInstance(requireContext())?.tagDao?.getbyId(id)!!
            // 0 - 4 , 1 day ,2 month , 3 year , 0 no
            activity?.runOnUiThread {
                loadChipGroup(viewss.findViewById<ChipGroup>(R.id.labellist),myinflater,p,viewss,id)
            }
        }
    }
    private fun loadChipGroup(findViewById: ChipGroup, inflater: LayoutInflater, p: List<Tag>,viewss: View,id: Long) {
        findViewById.removeAllViews()
        var o = 5
        if (p.size-1<5){
            o = p.size-1
        }
        for (r in 0..o){
            val i = p[r]
            var view2 = inflater.inflate(
                R.layout.chip,
                findViewById, false)
            view2.findViewById<Chip>(R.id.subscribe_label).text = i.tag
            view2.findViewById<Chip>(R.id.subscribe_label).setOnCloseIconClickListener  {
                thread {
                    TagDatabase.getInstance(requireContext())?.tagDao?.delete(i)!!
                }
                findViewById.removeView(view2)

            }
            findViewById.addView(view2)

        }
        val view = inflater.inflate(
            R.layout.chipaction,
            findViewById, false)
        view.findViewById<Chip>(R.id.subscribe_all).setOnClickListener {
            val i =  Intent(
                requireContext(),ChipsActivity::class.java
            )
            i.putExtra("id",id)
            startActivity(
               i
            )
        }
        findViewById.addView(view)


    }
    override fun onResume() {
        super.onResume()
        money_sum = 0.0
        loadDatabyMoneyDesc()

    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.fragment_home, container, false)
        myinflater = inflater
        views.findViewById<TextView>(R.id.date_money_cycle).text = arr[currentIndex]

        views.findViewById<LinearLayout>(R.id.ppp).setOnClickListener {
            show()
        }
        var isMenusOpen = false
        views.findViewById<ExtendedFloatingActionButton>(R.id.menus).setOnClickListener {
            if (!isMenusOpen){
                isMenusOpen=true
                views.findViewById<ExtendedFloatingActionButton>(R.id.sort).visibility = View.VISIBLE
                views.findViewById<ExtendedFloatingActionButton>(R.id.subscribe).visibility = View.VISIBLE
            }else{
                isMenusOpen=false
                views.findViewById<ExtendedFloatingActionButton>(R.id.sort).visibility = View.GONE
                views.findViewById<ExtendedFloatingActionButton>(R.id.subscribe).visibility = View.GONE

            }
        }
        views.findViewById<ExtendedFloatingActionButton>(R.id.sort).setOnClickListener {
            showPopupMenu(requireContext(),it)
        }
        views.findViewById<ExtendedFloatingActionButton>(R.id.subscribe).setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    AddSubscribeActivity::class.java
                )
            )
        }
        return views
    }
    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun showPopupMenu(context: Context, ancher: View) {
        val popupMenu = PopupMenu(context, ancher)
        //引入菜单资源
        popupMenu.inflate(R.menu.newmenu)

        //菜单项的监听
        popupMenu.setOnMenuItemClickListener { menuItem ->
            money_sum = 0.0
            when (menuItem.itemId) {
                R.id.nameasc -> loadDatabyNameAsc()
                R.id.namedesc -> loadDatabyNameDesc()
                R.id.time -> loadDatabyTimeAsc()
                R.id.timedesc -> loadDatabyTimeDesc()
                R.id.moneyasc -> loadDatabyMoneyAsc()
                R.id.moneydesc -> loadDatabyMoneyDesc()
            }
            true
        }
        //使用反射。强制显示菜单图标
        try {
            val field = popupMenu.javaClass.getDeclaredField("mPopup")
            field.isAccessible = true
            val mHelper = field[popupMenu] as MenuPopupHelper
            mHelper.setForceShowIcon(true)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        //显示PopupMenu
        popupMenu.show()
    }
    private fun showAlertDialog(view : View, linearLayout: LinearLayout, subscribe: Subscribe){
        var builder= MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("提示")
        builder.setMessage("是否要删除这个订阅么？")
        builder.setPositiveButton("确认"){dialog, which ->
            linearLayout.removeView(view)
            thread {
                SubscribeDatebase.getInstance(view.context)?.subsrcbleDao?.delete(subscribe)
            }
        }
        builder.setNegativeButton("取消"){dialog, which ->


        }
        var dialog: AlertDialog =builder.create()
        if (!dialog.isShowing) {
            dialog.show()
        }
    }
}

