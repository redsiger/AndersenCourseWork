package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentEpisodesListBinding
import com.example.androidschool.andersencoursework.databinding.MergeToolbarBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.characters.models.ListItem
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.util.InfiniteScrollListener
import com.example.androidschool.util.UIState
import javax.inject.Inject

class EpisodesListFragment: BaseFragment(R.layout.fragment_episodes_list) {

    private var _binding: FragmentEpisodesListBinding? = null
    private val viewBinding get() = _binding!!
    private var _toolbarBinding: MergeToolbarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private val mNavController: NavController by lazy { findNavController() }

    @Inject lateinit var viewModelFactory: EpisodesListViewModel.Factory
    private val viewModel: EpisodesListViewModel by viewModels { viewModelFactory }

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

        viewModel.uiState.observe(viewLifecycleOwner) {
            render(it)
        }

        submitListListener()
    }

    private fun render(state: UIState<List<ListItem>>) {
        when (state.TAG) {
            "EMPTY_PROGRESS" -> {
                hideAll()
                showLoading()
                Log.e("EMPTY_PROGRESS", "${state.data}")
            }
            "EMPTY_DATA" -> {
                hideAll()
                showEmptyData()
                Log.e("EMPTY_DATA", "${state.data}")
            }
            "EMPTY_ERROR" -> {
                hideAll()
                showEmptyError()
                Log.e("EMPTY_ERROR", "${state.data}")
            }
            "DATA" -> {
                hideAll()
                showData(state.data)
                Log.e("DATA", "${state.data}")
            }
            "PAGE_PROGRESS" -> { Log.e("$this", "PAGE_PROGRESS") }
            "ALL_DATA" -> { Log.e("$this", "ALL_DATA") }
            "PAGE_ERROR" -> {
                Log.e("$this", "PAGE_ERROR")
            }
            "REFRESH" -> {

            }
            else -> Log.e("$this", "unexpected state: $state")
        }
    }

    private fun showData(data: List<ListItem>) {
        viewBinding.fragmentEpisodesListRecycler.visibility = View.VISIBLE
        mAdapter.submitList(data)
    }

    private fun showEmptyError() {
        viewBinding.fragmentEpisodesEmptyError.visibility = View.VISIBLE
    }

    private fun hideAll() {
        hideLoading()
        viewBinding.fragmentEpisodesListRefreshContainer.children.forEach {
            it.visibility = View.GONE
        }
    }

    private fun showEmptyData() {
        viewBinding.fragmentEpisodesEmptyData.visibility = View.VISIBLE
    }


    private fun showLoading() {
        viewBinding.fragmentEpisodesListRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.fragmentEpisodesListRefresh.isRefreshing = false
    }

    private fun submitListListener() {
        with(viewBinding.fragmentEpisodesListRecycler) {
            addOnScrollListener(
                InfiniteScrollListener({ viewModel.loadNewPage() }, layoutManager as LinearLayoutManager)
            )
        }


    }

    private fun submitListRefresh() {
        viewBinding.fragmentEpisodesListRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        viewBinding.fragmentEpisodesEmptyErrorRetryBtn.setOnClickListener { viewModel.refresh() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toolbarBinding = null
    }
}