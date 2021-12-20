package me.li2.android.common.arch

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