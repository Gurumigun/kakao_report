package com.gurumi.kakaobankreport.utils.recyclerview.diffutil

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.gurumi.kakaobankreport.utils.recyclerview.RecyclerViewBindingModel

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/18
 */
class HashCodeDiffUtils : DiffUtil.ItemCallback<RecyclerViewBindingModel>() {
    override fun areItemsTheSame(
        oldItem: RecyclerViewBindingModel,
        newItem: RecyclerViewBindingModel
    ): Boolean = (oldItem.hashCode() == newItem.hashCode())

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: RecyclerViewBindingModel,
        newItem: RecyclerViewBindingModel
    ): Boolean = (oldItem == newItem)
}