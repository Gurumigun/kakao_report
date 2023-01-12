package com.gurumi.kakaobankreport.utils

import android.content.Context
import android.content.SharedPreferences

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/18
 */
class PreferenceWrapper(private val name: String = "") {
    private val mode: Int = Context.MODE_PRIVATE

    private var preferences: SharedPreferences? = null

    fun bind(context: Context): PreferenceWrapper {
        if (preferences == null) {
            preferences = context.applicationContext.getSharedPreferences(name, mode)
        }

        return this
    }

    internal fun getPreferenceOrThrow(): SharedPreferences = preferences ?: throw Error("Context is null!!")
}
