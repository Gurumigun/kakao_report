package com.gurumi.kakaobankreport.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.gurumi.kakaobankreport.R
import com.gurumi.kakaobankreport.databinding.FragmentBookmarkBinding
import com.gurumi.kakaobankreport.ui.base.BaseFragment
import com.gurumi.kakaobankreport.ui.viewmodel.BookMarkViewModel
import com.gurumi.kakaobankreport.utils.displayWidth
import com.gurumi.kakaobankreport.utils.dpToPx
import com.gurumi.kakaobankreport.utils.repeatOnStarted
import com.gurumi.kakaobankreport.utils.windowLayoutInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/15
 */
@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>() {
    private val binding: FragmentBookmarkBinding
        get() = _binding!!

    private val bookMarkViewModel by viewModels<BookMarkViewModel>()

    private val spanCount: Int
        get() = requireContext().let {
            return@let (it.displayWidth() / it.dpToPx(200)).toInt()
        }

    override fun getRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBookmarkBinding.inflate(inflater, container, false).root

    override fun initViewCreate() {
        super.initViewCreate()
        binding.viewModel = bookMarkViewModel
        repeatOnStarted {
            requireActivity().windowLayoutInfo {
                (binding.bookmarkRecyclerView.layoutManager as? GridLayoutManager)?.spanCount = maxOf(spanCount, 2)
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        bookMarkViewModel.getBookMarkItems()
        repeatOnStarted {
            bookMarkViewModel.onRemoveBookMarkItem.collectLatest {
                it ?: return@collectLatest
                AlertDialog.Builder((context))
                    .setMessage(getString(R.string.popup_bookmark_remove))
                    .setPositiveButton(getString(R.string.submit)) { dialog, _ ->
                        dialog.dismiss()
                        bookMarkViewModel.removeBookMarkItem(it)
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }
}