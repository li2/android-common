package me.li2.android.common.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable

/*
 * Created by Weiyi Li on 12/05/21.
 * https://github.com/li2
 */

/**
 * Subscribe on [Lifecycle.Event.ON_START] and dispose on [Lifecycle.Event.ON_STOP] automatically.
 *
 * @param lifecycle an instance of [androidx.lifecycle.LifecycleRegistry], for example, [androidx.activity.ComponentActivity.getLifecycle]
 * @param onNext the consumer to accept emissions from the current Observable.
 */
fun <T> Observable<T>.subscribeOnLifecycle(lifecycle: Lifecycle, onNext: (T) -> Unit) {
    val lifecycleObserver: LifecycleObserver = object : LifecycleObserver {
        private var subscription: Disposable? = null

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun subscribe() {
            subscription = subscribe(onNext)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun dispose() {
            subscription?.dispose()
        }
    }

    lifecycle.addObserver(lifecycleObserver)
}

/**
 * Subscribe on [Lifecycle.Event.ON_START] and dispose on [Lifecycle.Event.ON_STOP] automatically.
 *
 * @param lifecycle an instance of [androidx.lifecycle.LifecycleRegistry], for example, [androidx.activity.ComponentActivity.getLifecycle]
 * @param onNext the consumer to accept emissions from the current Flowable.
 */
fun <T> Flowable<T>.subscribeOnLifecycle(lifecycle: Lifecycle, onNext: (T) -> Unit) {
    val lifecycleObserver: LifecycleObserver = object : LifecycleObserver {
        private var subscription: Disposable? = null

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun subscribe() {
            subscription = subscribe(onNext)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun dispose() {
            subscription?.dispose()
        }
    }

    lifecycle.addObserver(lifecycleObserver)
}
