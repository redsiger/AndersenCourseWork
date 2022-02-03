package com.example.androidschool.andersencoursework.ui.characters.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidschool.andersencoursework.databinding.ListItemCharacterBinding
import com.example.androidschool.domain.characters.model.CharacterEntity

class CharactersListPagingAdapter(
    private val action: (characterId: Int) -> Unit
): PagingDataAdapter<CharacterEntity, CharactersListPagingAdapter.Holder>(COMPARATOR) {

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

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getItem(position)?.let { character ->
            holder.bind(character)
            holder.itemView.setOnClickListener { action(character.charId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCharacterBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    class Holder(
        private val binding: ListItemCharacterBinding
        ): RecyclerView.ViewHolder(binding.root) {

        fun bind(character: CharacterEntity) {
            with (binding) {
                listItemCharacterName.text = character.name
                Glide.with(binding.listItemCharacterImg.context)
                    .load(character.img)
                    .centerCrop()
                    .into(listItemCharacterImg)
            }
        }
    }
}