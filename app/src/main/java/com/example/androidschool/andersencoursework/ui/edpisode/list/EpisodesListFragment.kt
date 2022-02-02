package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentEpisodesListBinding
import com.example.androidschool.andersencoursework.databinding.MergeToolbarBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.core.BaseFragment

class EpisodesListFragment: BaseFragment(R.layout.fragment_episodes_list) {

    private var _binding: FragmentEpisodesListBinding? = null
    private val viewBinding get() = _binding!!
    private var _toolbarBinding: MergeToolbarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEpisodesListBinding.bind(view)
        _toolbarBinding = MergeToolbarBinding.bind(viewBinding.root)

        setupToolbarTitle(toolbarBinding.toolbar, getString(R.string.episodes_fragment_title))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toolbarBinding = null
    }
}