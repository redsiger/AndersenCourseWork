package com.example.androidschool.andersencoursework.ui.favorites

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentFavoritesBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.core.BaseFragment

class FavoritesFragment: BaseFragment(R.layout.fragment_favorites) {

    private var _binding: FragmentFavoritesBinding? = null
    private val viewBinding get() = _binding!!

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)
    }
}