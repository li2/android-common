/*
 * Created by Weiyi Li on 20/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")

package me.li2.android.common.framework

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable
import me.li2.android.common.logic.orFalse

object BluetoothUtils {

    /**
     * Return true if Bluetooth is currently enabled and ready for use.
     * @exception RuntimeException if don't have bluetooth permission.
     */
    @SuppressLint("MissingPermission")
    fun isBluetoothEnabled(activity: FragmentActivity): Observable<Boolean> {
        return activity.requestBluetoothPermission()
                .map { permissionResult ->
                    if (permissionResult == PermissionResult.GRANTED) {
                        isBluetoothEnabled()
                    } else {
                        throw RuntimeException("Bluetooth permission is not granted")
                    }
                }
    }

    @SuppressLint("MissingPermission")
    internal fun isBluetoothEnabled() = BluetoothAdapter.getDefaultAdapter()?.isEnabled.orFalse()

}