/*
 * Created by Weiyi Li on 2/05/21.
 * https://github.com/li2
 */
package me.li2.android.common.framework

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Create an RxJava [Observable] which emits the intent when the BroadcastReceiver is receiving an Intent broadcast.
 *
 * @param action The action to match, such as Intent.ACTION_MAIN.
 */
fun Context.onBroadcast(action: String): Observable<Intent> =
    onBroadcast(IntentFilter(action))

/**
 * Create an RxJava [Observable] which emits the intent when the BroadcastReceiver is receiving an Intent broadcast.
 *
 * @param filter Selects the Intent broadcasts to be received.
 */
fun Context.onBroadcast(filter: IntentFilter): Observable<Intent> {
    return Observable.create { emitter ->
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                emitter.onNext(intent)
            }
        }
        emitter.setDisposable(Disposable.fromRunnable {
            unregisterReceiver(broadcastReceiver)
        })
        registerReceiver(broadcastReceiver, filter)
    }
}
