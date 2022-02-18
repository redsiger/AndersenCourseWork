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
import javax.inject.Inject

class CharacterAppearanceDelegateAdapter@Inject constructor(
    private val resourceProvider: ResourceProvider
): DelegateAdapter<EpisodeListItemUI, CharacterAppearanceDelegateAdapter.ViewHolder>(EpisodeListItemUI::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_appearance, parent, false)
        return ViewHolder(itemView)
    }

    override fun bindViewHolder(model: EpisodeListItemUI, viewHolder: CharacterAppearanceDelegateAdapter.ViewHolder) {
        viewHolder.bind(model)
    }

    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {

        val viewBinding = ListItemAppearanceBinding.bind(itemView)

        fun bind(item: EpisodeListItemUI) {
            with(viewBinding) {
                listItemEpisodeTitle.text = item.title
                listItemEpisodeEpisode.text = resourceProvider.resources.getString(
                    R.string.appearance_list_item_episode,
                    item.episode
                )
                listItemEpisodeSeason.text = resourceProvider.resources.getString(
                    R.string.appearance_list_item_season,
                    item.season
                )
                listItemEpisodeAirDate.text = item.airDate
            }
        }
    }
}