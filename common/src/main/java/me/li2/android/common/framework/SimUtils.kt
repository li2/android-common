package me.li2.android.common.framework

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager

/*
 * Created by Weiyi Li on 16/03/21.
 * https://github.com/li2
 */

object SimUtils {
    fun getSimInfo(context: Context): String {
        val phoneManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val simCarrierIdName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            phoneManager.simCarrierIdName?.toString().orEmpty()
        } else {
            ""
        }

        return "simCountryIso=${phoneManager.simCountryIso}, simCarrierIdName=$simCarrierIdName, networkCountryIso=${phoneManager.networkCountryIso}"
    }
}
