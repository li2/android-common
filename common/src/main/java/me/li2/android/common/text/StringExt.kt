package me.li2.android.common.text

import android.util.Patterns

/**
 * @return true if the string is valid email format
 */
fun String.isEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()
