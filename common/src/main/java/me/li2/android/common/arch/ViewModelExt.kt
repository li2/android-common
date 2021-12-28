package me.li2.android.common.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.io(
    errorHandler: Exception.() -> Unit = {},
    ioHandler: suspend CoroutineScope.() -> Unit
) {
    viewModelScope.launch(Dispatchers.IO) {
        try {
            this.ioHandler()
        } catch (throwable: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                throwable.errorHandler()
            }
        }
    }
}

fun <T> ViewModel.ioWithState(
    state: MutableLiveData<Resource<T>>,
    ioHandler: suspend () -> T
) {
    state.postLoading()
    io({
        state.postError(this)
    }) {
        state.postSuccess(ioHandler())
    }
}
