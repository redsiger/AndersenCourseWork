package com.example.androidschool.andersencoursework.ui.edpisode.details

import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentEpisodeDetailsBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.di.util.ResourceProvider
import com.example.androidschool.andersencoursework.ui.characters.details.CharacterDetailsState
import com.example.androidschool.andersencoursework.ui.characters.list.CharactersListDelegateAdapter
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.example.androidschool.andersencoursework.ui.core.recycler.CompositeAdapter
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator
import com.example.androidschool.andersencoursework.util.UIState
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
            .add(charactersAdapter.create {  })
            .build()
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
    }

    private fun initBackBtn() {
        viewBinding.fragmentEpisodeDetailsBackBtn.setOnClickListener { navController.navigateUp() }
    }

    private fun handleState(state: UIState<EpisodeDetailsState>) {
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
        viewModel.load(episodeId)
    }

    private fun handleRefresh(state: UIState.Refresh) {
        showLoading()
        viewModel.load(episodeId)
    }

    private fun handleSuccess(state: UIState.Success<EpisodeDetailsState>) {
        hideLoading()
        showContent(state.data)
        viewBinding.episodeDetailsRefreshContainer.setOnRefreshListener {
            viewModel.refresh(episodeId)
        }
    }

    private fun handleError(state: UIState.Error<EpisodeDetailsState>) {
        hideLoading()
        hideAll()
        showContent(state.data)
        showErrorMessage(state)
        viewBinding.episodeDetailsRefreshContainer.setOnRefreshListener {
            viewModel.refresh(episodeId)
        }
    }

    private fun showErrorMessage(state: UIState.Error<EpisodeDetailsState>) {
        Snackbar.make(
            viewBinding.root,
            resourceProvider.resources. getString(R.string.default_error_message),
            Snackbar.LENGTH_LONG
        ).setAction(
            R.string.refresh_btn_title,
            object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    viewModel.refresh(episodeId)
                }
            }
        ).show()
    }

    private fun hideAll() {
        hideErrorBlock()
        viewBinding.episodeDetailsMainContent.visibility = View.GONE
    }

    private fun hideErrorBlock() {
        viewBinding.errorBlock.fragmentEmptyDataMessage.visibility = View.GONE
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.GONE
    }

    private fun showLoading() {
        viewBinding.episodeDetailsRefreshContainer.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.episodeDetailsRefreshContainer.isRefreshing = false
    }

    private fun showContent(data: EpisodeDetailsState) {
        hideErrorBlock()
        viewBinding.episodeDetailsMainContent.visibility = View.VISIBLE
        with(viewBinding) {

            episodeDetailsTitle.text = data.episode.title
            episodeDetailsSeason.text = data.episode.season
            episodeDetailsEpisode.text = data.episode.episode

            Log.e("CharactersInEpisode", "${data.characters}")
            mAdapter.submitList(data.characters)
        }
    }

    private fun showError(error: Exception) {
        hideAll()
        showErrorBlock(error)
    }

    private fun showErrorBlock(error: Exception) {
        hideLoading()
        viewBinding.errorBlock.fragmentEmptyError.visibility = View.VISIBLE
        viewBinding.episodeDetailsRefreshContainer.setOnRefreshListener {
            viewModel.retry(episodeId)
        }
        viewBinding.errorBlock.fragmentEmptyErrorRetryBtn.setOnClickListener {
            viewModel.retry(episodeId)
        }
    }

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }
}