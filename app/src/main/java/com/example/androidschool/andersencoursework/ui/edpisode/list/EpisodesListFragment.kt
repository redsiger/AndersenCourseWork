package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.content.Context
import android.view.MenuItem
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentEpisodesListBinding
import com.example.androidschool.andersencoursework.databinding.MergeToolbarBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import com.example.androidschool.andersencoursework.ui.characters.list.CharactersListFragmentDirections
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.ui.core.recycler.CompositeAdapter
import com.example.androidschool.andersencoursework.ui.core.recycler.DefaultErrorDelegateAdapter
import com.example.androidschool.andersencoursework.ui.core.recycler.DefaultLoadingDelegateAdapter
import com.example.androidschool.andersencoursework.ui.core.recycler.DiffComparable
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeListItemUI
import com.example.androidschool.andersencoursework.util.InfiniteScrollListener
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator
import com.example.androidschool.andersencoursework.util.UIStatePaging
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class EpisodesListFragment :
    BaseFragment<FragmentEpisodesListBinding>(R.layout.fragment_episodes_list) {

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var viewModelFactory: EpisodesListViewModel.Factory
    private val viewModel: EpisodesListViewModel by viewModels { viewModelFactory }

    private var _toolbarBinding: MergeToolbarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private val mScrollListener: InfiniteScrollListener by lazy {
        InfiniteScrollListener(
            action = viewModel::loadNewPage,
            layoutManager = viewBinding.fragmentEpisodesListRecycler.layoutManager as LinearLayoutManager
        )
    }

    @Inject
    lateinit var episodesListAdapter: EpisodesListDelegateAdapter.Factory

    private val mPagingAdapter: CompositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(episodesListAdapter.create(::navigateToDetails))
            .add(DefaultErrorDelegateAdapter(onTryAgain = { viewModel.loadNewPage() }))
            .add(DefaultLoadingDelegateAdapter())
            .build()
    }

    override fun initBinding(view: View): FragmentEpisodesListBinding {
        return FragmentEpisodesListBinding.bind(view)
    }

    override fun initFragment() {
        _toolbarBinding = MergeToolbarBinding.bind(viewBinding.root)

        initList(
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false),
            resourceProvider.resources.getDimensionPixelSize(R.dimen.app_offset_small)
        )
        initToolbar()
        hideAll()
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    private fun initToolbar() {

        setupToolbar(
            toolbar = toolbarBinding.toolbar,
            title = getString(R.string.episodes_fragment_title),
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

    private fun initList(
        linearLayoutManager: LinearLayoutManager,
        itemPadding: Int
    ) {
        with(viewBinding.fragmentEpisodesListRecycler) {
            adapter = mPagingAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(
                OffsetRecyclerDecorator(
                    itemPadding,
                    linearLayoutManager,
                    false
                )
            )
        }

        addScrollListener()
        collectStates()
        addListRefresher()
    }

    private fun addScrollListener() {
        with(viewBinding.fragmentEpisodesListRecycler) {
            addOnScrollListener(mScrollListener)
        }
    }

    private fun resetScrollListener() {
        mScrollListener.reset()
    }

    private fun removeScrollListener() {
        viewBinding.fragmentEpisodesListRecycler.removeOnScrollListener(mScrollListener)
        mScrollListener.reset()
    }

    private fun navigateToDetails(id: Int) {
        val action = EpisodesListFragmentDirections.fromListToDetails(id)
        navController.navigate(action)
    }

    private fun addListRefresher() {
        viewBinding.fragmentEpisodesListRefresh.setOnRefreshListener { viewModel.refresh() }
        viewBinding.errorBlock.fragmentEmptyErrorRetryBtn.setOnClickListener { viewModel.refresh() }
    }

    private fun collectStates() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state -> handleState(state) }
        }
    }

    private fun handleState(state: UIStatePaging<DiffComparable>) =
        when (state) {
            is UIStatePaging.EmptyLoading -> handleEmptyLoading(state)
            is UIStatePaging.EmptyData -> handleEmptyData(state)
            is UIStatePaging.EmptyError -> handleEmptyError(state)
            is UIStatePaging.PartialData -> handlePartialData(state)
            is UIStatePaging.LoadingPartialData -> handleLoadingPartialData(state)
            is UIStatePaging.AllData -> handleAllData(state)
            is UIStatePaging.LoadingPartialDataError -> handleLoadingPartialDataError(state)
            is UIStatePaging.Refresh -> handleRefresh(state)
        }

    private fun handleEmptyLoading(state: UIStatePaging.EmptyLoading<DiffComparable>) {
        hideAll()
        showLoading()
        viewModel.refresh()
    }

    private fun handleEmptyError(state: UIStatePaging.EmptyError<DiffComparable>) {
        hideAll()
        showEmptyError(state.error)
    }

    private fun handleEmptyData(state: UIStatePaging.EmptyData<DiffComparable>) {
        hideAll()
        showEmptyData()
    }

    private fun handlePartialData(state: UIStatePaging.PartialData<DiffComparable>) {
        hideLoading()
        showPartialData(state.data as List<EpisodeListItemUI>)
    }

    private fun handleLoadingPartialData(state: UIStatePaging.LoadingPartialData<DiffComparable>) {
        hideLoading()
        showPartialDataLoading(state.data as List<EpisodeListItemUI>)
        resetScrollListener()
    }

    private fun handleLoadingPartialDataError(state: UIStatePaging.LoadingPartialDataError<DiffComparable>) {
        hideLoading()
        showPartialDataError(state.data as List<EpisodeListItemUI>)
    }

    private fun handleAllData(state: UIStatePaging.AllData<DiffComparable>) {
        hideLoading()
        showAllData(state.data as List<EpisodeListItemUI>)
    }

    private fun handleRefresh(state: UIStatePaging.Refresh<DiffComparable>) {
        hideAll()
        resetScrollListener()
        mPagingAdapter.submitList(state.data)
        showLoading()
    }

    private fun hideAll() {
        hideLoading()
        viewBinding.fragmentEpisodesListRefreshContainer.children.forEach {
            it.visibility = View.GONE
        }
    }

    private fun showLoading() {
        viewBinding.fragmentEpisodesListRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.fragmentEpisodesListRefresh.isRefreshing = false
    }

    private fun showEmptyData() {
        viewBinding.errorBlock.root.visibility = View.VISIBLE
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.GONE
        viewBinding.errorBlock.fragmentEmptyDataMessage.visibility = View.VISIBLE
        viewBinding.errorBlock.fragmentEmptyDataMessage.text =
            getString(R.string.default_emptyData_message)
    }

    private fun showEmptyError(error: Exception) {
        viewBinding.errorBlock.root.visibility = View.VISIBLE
        viewBinding.errorBlock.fragmentEmptyDataMessage.visibility = View.GONE
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.VISIBLE
        viewBinding.errorBlock.fragmentEmptyErrorTitle.text =
            getString(R.string.default_error_message)
    }

    private fun showPartialData(data: List<EpisodeListItemUI>) {
        viewBinding.fragmentEpisodesListRecycler.visibility = View.VISIBLE
        mPagingAdapter.submitList(data)
    }

    private fun showPartialDataLoading(data: List<EpisodeListItemUI>) {
        viewBinding.fragmentEpisodesListRecycler.visibility = View.VISIBLE
        mPagingAdapter.submitList(data)
    }

    private fun showPartialDataError(data: List<EpisodeListItemUI>) {
        viewBinding.fragmentEpisodesListRecycler.visibility = View.VISIBLE
        mPagingAdapter.submitList(data)
    }

    private fun showAllData(data: List<EpisodeListItemUI>) {
        viewBinding.fragmentEpisodesListRecycler.visibility = View.VISIBLE
        mPagingAdapter.submitList(data)
    }

    override fun onDestroyView() {
        removeScrollListener()
        super.onDestroyView()
        _toolbarBinding = null
    }
}