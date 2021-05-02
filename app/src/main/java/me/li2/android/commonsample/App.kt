package me.li2.android.commonsample

import android.app.Application

/*
 * Created by Weiyi Li on 2/05/21.
 * https://github.com/li2
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.plantTimber()
    }
}