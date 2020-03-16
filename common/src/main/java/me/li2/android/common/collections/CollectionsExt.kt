/*
 * Created by Weiyi Li on 15/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")
package me.li2.android.common.collections

/**
 * Swap items with given index [indexA] and [indexB]
 *
 * @return swapped list or original list if index is out of bound.
 */
fun <T> List<T>.swap(indexA: Int, indexB: Int): List<T> {
    if (indexA >= size || indexB >= size) return this
    return this.toMutableList().also {
        it[indexA] = this[indexB]
        it[indexB] = this[indexA]
    }.toList()
}

/**
 * @return `true` if [element] is found in the collection.
 */
fun List<String>.containsIgnoreCase(element: String) =
        this.firstOrNull { it.equals(element, true) } != null
