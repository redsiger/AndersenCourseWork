package com.example.androidschool.andersencoursework.ui.seasons.list

import android.content.Context
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentSeasonsListBinding
import com.example.androidschool.andersencoursework.databinding.MergeToolbarBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.ui.core.recycler.CompositeAdapter
import com.example.androidschool.andersencoursework.ui.edpisode.list.EpisodesListFragmentDirections
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator
import com.example.androidschool.andersencoursework.util.UIState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SeasonsListFragment :
    BaseFragment<FragmentSeasonsListBinding>(R.layout.fragment_seasons_list) {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var viewModelFactory: SeasonsListViewModel.Factory
    private val viewModel: SeasonsListViewModel by viewModels { viewModelFactory }

    private var _toolbarBinding: MergeToolbarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    @Inject
    lateinit var seasonsListAdapter: SeasonsListDelegateAdapter.Factory

    private val mAdapter: CompositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(seasonsListAdapter.create(::navigateToDetails))
            .build()
    }

    private fun navigateToDetails(season: Int) {
        val bundle = bundleOf("season" to season.toString())
        navController.navigate(R.id.action_global_to_season_details, bundle)
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun initBinding(view: View): FragmentSeasonsListBinding {
        return FragmentSeasonsListBinding.bind(view)
    }

    override fun initFragment() {
        initToolbar()
        initPage()
    }

    private fun initToolbar() {
        _toolbarBinding = MergeToolbarBinding.bind(viewBinding.root)

        setupMainToolbar(
            toolbar = toolbarBinding.toolbar,
            title = getString(R.string.seasons_fragment_title),
            menuId = R.menu.search_menu,
            onItemClick = { item: MenuItem ->
                when (item.itemId) {
                    R.id.menu_item_search -> {
                        navController.navigate(R.id.action_global_to_search)
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

        with(viewBinding) {
            fragmentSeasonsListAllEpisodes.setOnClickListener {
                val action = SeasonsListFragmentDirections.toAllEpisodes()
                navController.navigate(action)
            }
        }

        with(viewBinding.fragmentSeasonsListRecycler) {
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

    private fun handleState(state: UIState<SeasonListState>) {
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
        viewModel.load()
    }

    private fun handleRefresh() {
        showLoading()
        viewModel.load()
    }

    private fun handleSuccess(state: UIState.Success<SeasonListState>) {
        hideLoading()
        showContent(state.data)
        viewBinding.fragmentSeasonsListRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun handleError(state: UIState.Error<SeasonListState>) {
        hideLoading()
        hideAll()
        showContent(state.data)
        showErrorMessage()
        viewBinding.fragmentSeasonsListRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun showErrorMessage() {
        Snackbar.make(
            viewBinding.root,
            resourceProvider.resources.getString(R.string.default_error_message),
            Snackbar.LENGTH_LONG
        ).setAction(
            R.string.refresh_btn_title
        ) { viewModel.refresh() }.show()
    }

    private fun hideAll() {
        hideErrorBlock()
        viewBinding.fragmentSeasonsListRefreshContainer.visibility = View.GONE
    }

    private fun hideErrorBlock() {
        viewBinding.errorBlock.fragmentEmptyDataMessage.visibility = View.GONE
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.GONE
    }

    private fun showLoading() {
        viewBinding.fragmentSeasonsListRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.fragmentSeasonsListRefresh.isRefreshing = false
    }

    private fun showContent(data: SeasonListState) {
        hideErrorBlock()
        viewBinding.fragmentSeasonsListRefreshContainer.visibility = View.VISIBLE
        Log.e("SeasonsListFragment", "${data.seasons}")
        mAdapter.submitList(data.seasons)
    }

    private fun showError() {
        hideAll()
        showErrorBlock()
    }

    private fun showErrorBlock() {
        hideLoading()
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.VISIBLE
        viewBinding.fragmentSeasonsListRefresh.setOnRefreshListener {
            viewModel.retry()
        }
        viewBinding.errorBlock.fragmentEmptyErrorRetryBtn.setOnClickListener {
            viewModel.retry()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _toolbarBinding = null
    }
}