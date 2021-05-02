/*
 * Created by Weiyi Li on 2019-11-04.
 * https://github.com/li2
 */
@file:Suppress("unused")
package me.li2.android.common.framework

import android.content.Context
import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager

/**
 * What's cookies?
 * Cookies are files created by websites you visit. They make your online experience
 * easier by saving browsing information. With cookies, sites can keep you signed in,
 * remember your site preferences, and give you locally relevant content.
 */
object CookiesUtil {

    /**
     * Sets a cookie for the given URL. Any existing cookie with the same host,
     * path and name will be replaced with the new cookie. The cookie being set
     * will be ignored if it is expired.
     *
     * @param url the URL for which the cookie is to be set
     * @param cookies a list pair of cookie-name and cookie-value.
     */
    fun setCookies(context: Context, url: String, cookies: HashMap<String, String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val cookieManager = CookieManager.getInstance()
            cookieManager.setCookies(url, cookies)
            cookieManager.flush()
        } else {
            CookieSyncManager.createInstance(context)
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeSessionCookie()
            cookieManager.setCookies(url, cookies)
            CookieSyncManager.getInstance().sync()
        }
    }

    /**
     * Clear cookies.
     */
    fun clearCookie(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookies(null)
            cookieManager.flush()
        } else {
            CookieSyncManager.createInstance(context)
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            CookieSyncManager.getInstance().sync()
        }
    }

    private fun CookieManager.setCookies(url: String, cookies: HashMap<String, String>) {
        for ((key, value) in cookies) {
            // Syntax: Set-Cookie: <cookie-name>=<cookie-value>;
            setCookie(url, "$key=$value;")
        }
    }
}
