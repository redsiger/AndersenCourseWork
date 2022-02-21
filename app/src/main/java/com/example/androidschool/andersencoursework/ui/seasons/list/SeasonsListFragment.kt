package com.example.androidschool.andersencoursework.ui.seasons.list

import android.content.Context
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
import com.example.androidschool.andersencoursework.ui.seasons.model.SeasonListItemUI
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator
import com.example.androidschool.util.Status
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

        with(viewBinding) {
            fragmentSeasonsListAllEpisodes.setOnClickListener {
                val action = SeasonsListFragmentDirections.toAllEpisodes()
                navController.navigate(action)
            }
        }

        viewBinding.fragmentSeasonsListRefresh.setOnRefreshListener { viewModel.retry() }
    }

    private fun handleState(state: Status<List<SeasonListItemUI>>) {
        when (state) {
            is Status.Initial -> {
                hideAll()
                showLoading()
                viewModel.load()
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
        viewBinding.fragmentSeasonsListRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.fragmentSeasonsListRefresh.isRefreshing = false
    }

    private fun hideAll() {
        hideNoData()
        viewBinding.fragmentSeasonsListMainContent.visibility = View.GONE
    }

    private fun showErrorMessage() {
        Snackbar.make(
            viewBinding.seasonsListCoordinator,
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

    private fun showContent(data: List<SeasonListItemUI>) {
        hideNoData()
        viewBinding.fragmentSeasonsListMainContent.visibility = View.VISIBLE
        mAdapter.submitList(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _toolbarBinding = null
    }
}