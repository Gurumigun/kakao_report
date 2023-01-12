package com.gurumi.kakaobankreport.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gurumi.kakaobankreport.data.model.ImageRes
import com.gurumi.kakaobankreport.data.model.MediaItem
import com.gurumi.kakaobankreport.data.model.VideoRes
import com.gurumi.kakaobankreport.data.repository.MediaPrefRepository
import com.gurumi.kakaobankreport.ui.uimodel.EmptyTypeUIModel
import com.gurumi.kakaobankreport.ui.uimodel.ImageUIModel
import com.gurumi.kakaobankreport.ui.uimodel.MediaUIMode
import com.gurumi.kakaobankreport.ui.uimodel.VideoUIModel
import com.gurumi.kakaobankreport.utils.recyclerview.diffutil.HashCodeDiffUtils
import com.gurumi.kakaobankreport.utils.recyclerview.state.ListRecyclerViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val preferenceRepository: MediaPrefRepository
) : ViewModel() {
    val currentBookMarkList = ListRecyclerViewState(HashCodeDiffUtils())

    private val _onRemoveBookMarkItem = MutableSharedFlow<MediaItem?>(0)
    val onRemoveBookMarkItem: SharedFlow<MediaItem?> = _onRemoveBookMarkItem

    fun getBookMarkItems() {
        viewModelScope.launch {
            preferenceRepository.getMyBookmarkList().map { mediaItem ->
                when (mediaItem) {
                    is ImageRes -> ImageUIModel(mediaItem, true) {
                        showRemoveMedia(it.data)
                    }
                    is VideoRes -> VideoUIModel(mediaItem, true) {
                        showRemoveMedia(it.data)
                    }
                    else -> EmptyTypeUIModel()
                }
            }.let {
                currentBookMarkList.submitList(it)
            }
        }
    }

    private fun showRemoveMedia(item: MediaItem) {
        viewModelScope.launch {
            _onRemoveBookMarkItem.emit(item)
        }
    }

    fun removeBookMarkItem(deleteItem: MediaItem) {
        viewModelScope.launch {
            currentBookMarkList
                .getCurrentItems()
                .indexOfFirst { (it as? MediaUIMode<*>)?.data?.url == deleteItem.url }
                .let {
                    if (it in (0..currentBookMarkList.getCurrentItems().size)) {
                        currentBookMarkList.getCurrentItems().toMutableList().let { newList ->
                            if (preferenceRepository.deleteBookmark(deleteItem)) {
                                newList.removeAt(it)
                                currentBookMarkList.submitList(newList)
                            }
                        }
                    }
                }
        }
    }
}