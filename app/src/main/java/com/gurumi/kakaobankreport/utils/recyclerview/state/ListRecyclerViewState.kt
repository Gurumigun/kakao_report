package com.gurumi.kakaobankreport.utils.recyclerview.state

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.DiffUtil
import com.gurumi.kakaobankreport.utils.recyclerview.ListAdapterInterface
import com.gurumi.kakaobankreport.utils.recyclerview.RecyclerViewBindingModel

/**
 *
 * Create : Kwon IkYoung
 * Date : 2022/12/14
 */
class ListRecyclerViewState<ITEM : RecyclerViewBindingModel>(val diffUtil: DiffUtil.ItemCallback<ITEM>)  {
    private var currentItems: List<ITEM>? = null
    private val handler = Handler(Looper.getMainLooper())

    var adapterDependency: ListAdapterInterface<ITEM>? = null
        private set

    fun getCurrentItems(): List<ITEM> = currentItems ?: listOf()

    fun setHasStableIds(enable: Boolean) {
        handler.post {
            adapterDependency?.setHasStableIds(enable)
        }
    }

    fun notifyDataSetChanged() {
        handler.post {
            val adapter = adapterDependency ?: return@post
            adapter.notifyDataSetChanged()
        }
    }

    fun notifyItemChanged(position: Int, payload: Any?) {
        handler.post {
            val adapter = adapterDependency ?: return@post
            adapter.notifyItemChanged(position, payload)
        }
    }

    fun notifyItemInserted(position: Int) {
        handler.post {
            val adapter = adapterDependency ?: return@post
            adapter.notifyItemInserted(position)
        }
    }

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payloads: Any?) {
        handler.post {
            val adapter = adapterDependency ?: return@post
            adapter.notifyItemRangeChanged(positionStart, itemCount, payloads)
        }
    }

    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        handler.post {
            val adapter = adapterDependency ?: return@post
            adapter.notifyItemRangeInserted(positionStart, itemCount)
        }
    }

    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        handler.post {
            val adapter = adapterDependency ?: return@post
            adapter.notifyItemRangeRemoved(positionStart, itemCount)
        }
    }

    fun notifyItemRemoved(position: Int) {
        handler.post {
            val adapter = adapterDependency ?: return@post
            adapter.notifyItemRemoved(position)
        }
    }

    fun submitList(list: List<ITEM>?, commitCallback: Runnable? = null) {
        handler.post {
            currentItems = list
            adapterDependency?.submitList(list, commitCallback)
        }
    }

    fun setAdapterDependency(adapterDependency: ListAdapterInterface<ITEM>?) {
        handler.post {
            adapterDependency?.submitList(currentItems)
            this.adapterDependency = adapterDependency
        }
    }
}