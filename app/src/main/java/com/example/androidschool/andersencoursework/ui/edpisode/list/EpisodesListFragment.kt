package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentEpisodesListBinding
import com.example.androidschool.andersencoursework.databinding.MergeToolbarBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.characters.list.CharactersListFragmentDirections
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.ui.core.setupGridLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        adapterFactory.create {}
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
    }

    override fun onStart() {
        super.onStart()
        setListListener()
    }

    private fun initToolbar() {

        val onItemClick = { item: MenuItem ->
            when(item.itemId) {
                R.id.menu_item_search -> {
                    val action = EpisodesListFragmentDirections.actionGlobalToSearch()
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

        context?.resources?.let {
            setupGridLayoutManager(
                list,
                mAdapter,
                it.getDimension(R.dimen.list_item_character_img_width)
            )
        }

        lifecycleScope.launch{
            viewModel.listData.collectLatest {
                Log.e("EpisodesFragment", "collect: ${it.size}")
                mAdapter.setList(it)
            }
        }
    }

    private fun setListListener() {
        val list = viewBinding.fragmentEpisodesListRecycler

        if (list.layoutManager != null) {
            viewBinding.fragmentEpisodesListRecycler.addOnScrollListener(object:
                RecyclerView.OnScrollListener() {
                val layoutManager = list.layoutManager as LinearLayoutManager
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemsCount = layoutManager.itemCount
                    val visibleItemCount = layoutManager.childCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    viewModel.onScrollLoad(totalItemsCount, visibleItemCount, lastVisibleItem)
                }
            })
        } else {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toolbarBinding = null
    }
}