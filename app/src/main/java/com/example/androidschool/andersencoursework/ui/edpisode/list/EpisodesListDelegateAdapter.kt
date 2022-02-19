package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.ListItemEpisodeBinding
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import com.example.androidschool.andersencoursework.ui.core.recycler.DelegateAdapter
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeListItemUI
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

class EpisodesListDelegateAdapter @AssistedInject constructor(
    @Assisted("onItemClick")
    private val onItemClick: (id: Int) -> Unit,
    private val resourceProvider: ResourceProvider
) :
    DelegateAdapter<EpisodeListItemUI, EpisodesListDelegateAdapter.ViewHolder>(EpisodeListItemUI::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_episode, parent, false)
        return ViewHolder(itemView)
    }

    override fun bindViewHolder(model: EpisodeListItemUI, viewHolder: ViewHolder) {
        viewHolder.bind(model)
    }

    inner class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

        val viewBinding = ListItemEpisodeBinding.bind(itemView)

        fun bind(item: EpisodeListItemUI) {
            with(viewBinding) {
                listItemEpisodeTitle.text =
                    resourceProvider.resources.getString(
                        R.string.episode_list_item_title,
                        item.season,
                        item.episode,
                        item.title
                    )
                listItemEpisodeAirDate.text = item.airDate
            }
            itemView.setOnClickListener { onItemClick.invoke(item.episodeId) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("onItemClick") onItemClick: (id: Int) -> Unit): EpisodesListDelegateAdapter
    }
}