package me.li2.android.common.sdcard

import android.content.Intent

enum class SdcardState(val action: String?) {
    UNKNOWN(null),
    MOUNTED(Intent.ACTION_MEDIA_MOUNTED),
    EJECT(Intent.ACTION_MEDIA_EJECT),
    UNMOUNTED(Intent.ACTION_MEDIA_UNMOUNTED),
    BAD_REMOVAL(Intent.ACTION_MEDIA_BAD_REMOVAL);

    fun isEject() = this == EJECT

    companion object {
        private val map = enumValues<SdcardState>().associateBy(SdcardState::action)

        val intentActions: List<String> = enumValues<SdcardState>().mapNotNull { it.action }

        fun from(action: String?): SdcardState = map[action] ?: UNKNOWN
    }
}