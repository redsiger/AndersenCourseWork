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
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterDetailsUI
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.ui.core.recycler.CompositeAdapter
import com.example.androidschool.andersencoursework.ui.seasons.model.SeasonListItemUI
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator
import com.example.androidschool.util.Status
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
            viewModel.uiState.collectLatest { state ->
                handleState(state)
            }
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

        viewBinding.characterDetailsRefreshContainer.setOnRefreshListener {
            viewModel.retry()
        }
    }

    private fun handleState(state: CharacterDetailsState) {
        handleCharacter(state.character)
        handleAppearance(state.appearance)
    }

    private fun handleCharacter(character: Status<CharacterDetailsUI>) {
        when (character) {
            is Status.Initial -> {
                hideAll()
                showLoading()
                viewModel.load(characterId)
            }
            is Status.EmptyError -> {
                hideAll()
                showNoData()
                hideLoading()
                showErrorMessage()
            }
            is Status.Success -> {
                hideLoading()
                showContent(character.extractData)
            }
            is Status.Error -> {
                hideLoading()
                showContent(character.extractData)
                showErrorMessage()
            }
        }
    }

    private fun handleAppearance(appearance: Status<List<SeasonListItemUI>>) {
        when (appearance) {
            is Status.Success -> {
                showAppearance(appearance.extractData)
            }
            is Status.Error -> {
                showAppearance(appearance.extractData)
                showErrorMessage()
            }
            else -> showNoAppearance()
        }
    }

    private fun showAppearance(data: List<SeasonListItemUI>) {
        if (data.isEmpty()) {
            showNoAppearance()
        } else {
            viewBinding.characterSeriesAppearanceListNoData.visibility = View.GONE
            mAdapter.submitList(data)
        }
    }

    private fun showNoAppearance() {
        viewBinding.characterSeriesAppearanceListNoData.visibility = View.VISIBLE
    }

    private fun showNoData() {
        viewBinding.errorBlock.visibility = View.VISIBLE
    }

    private fun hideNoData() {
        viewBinding.errorBlock.visibility = View.GONE
    }

    private fun showErrorMessage() {
        Snackbar.make(
            viewBinding.characterDetailsCoordinator,
            R.string.default_error_message,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.retry_btn_title) { viewModel.retry() }
            .show()
    }

    private fun hideAll() {
        hideNoData()
        viewBinding.characterDetailsMainContent.visibility = View.GONE
    }

    private fun showLoading() {
        viewBinding.characterDetailsRefreshContainer.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.characterDetailsRefreshContainer.isRefreshing = false
    }

    private fun showContent(data: CharacterDetailsUI) {
        hideNoData()
        viewBinding.characterDetailsMainContent.visibility = View.VISIBLE
        with(viewBinding) {
            with(data) {
                glide
                    .load(img)
                    .placeholder(R.drawable.splashscreen)
                    .error(R.drawable.ic_person)
                    .centerCrop()
                    .into(characterPhoto)

                characterNickname.text =
                    resourceProvider.resources.getString(
                        R.string.characters_details_nickname,
                        nickname
                    )

                characterName.text = name
            }
        }
    }

    private fun initBackBtn() {
        viewBinding.fragmentCharacterDetailsBackBtn
            .setOnClickListener { navController.navigateUp() }
    }
}