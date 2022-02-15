package com.example.androidschool.andersencoursework.ui.characters.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.IncludeDefaultLoadStateBinding
import com.example.androidschool.andersencoursework.databinding.ListItemCharacterBinding
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterListItemUI
import com.example.androidschool.andersencoursework.ui.core.recycler.ListItem
import com.example.androidschool.andersencoursework.ui.core.recycler.DiffComparable
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val ITEM_HOLDER = 0
private const val LOADING_AND_ERROR_HOLDER = 1

interface Binder<T: DiffComparable> {
    fun bind(item: ListItem<T>)
}

object ViewHolderFactory {
    fun create(view: View, layoutId: Int): RecyclerView.ViewHolder {
        return when (layoutId) {
            R.layout.include_default_load_state -> DefaultErrorAndLoadingHolder(view)
            R.layout.list_item_character -> CharacterListItemHolder(view)
            else -> throw Exception("No specific viewHolder")
        }
    }
}

class DefaultErrorAndLoadingHolder(view: View): RecyclerView.ViewHolder(view), Binder<CharacterListItemUI> {
    private val viewBinding = IncludeDefaultLoadStateBinding.bind(view)
    override fun bind(item: ListItem<CharacterListItemUI>) {
        when(item) {
            is ListItem.Loading -> {}
            is ListItem.Error -> {}
            else -> throw Exception("Wrong listItem type for loading or error")
        }
    }

}

class CharacterListItemHolder(view: View): RecyclerView.ViewHolder(view), Binder<CharacterListItemUI> {
    private val viewBinding = ListItemCharacterBinding.bind(itemView)
    override fun bind(item: ListItem<CharacterListItemUI>) {
        when (item) {
            is ListItem.Item -> {
                viewBinding.listItemCharacterName.text = item.item.name
                Glide.with(itemView.context)
                    .load(item.item.img)
                    .centerCrop()
                    .into(viewBinding.listItemCharacterImg)
            }
            else -> throw Exception("Wrong listItemType for item")
        }
    }
}

class GenericListAdapter<T: DiffComparable> @AssistedInject constructor(
    @Assisted("layoutId") private val layoutId: Int,
    @Assisted("errorAndLoadingLayoutId") private val errorAndLoadingLayoutId: Int,
    @Assisted("onClick") private val onClick: (id: Int) -> Unit,
    @Assisted("refresh") private val refresh: () -> Unit
): ListAdapter<ListItem<T>, RecyclerView.ViewHolder>(ListItemDiff()) {

    class ListItemDiff<T: DiffComparable>: DiffUtil.ItemCallback<ListItem<T>>() {
        override fun areItemsTheSame(oldItem: ListItem<T>, newItem: ListItem<T>): Boolean {
            return if (oldItem is ListItem.Item && newItem is ListItem.Item) {
                oldItem.item.areItemsTheSame(newItem.item)
            } else false
        }
        override fun areContentsTheSame(oldItem: ListItem<T>, newItem: ListItem<T>): Boolean {
            return if (oldItem is ListItem.Item && newItem is ListItem.Item) {
                oldItem.item.areContentsTheSame(newItem.item)
            } else false
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is ListItem.Item -> ITEM_HOLDER
            else -> LOADING_AND_ERROR_HOLDER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = when (viewType) {
            ITEM_HOLDER -> inflater.inflate(layoutId, parent, false)
            else -> inflater.inflate(errorAndLoadingLayoutId, parent, false)
        }

        return ViewHolderFactory.create(view, layoutId)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        (holder as Binder<T>).bind(item)
    }

    @AssistedFactory
    interface Factory<T: DiffComparable> {
        fun create(
            @Assisted("layoutId") layoutId: Int,
            @Assisted("errorAndLoadingLayoutId") errorAndLoadingLayoutId: Int,
            @Assisted("onClick") onClick: (id: Int) -> Unit,
            @Assisted("refresh") refresh: () -> Unit
        ): GenericListAdapter<T>
    }
}