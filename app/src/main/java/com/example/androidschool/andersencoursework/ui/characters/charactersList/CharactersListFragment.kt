package com.example.androidschool.andersencoursework.ui.characters.charactersList

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidschool.andersencoursework.databinding.FragmentCharactersListBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.BaseFragment
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharactersListFragment: BaseFragment() {

    companion object {
        private const val ARG_ID = 1
    }

    private var _binding: FragmentCharactersListBinding? = null
    private val viewBinding get() = _binding!!

    @Inject
    lateinit var charactersInteractor: CharactersInteractor

    private val viewModel: CharactersListViewModel by viewModels {
        CharactersListViewModel.Factory(charactersInteractor)
    }
//    @Inject
//    lateinit var viewModelFactory: CharactersListViewModel.Factory.Factory

    private val mAdapter: CharactersListPagingAdapter by lazy {
        CharactersListPagingAdapter()
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
        _binding = FragmentCharactersListBinding.inflate(inflater, container,false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Service", "$viewModel")

        with(viewBinding.charactersListRecycler) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.getCharactersListPaging().observe(viewLifecycleOwner) {
                mAdapter.submitData(lifecycle, it)
            }
        }
    }
}