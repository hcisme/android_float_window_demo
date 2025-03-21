package com.chc.floatservice.utils

import android.content.Context
import androidx.core.content.edit

object PreferenceManager {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_FIRST_OPEN = "has_launched_before"

    // 获取是否是第一次打开
    fun isFirstLaunch(context: Context, key: String = KEY_FIRST_OPEN): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return !prefs.contains(key)
    }

    // 设置已完成首次启动标记
    fun setFirstLaunchComplete(context: Context, key: String = KEY_FIRST_OPEN) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putBoolean(key, true)
            }
    }
}