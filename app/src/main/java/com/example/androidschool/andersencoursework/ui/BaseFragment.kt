package com.example.androidschool.andersencoursework.ui

import android.view.MenuItem
import android.view.ViewTreeObserver
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.andersencoursework.ui.characters.charactersList.CharactersListPagingAdapter
import com.example.androidschool.andersencoursework.ui.seacrh.SearchAdapter
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator

private const val DEFAULT_FRAGMENT_TITLE = ""

open class BaseFragment: Fragment() {

    fun setupToolbar(toolbar: Toolbar) {
        setupNavigation(toolbar, DEFAULT_FRAGMENT_TITLE)
    }

    fun setupToolbar(toolbar: Toolbar, title: String) {
        setupNavigation(toolbar, title)
    }

    fun setupToolbar(
        toolbar: Toolbar,
        title: String,
        menuId: Int,
        onClickListener: (item: MenuItem) -> Boolean
    ) {
        setupNavigation(toolbar, title)
        setupToolbarMenu(toolbar, menuId, onClickListener)
    }

    /**
     * Function responsible for setting up a @toolbar with navController,
     * and adding title in Toolbar
     */
    fun setupNavigation(toolbar: Toolbar, title: String) {
        val navController = findNavController()
        NavigationUI.setupWithNavController(toolbar, navController)
        toolbar.title = title
    }

    /**
     * Function to populate toolbar with menu items
     */
    fun setupToolbarMenu(
        toolbar: Toolbar,
        menuId: Int,
        onClickListener: (item: MenuItem) -> Boolean
    ) {
        toolbar.inflateMenu(menuId)
        toolbar.setOnMenuItemClickListener(onClickListener)
    }
}

fun BaseFragment.setupGridLayoutManager(recyclerView: RecyclerView, recyclerAdapter: CharactersListPagingAdapter, itemWidth: Float) {

    recyclerView.viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = recyclerView.width
                val count = recyclerView.childCount
                if (width > 0 && itemWidth > 0) {
                    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val spanCountWithOffset = width / itemWidth
                    val spanCount = spanCountWithOffset.toInt()

                    val itemsWidth = spanCount * itemWidth
                    val rest = width - itemsWidth
                    val offset = rest / (spanCount * 2)

                    recyclerView.adapter = recyclerAdapter
                    val gridLayoutManager = GridLayoutManager(requireContext(), spanCount)
                    recyclerView.layoutManager = gridLayoutManager
                    recyclerView.addItemDecoration(OffsetRecyclerDecorator(offset.toInt(), gridLayoutManager, false))
                }
            }
        }
    )
}

fun BaseFragment.setupGridLayoutManager(recyclerView: RecyclerView, recyclerAdapter: SearchAdapter, itemWidth: Float) {

    recyclerView.viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = recyclerView.width
                val count = recyclerView.childCount
                if (width > 0 && itemWidth > 0) {
                    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val spanCountWithOffset = width / itemWidth
                    val spanCount = spanCountWithOffset.toInt()

                    val itemsWidth = spanCount * itemWidth
                    val rest = width - itemsWidth
                    val offset = rest / (spanCount * 2)

                    recyclerView.adapter = recyclerAdapter
                    val gridLayoutManager = GridLayoutManager(requireContext(), spanCount)
                    recyclerView.layoutManager = gridLayoutManager
                    recyclerView.addItemDecoration(OffsetRecyclerDecorator(offset.toInt(), gridLayoutManager, false))
                }
            }
        }
    )
}