package com.gurumi.kakaobankreport.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
@Serializable
data class Meta(
    @SerialName("is_end") val isEnd: Boolean,
    @SerialName("pageable_count") val pageableCount: Int,
    @SerialName("total_count") val totalCount: Int
)

@Serializable
data class KAKAOBaseResponse<T>(
    @SerialName("documents") val documents: List<T>? = null,
    @SerialName("meta") val meta: Meta? = null,
    @SerialName("errorType") val errorType: String? = null,
    @SerialName("message") val message: String? = null
)

@Serializable
data class ImageRes(
    @SerialName("collection") val collection: String,
    @SerialName("display_sitename") val displaySiteName: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int
) : MediaItem()

@Serializable
data class VideoRes(
    @SerialName("title") val title: String,
    @SerialName("play_time") val playTime: Int,
    @SerialName("author") val author: String
) : MediaItem()

@Serializable
open class MediaItem(
    @SerialName("datetime") val dateTime: String = "",
    @JsonNames("thumbnail_url") @SerialName("thumbnail") val thumbnail: String = "",
    @JsonNames("doc_url") @SerialName("url") val url: String = ""
)
