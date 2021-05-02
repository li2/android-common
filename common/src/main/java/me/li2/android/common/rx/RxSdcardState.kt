package me.li2.android.common.rx

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.rxjava3.core.Observable

/*
 * @created: 01 May 2021.
 * @author: Weiyi Li
 */
enum class SdcardState(val action: String?) {
    UNKNOWN(null),
    MOUNTED(Intent.ACTION_MEDIA_MOUNTED),
    EJECT(Intent.ACTION_MEDIA_EJECT),
    UNMOUNTED(Intent.ACTION_MEDIA_UNMOUNTED),
    BAD_REMOVAL(Intent.ACTION_MEDIA_BAD_REMOVAL);

    fun isEject() = this == EJECT

    companion object {
        private val map = enumValues<SdcardState>().associateBy(SdcardState::action)

        val intentActions: List<String> = enumValues<SdcardState>().mapNotNull { it.action }

        fun from(action: String?): SdcardState = map[action] ?: UNKNOWN
    }
}

/**
 * Create an observable which emits the state of sdcard
 */
fun Context.sdcardStateChanges(): Observable<SdcardState> {
    val intentFilter = IntentFilter()
        .apply {
            SdcardState.intentActions.forEach { action -> addAction(action) }
            addDataScheme("file")
        }
    return onBroadcast(intentFilter).map { intent -> SdcardState.from(intent.action) }
}
