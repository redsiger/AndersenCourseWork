package com.example.androidschool.andersencoursework.ui.characters.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidschool.andersencoursework.databinding.IncludeDefaultLoadStateBinding
import com.example.androidschool.andersencoursework.databinding.ListItemCharacterBinding
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.andersencoursework.ui.characters.models.ListItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val ITEM_HOLDER = 0
private const val LOADING_HOLDER = 1
private const val ERROR_HOLDER = 2

class CharactersListPagingAdapter @AssistedInject constructor (
    private val glide: RequestManager,
    @Assisted("onClick") private val onClick: (id: Int) -> Unit,
    @Assisted("refresh") private val refresh: () -> Unit
): ListAdapter<ListItem<CharacterUIEntity>, RecyclerView.ViewHolder>(ListItemDiff) {

    companion object ListItemDiff: DiffUtil.ItemCallback<ListItem<CharacterUIEntity>>() {
        override fun areItemsTheSame(oldItem: ListItem<CharacterUIEntity>, newItem: ListItem<CharacterUIEntity>): Boolean {
            return if (oldItem is ListItem.Item && newItem is ListItem.Item) {
                oldItem.character.charId == newItem.character.charId
            } else false
        }
        override fun areContentsTheSame(oldItem: ListItem<CharacterUIEntity>, newItem: ListItem<CharacterUIEntity>): Boolean {
            return if (oldItem is ListItem.Item && newItem is ListItem.Item) {
                oldItem.character == newItem.character
            } else false
        }
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
        val item = currentList[position]
        when (holder) {
            is ItemHolder -> holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is ListItem.Item -> ITEM_HOLDER
            is ListItem.Loading -> LOADING_HOLDER
            is ListItem.Error -> ERROR_HOLDER
        }
    }

    inner class ItemHolder(
        private val view: View,
        private val glide: RequestManager
        ): RecyclerView.ViewHolder(view) {

        fun bind(item: ListItem<CharacterUIEntity>) {
            when(item) {
                is ListItem.Item -> bindItem(item)
                is ListItem.Loading -> bindLoading(item)
                is ListItem.Error -> bindError(item)
            }
        }

        private fun bindItem(item: ListItem.Item<CharacterUIEntity>) {
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

        private fun bindLoading(item: ListItem.Loading<CharacterUIEntity>) {
            val binding = IncludeDefaultLoadStateBinding.bind(view)

            with(binding) {
                progressBar.visibility = View.VISIBLE
                messageTextView.visibility = View.GONE
                tryAgainButton.visibility = View.GONE
            }
        }

        private fun bindError(item: ListItem.Error<CharacterUIEntity>) {
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

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onClick") onClick: (id: Int) -> Unit,
            @Assisted("refresh") refresh: () -> Unit
        ): CharactersListPagingAdapter
    }
}