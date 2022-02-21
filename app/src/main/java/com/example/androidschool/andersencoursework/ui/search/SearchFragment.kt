package com.example.androidschool.andersencoursework.ui.search

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentSearchBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.ui.core.recycler.CompositeAdapter
import com.example.androidschool.andersencoursework.ui.core.recycler.DiffComparable
import com.example.androidschool.andersencoursework.ui.edpisode.list.EpisodesListDelegateAdapter
import com.example.androidschool.andersencoursework.ui.search.filter.SearchFilter
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var resourceProvider: ResourceProvider

    @Inject
    lateinit var viewModelFactory: SearchViewModel.Factory
    private val viewModel: SearchViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var characterAdapter: CharacterSearchDelegateAdapter.Factory

    @Inject
    lateinit var episodeAdapter: EpisodesListDelegateAdapter.Factory

    private val mAdapter: CompositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(characterAdapter.create(::toCharacterDetails))
            .add(episodeAdapter.create(::toEpisodeDetails))
            .build()
    }

    private fun toCharacterDetails(id: Int) {
        val action = SearchFragmentDirections.toCharacterDetails(id)
        navController.navigate(action)
    }

    private fun toEpisodeDetails(id: Int) {
        val action = SearchFragmentDirections.toEpisodeDetails(id)
        navController.navigate(action)
    }

    private fun toFilter(filter: SearchFilter) {
        val action = SearchFragmentDirections.toFilter(filter)
        navController.navigate(action)
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun initBinding(view: View): FragmentSearchBinding = FragmentSearchBinding.bind(view)

    override fun initFragment() {

        registerResultListener()

        setupToolbar(viewBinding.fragmentSearchToolbar)

        initSearchResultList()

        with(viewBinding) {
            initSearch(
                fragmentSearchInput,
                fragmentSearchClearBtn,
                ::search
            )
        }

        collectStates()
        viewBinding.fragmentSearchFilterBtn.setOnClickListener { toFilter(SearchFilter(viewModel.currentFilter)) }
        viewBinding.searchFragmentRefreshContainer.setOnRefreshListener { viewModel.retry() }
    }

    private fun registerResultListener() {
        setFragmentResultListener("FILTER") { key, bundle ->
            val filter = bundle.getParcelable<SearchFilter>("FILTER")
            filter?.let {
                viewModel.filter(filter.filter)
                Log.e("FILTER", "$filter")
            }
        }
    }

    private fun initSearchResultList() {
        with(viewBinding.fragmentSearchResultsList) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(mContext)
            addItemDecoration(
                OffsetRecyclerDecorator(
                    resourceProvider.resources.getDimension(R.dimen.app_offset_small).toInt(),
                    this.layoutManager as LinearLayoutManager,
                    false
                )
            )
        }
    }

    private fun collectStates() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state ->
                handleState(state)
            }
        }
    }

    private fun handleState(state: SearchState<DiffComparable>) {
        Log.e("SEARCH STATE", "$state")
        when (state) {
            is SearchState.Loading -> handleLoading()
            is SearchState.Initial -> handleInitial()
            is SearchState.Success -> handleSuccess(state.data)
            is SearchState.Error -> handleError(state.data)
            is SearchState.EmptySuccess -> handleEmptySuccess()
            is SearchState.EmptyError -> handleEmptyError()
        }
    }

    private fun handleInitial() {
        hideAll()
    }

    private fun handleLoading() {
        showLoading()
    }

    private fun handleSuccess(data: List<DiffComparable>) {
        hideLoading()
        hideAll()
        showContent(data)
    }

    private fun handleError(data: List<DiffComparable>) {
        hideLoading()
        hideAll()
        showContent(data)
        showErrorMessage()
    }

    private fun showContent(data: List<DiffComparable>) {
        hideNoData()
        viewBinding.fragmentSearchResultsList.visibility = View.VISIBLE

        mAdapter.submitList(data)
    }

    private fun handleEmptySuccess() {
        hideLoading()
        hideAll()
        showNoData()
    }

    private fun handleEmptyError() {
        hideLoading()
        hideAll()
        showNoData()
        showErrorMessage()
    }

    private fun showErrorMessage() {
        Snackbar.make(
            viewBinding.searchFragmentCoordinator,
            R.string.default_error_message,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.retry_btn_title) { viewModel.retry() }
            .show()
    }

    private fun hideAll() {
        hideNoData()
        viewBinding.fragmentSearchResultsList.visibility = View.GONE
    }

    private fun showNoData() {
        viewBinding.errorBlock.visibility = View.VISIBLE
    }

    private fun hideNoData() {
        viewBinding.errorBlock.visibility = View.GONE
    }

    private fun showLoading() {
        viewBinding.searchFragmentRefreshContainer.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.searchFragmentRefreshContainer.isRefreshing = false
    }

    private fun search(query: String) {
        viewModel.search(query)
    }

    private fun initSearch(
        searchEditText: EditText,
        clearBtn: View,
        actionSearch: (String) -> Unit
    ) {

        searchEditText.requestFocus()
        showSoftKeyboard(searchEditText, searchEditText.context)
        searchEditText.addTextChangedListener(object : TextWatcher {
            private var prevQuery = ""
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {

                text?.trim()?.apply {
                    val query = this.toString()
                    if (prevQuery == query) return

                    prevQuery = query

                    lifecycleScope.launch {
                        delay(500)
                        if (prevQuery != query) return@launch
                        actionSearch(prevQuery)
                    }
                }
            }

            override fun beforeTextChanged(
                text: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) = Unit

            override fun afterTextChanged(text: Editable?) {
                clearBtn.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })

        initClearBtn(searchEditText, clearBtn)
    }

    private fun initClearBtn(searchEditText: EditText, clearBtn: View) {
        clearBtn.setOnClickListener {
            searchEditText.text.clear()
        }
    }

    /**
     * Function to show keyboard, when view is on focus
     */
    private fun showSoftKeyboard(view: View, context: Context) {
        if (view.requestFocus()) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}