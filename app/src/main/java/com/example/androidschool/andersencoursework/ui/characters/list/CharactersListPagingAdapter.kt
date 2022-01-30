package com.example.androidschool.andersencoursework.ui.characters.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.ListItemCharacterBinding
import com.example.androidschool.domain.characters.model.CharacterEntity

class CharactersListPagingAdapter: PagingDataAdapter<CharacterEntity, CharactersListPagingAdapter.CharacterViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<CharacterEntity>() {
            override fun areItemsTheSame(
                oldItem: CharacterEntity,
                newItem: CharacterEntity
            ): Boolean {
                return oldItem.charId == newItem.charId
            }

            override fun areContentsTheSame(
                oldItem: CharacterEntity,
                newItem: CharacterEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let { character ->
            holder.bind(character)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_character, parent, false)
        )
    }

    class CharacterViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        private val viewBinding = ListItemCharacterBinding.bind(view)

        fun bind(character: CharacterEntity) {
            with (viewBinding) {
                listItemCharacterName.text = character.name
                listItemCharacterNickname.text = character.nickname
                Glide.with(view.context)
                    .load(character.img)
                    .centerCrop()
                    .into(listItemCharacterImg)
            }
        }
    }
}