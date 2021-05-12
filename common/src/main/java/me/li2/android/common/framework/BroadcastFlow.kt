package me.li2.android.common.framework

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/*
 * Created by Weiyi Li on 12/05/21.
 * https://github.com/li2
 */

/**
 * Create an coroutine [Flow] which emits the intent when the BroadcastReceiver is receiving an Intent broadcast.
 *
 * @param action The action to match, such as Intent.ACTION_MAIN.
 */
@ExperimentalCoroutinesApi
fun Context.broadcastFlow(action: String): Flow<Intent> =
    broadcastFlow(IntentFilter(action))

/**
 * Create an coroutine [Flow] which emits the intent when the BroadcastReceiver is receiving an Intent broadcast.
 *
 * @param filter Selects the Intent broadcasts to be received.
 */
@ExperimentalCoroutinesApi
fun Context.broadcastFlow(filter: IntentFilter): Flow<Intent> = callbackFlow {
    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            sendBlocking(intent)
        }
    }
    registerReceiver(broadcastReceiver, filter)
    awaitClose {
        unregisterReceiver(broadcastReceiver)
    }
}
