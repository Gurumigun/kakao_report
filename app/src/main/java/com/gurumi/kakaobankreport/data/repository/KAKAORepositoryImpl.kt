package com.gurumi.kakaobankreport.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gurumi.kakaobankreport.data.KAKAOApis
import com.gurumi.kakaobankreport.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response
import javax.inject.Inject

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */

class KAKAORepositoryImpl @Inject constructor(
    private val apiService: KAKAOApis
) : KAKAORepository {
    override suspend fun getSearchImage(searchRequest: SearchRequest): Flow<APIResult> = safeApiFlow {
        apiService.searchImages(searchRequest.query, searchRequest.sort, searchRequest.page, searchRequest.size)
    }

    override suspend fun getSearchVideo(searchRequest: SearchRequest): Flow<APIResult> = safeApiFlow {
        apiService.searchVideos(searchRequest.query, searchRequest.sort, searchRequest.page, searchRequest.size)
    }

    override fun getSearchMedia(searchRequest: SearchRequest): Flow<PagingData<MediaItem>> {
        val pagingSourceFactory = {
            MediaPagingSource(this, searchRequest)
        }

        return Pager(
            config = PagingConfig(
                pageSize = searchRequest.size,
                enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}