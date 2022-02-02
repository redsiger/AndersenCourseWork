package com.example.androidschool.andersencoursework.ui.seacrh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidschool.andersencoursework.databinding.ListItemCharacterBinding
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.andersencoursework.ui.seacrh.models.CharacterSearchEntity

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.SearchResultViewHolder>() {

    private var list: List<CharacterSearchEntity> = emptyList()
    fun setList(newList: List<CharacterSearchEntity>) {
        val callback = Comparator(newList, list)
        val diffResult = DiffUtil.calculateDiff(callback)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCharacterBinding.inflate(inflater, parent, false)

        return SearchResultViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val character = list[position]

        holder.bind(character)
    }

    override fun getItemCount(): Int = list.size

    class SearchResultViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val viewBinding = ListItemCharacterBinding.bind(view)

        fun bind(character: CharacterSearchEntity) {
            with(viewBinding) {
                listItemCharacterName.text = character.name
                Glide.with(itemView.context)
                    .load(character.img)
                    .centerCrop()
                    .into((listItemCharacterImg))
            }
        }
    }

    class Comparator(
        private val newList: List<CharacterSearchEntity>,
        private val oldList: List<CharacterSearchEntity>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.charId == newItem.charId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}