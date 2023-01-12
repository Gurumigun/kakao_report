package com.gurumi.kakaobankreport.data.model

import kotlinx.serialization.Serializable

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/18
 */

@Serializable
data class MediaItemWithIndex(
    val index: Int,
    val isImage: Boolean,
    val item: String)