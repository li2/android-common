package me.li2.android.commonsample

import android.hardware.Sensor
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import me.li2.android.common.bluetooth.bluetoothStateChanges
import me.li2.android.common.rx.subscribeOnLifecycle
import me.li2.android.common.sdcard.sdcardStateChanges
import me.li2.android.common.sensor.sensorChanges
import timber.log.Timber.d
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var sensorDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sdcardStateChanges().subscribeOnLifecycle(lifecycle) {
            d("sdcard state changed: $it")
        }

        bluetoothStateChanges().subscribeOnLifecycle(lifecycle) {
            d("bluetooth state changed: $it")
        }

        trySensorChangesFlowable()
    }

    private fun trySensorChangesFlowable() {
        sensorDisposable = sensorChanges(Sensor.TYPE_LINEAR_ACCELERATION)
            .doOnSubscribe { d("doOnSubscribe") }
            .doOnCancel { d("doOnCancel") }
            .throttleFirst(5, TimeUnit.SECONDS)
            .doOnNext { sensorEvent -> d("doOnNext: $sensorEvent, ${System.currentTimeMillis()}") }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        findViewById<Button>(R.id.resultTextView).setOnClickListener {
            sensorDisposable.dispose()
        }
    }
}
