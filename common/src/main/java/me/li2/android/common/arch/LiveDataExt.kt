/*
 * Created by Weiyi Li on 15/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")

package me.li2.android.common.arch

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import me.li2.android.common.arch.Resource.Status.ERROR
import me.li2.android.common.arch.Resource.Status.LOADING

fun <T> MutableLiveData<Resource<T>>.postSuccess(data: T) =
        postValue(Resource.success(data))

fun <T> MutableLiveData<Resource<T>>.postLoading() =
        postValue(Resource.loading(value?.data))

fun <T> MutableLiveData<Resource<T>>.postError(exception: Exception? = null) =
        postValue(Resource.error(exception, value?.data))

fun<T> MutableLiveData<Resource<T>>.isIdle(): Boolean {
    val status = value?.status
    return status != LOADING && status != ERROR
}

fun<T> MutableLiveData<Resource<T>>.isLoading(): Boolean {
    return value?.status == LOADING
}

fun<T> MutableLiveData<Resource<T>>.isError(): Boolean {
    return value?.status == ERROR
}

fun <T : Any, L : LiveData<T>> Fragment.observeOnView(liveData: L, body: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer(body))
}
