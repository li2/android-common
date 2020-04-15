package me.li2.android.common.framework

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat

/** Open another app.
 * @param packageName the full package name of the app to open
 * @return true if likely successful, false if unsuccessful
 */
fun Context.openApp(packageName: String, onAppNotFound: () -> Unit = {}) {
    return try {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
            ?: throw ActivityNotFoundException()
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        this.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        onAppNotFound()
    }
}

/**
 * Open application settings page.
 * @param appId application ID
 */
fun Context.openAppSettings(appId: String) {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$appId")))
}

/**
 * Send email through 3rd email App.
 * @param email address to send
 * @param subject the subject of email
 * @param content the content of email
 * @return false if no email apps installed.
 */
fun Context.email(email: String, subject: String = "", content: String = ""): Boolean {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    if (subject.isNotEmpty())
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    if (content.isNotEmpty())
        intent.putExtra(Intent.EXTRA_TEXT, content)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        return true
    }
    return false

}

/**
 * Make a call. Require [Manifest.permission.CALL_PHONE] permission.
 */
@RequiresPermission(Manifest.permission.CALL_PHONE)
fun Context.call(number: String): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

/**
 * Open system dial App. no permission needed.
 */
fun Context.dial(number: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$number")
    }
    ContextCompat.startActivity(this, intent, null)
}

/**
 * Open share sheet.
 */
fun Context.share(text: String, subject: String = ""): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, null))
        true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        false
    }
}
