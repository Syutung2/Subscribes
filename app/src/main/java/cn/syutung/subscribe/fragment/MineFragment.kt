package cn.syutung.subscribe.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import cn.syutung.subscribe.R
import cn.syutung.subscribe.activitys.BacUpInddexActivity
import cn.syutung.subscribe.activitys.DrawCakeActivity
import cn.syutung.subscribe.activitys.WebActivity
import cn.syutung.subscribe.utils.Nums
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.card.MaterialCardView


class MineFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val views = inflater.inflate(R.layout.fragment_mine, container, false)
        views.findViewById<MaterialCardView>(R.id.drawCake).setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),DrawCakeActivity::class.java
                )
            )
        }
        views.findViewById<MaterialCardView>(R.id.backup).setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),BacUpInddexActivity::class.java
                )
            )
        }
        views.findViewById<MaterialCardView>(R.id.ysxy_button).setOnClickListener {
            val intent = Intent(requireContext(), WebActivity::class.java)
            intent.putExtra("mode",2)
            startActivity(intent)
        }
        views.findViewById<MaterialCardView>(R.id.user_button).setOnClickListener {
            val intent = Intent(requireContext(), WebActivity::class.java)
            intent.putExtra("mode",1)
            startActivity(intent)
        }
        views.findViewById<MaterialCardView>(R.id.ysxy_chexiao).setOnClickListener {
            val sharedPreferences = activity?.getSharedPreferences(Nums.PACKAGENAME,0)
            sharedPreferences?.edit()?.putBoolean("isFirstYinsi",false)?.putBoolean("isFirstUser",false)?.apply()
            activity?.finish()

        }
        views.findViewById<MaterialCardView>(R.id.licenses).setOnClickListener {

            startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.app_name));
        }
        views.findViewById<AppCompatTextView>(R.id.version).text = "version ${packageName(requireContext())}(${packageCode(requireContext())})"
        return views
    }

    fun packageName(context: Context): String? {
        val manager: PackageManager = context.getPackageManager()
        var name: String? = null
        try {
            val info: PackageInfo = manager.getPackageInfo(context.getPackageName(), 0)
            name = info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return name
    }
    fun packageCode(context: Context): Int {
        val manager: PackageManager = context.getPackageManager()
        var code = 0
        try {
            val info: PackageInfo = manager.getPackageInfo(context.getPackageName(), 0)
            code = info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return code
    }
}