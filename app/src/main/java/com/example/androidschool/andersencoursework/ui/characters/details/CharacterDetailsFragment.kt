package com.example.androidschool.andersencoursework.ui.characters.details

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentCharacterDetailsBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterDetailsUI
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.util.UIState
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class CharacterDetailsFragment: BaseFragment(R.layout.fragment_character_details) {

    private var _binding: FragmentCharacterDetailsBinding? = null
    private val viewBinding get() = _binding!!

    private val args: CharacterDetailsFragmentArgs by navArgs()
    private val characterId by lazy { args.characterId }

    private val mNavController: NavController by lazy { findNavController() }

    @Inject lateinit var viewModelFactory: CharacterDetailsViewModel.Factory
    private val viewModel: CharacterDetailsViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharacterDetailsBinding.bind(view)

        initBackBtn()
        initPage()
    }

    private fun initPage() {
        lifecycleScope.launchWhenStarted {
            viewModel.character.collectLatest { state -> handleState(state) }
        }
    }

    private fun handleState(state: UIState<CharacterDetailsUI>) {
        when (state) {
            is UIState.Success -> handleSuccess(state)
            is UIState.Error -> handleError(state)
            is UIState.InitialLoading -> handleInitialLoading(state)
            is UIState.Refresh -> handleRefresh(state)
        }
    }

    private fun handleInitialLoading(state: UIState.InitialLoading) {
        hideAll()
        showLoading()
        viewModel.loadCharacter(characterId)
    }

    private fun handleRefresh(state: UIState.Refresh<CharacterDetailsUI>) {
        showLoading()
        showContent(state.currentData)
    }

    private fun handleSuccess(state: UIState.Success<CharacterDetailsUI>) {
        hideLoading()
        showContent(state.data)
    }

    private fun handleError(state: UIState.Error<CharacterDetailsUI>) {
        hideLoading()
        showError(state.error, state.localData)
    }

    private fun hideAll() {
        hideErrorBlock()
        viewBinding.characterDetailsMainContent.visibility = View.GONE
    }

    private fun hideErrorBlock() {
        viewBinding.errorBlock.fragmentEmptyDataMessage.visibility = View.GONE
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.GONE
    }

    private fun showLoading() {
        viewBinding.characterDetailsRefreshContainer.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.characterDetailsRefreshContainer.isRefreshing = false
    }

    private fun showContent(data: CharacterDetailsUI) {
        hideErrorBlock()
        viewBinding.characterDetailsMainContent.visibility = View.VISIBLE
        with(viewBinding) {
            Glide.with(requireContext())
                .load(data.img)
                .centerCrop()
                .into(characterPhoto)
            characterNickname.text = data.nickname
            characterName.text = data.name
        }
    }

    private fun showError(error: Exception, localData: CharacterDetailsUI) {
        hideAll()
        showErrorBlock(error)
    }

    private fun showErrorBlock(error: Exception) {
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.VISIBLE
        viewBinding.errorBlock.fragmentEmptyErrorRetryBtn.setOnClickListener { refresh() }
    }

    private fun refresh() {
        showLoading()
        viewModel.loadCharacter(characterId)
    }

    private fun initBackBtn() {
        viewBinding.fragmentCharacterDetailsBackBtn.setOnClickListener { mNavController.navigateUp() }
    }
}