package com.example.androidschool.andersencoursework.ui.characters.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.ListItemCharacterBinding
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterListItemUI
import com.example.androidschool.andersencoursework.ui.core.recycler.DelegateAdapter
import dagger.Component
import javax.inject.Inject

class CharactersListDelegateAdapter @Inject constructor (
    private val glide: RequestManager
):
    DelegateAdapter<CharacterListItemUI, CharactersListDelegateAdapter.ViewHolder>(CharacterListItemUI::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_character, parent, false)
        return ViewHolder(itemView)
    }

    override fun bindViewHolder(model: CharacterListItemUI, viewHolder: ViewHolder) {
        viewHolder.bind(model)
    }

    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {

        val viewBinding = ListItemCharacterBinding.bind(itemView)

        fun bind(item: CharacterListItemUI) {
            viewBinding.listItemCharacterName.text = item.name
            glide
                .load(item.img)
                .centerCrop()
                .into(viewBinding.listItemCharacterImg)
        }
    }
}