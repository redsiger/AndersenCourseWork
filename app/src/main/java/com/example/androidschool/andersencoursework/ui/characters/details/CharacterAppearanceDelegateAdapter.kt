package com.example.androidschool.andersencoursework.ui.characters.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.ListItemAppearanceBinding
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import com.example.androidschool.andersencoursework.ui.core.recycler.DelegateAdapter
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeListItemUI
import com.example.androidschool.andersencoursework.ui.seasons.list.SeasonsListDelegateAdapter
import com.example.androidschool.andersencoursework.ui.seasons.model.SeasonListItemUI
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

class CharacterAppearanceDelegateAdapter @AssistedInject constructor(
    @Assisted("onItemClick")
    private val onItemClick: (season: String) -> Unit,
    private val resourceProvider: ResourceProvider
) : DelegateAdapter<SeasonListItemUI, CharacterAppearanceDelegateAdapter.ViewHolder>(
    SeasonListItemUI::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_appearance, parent, false)
        return ViewHolder(itemView)
    }

    override fun bindViewHolder(
        model: SeasonListItemUI,
        viewHolder: CharacterAppearanceDelegateAdapter.ViewHolder
    ) {
        viewHolder.bind(model)
    }

    inner class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

        val viewBinding = ListItemAppearanceBinding.bind(itemView)

        fun bind(item: SeasonListItemUI) {
            with(viewBinding) {
                listItemSeasonTitle.text = resourceProvider.resources.getString(
                    R.string.appearance_list_item_season,
                    item.season
                )
            }
            itemView.setOnClickListener { onItemClick.invoke(item.season) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onItemClick") onItemClick: (season: String) -> Unit
        ): CharacterAppearanceDelegateAdapter
    }
}