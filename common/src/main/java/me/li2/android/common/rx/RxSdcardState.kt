package me.li2.android.common.rx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable

/*
 * @created: 01 May 2021.
 * @author: Weiyi Li
 */
enum class SdcardState(val action: String) {
    MOUNTED(Intent.ACTION_MEDIA_MOUNTED),
    EJECT(Intent.ACTION_MEDIA_EJECT),
    UNMOUNTED(Intent.ACTION_MEDIA_UNMOUNTED),
    BAD_REMOVAL(Intent.ACTION_MEDIA_BAD_REMOVAL);

    fun isEject() = this == EJECT

    companion object {
        private val map = enumValues<SdcardState>().associateBy(SdcardState::action)

        val intentActions: List<String> = enumValues<SdcardState>().map { it.action }

        fun from(action: String?): SdcardState? = map[action]
    }
}

/**
 * Create an observable which emits the state of sdcard
 */
fun Context.onSdcardStateChanges(): Observable<SdcardState> =
    Observable.defer { Observable.create(SdcardStateChangeObservable(this)) }


private class SdcardStateChangeObservable(private val context: Context) :
    ObservableOnSubscribe<SdcardState>,
    Disposable {

    private var intentFilter: IntentFilter = IntentFilter()
        .apply {
            SdcardState.intentActions.forEach { action -> addAction(action) }
            addDataScheme("file")
        }
    private var broadcastReceiver: BroadcastReceiver?
    private lateinit var emitter: Emitter<in SdcardState>

    init {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (::emitter.isInitialized) {
                    SdcardState.from(intent.action)?.let { sdcardState ->
                        emitter.onNext(sdcardState)
                    }
                }
            }
        }
    }

    override fun subscribe(emitter: ObservableEmitter<SdcardState>) {
        this.emitter = emitter
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun dispose() {
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver)
        }
        broadcastReceiver = null
    }

    override fun isDisposed(): Boolean = broadcastReceiver != null
}
