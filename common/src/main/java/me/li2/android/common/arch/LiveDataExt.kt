/*
 * Created by Weiyi Li on 15/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")

package me.li2.android.common.arch

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import me.li2.android.common.arch.Resource.Status.ERROR
import me.li2.android.common.arch.Resource.Status.LOADING

fun <T : Any, L : LiveData<T>> Fragment.observeOnView(liveData: L, body: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer(body))
}

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

/**
 * Sets the value to the result of a function that is called when both `LiveData`s have data
 * or when they receive updates after that.
 *
 * @return mediatorLiveData which must have an active observer attached
 * @see <a href="https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7">LiveData beyond the ViewModel — Reactive patterns using Transformations and MediatorLiveData</a>
 */
fun <T, A, B> LiveData<A>.combine(other: LiveData<B>, onChange: (A, B) -> T): MediatorLiveData<T> {
    var source1emitted = false
    var source2emitted = false

    val result = MediatorLiveData<T>()

    val mergeF = {
        val source1Value = this.value
        val source2Value = other.value

        if (source1emitted && source2emitted) {
            result.value = onChange.invoke(source1Value!!, source2Value!!)
        }
    }

    result.addSource(this) { source1emitted = true; mergeF.invoke() }
    result.addSource(other) { source2emitted = true; mergeF.invoke() }

    return result
}

/**
 * Combine the latest value emitted by each LiveData.
 *
 * CombineLatest emits an item whenever any of the source Observables emits an item
 * (so long as each of the source Observables has emitted at least one item).
 *
 * @return mediatorLiveData which must have an active observer attached
 * @see <a href="https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7">LiveData beyond the ViewModel — Reactive patterns using Transformations and MediatorLiveData</a>
 */
fun <R> combineLatest(vararg sources: LiveData<*>,
                      onChange: (results: List<Any>) -> R): MediatorLiveData<R> {
    val sourcesEmitted = sources.map { false }.toMutableList()
    val result = MediatorLiveData<R>()

    val mergeF = {
        if (!sourcesEmitted.contains(false)) {
            // all sources have emitted value
            result.value = onChange.invoke(sources.map { it.value!! })
        }
    }

    sources.forEachIndexed { index, liveData ->
        result.addSource(liveData) {
            sourcesEmitted[index] = true
            mergeF.invoke()
        }
    }

    return result
}