package com.example.androidschool.andersencoursework.ui.core

import android.content.Context
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidschool.andersencoursework.util.InfiniteScrollListener
import com.example.androidschool.andersencoursework.util.OffsetRecyclerDecorator

private const val DEFAULT_FRAGMENT_TITLE = ""

open class BaseFragment(resId: Int): Fragment(resId) {

    fun setupToolbarTitle(toolbar: Toolbar, title: String) {
        toolbar.title = title
    }

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
        onItemClick: (item: MenuItem) -> Boolean
    ) {
        setupNavigation(toolbar, title)
        setupToolbarMenu(toolbar, menuId, onItemClick)
    }

    fun setupMainToolbar(
        toolbar: Toolbar,
        title: String,
        menuId: Int,
        onItemClick: (item: MenuItem) -> Boolean
    ) {
        setupToolbarTitle(toolbar, title)
        setupToolbarMenu(toolbar, menuId, onItemClick)
    }

    /**
     * Function responsible for setting up a @toolbar with navController,
     * and adding title in Toolbar
     */
    fun setupNavigation(toolbar: Toolbar, title: String) {
        val navController = findNavController()
        NavigationUI.setupWithNavController(toolbar, navController)
        setupToolbarTitle(toolbar, title)
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

fun RecyclerView.initRecyclerPaging(
    itemWidthResId: Int,
    context: Context,
    actionLoadMore: () -> Unit
    ) {
    val displayMetrics = context.resources.displayMetrics
    val itemWidthPx = context.resources.getDimension(itemWidthResId)

    val screenWidthPx = displayMetrics.widthPixels
    val columnsCount = (screenWidthPx / itemWidthPx).toInt()
    val itemsWidthPx = columnsCount * itemWidthPx

    val rest = screenWidthPx - itemsWidthPx
    val offset = rest / (columnsCount * 2)

    val linearLayoutManager = GridLayoutManager(context, columnsCount)

    this.apply {
        layoutManager = linearLayoutManager
        addItemDecoration(
            OffsetRecyclerDecorator(offset.toInt(), linearLayoutManager, false)
        )
        addOnScrollListener(
            InfiniteScrollListener(actionLoadMore, linearLayoutManager)
        )
    }
}

