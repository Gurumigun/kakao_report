package com.gurumi.kakaobankreport.utils

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.paging.LoadState
import coil.load

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */

@BindingAdapter("loadImgWithUrl")
fun ImageView.loadUrl(url: String?) {
    url ?: return
    load(url)
}

@BindingAdapter("changeSelected")
fun View.changeSelected(isSelected: Boolean) {
    this.isSelected = isSelected
}

@BindingAdapter("bindIsVisibleOrGone")
fun View.bindIsVisibleOrGone(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("bindThrowShowToast")
fun View.bindThrowShowToast(state: LoadState?) {
    if (state is LoadState.Error) {
        state.error.message?.let {
            showToast(it)
        }
    }
}

@BindingAdapter("showToast")
fun View.showToast(message: String?) {
    message?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
}