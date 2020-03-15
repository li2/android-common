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

fun <T> MutableLiveData<Resource<T>>.postSuccess(data: T) =
        postValue(Resource.success(data))

fun <T> MutableLiveData<Resource<T>>.postLoading() =
        postValue(Resource.loading(value?.data))

fun <T> MutableLiveData<Resource<T>>.postError(exception: Exception? = null) =
        postValue(Resource.error(exception))

fun <T : Any, L : LiveData<T>> Fragment.observeOnView(liveData: L, body: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer(body))
}
