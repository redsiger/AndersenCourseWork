package com.example.androidschool.andersencoursework.ui.favorites

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentFavoritesBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.core.BaseFragment

class FavoritesFragment: BaseFragment<FragmentFavoritesBinding>(R.layout.fragment_favorites) {

    override fun initBinding(view: View): FragmentFavoritesBinding = FragmentFavoritesBinding.bind(view)

    override fun initFragment() { }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }
}