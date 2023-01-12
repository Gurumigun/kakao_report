package com.gurumi.kakaobankreport.data.repository

import com.gurumi.kakaobankreport.data.model.MediaItem

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
interface MediaPrefRepository {
    suspend fun isMediaBookmark(item: MediaItem): Boolean

    suspend fun insertBookmark(item: MediaItem): Boolean

    suspend fun deleteBookmark(item: MediaItem): Boolean

    suspend fun getMyBookmarkList() : List<MediaItem>
}