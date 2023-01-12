package com.gurumi.kakaobankreport.utils.recyclerview

import androidx.annotation.LayoutRes

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
interface RecyclerViewBindingModel {
    @LayoutRes
    fun layoutId(): Int
    fun bindingVariableId(): Int? = null
    fun requireExecutePendingBindings(): Boolean = false
    fun itemId(): Long? = null
}