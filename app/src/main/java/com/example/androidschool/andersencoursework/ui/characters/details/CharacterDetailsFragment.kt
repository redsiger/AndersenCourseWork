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
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterDetailsUI
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.util.UIState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class CharacterDetailsFragment: BaseFragment<FragmentCharacterDetailsBinding>(R.layout.fragment_character_details) {

    @Inject lateinit var resources: ResourceProvider
    @Inject lateinit var mContext: Context

    private val args: CharacterDetailsFragmentArgs by navArgs()
    private val characterId by lazy { args.characterId }

    private val mNavController: NavController by lazy { findNavController() }

    @Inject lateinit var viewModelFactory: CharacterDetailsViewModel.Factory
    private val viewModel: CharacterDetailsViewModel by viewModels { viewModelFactory }

    override fun initBinding(view: View): FragmentCharacterDetailsBinding = FragmentCharacterDetailsBinding.bind(view)

    override fun initFragment() {
        initBackBtn()
        initPage()
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
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
            is UIState.EmptyError -> handleEmptyError(state)
            is UIState.InitialLoading -> handleInitialLoading(state)
            is UIState.Refresh -> handleRefresh(state)
        }
    }

    private fun handleEmptyError(state: UIState.EmptyError) {
        hideAll()
        showError(state.error)
    }

    private fun handleInitialLoading(state: UIState.InitialLoading) {
        hideAll()
        showLoading()
        viewModel.loadCharacter(characterId)
    }

    private fun handleRefresh(state: UIState.Refresh<CharacterDetailsUI>) {
        showLoading()
        showContent(state.data)
        viewModel.loadCharacter(characterId)
    }

    private fun handleSuccess(state: UIState.Success<CharacterDetailsUI>) {
        hideLoading()
        showContent(state.data)
        viewBinding.characterDetailsRefreshContainer.setOnRefreshListener {
            viewModel.refresh(state.data, characterId)
        }
    }

    private fun handleError(state: UIState.Error<CharacterDetailsUI>) {
        hideLoading()
        hideAll()
        showContent(state.data)
        showErrorMessage(state.data)
        viewBinding.characterDetailsRefreshContainer.setOnRefreshListener {
            viewModel.refresh(state.data, characterId)
        }
    }

    private fun showErrorMessage(data: CharacterDetailsUI) {
        Snackbar.make(
            viewBinding.root,
            resources.getString(R.string.default_error_message),
            Snackbar.LENGTH_LONG)
            .setAction(
                R.string.refresh_btn_title,
                object: View.OnClickListener {
                    override fun onClick(p0: View?) {
                        viewModel.refresh(data, characterId)
                    }
                })
            .show()
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

    private fun showError(error: Exception) {
        hideAll()
        showErrorBlock(error)
    }

    private fun showErrorBlock(error: Exception) {
        hideLoading()
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.VISIBLE
        viewBinding.characterDetailsRefreshContainer.setOnRefreshListener {
            viewModel.retry(characterId)
        }
        viewBinding.errorBlock.fragmentEmptyErrorRetryBtn.setOnClickListener {
            viewModel.retry(characterId)
        }
    }

    private fun initBackBtn() {
        viewBinding.fragmentCharacterDetailsBackBtn.setOnClickListener { mNavController.navigateUp() }
    }
}