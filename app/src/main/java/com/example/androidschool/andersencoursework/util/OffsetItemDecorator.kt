package com.example.androidschool.andersencoursework.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OffsetRecyclerDecorator(private val marginTop: Int = 0,
                              private val marginBottom: Int = 0,
                              private val marginLeft: Int = 0,
                              private val marginRight: Int = 0,
                              private val layoutManager: RecyclerView.LayoutManager,
                              private val inDp: Boolean
): RecyclerView.ItemDecoration() {

    constructor(margin: Int = 0, layoutManager: RecyclerView.LayoutManager, inDp: Boolean) : this(
        marginTop = margin,
        marginBottom = margin,
        marginLeft = margin,
        marginRight = margin,
        layoutManager = layoutManager,
        inDp = inDp
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        if (inDp) {
            outRect.top = getPx(marginTop, view.context)
            outRect.bottom = getPx(marginBottom, view.context)
            outRect.left = getPx(marginLeft, view.context)
            outRect.right = getPx(marginRight, view.context)
        } else {
            outRect.top = marginTop
            outRect.bottom = marginBottom
            outRect.left = marginLeft
            outRect.right = marginRight
        }

//        if (layoutManager is GridLayoutManager) {
//            view.layoutParams.width = -1
//        }
    }

    private fun getPx(dp: Int, context: Context): Int {
        return Math.round(
            dp * context.resources.displayMetrics.density
        )
    }
}