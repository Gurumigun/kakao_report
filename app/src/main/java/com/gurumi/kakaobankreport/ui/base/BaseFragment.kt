package com.gurumi.kakaobankreport.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
abstract class BaseFragment<BINDING : ViewDataBinding>() : Fragment() {
    protected var _binding: BINDING? = null

    abstract fun getRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getRootView(inflater, container , savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind<BINDING>(view)?.also { binding ->
            binding.lifecycleOwner = this@BaseFragment.viewLifecycleOwner
        }
        initViewCreate()
        initViewModel()
    }

    protected open fun initOnCreateView() {}
    protected open fun initViewCreate() {}
    protected open fun initViewModel() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}