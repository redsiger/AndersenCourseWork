package com.example.androidschool.andersencoursework.ui.edpisode.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidschool.andersencoursework.databinding.FragmentEpisodesListBinding
import com.example.androidschool.andersencoursework.databinding.MergeToolbarBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.core.BaseFragment

class EpisodesListFragment: BaseFragment() {

    private var _binding: FragmentEpisodesListBinding? = null
    private val viewBinding get() = _binding!!
    private var _toolbarBinding: MergeToolbarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEpisodesListBinding.inflate(inflater, container, false)
        _toolbarBinding = MergeToolbarBinding.bind(viewBinding.root)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbarTitle(toolbarBinding.toolbar, "Episodes")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toolbarBinding = null
    }
}