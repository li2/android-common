/*
 * Created by Weiyi Li on 15/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")

package me.li2.android.common.number

import android.content.Context
import androidx.annotation.Dimension
import androidx.annotation.Px

fun Int?.orZero() = this ?: 0

@Px
fun Int.dpToPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()

@Dimension(unit = Dimension.DP)
fun Int.pxToDp(context: Context): Int = (this / context.resources.displayMetrics.density).toInt()