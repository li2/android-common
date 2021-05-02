package me.li2.android.commonsample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.li2.android.common.framework.SimUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.resultTextView).text = SimUtils.getSimInfo(this)
    }
}
