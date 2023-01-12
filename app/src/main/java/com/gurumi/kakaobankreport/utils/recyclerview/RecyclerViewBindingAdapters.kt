package com.gurumi.kakaobankreport.utils.recyclerview

import androidx.databinding.BindingAdapter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.gurumi.kakaobankreport.utils.findLifecycleOwner
import com.gurumi.kakaobankreport.utils.logW
import com.gurumi.kakaobankreport.utils.recyclerview.state.ListRecyclerViewState
import com.gurumi.kakaobankreport.utils.recyclerview.state.PagingRecyclerViewState

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */

@BindingAdapter(
    value = ["paging_recycler_view_state", "paging_recycler_lifecycle_owner"],
    requireAll = false
)
fun <T: RecyclerViewBindingModel> RecyclerView.setState(
    viewState: PagingRecyclerViewState<T>?,
    lifecycleOwner: LifecycleOwner? = null
) {
    "paging_recycler_view_state is Init ${adapter == null}!!".logW()
    if (viewState == null) {
        adapter = null
        return
    }
    val owner = lifecycleOwner ?: findLifecycleOwner() ?: throw IllegalStateException("lifecycleOwner is Null")
    val adapter = CustomPagingAdapter(viewState.diffUtil)
    owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            viewState.setAdapterDependency(null)
            this@setState.adapter = null
        }
    })
    adapter.addLoadStateListener {
        viewState.loadState.value = it.refresh
    }
    viewState.setAdapterDependency(adapter)
    this.adapter = adapter
}

@BindingAdapter(
    value = ["list_recycler_view_state", "list_recycler_has_stable_id", "list_recycler_lifecycle_owner"],
    requireAll = false
)
fun <T : RecyclerViewBindingModel> RecyclerView.setState(
    viewState: ListRecyclerViewState<T>?,
    hasStableId: Boolean = false,
    lifecycleOwner: LifecycleOwner? = null
) {
    if (viewState == null) {
        adapter = null
        return
    }
    val owner = lifecycleOwner ?: findLifecycleOwner()
        ?: throw IllegalStateException("lifecycleOwner is Null")
    val adapter = CustomListAdapter(viewState.diffUtil, owner)
        .apply { setHasStableIds(hasStableId) }
    owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            viewState.setAdapterDependency(null)
            this@setState.adapter = null
        }
    })
    viewState.setAdapterDependency(adapter)
    this.adapter = adapter
}