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
import com.example.androidschool.data.database.CharactersDao
import com.example.androidschool.data.database.DatabaseMapper
import com.example.androidschool.data.database.model.CharacterRoomEntity
import com.example.androidschool.domain.characters.interactors.CharactersInteractor
import com.example.androidschool.domain.characters.model.CharacterEntity
import com.example.androidschool.util.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    @Inject
    lateinit var charactersDao: CharactersDao

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

        initCharacterList()

//        lifecycleScope.launch(Dispatchers.IO) {
//            val status = viewModel.getCharacter(1)
//            when (status) {
//                is Status.Success -> {
//                    val characterEntity = status.data
//                    printCharacterEntityInLog(characterEntity)
//                    saveCharacterEntity(characterEntity)
////                    delay(5000)
////                    printCharacterRoomInLog(charactersDao.getAll().first())
//                }
//            }
//            printCharacters()
//        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.getCharactersListPaging().observe(viewLifecycleOwner) {
                mAdapter.submitData(lifecycle, it)
            }
        }
    }

    private fun initCharacterList() {
        with(viewBinding.charactersListRecycler) {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private suspend fun saveCharacterEntity(character: CharacterEntity) {
        val mapper = DatabaseMapper()
        charactersDao.insertCharacter(mapper.mapCharacterEntity(character))
    }

//    private suspend fun printCharacters() {
//        val characters = charactersDao.getCharactersPaging(10, 0)
//        Log.e("DATABASE", characters.toString())
//    }
//
//    private fun printCharacterRoomInLog(character: CharacterRoomEntity) {
//        Log.e("printCharacterInLog", character.toString())
//    }
//
//    private fun printCharacterEntityInLog(character: CharacterEntity) {
//        Log.e("printCharacterInLog", character.toString())
//    }
}