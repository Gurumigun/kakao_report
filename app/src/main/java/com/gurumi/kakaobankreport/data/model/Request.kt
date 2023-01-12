package com.gurumi.kakaobankreport.data.model

import com.gurumi.kakaobankreport.utils.DEFAULT_PAGE_SIZE
import com.gurumi.kakaobankreport.utils.DEFAULT_SORT_TYPE
import com.gurumi.kakaobankreport.utils.NETWORK_START_PAGE_INDEX

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */

data class SearchRequest(
    val query: String,
    val sort: String = DEFAULT_SORT_TYPE,
    var page: Int = NETWORK_START_PAGE_INDEX,
    val size: Int = DEFAULT_PAGE_SIZE
)