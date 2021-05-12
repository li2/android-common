/*
 * @created: 11 May 2021.
 * @author: Weiyi Li
 * Copyright (c) 2021 EROAD Limited. All rights reserved.
 */
package me.li2.android.common.kotlinx

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun CoroutineScope.io(
    onError: (Exception) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(Dispatchers.IO) {
        try {
            block()
        } catch (exception: Exception) {
            onError(exception)
        }
    }
}

fun CoroutineScope.computation(
    onError: (Exception) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(Dispatchers.IO) {
        try {
            block()
        } catch (exception: Exception) {
            onError(exception)
        }
    }
}

fun <T> CoroutineScope.ui(block: CoroutineScope.() -> T): Job {
    return launch(Dispatchers.Main) {
        block()
    }
}
