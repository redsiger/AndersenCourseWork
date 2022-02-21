package com.example.androidschool.andersencoursework.ui.seasons.details

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentSeasonDetailsBinding
import com.example.androidschool.andersencoursework.databinding.MergeToolbarBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.ui.core.recycler.CompositeAdapter
import com.example.androidschool.andersencoursework.ui.core.recycler.ListItemUI
import com.example.androidschool.andersencoursework.ui.edpisode.list.EpisodesListDelegateAdapter
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator
import com.example.androidschool.util.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SeasonDetailsFragment :
    BaseFragment<FragmentSeasonDetailsBinding>(R.layout.fragment_season_details) {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var mContext: Context

    private val args: SeasonDetailsFragmentArgs by navArgs()
    private val season: String by lazy { args.season }

    @Inject
    lateinit var viewModelFactory: SeasonDetailsViewModel.Factory
    private val viewModel: SeasonDetailsViewModel by viewModels { viewModelFactory }

    private var _toolbarBinding: MergeToolbarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    @Inject
    lateinit var seasonEpisodesAdapter: EpisodesListDelegateAdapter.Factory

    private val mAdapter: CompositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(seasonEpisodesAdapter.create(::navigateToDetails))
            .build()
    }

    private fun navigateToDetails(episodeId: Int) {
        val action = SeasonDetailsFragmentDirections.toEpisode(episodeId)
        navController.navigate(action)
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun initBinding(view: View): FragmentSeasonDetailsBinding {
        return FragmentSeasonDetailsBinding.bind(view)
    }

    override fun initFragment() {
        initToolbar()
        initPage()
    }

    private fun initToolbar() {
        _toolbarBinding = MergeToolbarBinding.bind(viewBinding.root)

        setupToolbar(
            toolbar = toolbarBinding.toolbar,
            title = getString(R.string.seasons_fragment_details_title, season),
            menuId = R.menu.search_menu,
            onItemClick = { item: MenuItem ->
                when (item.itemId) {
                    R.id.menu_item_search -> {
                        navController.navigate(R.id.search_nav_graph)
                        true
                    }
                    else -> false
                }
            }
        )
    }

    private fun initPage() {

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state -> handleState(state) }
        }

        with(viewBinding.fragmentSeasonsDetailsRecycler) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                mContext,
                LinearLayoutManager.VERTICAL,
                false
            )
            addItemDecoration(
                OffsetRecyclerDecorator(
                    margin = resourceProvider.resources
                        .getDimension(R.dimen.app_offset_small).toInt(),
                    layoutManager = layoutManager as LinearLayoutManager,
                    inDp = false
                )
            )
        }

        viewBinding.fragmentSeasonsDetailsRefresh.setOnRefreshListener { viewModel.retry() }
    }

    private fun handleState(state: Status<List<ListItemUI.EpisodeListItemUI>>) {
        when (state) {
            is Status.Initial -> {
                hideAll()
                showLoading()
                viewModel.load(season)
            }
            is Status.EmptyError -> {
                hideAll()
                showNoData()
                hideLoading()
                showErrorMessage()
            }
            is Status.Success -> {
                hideLoading()
                showContent(state.extractData)
            }
            is Status.Error -> {
                hideLoading()
                showContent(state.extractData)
                showErrorMessage()
            }
        }
    }

    private fun showLoading() {
        viewBinding.fragmentSeasonsDetailsRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.fragmentSeasonsDetailsRefresh.isRefreshing = false
    }

    private fun hideAll() {
        hideNoData()
        viewBinding.fragmentSeasonsDetailsMainContent.visibility = View.GONE
    }

    private fun showErrorMessage() {
        Snackbar.make(
            viewBinding.fragmentSeasonDetailsCoordinator,
            R.string.default_error_message,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.retry_btn_title) { viewModel.retry() }
            .show()
    }

    private fun showNoData() {
        viewBinding.errorBlock.visibility = View.VISIBLE
    }

    private fun hideNoData() {
        viewBinding.errorBlock.visibility = View.GONE
    }

    private fun showContent(data: List<ListItemUI.EpisodeListItemUI>) {
        hideNoData()
        if (data.isNotEmpty()) {
            viewBinding.fragmentSeasonsDetailsMainContent.visibility = View.VISIBLE
            mAdapter.submitList(data)
        } else showNoData()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _toolbarBinding = null
    }
}