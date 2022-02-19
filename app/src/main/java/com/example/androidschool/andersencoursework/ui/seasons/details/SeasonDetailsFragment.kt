package com.example.androidschool.andersencoursework.ui.seasons.details

import android.content.Context
import android.util.Log
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
import com.example.androidschool.andersencoursework.ui.edpisode.list.EpisodesListDelegateAdapter
import com.example.androidschool.andersencoursework.ui.edpisode.list.EpisodesListFragmentDirections
import com.example.androidschool.andersencoursework.ui.seasons.list.SeasonsListFragmentDirections
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator
import com.example.androidschool.andersencoursework.util.UIState
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
    }

    private fun handleState(state: UIState<SeasonDetailsState>) {
        when (state) {
            is UIState.Success -> handleSuccess(state)
            is UIState.Error -> handleError(state)
            is UIState.EmptyError -> handleEmptyError()
            is UIState.InitialLoading -> handleInitialLoading()
            is UIState.Refresh -> handleRefresh()
        }
    }

    private fun handleEmptyError() {
        hideAll()
        showError()
    }

    private fun handleInitialLoading() {
        hideAll()
        showLoading()
        viewModel.load(season)
    }

    private fun handleRefresh() {
        showLoading()
        viewModel.load(season)
    }

    private fun handleSuccess(state: UIState.Success<SeasonDetailsState>) {
        hideLoading()
        showContent(state.data)
        viewBinding.fragmentSeasonsDetailsRefresh.setOnRefreshListener {
            viewModel.refresh(season)
        }
    }

    private fun handleError(state: UIState.Error<SeasonDetailsState>) {
        hideLoading()
        hideAll()
        showContent(state.data)
        showErrorMessage()
        viewBinding.fragmentSeasonsDetailsRefresh.setOnRefreshListener {
            viewModel.refresh(season)
        }
    }

    private fun showErrorMessage() {
        Snackbar.make(
            viewBinding.root,
            resourceProvider.resources.getString(R.string.default_error_message),
            Snackbar.LENGTH_LONG
        ).setAction(
            R.string.refresh_btn_title
        ) { viewModel.refresh(season) }.show()
    }

    private fun hideAll() {
        hideErrorBlock()
        viewBinding.fragmentSeasonsDetailsRefreshContainer.visibility = View.GONE
    }

    private fun hideErrorBlock() {
        viewBinding.errorBlock.fragmentEmptyDataMessage.visibility = View.GONE
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.GONE
    }

    private fun showLoading() {
        viewBinding.fragmentSeasonsDetailsRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.fragmentSeasonsDetailsRefresh.isRefreshing = false
    }

    private fun showContent(data: SeasonDetailsState) {
        hideErrorBlock()
        viewBinding.fragmentSeasonsDetailsRefreshContainer.visibility = View.VISIBLE
        Log.e("SeasonsListFragment", "${data.episodes}")
        mAdapter.submitList(data.episodes)
    }

    private fun showError() {
        hideAll()
        showErrorBlock()
    }

    private fun showErrorBlock() {
        hideLoading()
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.VISIBLE
        viewBinding.fragmentSeasonsDetailsRefresh.setOnRefreshListener {
            viewModel.retry(season)
        }
        viewBinding.errorBlock.fragmentEmptyErrorRetryBtn.setOnClickListener {
            viewModel.retry(season)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _toolbarBinding = null
    }
}