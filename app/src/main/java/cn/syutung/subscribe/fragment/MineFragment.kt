package cn.syutung.subscribe.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.syutung.subscribe.R
import cn.syutung.subscribe.activitys.BacUpInddexActivity
import cn.syutung.subscribe.activitys.DrawCakeActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView


class MineFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        return views
    }


}