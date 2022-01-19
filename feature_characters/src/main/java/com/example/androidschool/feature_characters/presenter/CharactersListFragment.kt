package com.example.androidschool.feature_characters.presenter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.androidschool.feature_characters.R
import com.example.androidschool.feature_characters.data.network.CharactersService
import com.example.androidschool.feature_characters.di.CharactersComponent
import com.example.androidschool.feature_characters.di.CharactersComponentProvider
import javax.inject.Inject

class CharactersListFragment: Fragment(R.layout.fragment_characters_list) {

    lateinit var charactersComponent: CharactersComponent

    @Inject
    lateinit var service: CharactersService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        charactersComponent = (requireActivity().applicationContext as CharactersComponentProvider).provideCharactersComponent()
        charactersComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Service", "$service")
    }
}