/*
 * Created by Weiyi Li on 15/03/20.
 * https://github.com/li2
 */
@file:JvmName("Uri")
package me.li2.android.common.net

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import me.li2.android.common.logic.orFalse

/**
 * @return mime type of file from Uri (Uniform Resource Identifier,
 *  a string of characters that identifies a particular resource).
 * @see <a href="http://androidxref.com/4.4.4_r1/xref/frameworks/base/media/java/android/media/MediaFile.java#174">Android available mime-types</a>
 * @see <a href="https://stackoverflow.com/a/31691791/2722270">Detect mime type of any file</a>
 */
@SuppressLint("DefaultLocale")
fun Uri.toMimeType(context: Context): String? {
    return if (scheme == ContentResolver.SCHEME_CONTENT) {
        context.contentResolver.getType(this)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(toString())
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
    }
}

fun Uri.isVideo(context: Context) = toMimeType(context)?.startsWith("video").orFalse()

fun Uri.isImage(context: Context) = toMimeType(context)?.startsWith("image").orFalse()
