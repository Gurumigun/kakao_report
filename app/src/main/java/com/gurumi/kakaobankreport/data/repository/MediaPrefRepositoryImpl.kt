package com.gurumi.kakaobankreport.data.repository

import android.content.Context
import com.gurumi.kakaobankreport.data.model.ImageRes
import com.gurumi.kakaobankreport.data.model.MediaItem
import com.gurumi.kakaobankreport.data.model.MediaItemWithIndex
import com.gurumi.kakaobankreport.data.model.VideoRes
import com.gurumi.kakaobankreport.utils.PREF_NAME_KAKAO_MEDIA
import com.gurumi.kakaobankreport.utils.PreferenceWrapper
import com.gurumi.kakaobankreport.utils.get
import com.gurumi.kakaobankreport.utils.set
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */

class MediaPrefRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) : MediaPrefRepository {
    companion object {
        const val PREF_MY_BOOKMARK_LIST = "PREF_MY_BOOKMARK_LIST"
    }

    private val preferences by lazy {
        PreferenceWrapper(PREF_NAME_KAKAO_MEDIA).bind(context)
    }

    private val currentBookMarkList = mutableListOf<MediaItem>()

    init {
        preferences[PREF_MY_BOOKMARK_LIST, ""]?.let {
            if (it.isNotEmpty()) {
                json.decodeFromString<List<MediaItemWithIndex>>(it).forEach { item ->
                    if (item.isImage) {
                        currentBookMarkList.add(json.decodeFromString<ImageRes>(item.item))
                    } else if (!item.isImage) {
                        currentBookMarkList.add(json.decodeFromString<VideoRes>(item.item))
                    }
                }
            }
        }
    }

    override suspend fun isMediaBookmark(item: MediaItem): Boolean =
        currentBookMarkList.any { it.thumbnail == item.thumbnail }

    override suspend fun insertBookmark(item: MediaItem): Boolean = if (isMediaBookmark(item).not()) {
        currentBookMarkList.add(item)
        updateBookMarkList()
        true
    } else {
        false
    }

    override suspend fun deleteBookmark(item: MediaItem): Boolean = if (isMediaBookmark(item)) {
        currentBookMarkList.removeIf { it.thumbnail == item.thumbnail }
        updateBookMarkList()
        true
    } else {
        false
    }

    override suspend fun getMyBookmarkList(): List<MediaItem> = currentBookMarkList

    private fun updateBookMarkList() {
        currentBookMarkList.mapIndexed { index, mediaItem ->
            MediaItemWithIndex(index, mediaItem is ImageRes, when (mediaItem) {
                is ImageRes -> {
                    json.encodeToString(mediaItem)
                }
                is VideoRes -> {
                    json.encodeToString(mediaItem)
                }
                else -> ""
            }
            )
        }.let {
            preferences[PREF_MY_BOOKMARK_LIST] = json.encodeToString(it)
        }
    }
}