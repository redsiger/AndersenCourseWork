package com.example.androidschool.andersencoursework.ui.characters.details

import android.content.Context
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentCharacterDetailsBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.ui.core.recycler.CompositeAdapter
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator
import com.example.androidschool.andersencoursework.util.UIState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class CharacterDetailsFragment :
    BaseFragment<FragmentCharacterDetailsBinding>(R.layout.fragment_character_details) {

    @Inject
    lateinit var resourceProvider: ResourceProvider
    @Inject
    lateinit var mContext: Context
    @Inject
    lateinit var glide: RequestManager

    private val args: CharacterDetailsFragmentArgs by navArgs()
    private val characterId by lazy { args.characterId }

    @Inject
    lateinit var viewModelFactory: CharacterDetailsViewModel.Factory
    private val viewModel: CharacterDetailsViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var appearanceAdapter: CharacterAppearanceDelegateAdapter.Factory
    private val mAdapter: CompositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(appearanceAdapter.create(::navigateToSeason))
            .build()
    }

    private fun navigateToSeason(season: String) {
        val bundle = bundleOf("season" to season)
        navController.navigate(R.id.action_global_to_season_details, bundle)
    }

    override fun initBinding(view: View): FragmentCharacterDetailsBinding =
        FragmentCharacterDetailsBinding.bind(view)

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
            viewModel.uiState.collectLatest { state -> handleState(state) }
        }
        with(viewBinding.characterSeriesAppearanceList) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                OffsetRecyclerDecorator(
                    margin = resourceProvider.resources.getDimension(
                        R.dimen.app_offset_small
                    ).toInt(),
                    layoutManager = layoutManager as LinearLayoutManager,
                    inDp = false
                )
            )
        }
    }

    private fun handleState(state: UIState<CharacterDetailsState>) {
        when (state) {
            is UIState.Success -> handleSuccess(state)
            is UIState.Error -> handleError(state)
            is UIState.EmptyError -> handleEmptyError()
            is UIState.InitialLoading -> handleInitialLoading()
            is UIState.Refresh -> handleRefresh()
        }
    }

    private fun handleEmptyError() {
        hideAll()
        showError()
    }

    private fun handleInitialLoading() {
        hideAll()
        showLoading()
        viewModel.load(characterId)
    }

    private fun handleRefresh() {
        showLoading()
        viewModel.load(characterId)
    }

    private fun handleSuccess(state: UIState.Success<CharacterDetailsState>) {
        hideLoading()
        showContent(state.data)
        viewBinding.characterDetailsRefreshContainer.setOnRefreshListener {
            viewModel.refresh(characterId)
        }
    }

    private fun handleError(state: UIState.Error<CharacterDetailsState>) {
        hideLoading()
        hideAll()
        showContent(state.data)
        showErrorMessage()
        viewBinding.characterDetailsRefreshContainer.setOnRefreshListener {
            viewModel.refresh(characterId)
        }
    }

    private fun showErrorMessage() {
        Snackbar.make(
            viewBinding.root,
            resourceProvider.resources.getString(R.string.default_error_message),
            Snackbar.LENGTH_LONG
        ).setAction(
            R.string.refresh_btn_title
        ) { viewModel.refresh(characterId) }.show()
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

    private fun showContent(data: CharacterDetailsState) {
        hideErrorBlock()
        viewBinding.characterDetailsMainContent.visibility = View.VISIBLE
        with(viewBinding) {
            with(data) {
                glide
                    .load(character.img)
                    .centerCrop()
                    .into(characterPhoto)

                characterNickname.text =
                    resourceProvider.resources.getString(
                        R.string.characters_details_nickname,
                        character.nickname
                    )

                characterName.text = character.name

                mAdapter.submitList(appearance)
            }
        }
    }

    private fun showError() {
        hideAll()
        showErrorBlock()
    }

    private fun showErrorBlock() {
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
        viewBinding.fragmentCharacterDetailsBackBtn
            .setOnClickListener { navController.navigateUp() }
    }
}