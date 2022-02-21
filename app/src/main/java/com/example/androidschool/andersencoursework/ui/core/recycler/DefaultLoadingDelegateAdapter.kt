package com.example.androidschool.andersencoursework.ui.core.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.IncludeDefaultLoadStateBinding

class DefaultLoadingDelegateAdapter
    :DelegateAdapter<DefaultRecyclerLoading, DefaultLoadingDelegateAdapter.ViewHolder>(DefaultRecyclerLoading::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.include_default_load_state, parent, false)
        return ViewHolder(itemView)
    }

    override fun bindViewHolder(item: DefaultRecyclerLoading, viewHolder: ViewHolder) {
        viewHolder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val viewBinding = IncludeDefaultLoadStateBinding.bind(itemView)

        fun bind() {
            with(viewBinding) {
                messageTextView.visibility = View.GONE
                tryAgainButton.visibility = View.GONE
            }
        }
    }
}