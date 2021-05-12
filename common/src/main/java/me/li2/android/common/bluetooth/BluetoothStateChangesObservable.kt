/*
 * Created by Weiyi Li on 20/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")

package me.li2.android.common.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter.*
import android.content.Context
import androidx.fragment.app.FragmentActivity
import io.reactivex.rxjava3.core.Observable
import me.li2.android.common.framework.PermissionResult
import me.li2.android.common.framework.checkAndRequestBluetoothPermission
import me.li2.android.common.framework.onBroadcast
import me.li2.android.common.logic.orFalse

/**
 * Create an observable which emits the state (on/off) of system bluetooth.
 * @return true if bluetooth is on, otherwise, false.
 *
 * @see <a href="https://gist.github.com/magillus/927f863987575021dc78249bd8064423">magillus/RxBroadcastReceiver.java</a>
 * @see <a href="https://developer.android.com/guide/topics/connectivity/bluetooth">Bluetooth overview</a>
 */
fun Context.bluetoothStateChanges(): Observable<Boolean> {
    return onBroadcast(ACTION_STATE_CHANGED)
        .map { intent -> intent.getIntExtra(EXTRA_STATE, ERROR) }
        .filter { state -> state == STATE_ON || state == STATE_OFF } // don't care the intermediate states
        .map { state -> state == STATE_ON }
        .startWithItem(isBluetoothEnabled())
}

/**
 * Return true if Bluetooth is currently enabled and ready for use.
 * @exception RuntimeException if don't have bluetooth permission.
 */
fun isBluetoothEnabled(activity: FragmentActivity): Observable<Boolean> {
    return activity.checkAndRequestBluetoothPermission()
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
