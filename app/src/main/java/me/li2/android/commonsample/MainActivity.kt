package me.li2.android.commonsample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import me.li2.android.common.framework.SimUtils
import me.li2.android.common.rx.bluetoothStateChanges
import me.li2.android.common.rx.sdcardStateChanges
import me.li2.android.common.rx.subscribeOnLifecycle
import timber.log.Timber.d

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.resultTextView).text = SimUtils.getSimInfo(this)

        sdcardStateChanges().subscribeOnLifecycle(lifecycle) {
            d("sdcard state changed: $it")
        }

        bluetoothStateChanges().subscribeOnLifecycle(lifecycle) {
            d("bluetooth state changed: $it")
        }
    }
}
