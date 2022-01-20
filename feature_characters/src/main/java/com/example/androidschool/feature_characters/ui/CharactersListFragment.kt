package com.example.androidschool.feature_characters.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.androidschool.feature_characters.R
import com.example.androidschool.feature_characters.databinding.FragmentCharactersListBinding
import com.example.androidschool.feature_characters.di.CharactersComponent
import com.example.androidschool.feature_characters.di.CharactersComponentProvider
import com.example.androidschool.feature_characters.domain.usecase.GetCharactersPagingUseCase
import com.example.androidschool.feature_characters.ui.model.CharacterUIMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersListFragment: Fragment(R.layout.fragment_characters_list) {

    lateinit var charactersComponent: CharactersComponent

    @Inject
    lateinit var getCharactersUseCase: GetCharactersPagingUseCase

    private var _binding: FragmentCharactersListBinding? = null
    private val viewBinding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        charactersComponent = (requireActivity().applicationContext as CharactersComponentProvider).provideCharactersComponent()
        charactersComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharactersListBinding.inflate(inflater, container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Service", "$getCharactersUseCase")

        lifecycleScope.launch {
            viewBinding.characters.text = getCharactersUseCase.execute()
                .map {
                    CharacterUIMapper().fromDomain(it)
                }.toString()
        }
    }
}