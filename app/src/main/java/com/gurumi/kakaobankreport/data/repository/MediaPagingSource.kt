package com.gurumi.kakaobankreport.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gurumi.kakaobankreport.data.model.*
import com.gurumi.kakaobankreport.utils.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/15
 */
class MediaPagingSource(
    private val api: KAKAORepository,
    private val searchRequest: SearchRequest
) : PagingSource<Int, MediaItem>() {
    private var isImageEndPage = false
    private var isVideoEndPage = false

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItem> {
        try {
            val page = params.key ?: NETWORK_START_PAGE_INDEX
            val prevKey = if (page == NETWORK_START_PAGE_INDEX) {
                isImageEndPage = false
                isVideoEndPage = false
                null
            } else page - 1
            var nextKey: Int? = null
            var result: LoadResult<Int, MediaItem> =
                LoadResult.Page(data = listOf(), prevKey = prevKey, nextKey = nextKey)

            val nextRequest = searchRequest.apply {
                this.page = page
            }

            val imageFlow = if (isImageEndPage)
                flow {
                    APIResult.Failure(
                        10,
                        "",
                        ""
                    )
                } else api.getSearchImage(nextRequest).handlingError()
            val videoFlow = if (isVideoEndPage) flow {
                APIResult.Failure(
                    10,
                    "",
                    ""
                )
            } else api.getSearchVideo(nextRequest).handlingError()

            imageFlow.zip(videoFlow) { image, video ->
                val newList = mutableListOf<MediaItem>()
                isImageEndPage = when (image) {
                    is APIResult.Success -> {
                        image.data.documents?.let { newList.addAll(it) }
                        image.data.meta?.isEnd == true
                    }
                    is APIResult.NetworkError -> {
                        throw image.error
                    }
                    else -> {
                        true
                    }
                }

                isVideoEndPage = when (video) {
                    is APIResult.Success -> {
                        video.data.documents?.let { newList.addAll(it) }
                        video.data.meta?.isEnd == true
                    }
                    is APIResult.NetworkError -> {
                        throw video.error
                    }
                    else -> {
                        true
                    }
                }

                nextKey = if (isImageEndPage && isVideoEndPage) {
                    null
                } else {
                    page + (params.loadSize / DEFAULT_PAGE_SIZE)
                }
                newList.sortByDescending {
                    it.dateTime.convertTimeStringToDate()?.time ?: Long.MAX_VALUE
                }
                return@zip newList
            }.collectLatest {
                result = LoadResult.Page(data = it, prevKey = prevKey, nextKey = nextKey)
            }
            return result
        } catch (ex: Exception) {
            return LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MediaItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

}