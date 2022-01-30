package com.example.androidschool.andersencoursework.ui.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidschool.andersencoursework.databinding.FragmentFavoritesBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.core.BaseFragment

class FavoritesFragment: BaseFragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val viewBinding get() = _binding!!

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}