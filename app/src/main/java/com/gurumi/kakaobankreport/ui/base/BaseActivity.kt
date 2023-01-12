package com.gurumi.kakaobankreport.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
abstract class BaseActivity<BINDING: ViewDataBinding>(@LayoutRes private val layoutId: Int): AppCompatActivity() {
    protected var _binding: BINDING? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView<BINDING>(this@BaseActivity, layoutId).apply {
            lifecycleOwner = this@BaseActivity
        }
        initView()
        initViewModel()
    }

    protected open fun initView() {}
    protected open fun initViewModel() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}