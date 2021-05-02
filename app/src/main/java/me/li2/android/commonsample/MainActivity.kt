package me.li2.android.commonsample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import me.li2.android.common.framework.SimUtils
import me.li2.android.common.rx.onSdcardStateChanges
import timber.log.Timber.d

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.resultTextView).text = SimUtils.getSimInfo(this)

        compositeDisposable += onSdcardStateChanges().subscribe {
            d("sdcard state changed: $it")
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
