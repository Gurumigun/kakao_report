package com.gurumi.kakaobankreport.ui.uimodel

import com.gurumi.kakaobankreport.BR
import com.gurumi.kakaobankreport.R
import com.gurumi.kakaobankreport.data.model.ImageRes
import com.gurumi.kakaobankreport.data.model.MediaItem
import com.gurumi.kakaobankreport.data.model.VideoRes
import com.gurumi.kakaobankreport.utils.convertTimeToWithMin
import com.gurumi.kakaobankreport.utils.recyclerview.RecyclerViewBindingModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */

abstract class MediaUIMode<T : MediaItem>(
    val data: T,
    isBookmark: Boolean = false
) : RecyclerViewBindingModel {
    val isBookmark = MutableStateFlow(false)
    val displayDate = data.dateTime.convertTimeToWithMin()

    init {
        this.isBookmark.value = isBookmark
    }
}

class ImageUIModel(
    imageData: ImageRes,
    isBookmark: Boolean = false,
    val onClickBookMark: (ImageUIModel) -> Unit
) : MediaUIMode<ImageRes>(imageData, isBookmark) {
    override fun layoutId(): Int = R.layout.row_image
    override fun bindingVariableId(): Int = BR.model
}

class VideoUIModel(
    data: VideoRes,
    isBookmark: Boolean = false,
    val onClickBookMark: (VideoUIModel) -> Unit
) : MediaUIMode<VideoRes>(data, isBookmark) {
    override fun layoutId(): Int = R.layout.row_video
    override fun bindingVariableId(): Int = BR.model
}


class EmptyTypeUIModel() : RecyclerViewBindingModel {
    override fun layoutId(): Int = R.layout.row_empty
}
