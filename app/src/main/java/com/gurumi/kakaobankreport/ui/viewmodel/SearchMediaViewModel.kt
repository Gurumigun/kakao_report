package com.gurumi.kakaobankreport.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.gurumi.kakaobankreport.data.model.ImageRes
import com.gurumi.kakaobankreport.data.model.MediaItem
import com.gurumi.kakaobankreport.data.model.SearchRequest
import com.gurumi.kakaobankreport.data.model.VideoRes
import com.gurumi.kakaobankreport.data.repository.KAKAORepository
import com.gurumi.kakaobankreport.data.repository.MediaPrefRepository
import com.gurumi.kakaobankreport.ui.uimodel.EmptyTypeUIModel
import com.gurumi.kakaobankreport.ui.uimodel.ImageUIModel
import com.gurumi.kakaobankreport.ui.uimodel.MediaUIMode
import com.gurumi.kakaobankreport.ui.uimodel.VideoUIModel
import com.gurumi.kakaobankreport.utils.logD
import com.gurumi.kakaobankreport.utils.recyclerview.diffutil.HashCodeDiffUtils
import com.gurumi.kakaobankreport.utils.recyclerview.state.PagingRecyclerViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
private const val SAVE_STATE_QUERY = "SAVE_STATE_QUERY"

@HiltViewModel
class SearchMediaViewModel @Inject constructor(
    private val repository: KAKAORepository,
    private val preferenceRepository : MediaPrefRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val pagingViewState = PagingRecyclerViewState(viewModelScope, HashCodeDiffUtils())

    private var query: String = savedStateHandle[SAVE_STATE_QUERY] ?: ""
        set(value) {
            savedStateHandle[SAVE_STATE_QUERY] = value
            field = value
        }

    private val _onBookMarkClick = MutableSharedFlow<Pair<Boolean, MediaItem?>>(0)
    val onBookMarkClick: SharedFlow<Pair<Boolean, MediaItem?>> = _onBookMarkClick

//    val searchFlow = flow {
//        val params = SearchRequest(query)
//        emit(repository.getSearchMedia(params).cachedIn(viewModelScope))
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.Eagerly,
//        initialValue = PagingData.empty<MediaItem>()
//    )

    fun requestSearchMedia(newQuery: String) {
        fun onClickBookMark(item: MediaUIMode<MediaItem>) {
            viewModelScope.launch {
                val isBookmark = if (preferenceRepository.isMediaBookmark(item.data).not()) {
                    preferenceRepository.insertBookmark(item.data)
                    true
                } else {
                    preferenceRepository.deleteBookmark(item.data)
                    false
                }
                _onBookMarkClick.emit(Pair(isBookmark, item.data))
                item.isBookmark.value = isBookmark
            }
        }

        if (query == newQuery) {
            if (newQuery.isEmpty()) {
                query = newQuery
            }
            return
        }
        query = newQuery
        viewModelScope.launch {
            val params = SearchRequest(query)
            repository.getSearchMedia(params)
                .cachedIn(this)
                .collectLatest { item ->
                    pagingViewState.collectLatest(item.map {
                        val isBookmarkItem = preferenceRepository.isMediaBookmark(it)
                        "PagerViewState $isBookmarkItem".logD()
                        when (it) {
                            is ImageRes -> ImageUIModel(it, isBookmarkItem) {
                                onClickBookMark(it as MediaUIMode<MediaItem>)
                            }
                            is VideoRes -> VideoUIModel(it, isBookmarkItem ) {
                                onClickBookMark(it as MediaUIMode<MediaItem>)
                            }
                            else -> EmptyTypeUIModel()
                        }
                    })
                }
        }
    }
}