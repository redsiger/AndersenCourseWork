package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidschool.andersencoursework.databinding.ListItemCharacterBinding
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class EpisodesListAdapter @AssistedInject constructor (
    private val glide: RequestManager,
    @Assisted("onClick") private val onClick: (id: Int) -> Unit
): RecyclerView.Adapter<EpisodesListAdapter.Holder>() {
    private var dataList: MutableList<CharacterUIEntity> = mutableListOf()

    fun setList(newList: List<CharacterUIEntity>) {
        val callback = Comparator(newList, dataList)
        val diff = DiffUtil.calculateDiff(callback)
        dataList = newList.toMutableList()
        diff.dispatchUpdatesTo(this)
    }

    fun addToList(newList: List<CharacterUIEntity>) {
        val callback = Comparator(newList, dataList)
        val diff = DiffUtil.calculateDiff(callback)
        dataList.addAll(newList)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCharacterBinding.inflate(inflater, parent, false)
        return Holder(binding.root, glide)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val character = dataList[position]
        holder.bind(character)
        holder.itemView.setOnClickListener { onClick(character.charId) }
    }

    override fun getItemCount(): Int = dataList.size

    private class Comparator(
        private val newList: List<CharacterUIEntity>,
        private val oldList: List<CharacterUIEntity>
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

    class Holder(
        view: View,
        private val glide: RequestManager
    ): RecyclerView.ViewHolder(view) {
        val binding = ListItemCharacterBinding.bind(view)

        fun bind(character: CharacterUIEntity) {
            with(binding) {
                listItemCharacterName.text = character.name
                glide
                    .load(character.img)
                    .centerCrop()
                    .into((listItemCharacterImg))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onClick") onClick: (id: Int) -> Unit
        ): EpisodesListAdapter
    }
}