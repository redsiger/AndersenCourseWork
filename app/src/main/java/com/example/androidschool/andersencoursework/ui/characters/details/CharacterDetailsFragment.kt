package com.example.androidschool.andersencoursework.ui.characters.details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.androidschool.andersencoursework.databinding.FragmentCharacterDetailsBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.util.Status
import javax.inject.Inject

class CharacterDetailsFragment: Fragment() {

    private var _binding: FragmentCharacterDetailsBinding? = null
    private val viewBinding get() = _binding!!

    private val args: CharacterDetailsFragmentArgs by navArgs()
    private val characterId by lazy {args.characterId}

    @Inject lateinit var viewModelFactory: CharacterDetailsViewModel.Factory
    private val viewModel: CharacterDetailsViewModel by viewModels { viewModelFactory }

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

        initBackBtn()
        initRefreshContainer()
        viewModel.getCharacter(characterId)

        viewModel.character.observe(viewLifecycleOwner) {
            when (it) {
                is Status.Success -> {
                    viewBinding.characterDetailsRefreshContainer.isRefreshing = false
                    viewBinding.characterName.text = it.data.name
                    viewBinding.characterNickname.text = it.data.nickname
                    Glide
                        .with(requireActivity())
                        .load(it.data.img)
                        .centerCrop()
                        .into(viewBinding.characterPhoto)
                }
                is Status.Error -> {
                    Log.e("ERROR", it.toString())
                    viewBinding.characterName.text = it.exception.message
                }
                else -> {
                    viewBinding.characterDetailsRefreshContainer.isRefreshing = true
                }
            }
        }
    }

    private fun initRefreshContainer() {
        viewBinding.characterDetailsRefreshContainer.setOnRefreshListener {
            viewModel.getCharacter(characterId)
        }
    }

    private fun initBackBtn() {
        viewBinding.characterDetailsBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }


}