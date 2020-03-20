/*
 * Created by Weiyi Li on 20/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")

package me.li2.android.common.rx

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.fragment.app.FragmentActivity
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import me.li2.android.common.logic.orFalse

/**
 * Create an observable which emits the state (on/off) of system bluetooth.
 * @return true if bluetooth is on, otherwise, false.
 */
fun Context.onBluetoothStateChanges(): Observable<Boolean> =
        Observable.defer { Observable.create(BluetoothStateChangeObservable(this)) }

/**
 * Return true if Bluetooth is currently enabled and ready for use.
 * @exception RuntimeException if don't have bluetooth permission.
 */
fun isBluetoothEnabled(activity: FragmentActivity): Observable<Boolean> {
    return activity.requestBluetoothPermission()
            .map { permissionResult ->
                if (permissionResult == PermissionResult.GRANTED) {
                    isBluetoothEnabled()
                } else {
                    throw RuntimeException("Bluetooth's permission is not granted")
                }
            }
}

@SuppressLint("MissingPermission")
private fun isBluetoothEnabled() = getDefaultAdapter()?.isEnabled.orFalse()

/**
 * RxJava based bluetooth state changes
 * @see <a href="https://gist.github.com/magillus/927f863987575021dc78249bd8064423">magillus/RxBroadcastReceiver.java</a>
 * @see <a href="https://developer.android.com/guide/topics/connectivity/bluetooth">Bluetooth overview</a>
 */
private class BluetoothStateChangeObservable(private val context: Context)
    : ObservableOnSubscribe<Boolean>, Disposable {

    private var intentFilter: IntentFilter = IntentFilter(ACTION_STATE_CHANGED)
    private var broadcastReceiver: BroadcastReceiver?
    private lateinit var emitter: Emitter<in Boolean>

    init {
        // Receives the [android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED].
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (::emitter.isInitialized) {
                    when (intent.getIntExtra(EXTRA_STATE, ERROR)) {
                        STATE_ON -> emitter.onNext(true)
                        STATE_OFF -> emitter.onNext(false)
                    }
                }
            }
        }
    }

    override fun subscribe(emitter: ObservableEmitter<Boolean>) {
        this.emitter = emitter
        emitter.onNext(isBluetoothEnabled())
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun dispose() {
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver)
        }
        broadcastReceiver = null
    }

    override fun isDisposed(): Boolean = broadcastReceiver != null
}
