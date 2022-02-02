package com.example.androidschool.andersencoursework.ui.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidschool.andersencoursework.databinding.IncludeDefaultLoadStateBinding

class DefaultLoadStateAdapter: LoadStateAdapter<DefaultLoadStateAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = IncludeDefaultLoadStateBinding.inflate(inflater, parent, false)
        return Holder(binding, null)
    }

    class Holder(
        private val binding: IncludeDefaultLoadStateBinding,
        private val swipeRefreshLayout: SwipeRefreshLayout?
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) = with(binding) {
                messageTextView.isVisible = loadState is LoadState.Error
                tryAgainButton.isVisible = loadState is LoadState.Error
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.isRefreshing = loadState is LoadState.Loading
                    progressBar.isVisible = false
                } else {
                    progressBar.isVisible = loadState is LoadState.Loading
                }
            }
    }
}