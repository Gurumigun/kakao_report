package com.gurumi.kakaobankreport.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gurumi.kakaobankreport.R
import com.gurumi.kakaobankreport.databinding.FragmentSearchMediaBinding
import com.gurumi.kakaobankreport.ui.base.BaseFragment
import com.gurumi.kakaobankreport.ui.viewmodel.SearchMediaViewModel
import com.gurumi.kakaobankreport.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/15
 */
@AndroidEntryPoint
class SearchMediaFragment : BaseFragment<FragmentSearchMediaBinding>() {
    private val binding: FragmentSearchMediaBinding
        get() = _binding!!

    private val searchMediaViewModel by viewModels<SearchMediaViewModel>()

    private val spanCount: Int
        get() = requireContext().let {
            return@let (it.displayWidth() / it.dpToPx(200)).toInt()
        }

    override fun getRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSearchMediaBinding.inflate(inflater, container, false).root

    override fun initViewCreate() {
        super.initViewCreate()
        with(binding) {
            viewModel = searchMediaViewModel
            inputSearchMedia.doAfterTextChanged {
                val inputText = it?.toString() ?: return@doAfterTextChanged
                searchMediaViewModel.requestSearchMedia(inputText)
//                repeatOnStarted {
//                    searchMediaViewModel.searchFlow.collectLatest {
//                        it.logD()
//                    }
//                }
            }
        }
        repeatOnStarted {
            requireActivity().windowLayoutInfo {
                (binding.recyclerView.layoutManager as? GridLayoutManager)?.spanCount =
                    maxOf(spanCount, 2)
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        lifecycleScope.launch {
            searchMediaViewModel.onBookMarkClick.collectLatest {
                val (isBookMark, item) = it
                item?.let {
                    Snackbar.make(
                        binding.root, getString(R.string.update_bookmark_result).format(
                            if (isBookMark) getString(R.string.add) else getString(R.string.remove)
                        ), Snackbar.LENGTH_SHORT
                    ).apply {
                        if (isBookMark) {
                            this.setAction(getString(R.string.move_to_bookmark)) {
                                (this@SearchMediaFragment.requireActivity() as? IBottomNavController)?.moveToBookmark()
                            }
                        }
                    }.show()
                }
            }
        }
    }
}