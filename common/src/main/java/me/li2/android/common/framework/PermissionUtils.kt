/*
 * Created by Weiyi Li on 16/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")

package me.li2.android.common.framework

import android.Manifest.permission.*
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import me.li2.android.common.framework.PermissionResult.*
import me.li2.android.common.framework.PermissionUtils.isPermissionGranted
import me.li2.android.common.framework.PermissionUtils.requestPermission
import me.li2.android.common.framework.PermissionUtils.requestPermissions

enum class PermissionResult {
    GRANTED,
    DENIED,
    DENIED_NOT_ASK_AGAIN,
}

object PermissionUtils {

    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED
    }

    fun isPermissionsGranted(context: Context, permissions: List<String>): Boolean {
        var granted = false
        permissions.forEach { permission ->
            granted = granted && isPermissionGranted(context, permission)
        }
        return granted
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
}

fun Context.isLocationPermissionGranted(): Boolean =
        isPermissionGranted(this, ACCESS_COARSE_LOCATION)
                || isPermissionGranted(this, ACCESS_FINE_LOCATION)

fun FragmentActivity.requestLocationPermission(): Observable<PermissionResult> {
    return Observable.just(isLocationPermissionGranted())
            .flatMap { granted ->
                if (granted) {
                    Observable.just(GRANTED)
                } else {
                    requestPermissions(this, listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION))
                }
            }
}

fun FragmentActivity.requestCameraPermission(): Observable<PermissionResult> {
    return Observable.just(isPermissionGranted(this, CAMERA))
            .flatMap { granted ->
                if (granted) {
                    Observable.just(GRANTED)
                } else {
                    requestPermission(this, CAMERA)
                }
            }
}