package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentEpisodesListBinding
import com.example.androidschool.andersencoursework.databinding.MergeToolbarBinding
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.util.InfiniteScrollListener
import javax.inject.Inject

class EpisodesListFragment: BaseFragment<FragmentEpisodesListBinding>(R.layout.fragment_episodes_list) {

    @Inject lateinit var viewModelFactory: EpisodesListViewModel.Factory
    private val viewModel: EpisodesListViewModel by viewModels { viewModelFactory }

    private var _toolbarBinding: MergeToolbarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private val mNavController: NavController by lazy { findNavController() }

    private val mScrollListener: InfiniteScrollListener by lazy {
        InfiniteScrollListener(
            action = viewModel::loadNewPage,
            layoutManager = viewBinding.fragmentEpisodesListRecycler.layoutManager as LinearLayoutManager
        )
    }

    override fun initBinding(view: View): FragmentEpisodesListBinding {
        return FragmentEpisodesListBinding.bind(view)
    }

    override fun initFragment() {}
}