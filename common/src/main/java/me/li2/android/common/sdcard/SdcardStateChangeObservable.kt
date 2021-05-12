/*
 * @created: 01 May 2021.
 * @author: Weiyi Li
 */
package me.li2.android.common.sdcard

import android.content.Context
import android.content.IntentFilter
import io.reactivex.rxjava3.core.Observable
import me.li2.android.common.framework.onBroadcast

/**
 * Create an RxJava [Observable] which emits the [SdcardState] when sdcard state changes.
 */
fun Context.sdcardStateChanges(): Observable<SdcardState> {
    val intentFilter = IntentFilter()
        .apply {
            SdcardState.intentActions.forEach { action -> addAction(action) }
            addDataScheme("file")
        }
    return onBroadcast(intentFilter).map { intent -> SdcardState.from(intent.action) }
}
