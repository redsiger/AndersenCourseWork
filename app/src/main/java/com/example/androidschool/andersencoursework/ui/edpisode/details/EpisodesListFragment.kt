package com.example.androidschool.andersencoursework.ui.edpisode.details

import android.view.View
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentEpisodesListBinding
import com.example.androidschool.andersencoursework.ui.core.BaseFragment

class EpisodesListFragment: BaseFragment<FragmentEpisodesListBinding>(R.layout.fragment_episodes_list) {

    override fun initBinding(view: View): FragmentEpisodesListBinding {
        return FragmentEpisodesListBinding.bind(view)
    }

    override fun initFragment() {}
}