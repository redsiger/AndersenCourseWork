package com.example.androidschool.andersencoursework.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.ListItemSearchCharacterBinding
import com.example.androidschool.andersencoursework.ui.core.recycler.DelegateAdapter
import com.example.androidschool.andersencoursework.ui.core.recycler.ListItemUI
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CharacterSearchDelegateAdapter @AssistedInject constructor(
    @Assisted("onItemClick")
    private val onItemClick: (id: Int) -> Unit,
    private val glide: RequestManager
) : DelegateAdapter<ListItemUI.CharacterListItemUI, CharacterSearchDelegateAdapter.ViewHolder>(
    ListItemUI.CharacterListItemUI::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_search_character, parent, false)
        return ViewHolder(itemView)
    }

    override fun bindViewHolder(item: ListItemUI.CharacterListItemUI, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val viewBinding = ListItemSearchCharacterBinding.bind(itemView)

        fun bind(item: ListItemUI.CharacterListItemUI) {
            with(viewBinding) {
                listItemSearchCharacterName.text = item.name
                listItemSearchCharacterNickname.text = item.nickname
                glide
                    .load(item.img)
                    .centerCrop()
                    .placeholder(R.drawable.splashscreen)
                    .into(listItemSearchCharacterImg)
            }
            itemView.setOnClickListener { onItemClick.invoke(item.charId) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onItemClick") onItemClick: (id: Int) -> Unit
        ): CharacterSearchDelegateAdapter
    }
}