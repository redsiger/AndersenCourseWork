package com.example.androidschool.andersencoursework.ui.core.recycler

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException

class CompositeAdapter(
    private val delegates: SparseArray<DelegateAdapter<DiffComparable, RecyclerView.ViewHolder>>
): ListAdapter<DiffComparable, RecyclerView.ViewHolder>(ListItemDiff()) {

    override fun getItemViewType(position: Int): Int {
        for (i in 0 until delegates.size()) {
            if (delegates[i].modelClass == getItem(position).javaClass)
                return delegates.keyAt(i)
        }
        throw IllegalArgumentException("Illegal item viewType: ${getItem(position)}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegates[viewType].createViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val delegateAdapter = delegates[getItemViewType(position)]

        if (delegateAdapter != null) {
            delegateAdapter.bindViewHolder(getItem(position), holder)
        } else throw IllegalArgumentException("Illegal item viewType: ${getItem(position)}")
    }

    class Builder {

        private var count: Int = 0
        private val delegates: SparseArray<DelegateAdapter<DiffComparable, RecyclerView.ViewHolder>> = SparseArray()

        fun add(delegateAdapter: DelegateAdapter<out DiffComparable, *>): Builder {
            delegates.put(
                count++,
                delegateAdapter as DelegateAdapter<DiffComparable, RecyclerView.ViewHolder>
            )
            return this
        }

        fun build(): CompositeAdapter {
            require(count != 0) {"Register at least one adapter"}
            return CompositeAdapter(delegates)
        }
    }

    // Diff
    class ListItemDiff<T: DiffComparable>: DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return if (oldItem::class.java == newItem::class.java) {
                oldItem.areItemsTheSame(newItem)
            } else false
        }
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.areContentsTheSame(newItem)
        }
    }
}