package com.gurumi.kakaobankreport.utils

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */


fun View.findFragmentOrNull(): Fragment? {
    return try {
        findFragment()
    } catch (e: IllegalStateException) {
        null
    }
}

fun View.findActivityOrNull(): ComponentActivity? {
    var context: Context = context
    while (context is ContextWrapper) {
        if (context is ComponentActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun View.findLifecycleOwner(): LifecycleOwner? {
    return this.findFragmentOrNull()?.viewLifecycleOwner ?: findActivityOrNull()
}

fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}

fun <T> Flow<T>.handlingError(): Flow<T> = catch { e ->
    throw e
}

fun LoadState?.isLoading(): Boolean = this is LoadState.Loading

fun String.convertTimeStringToDate(): Date? = SimpleDateFormat(KAKAO_TIME_STRING_FORMAT, Locale.KOREAN).parse(this)

fun String.convertTimeToWithMin(): String = convertTimeStringToDate()?.let {
    SimpleDateFormat("yyyy.MM.dd HH:MM", Locale.KOREAN).format(it)
} ?: ""

fun Context.dpToPx(dp: Int): Float {
    return dp * resources.displayMetrics.density
}

fun Context.displayWidth(): Int = resources.displayMetrics.widthPixels

suspend fun ComponentActivity.windowLayoutInfo(block: (WindowLayoutInfo) -> Unit) {
    WindowInfoTracker.getOrCreate(this)
        .windowLayoutInfo(this)
        .collect { newLayoutInfo ->
            block(newLayoutInfo)
        }
}