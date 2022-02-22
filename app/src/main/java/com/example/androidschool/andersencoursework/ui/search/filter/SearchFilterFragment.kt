package com.example.androidschool.andersencoursework.ui.search.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.example.androidschool.andersencoursework.databinding.FragmentSearchFilterBinding
import com.example.androidschool.andersencoursework.ui.search.SearchFragment.Companion.FILTER_KEY
import com.example.androidschool.domain.search.ListItemType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SearchFilterFragment : BottomSheetDialogFragment() {

    companion object {
        const val FILTERS = "FILTERS"
    }

    private var _viewBinding: FragmentSearchFilterBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val args: SearchFilterFragmentArgs by navArgs()
    private val filter: SearchFilter by lazy { args.filter }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSearchFilterBinding.inflate(layoutInflater, container, false)

        initFragment()

        return viewBinding.root
    }


    private fun initFragment() {

        viewBinding.fragmentSearchFilterIsCharacter.isChecked =
            filter.filter.contains(ListItemType.CHARACTER)
        viewBinding.fragmentSearchFilterIsEpisode.isChecked =
            filter.filter.contains(ListItemType.EPISODE)

        viewBinding.fragmentSearchFilterIsEpisode.setOnClickListener {
            viewBinding.fragmentSearchFilterIsEpisode.isChecked =
                !viewBinding.fragmentSearchFilterIsEpisode.isChecked
        }
        viewBinding.fragmentSearchFilterIsCharacter.setOnClickListener {
            viewBinding.fragmentSearchFilterIsCharacter.isChecked =
                !viewBinding.fragmentSearchFilterIsCharacter.isChecked
        }

        initFragmentResult()
    }

    private fun initFragmentResult() {
        viewBinding.fragmentSearchFilterBtnApply.setOnClickListener {
            val categories = mutableListOf<ListItemType>()
            if (viewBinding.fragmentSearchFilterIsEpisode.isChecked) categories.add(ListItemType.EPISODE)
            if (viewBinding.fragmentSearchFilterIsCharacter.isChecked) categories.add(ListItemType.CHARACTER)

            val filter = SearchFilter(categories.toList())

            setFragmentResult(FILTER_KEY, bundleOf(FILTERS to filter))
            dismiss()
        }

        viewBinding.fragmentSearchFilterBtnCancel.setOnClickListener { dismiss() }
    }
}