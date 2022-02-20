package com.example.androidschool.andersencoursework.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidschool.andersencoursework.databinding.FragmentSearchFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SearchFilterFragment : BottomSheetDialogFragment() {

    private var _viewBinding: FragmentSearchFilterBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSearchFilterBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }
}