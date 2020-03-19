/*
 * Created by Weiyi Li on 15/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")
package me.li2.android.common.collections

import java.util.*

/**
 * Similar to data class copying, allows you to update/replace element in the immutable list.
 *
 * val updatedPlayers = players.copy { this`[2`] = updatedPlayer }
 * @return a new list with updated item.
 */
inline fun <T> List<T>.copy(mutatorBlock: MutableList<T>.() -> Unit): List<T> {
    return toMutableList().apply(mutatorBlock)
}

/**
 * @return `true` if [element] is found in the collection.
 */
fun List<String>.containsIgnoreCase(element: String) =
    this.firstOrNull { it.equals(element, true) } != null

/**
 * Swap items with given index [indexA] and [indexB]
 *
 * @return swapped list or original list if index is out of bound.
 */
fun <T> List<T>.swap(indexA: Int, indexB: Int): List<T> {
    if (indexA >= size || indexB >= size) return this
    Collections.swap(this, indexA, indexB)
    return this
}
