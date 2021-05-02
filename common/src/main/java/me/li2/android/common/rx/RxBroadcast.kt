/*
 * Created by Weiyi Li on 2/05/21.
 * https://github.com/li2
 */
package me.li2.android.common.rx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.rxjava3.core.Emitter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Create an observable which emits the intent when received broadcast.
 */
fun Context.onBroadcast(action: String): Observable<Intent> =
    onBroadcast(IntentFilter(action))

fun Context.onBroadcast(intentFilter: IntentFilter): Observable<Intent> =
    Observable.defer { Observable.create(BroadcastObservable(this, intentFilter)) }

private class BroadcastObservable(
    private val context: Context,
    private val intentFilter: IntentFilter
) : ObservableOnSubscribe<Intent>, Disposable {

    private var broadcastReceiver: BroadcastReceiver?
    private lateinit var emitter: Emitter<in Intent>

    init {
        // Receives the [android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED].
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (::emitter.isInitialized) {
                    emitter.onNext(intent)
                }
            }
        }
    }

    override fun subscribe(emitter: ObservableEmitter<Intent>) {
        this.emitter = emitter
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
