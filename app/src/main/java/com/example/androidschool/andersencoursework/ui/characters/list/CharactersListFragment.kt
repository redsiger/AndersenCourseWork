package com.example.androidschool.andersencoursework.ui.characters.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentCharactersListBinding
import com.example.androidschool.andersencoursework.databinding.MergeToolbarBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterUIEntity
import com.example.androidschool.andersencoursework.ui.characters.models.UIMapper
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.ui.core.DefaultLoadStateAdapter
import com.example.androidschool.andersencoursework.ui.core.setupGridLayoutManager
import com.example.androidschool.andersencoursework.util.TryAgainAction
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersListFragment: BaseFragment(R.layout.fragment_characters_list) {

    companion object {
        private const val ARG_ID = 1
    }

    private var _binding: FragmentCharactersListBinding? = null
    private val viewBinding get() = _binding!!
    private var _toolbarBinding: MergeToolbarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private val mapper = UIMapper()

    @Inject
    lateinit var viewModelFactory: CharactersListViewModel.Factory
    private val viewModel: CharactersListViewModel by viewModels { viewModelFactory }

    private val mAdapter: CharactersListPagingAdapter by lazy {
        CharactersListPagingAdapter { id: Int ->
            val action = CharactersListFragmentDirections.actionCharactersListToDetails(id)
            findNavController().navigate(action)
        }
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharactersListBinding.bind(view)
        _toolbarBinding = MergeToolbarBinding.bind(view)

        setupToolbar(
            toolbarBinding.toolbar,
            getString(R.string.characters_fragment_title),
            R.menu.search_menu
        ) {
            when (it.itemId) {
                R.id.menu_item_search -> {
                    Log.e("MENU ITEM", "$it CLICKED")
                    val action = CharactersListFragmentDirections.actionGlobalToSearch()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }

        initCharacterList()
    }

    private fun initCharacterList() {
        val action: TryAgainAction = { mAdapter.retry() }

        val footerAdapter = com.example.androidschool.andersencoursework.util.DefaultLoadStateAdapter(action)

        with(viewBinding.fragmentCharactersListRecycler) {
            val itemWidth = context.resources.getDimension(R.dimen.list_item_character_img_width)
            setupGridLayoutManager(
                this,
                mAdapter.withLoadStateFooter(footerAdapter),
                itemWidth
            )
        }
        with(viewBinding.fragmentCharactersListRefresh){
            setOnRefreshListener { updateList(this) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.charactersFlow.collectLatest {
                Log.e("Collected", "Collected")
                mAdapter.submitData(it)
            }
        }
    }

    private fun updateList(list: SwipeRefreshLayout) {
        list.isRefreshing = true
        mAdapter.refresh()
        list.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toolbarBinding = null
    }
}