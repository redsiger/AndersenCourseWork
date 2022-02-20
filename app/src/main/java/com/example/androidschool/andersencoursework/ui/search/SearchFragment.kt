package com.example.androidschool.andersencoursework.ui.search

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentSearchBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    @Inject
    lateinit var viewModelFactory: SearchViewModel.Factory
    private val viewModel: SearchViewModel by viewModels { viewModelFactory }

    private val mAdapter: SearchAdapter by lazy { SearchAdapter() }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun initBinding(view: View): FragmentSearchBinding = FragmentSearchBinding.bind(view)

    override fun initFragment() {
        setupToolbar(viewBinding.fragmentSearchToolbar)
        initSearchResultList()

        with(viewBinding) {
            initSearch(
                fragmentSearchInput,
                fragmentSearchClearBtn,
                this@SearchFragment::search
            )
        }

        viewBinding.fragmentSearchFilterBtn.setOnClickListener { toFilter() }
    }

    override fun onPause() {
        Log.e("$this", "PAUSE")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.e("$this", "RESUME")
        val input = viewBinding.fragmentSearchInput
        input.requestFocus()
        showSoftKeyboard(input, input.context)
    }

    private fun toFilter() {
        val action = SearchFragmentDirections.toFilter()
        navController.navigate(action)
    }

    private fun initSearchResultList() {
//        setupGridLayoutManager(
//            viewBinding.fragmentSearchResultsList,
//            mAdapter,
//            requireContext().resources.getDimension(R.dimen.list_item_character_img_width)
//        )
    }

    private fun search(query: String) {
        lifecycleScope.launch {
            viewModel.getSearchResults(query).collectLatest { list ->
                list.forEach {
                    Log.e("SEARCH", it.toString())
                }
//                mAdapter.setList(list)
            }
        }
    }

    private fun initSearch(
        searchEditText: EditText,
        clearBtn: View,
        actionSearch: (String) -> Unit
    ) {

        searchEditText.requestFocus()
        showSoftKeyboard(searchEditText, searchEditText.context)
        searchEditText.addTextChangedListener(object : TextWatcher {
            private var _query = ""
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {

                text?.trim()?.apply {
                    val query = this.toString()
                    if (_query == query || query.isBlank()) return

                    _query = query

                    lifecycleScope.launch {
                        delay(1000)
                        if (_query != query) return@launch
                        actionSearch(_query)
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