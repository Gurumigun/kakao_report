package com.gurumi.kakaobankreport.utils

import android.util.Log
import com.gurumi.kakaobankreport.BuildConfig

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */

const val TAG = "KAKAO_KIY"

fun Any.logD(prefix: String = "") {
    if (BuildConfig.DEBUG) {
        Log.d(TAG, "[$prefix] $this")
    }
}

fun Any.logE(prefix: String = "") {
    if (BuildConfig.DEBUG) {
        Log.e(TAG, "[$prefix] $this")
    }
}

fun Any.logW(prefix: String = "") {
    if (BuildConfig.DEBUG) {
        Log.w(TAG, "[$prefix] $this")
    }
}

fun Any.logI(prefix: String = "") {
    if (BuildConfig.DEBUG) {
        Log.i(TAG, "[$prefix] $this")
    }
}