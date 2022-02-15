package com.example.androidschool.andersencoursework.ui.core.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.IncludeDefaultLoadStateBinding

class DefaultErrorDelegateAdapter(
    private val onTryAgain: () -> Unit
): DelegateAdapter<DefaultRecyclerError, DefaultErrorDelegateAdapter.ViewHolder>(
    DefaultRecyclerError::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.include_default_load_state, parent, false)
        return ViewHolder(itemView)
    }

    override fun bindViewHolder(model: DefaultRecyclerError, viewHolder: ViewHolder) {
        viewHolder.bind(model)
    }

    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {

        val viewBinding = IncludeDefaultLoadStateBinding.bind(itemView)

        fun bind(item: DefaultRecyclerError) {
            with(viewBinding) {
                progressBar.visibility = View.GONE
                messageTextView.text = item.error.toString()
                tryAgainButton.setOnClickListener { onTryAgain.invoke() }
            }
        }
    }
}