package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentEpisodesListBinding
import com.example.androidschool.andersencoursework.databinding.MergeToolbarBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.characters.models.ListItem
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.util.InfiniteScrollListener
import javax.inject.Inject

class EpisodesListStateFragment: BaseFragment(R.layout.fragment_episodes_list) {

    private var _binding: FragmentEpisodesListBinding? = null
    private val viewBinding get() = _binding!!
    private var _toolbarBinding: MergeToolbarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private val mNavController: NavController by lazy { findNavController() }

    private val mScrollListener: InfiniteScrollListener by lazy {
        InfiniteScrollListener({ viewModel.loadNewPage() }, viewBinding.fragmentEpisodesListRecycler.layoutManager as LinearLayoutManager)
    }

    @Inject
    lateinit var viewModelFactory: EpisodesListStateViewModel.Factory
    private val viewModel: EpisodesListStateViewModel by viewModels { viewModelFactory }

    @Inject lateinit var adapterFactory: EpisodesListAdapter.Factory
    private val mAdapter: EpisodesListAdapter by lazy {
        adapterFactory.create(
            { id: Int -> Log.e("onClick", "$id") },
            { Log.e("refresh", "refresh") }
        )
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEpisodesListBinding.bind(view)
        _toolbarBinding = MergeToolbarBinding.bind(viewBinding.root)

        initToolbar()
        initList()
        submitListRefresh()
    }

    private fun initToolbar() {

        val onItemClick = { item: MenuItem ->
            when(item.itemId) {
                R.id.menu_item_search -> {
                    val action = EpisodesListStateFragmentDirections.actionGlobalToSearch()
                    mNavController.navigate(action)
                    true
                }
                else -> false
            }
        }

        setupMainToolbar(
            toolbarBinding.toolbar,
            getString(R.string.episodes_fragment_title),
            R.menu.search_menu,
            onItemClick
        )
    }

    private fun initList() {
        val list = viewBinding.fragmentEpisodesListRecycler

        with(list) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        }

        submitListListener()
        collectStates()
    }

    private fun collectStates() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                handleState(state)
            }
        }
    }

    private fun submitListListener() {
        with(viewBinding.fragmentEpisodesListRecycler) {
            addOnScrollListener(mScrollListener)
        }
    }

    private fun submitListRefresh() {
        viewBinding.fragmentEpisodesListRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        viewBinding.fragmentEpisodesEmptyErrorRetryBtn.setOnClickListener { viewModel.refresh() }
    }

    private fun handleState(state: UIStatePaging) {
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
    }

    private fun handleEmptyLoading(state: UIStatePaging.EmptyLoading) {
        hideAll()
        showLoading()
        Log.e("UIStatePaging.EmptyLoading", "state: ${state.data.size},  offset: ${state.offset}, ${state.data}")
        viewModel.refresh()
    }

    private fun handleEmptyError(state: UIStatePaging.EmptyError) {
        hideLoading()
        showEmptyError()
        Log.e("UIStatePaging.EmptyError", "state:  offset: ${state.offset}, ${state.data}")
    }

    private fun handleEmptyData(state: UIStatePaging.EmptyData) {
        hideLoading()
        showEmptyData()
        Log.e("UIStatePaging.EmptyData", "state: ${state.data.size},  offset: ${state.offset}, ${state.data}")
    }

    private fun handlePartialData(state: UIStatePaging.PartialData) {
        hideLoading()
        showPartialData(state.data)
        Log.e("UIStatePaging.PartialData", "state: ${state.data.size},  offset: ${state.offset}, ${state.data}")
    }

    private fun handleLoadingPartialData(state: UIStatePaging.LoadingPartialData) {
        hideLoading()
        showPartialDataLoading(state.data)
        Log.e("UIStatePaging.LoadingPartialData", "state: ${state.data.size},  offset: ${state.offset}, ${state.data}")
    }

    private fun handleLoadingPartialDataError(state: UIStatePaging.LoadingPartialDataError) {
        hideLoading()
        showPartialDataError(state.data)
        Log.e("UIStatePaging.LoadingPartialDataError", "state: ${state.data.size},  offset: ${state.offset}, ${state.data}")
    }

    private fun handleAllData(state: UIStatePaging.AllData) {
        hideLoading()
        showAllData(state.data)
        Log.e("UIStatePaging.AllData", "state: ${state.data.size},  offset: ${state.offset}, ${state.data}")
    }

    private fun handleRefresh(state: UIStatePaging.Refresh) {
        hideAll()
        mAdapter.submitList(state.data)
        showLoading()
        mScrollListener.reset()
        Log.e("UIStatePaging.Refresh", "state: ${state.data.size},  offset: ${state.offset}, ${state.data}")
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
        viewBinding.fragmentEpisodesEmptyData.visibility = View.VISIBLE
    }

    private fun showEmptyError() {
        viewBinding.fragmentEpisodesEmptyError.visibility = View.VISIBLE
    }

    private fun showPartialData(data: List<ListItem>) {
        viewBinding.fragmentEpisodesListRecycler.visibility = View.VISIBLE
        mAdapter.submitList(data)
    }

    private fun showPartialDataLoading(data: List<ListItem>) {
        viewBinding.fragmentEpisodesListRecycler.visibility = View.VISIBLE
        mAdapter.submitList(data)
    }

    private fun showPartialDataError(data: List<ListItem>) {
        viewBinding.fragmentEpisodesListRecycler.visibility = View.VISIBLE
        mAdapter.submitList(data)
    }

    private fun showAllData(data: List<ListItem>) {
        viewBinding.fragmentEpisodesListRecycler.visibility = View.VISIBLE
        mAdapter.submitList(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toolbarBinding = null
    }
}