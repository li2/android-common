/*
 * Created by Weiyi Li on 15/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")

package me.li2.android.common.rx

import android.content.DialogInterface
import android.content.DialogInterface.*
import androidx.appcompat.app.AlertDialog
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import me.li2.android.common.rx.internal.checkMainThread

/**
 * Create an observable which emits the identifier of clicked button in the dialog.
 * @return [BUTTON_POSITIVE], [BUTTON_NEUTRAL], [BUTTON_NEGATIVE].
 *  Please note dismiss dialog also consider as negative.
 */
fun AlertDialog.buttonClicks(): Observable<Int> {
    return AlertDialogButtonClickObservable(this)
}

private class AlertDialogButtonClickObservable(
        private val dialog: AlertDialog
) : Observable<Int>() {

    override fun subscribeActual(observer: Observer<in Int>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(dialog, observer)
        observer.onSubscribe(listener)
        dialog.setOnClickListener(listener)
    }

    private class Listener(
            private val dialog: AlertDialog,
            private val observer: Observer<in Int>
    ) : MainThreadDisposable(), OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            if (!isDisposed) {
                dialog?.dismiss()
                observer.onNext(which)
            }
        }

        override fun onDispose() {
            dialog.setOnClickListener(null)
        }
    }
}

private fun AlertDialog.setOnClickListener(onClickListener: OnClickListener?) {
    fun callback(which: Int) = onClickListener?.onClick(this, which)
    fun setButtonClickListener(which: Int) {
        this.getButton(which)?.let { button ->
            if (onClickListener != null) {
                button.setOnClickListener { callback(which) }
            } else {
                button.setOnClickListener(null)
            }
        }
    }
    setButtonClickListener(BUTTON_POSITIVE)
    setButtonClickListener(BUTTON_NEGATIVE)
    setButtonClickListener(BUTTON_NEUTRAL)

    if (onClickListener != null) {
        setOnDismissListener { callback(BUTTON_NEGATIVE) }
    } else {
        setOnDismissListener(null)
    }
}