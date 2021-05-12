/*
 * Created by Weiyi Li on 15/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")
package me.li2.android.common.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

private const val CLICK_THROTTLE_DELAY = 500L

private const val CLICK_THROTTLE_DELAY_LONG = 2000L

fun Observable<CharSequence>.mapToString(): Observable<String> = this.map { it.toString() }

fun <T> Observable<T>.throttleFirstShort() = this.throttleFirst(CLICK_THROTTLE_DELAY, TimeUnit.MILLISECONDS)!!

fun <T> Observable<T>.throttleFirstLong() = this.throttleFirst(CLICK_THROTTLE_DELAY_LONG, TimeUnit.MILLISECONDS)!!

fun <T> Observable<T>.forUi(): Observable<T> =
        this.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

fun <T> Single<T>.forUi(): Single<T> =
        this.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

fun Completable.forUi(): Completable =
        this.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
