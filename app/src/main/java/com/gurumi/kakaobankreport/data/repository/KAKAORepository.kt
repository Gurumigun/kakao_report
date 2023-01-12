package com.gurumi.kakaobankreport.data.repository

import androidx.paging.PagingData
import com.gurumi.kakaobankreport.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
interface KAKAORepository {
    suspend fun getSearchImage(searchRequest: SearchRequest): Flow<APIResult<KAKAOBaseResponse<ImageRes>>>

    suspend fun getSearchVideo(searchRequest: SearchRequest): Flow<APIResult<KAKAOBaseResponse<VideoRes>>>

    fun getSearchMedia(searchRequest: SearchRequest): Flow<PagingData<MediaItem>>
}