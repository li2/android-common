package me.li2.android.commonsample

import timber.log.Timber


/*
 * Created by Weiyi Li on 2/05/21.
 * https://github.com/li2
 */
object Utils {
    fun plantTimber() {
        // setup Timber
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return "[${super.createStackElementTag(element)}#${element.methodName}:${element.lineNumber}]"
            }
        })
    }
}