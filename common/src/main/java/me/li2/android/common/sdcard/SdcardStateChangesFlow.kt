package me.li2.android.common.sdcard

import android.content.Context
import android.content.IntentFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.li2.android.common.framework.broadcastFlow

/*
 * Created by Weiyi Li on 12/05/21.
 * https://github.com/li2
 */

/**
 * Create a coroutine [Flow] which emits the [SdcardState] when sdcard state changes.
 */
@ExperimentalCoroutinesApi
fun Context.sdcardStateChangesFlow(): Flow<SdcardState> {
    val intentFilter = IntentFilter()
        .apply {
            SdcardState.intentActions.forEach { action -> addAction(action) }
            addDataScheme("file")
        }
    return broadcastFlow(intentFilter).map { intent -> SdcardState.from(intent.action) }
}
