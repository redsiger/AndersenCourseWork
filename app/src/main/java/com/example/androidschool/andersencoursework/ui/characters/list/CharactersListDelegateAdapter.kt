package com.example.androidschool.andersencoursework.ui.characters.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.ListItemCharacterBinding
import com.example.androidschool.andersencoursework.ui.core.recycler.DelegateAdapter
import com.example.androidschool.andersencoursework.ui.core.recycler.ListItemUI
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class CharactersListDelegateAdapter @AssistedInject constructor (
    private val glide: RequestManager,
    @Assisted("onItemClick") private val onItemClick: (id: Int) -> Unit
):
    DelegateAdapter<ListItemUI.CharacterListItemUI, CharactersListDelegateAdapter.ViewHolder>(
        ListItemUI.CharacterListItemUI::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_character, parent, false)
        return ViewHolder(itemView)
    }

    override fun bindViewHolder(item: ListItemUI.CharacterListItemUI, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val viewBinding = ListItemCharacterBinding.bind(itemView)

        fun bind(item: ListItemUI.CharacterListItemUI) {
            viewBinding.listItemCharacterName.text = item.name
            glide
                .load(item.img)
                .placeholder(R.drawable.splashscreen)
                .error(R.drawable.ic_person)
                .centerCrop()
                .into(viewBinding.listItemCharacterImg)

            itemView.setOnClickListener { onItemClick.invoke(item.charId) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onItemClick") onItemClick: (id: Int) -> Unit
        ): CharactersListDelegateAdapter
    }

}