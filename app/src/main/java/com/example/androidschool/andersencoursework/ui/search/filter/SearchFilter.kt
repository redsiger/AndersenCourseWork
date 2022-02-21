package com.example.androidschool.andersencoursework.ui.search.filter

import android.os.Parcelable
import com.example.androidschool.domain.search.ListItemType
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchFilter(
    val filter: List<ListItemType>
): Parcelable
