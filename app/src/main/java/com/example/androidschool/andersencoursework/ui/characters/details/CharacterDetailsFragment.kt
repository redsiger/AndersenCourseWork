package com.example.androidschool.andersencoursework.ui.characters.details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.androidschool.andersencoursework.databinding.FragmentCharacterDetailsBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.util.Status
import javax.inject.Inject

class CharacterDetailsFragment: Fragment() {

    private var _binding: FragmentCharacterDetailsBinding? = null
    private val viewBinding get() = _binding!!

    private val args: CharacterDetailsFragmentArgs by navArgs()
    private val characterId by lazy { args.characterId }

    @Inject lateinit var charactersInteractor: CharactersInteractor

    private val viewModel: CharacterDetailsViewModel by viewModels {
        CharacterDetailsViewModel.Factory(characterId, charactersInteractor)
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.character.observe(viewLifecycleOwner, {
            when (it) {
                is Status.Success -> {
                    viewBinding.characterName.text = it.data.name
                    Glide
                        .with(requireActivity())
                        .load(it.data.img)
                        .into(viewBinding.characterPhoto)
                }
                is Status.Error -> {
                    Log.e("ERROR", it.toString())
                    viewBinding.characterName.text = it.exception.message
                }
                else -> viewBinding.characterName.text = "something else"
            }
        })
    }
}