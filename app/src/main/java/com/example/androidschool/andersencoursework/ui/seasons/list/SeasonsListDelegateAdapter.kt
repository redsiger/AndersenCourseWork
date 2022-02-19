package com.example.androidschool.andersencoursework.ui.seasons.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.ListItemSeasonBinding
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import com.example.androidschool.andersencoursework.ui.core.recycler.DelegateAdapter
import com.example.androidschool.andersencoursework.ui.edpisode.list.EpisodesListDelegateAdapter
import com.example.androidschool.andersencoursework.ui.seasons.model.SeasonListItemUI
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SeasonsListDelegateAdapter @AssistedInject constructor(
    @Assisted("onItemClick")
    private val onItemClick: (season: Int) -> Unit,
    private val resourceProvider: ResourceProvider
):
    DelegateAdapter<SeasonListItemUI, SeasonsListDelegateAdapter.ViewHolder>(SeasonListItemUI::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_season, parent, false)
        return ViewHolder(itemView)
    }

    override fun bindViewHolder(model: SeasonListItemUI, viewHolder: ViewHolder) {
        viewHolder.bind(model)
    }

    inner class ViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {

        val viewBinding = ListItemSeasonBinding.bind(itemView)

        fun bind(item: SeasonListItemUI) {
            with(viewBinding) {
                listItemSeasonTitle.text =
                    resourceProvider.resources.getString(
                        R.string.season_list_item_title,
                        item.season
                    )
            }
            itemView.setOnClickListener { onItemClick.invoke(item.season.toInt()) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("onItemClick") onItemClick: (id: Int) -> Unit): SeasonsListDelegateAdapter
    }
}