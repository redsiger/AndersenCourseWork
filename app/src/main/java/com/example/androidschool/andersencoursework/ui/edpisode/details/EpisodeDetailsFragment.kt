package com.example.androidschool.andersencoursework.ui.edpisode.details

import android.content.Context
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentEpisodeDetailsBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import com.example.androidschool.andersencoursework.ui.characters.list.CharactersListDelegateAdapter
import com.example.androidschool.andersencoursework.ui.characters.models.CharacterListItemUI
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.ui.core.recycler.CompositeAdapter
import com.example.androidschool.andersencoursework.ui.edpisode.models.EpisodeDetailsUI
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator
import com.example.androidschool.andersencoursework.util.UIState
import com.example.androidschool.util.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class EpisodeDetailsFragment: BaseFragment<FragmentEpisodeDetailsBinding>(R.layout.fragment_episode_details) {

    @Inject lateinit var resourceProvider: ResourceProvider
    @Inject lateinit var mContext: Context

    private val args: EpisodeDetailsFragmentArgs by navArgs()
    private val episodeId by lazy { args.episodeId }

    @Inject lateinit var viewModelFactory: EpisodeDetailsViewModel.Factory
    private val viewModel: EpisodeDetailsViewModel by viewModels { viewModelFactory }

    @Inject lateinit var charactersAdapter: CharactersListDelegateAdapter.Factory
    private val mAdapter: CompositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(charactersAdapter.create(::toCharacterDetails))
            .build()
    }

    private fun toCharacterDetails(id: Int) {
        val action = EpisodeDetailsFragmentDirections.toCharacterDetailsGraph(id)
        navController.navigate(action)
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun initBinding(view: View): FragmentEpisodeDetailsBinding =
        FragmentEpisodeDetailsBinding.bind(view)

    override fun initFragment() {
        initBackBtn()
        initPage()
    }

    private fun initPage() {

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state -> handleState(state) }
        }

        with(viewBinding.episodeDetailsCharactersList) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                OffsetRecyclerDecorator(
                    margin =  resourceProvider.resources.getDimension(
                        R.dimen.app_offset_small
                    ).toInt(),
                    layoutManager = layoutManager as LinearLayoutManager,
                    inDp = false
                )
            )
        }

        viewBinding.episodeDetailsRefreshContainer.setOnRefreshListener { viewModel.retry() }
    }

    private fun initBackBtn() {
        viewBinding.fragmentEpisodeDetailsBackBtn.setOnClickListener { navController.navigateUp() }
    }

    private fun handleState(state: EpisodeDetailsState) {
        handleEpisode(state.episode)
        handleCharacters(state.characters)
    }

    private fun handleEpisode(episode: Status<EpisodeDetailsUI>) {
        when (episode) {
            is Status.Empty -> {
                hideAll()
                showLoading()
                viewModel.load(episodeId)
            }
            is Status.EmptyError -> {
                hideAll()
                showNoData()
                hideLoading()
                showErrorMessage()
            }
            is Status.Success -> {
                hideLoading()
                showContent(episode.extractData)
            }
            is Status.Error -> {
                hideLoading()
                showContent(episode.extractData)
                showErrorMessage()
            }
        }
    }

    private fun handleCharacters(characters: Status<List<CharacterListItemUI>>) {
        when (characters) {
            is Status.Success -> {
                showCharacters(characters.extractData)
            }
            is Status.Error -> {
                showCharacters(characters.extractData)
                showErrorMessage()
            }
            else -> showNoCharacters()
        }
    }

    private fun showCharacters(data: List<CharacterListItemUI>) {
        if (data.isEmpty()) {
            showNoCharacters()
        } else {
            viewBinding.episodeDetailsCharactersListNoData.visibility = View.GONE
            mAdapter.submitList(data)
        }
    }

    private fun showNoCharacters() {
        viewBinding.episodeDetailsCharactersListNoData.visibility = View.VISIBLE
    }

    private fun showNoData() {
        viewBinding.errorBlock.visibility = View.VISIBLE
    }

    private fun hideNoData() {
        viewBinding.errorBlock.visibility = View.GONE
    }

    private fun showErrorMessage() {
        Snackbar.make(
            viewBinding.episodeDetailsCoordinator,
            R.string.default_error_message,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.retry_btn_title) { viewModel.retry() }
            .show()
    }

    private fun hideAll() {
        hideNoData()
        viewBinding.episodeDetailsMainContent.visibility = View.GONE
    }


    private fun showLoading() {
        viewBinding.episodeDetailsRefreshContainer.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.episodeDetailsRefreshContainer.isRefreshing = false
    }

    private fun showContent(episode: EpisodeDetailsUI) {
        hideNoData()
        viewBinding.episodeDetailsMainContent.visibility = View.VISIBLE
        with(viewBinding) {

            episodeDetailsTitle.text = episode.title
            episodeDetailsSeason.text = episode.season
            episodeDetailsEpisode.text = episode.episode
        }
    }
}