/*
 * Created by Weiyi Li on 16/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")

package me.li2.android.common.framework

import android.Manifest.permission.*
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.tbruyelle.rxpermissions2.RxPermissions
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Observable
import me.li2.android.common.framework.PermissionResult.*

enum class PermissionResult {
    GRANTED,
    DENIED,
    DENIED_NOT_ASK_AGAIN,
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED
}

fun Activity.requestPermission(permission: String): Observable<PermissionResult> =
    requestPermissions(listOf(permission))

fun Activity.requestPermissions(permissions: List<String>): Observable<PermissionResult> =
    // todo remove RxJavaBridge when RxPermissions migrated to RxJava3
    RxJavaBridge
        .toV3Observable(RxPermissions(this).requestEach(*permissions.toTypedArray()))
        .map { permission ->
            when {
                // permission is granted
                permission.granted -> GRANTED
                // Denied permission without ask never again
                permission.shouldShowRequestPermissionRationale -> DENIED
                // Denied permission with ask never again, need to go to the settings
                else -> DENIED_NOT_ASK_AGAIN
            }
        }

fun Activity.checkAndRequestPermission(
    permission: String,
    prompt: AlertDialog? = null
): Observable<PermissionResult> {
    return Observable.just(isPermissionGranted(permission))
        .flatMap { granted ->
            when {
                !granted && prompt != null -> {
                    prompt.also { it.show() }.buttonClicks().flatMap { which ->
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            requestPermission(permission)
                        } else {
                            Observable.just(DENIED)
                        }
                    }
                }
                !granted && prompt == null -> requestPermission(permission)
                else -> Observable.just(GRANTED)
            }
        }
}

/*
 * ACCESS_FINE_LOCATION for both NETWORK_PROVIDER and GPS_PROVIDER
 * ACCESS_COARSE_LOCATION only for NETWORK_PROVIDER.
 */
fun Context.isLocationPermissionGranted(): Boolean = isPermissionGranted(ACCESS_FINE_LOCATION)

fun Activity.checkAndRequestLocationPermission(prompt: AlertDialog? = null): Observable<PermissionResult> =
    checkAndRequestPermission(ACCESS_FINE_LOCATION, prompt)

fun Activity.checkAndRequestCameraPermission(prompt: AlertDialog? = null): Observable<PermissionResult> =
    checkAndRequestPermission(CAMERA, prompt)

fun Activity.checkAndRequestBluetoothPermission(prompt: AlertDialog? = null): Observable<PermissionResult> =
    checkAndRequestPermission(BLUETOOTH, prompt)
