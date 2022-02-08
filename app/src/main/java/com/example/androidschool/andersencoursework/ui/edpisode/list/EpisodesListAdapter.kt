package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidschool.andersencoursework.databinding.IncludeDefaultLoadStateBinding
import com.example.androidschool.andersencoursework.databinding.ListItemCharacterBinding
import com.example.androidschool.andersencoursework.ui.characters.models.ListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val ITEM_HOLDER = 0
private const val LOADING_HOLDER = 1
private const val ERROR_HOLDER = 2


class EpisodesListAdapter @AssistedInject constructor (
    private val glide: RequestManager,
    @Assisted("onClick") private val onClick: (id: Int) -> Unit,
    @Assisted("refresh") private val refresh: () -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataList: MutableList<ListItem> = mutableListOf()

    fun setList(newList: List<ListItem>) {
        val callback = Comparator(newList, dataList)
        val diff = DiffUtil.calculateDiff(callback)
        dataList = newList.toMutableList()
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val layout = when (viewType) {
            ITEM_HOLDER -> ListItemCharacterBinding.inflate(inflater, parent, false)
            else -> IncludeDefaultLoadStateBinding.inflate(inflater, parent, false)
        }

        return ItemHolder(layout.root , glide)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        when (holder) {
            is ItemHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return when (dataList[position]) {
            is ListItem.CharacterItem -> ITEM_HOLDER
            is ListItem.Loading -> LOADING_HOLDER
            is ListItem.Error -> ERROR_HOLDER
        }
    }

    inner class ItemHolder(
        private val view: View,
        private val glide: RequestManager
        ): RecyclerView.ViewHolder(view) {

        fun bind(item: ListItem) {
            when(item) {
                is ListItem.CharacterItem -> bindItem(item)
                is ListItem.Loading -> bindLoading(item)
                is ListItem.Error -> bindError(item)
            }
        }

        private fun bindItem(item: ListItem.CharacterItem) {
            val binding = ListItemCharacterBinding.bind(view)

            with(binding) {
                listItemCharacterName.text = item.character.name
                glide
                    .load(item.character.img)
                    .centerCrop()
                    .into((listItemCharacterImg))
            }

            view.setOnClickListener { onClick(item.character.charId) }
        }

        private fun bindLoading(item: ListItem.Loading) {
            val binding = IncludeDefaultLoadStateBinding.bind(view)

            with(binding) {
                progressBar.visibility = View.VISIBLE
                messageTextView.visibility = View.GONE
                tryAgainButton.visibility = View.GONE
            }
        }

        private fun bindError(item: ListItem.Error) {
            val binding = IncludeDefaultLoadStateBinding.bind(view)

            with(binding) {
                progressBar.visibility = View.GONE
                messageTextView.visibility = View.VISIBLE
                messageTextView.text = item.error.toString()
                tryAgainButton.visibility = View.VISIBLE

                tryAgainButton.setOnClickListener { refresh() }
            }
        }
    }

    private class Comparator(
        private val newList: List<ListItem>,
        private val oldList: List<ListItem>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            val areItems = oldItem is ListItem.CharacterItem && newItem is ListItem.CharacterItem
            val areLoadings = oldItem is ListItem.Loading && newItem is ListItem.Loading
            val areErrors = oldItem is ListItem.Error && newItem is ListItem.Error

            return areItems || areLoadings || areErrors
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onClick") onClick: (id: Int) -> Unit,
            @Assisted("refresh") refresh: () -> Unit
        ): EpisodesListAdapter
    }
}