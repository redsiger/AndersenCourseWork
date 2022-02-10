package com.example.androidschool.andersencoursework.ui.characters.list

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
import com.example.androidschool.andersencoursework.databinding.FragmentCharactersListBinding
import com.example.androidschool.andersencoursework.databinding.MergeToolbarBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.andersencoursework.ui.characters.models.ListItem
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.ui.core.initRecyclerPaging
import com.example.androidschool.andersencoursework.util.InfiniteScrollListener
import com.example.androidschool.andersencoursework.util.UIStatePaging
import javax.inject.Inject

class CharactersListFragment: BaseFragment(R.layout.fragment_characters_list) {

    private var _binding: FragmentCharactersListBinding? = null
    private val viewBinding get() = _binding!!
    private var _toolbarBinding: MergeToolbarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private val mNavController: NavController by lazy { findNavController() }

    private val mScrollListener: InfiniteScrollListener by lazy {
        InfiniteScrollListener({ viewModel.loadNewPage() }, viewBinding.fragmentCharactersListRecycler.layoutManager as LinearLayoutManager)
    }

    @Inject
    lateinit var viewModelFactory: CharactersListStateViewModel.Factory
    private val viewModel: CharactersListStateViewModel by viewModels { viewModelFactory }

    @Inject lateinit var pagingAdapterFactory: CharactersListPagingAdapter.Factory
    private val mPagingAdapter: CharactersListPagingAdapter by lazy {
        pagingAdapterFactory.create(
            { id: Int ->
                val action = CharactersListFragmentDirections.fromListToDetails(id)
                mNavController.navigate(action)
            },
            { viewModel.loadNewPage() }
        )
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharactersListBinding.bind(view)
        _toolbarBinding = MergeToolbarBinding.bind(viewBinding.root)

        initToolbar()
        initList()
        addListRefresher()
        hideAll()
    }

    private fun initToolbar() {

        setupMainToolbar(
            toolbar = toolbarBinding.toolbar,
            title = getString(R.string.characters_fragment_title),
            menuId = R.menu.search_menu,
            onItemClick = { item: MenuItem ->
                when(item.itemId) {
                    R.id.menu_item_search -> {
                        val action =
                            CharactersListFragmentDirections.actionGlobalToSearch()
                        mNavController.navigate(action)
                        true
                    }
                    else -> false
                }
            }
        )
    }

    private fun initList() {
        with(viewBinding.fragmentCharactersListRecycler) {
            adapter = mPagingAdapter
            initRecyclerPaging(R.dimen.list_item_character_img_width, requireContext()
            ) { viewModel.loadNewPage() }
        }

        addScrollListener()
        collectStates()
    }

    private fun collectStates() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state -> handleState(state) }
        }
    }

    private fun addScrollListener() {
        with(viewBinding.fragmentCharactersListRecycler) {
            addOnScrollListener(mScrollListener)
        }
    }

    private fun addListRefresher() {
        viewBinding.fragmentCharactersListRefresh.setOnRefreshListener { viewModel.refresh() }
        viewBinding.errorBlock.fragmentEmptyErrorRetryBtn.setOnClickListener { viewModel.refresh() }
    }

    private fun handleState(state: UIStatePaging<CharacterUIEntity>) {
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

    private fun handleEmptyLoading(state: UIStatePaging.EmptyLoading<CharacterUIEntity>) {
        hideAll()
        showLoading()
        viewModel.refresh()
    }

    private fun handleEmptyError(state: UIStatePaging.EmptyError<CharacterUIEntity>) {
        hideAll()
        showEmptyError(state.error)
    }

    private fun handleEmptyData(state: UIStatePaging.EmptyData<CharacterUIEntity>) {
        hideAll()
        showEmptyData()
    }

    private fun handlePartialData(state: UIStatePaging.PartialData<CharacterUIEntity>) {
        hideLoading()
        showPartialData(state.data)
    }

    private fun handleLoadingPartialData(state: UIStatePaging.LoadingPartialData<CharacterUIEntity>) {
        hideLoading()
        showPartialDataLoading(state.data)
    }

    private fun handleLoadingPartialDataError(state: UIStatePaging.LoadingPartialDataError<CharacterUIEntity>) {
        hideLoading()
        showPartialDataError(state.data)
    }

    private fun handleAllData(state: UIStatePaging.AllData<CharacterUIEntity>) {
        hideLoading()
        showAllData(state.data)
    }

    private fun handleRefresh(state: UIStatePaging.Refresh<CharacterUIEntity>) {
        hideAll()
        mPagingAdapter.submitList(state.data)
        showLoading()
        mScrollListener.reset()
    }

    private fun hideAll() {
        hideLoading()
        viewBinding.fragmentCharactersListRefreshContainer.children.forEach {
            it.visibility = View.GONE
        }
    }

    private fun showLoading() {
        viewBinding.fragmentCharactersListRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.fragmentCharactersListRefresh.isRefreshing = false
    }

    private fun showEmptyData() {
        viewBinding.errorBlock.root.visibility = View.VISIBLE
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.GONE
        viewBinding.errorBlock.fragmentEmptyDataMessage.visibility = View.VISIBLE
        viewBinding.errorBlock.fragmentEmptyDataMessage.text = getString(R.string.default_emptyData_message)
    }

    private fun showEmptyError(error: Exception) {
        viewBinding.errorBlock.root.visibility = View.VISIBLE
        viewBinding.errorBlock.fragmentEmptyDataMessage.visibility = View.GONE
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.VISIBLE
        viewBinding.errorBlock.fragmentEmptyErrorTitle.text = getString(R.string.default_error_message)
    }

    private fun showPartialData(data: List<ListItem<CharacterUIEntity>>) {
        viewBinding.fragmentCharactersListRecycler.visibility = View.VISIBLE
        mPagingAdapter.submitList(data)
    }

    private fun showPartialDataLoading(data: List<ListItem<CharacterUIEntity>>) {
        viewBinding.fragmentCharactersListRecycler.visibility = View.VISIBLE
        mPagingAdapter.submitList(data)
    }

    private fun showPartialDataError(data: List<ListItem<CharacterUIEntity>>) {
        viewBinding.fragmentCharactersListRecycler.visibility = View.VISIBLE
        mPagingAdapter.submitList(data)
    }

    private fun showAllData(data: List<ListItem<CharacterUIEntity>>) {
        viewBinding.fragmentCharactersListRecycler.visibility = View.VISIBLE
        mPagingAdapter.submitList(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toolbarBinding = null
    }
}