/*
 * Created by Weiyi Li on 16/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")

package me.li2.android.common.rx

import android.Manifest.permission.*
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import me.li2.android.common.rx.PermissionResult.*
import me.li2.android.common.rx.PermissionUtils.checkAndRequestPermission
import me.li2.android.common.rx.PermissionUtils.isPermissionGranted

enum class PermissionResult {
    GRANTED,
    DENIED,
    DENIED_NOT_ASK_AGAIN,
}

object PermissionUtils {

    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED
    }

    fun requestPermission(activity: FragmentActivity,
                          permission: String): Observable<PermissionResult> =
            requestPermissions(activity, listOf(permission))


    fun requestPermissions(activity: FragmentActivity,
                           permissions: List<String>): Observable<PermissionResult> =
            RxPermissions(activity).requestEach(*permissions.toTypedArray()).map { permission ->
                when {
                    // permission is granted
                    permission.granted -> GRANTED
                    // Denied permission without ask never again
                    permission.shouldShowRequestPermissionRationale -> DENIED
                    // Denied permission with ask never again, need to go to the settings
                    else -> DENIED_NOT_ASK_AGAIN
                }
            }

    fun checkAndRequestPermission(activity: FragmentActivity,
                                  permission: String,
                                  prompt: AlertDialog? = null): Observable<PermissionResult> {
        return Observable.just(isPermissionGranted(activity, permission))
                .flatMap { granted ->
                    when {
                        !granted && prompt != null -> {
                            prompt.buttonClicks().flatMap { which ->
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    requestPermission(activity, permission)
                                } else {
                                    Observable.just(DENIED)
                                }
                            }
                        }
                        !granted && prompt == null -> requestPermission(activity, permission)
                        else -> Observable.just(GRANTED)
                    }
                }
    }
}

/*
 * ACCESS_FINE_LOCATION for both NETWORK_PROVIDER and GPS_PROVIDER
 * ACCESS_COARSE_LOCATION only for NETWORK_PROVIDER.
 */
fun Context.isLocationPermissionGranted(): Boolean = isPermissionGranted(this, ACCESS_FINE_LOCATION)

fun FragmentActivity.checkAndRequestLocationPermission(prompt: AlertDialog? = null): Observable<PermissionResult> =
    checkAndRequestPermission(this, ACCESS_FINE_LOCATION)

fun FragmentActivity.checkAndRequestCameraPermission(prompt: AlertDialog? = null): Observable<PermissionResult> =
        checkAndRequestPermission(this, CAMERA)

fun FragmentActivity.checkAndRequestBluetoothPermission(prompt: AlertDialog? = null): Observable<PermissionResult> =
        checkAndRequestPermission(this, BLUETOOTH)
