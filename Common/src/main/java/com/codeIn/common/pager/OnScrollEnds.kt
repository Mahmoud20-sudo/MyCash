package com.codeIn.common.pager

import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.onScrollEndsVertically(direction: Int = 1, block: () -> Unit) {
    this.addOnScrollListener(
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(direction)) {
                    block.invoke()
                }
            }
        }
    )
}

fun RecyclerView.onScrollEndsHorizontally(block: () -> Unit){
    this.addOnScrollListener(
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val direction = if (recyclerView.layoutDirection == View.LAYOUT_DIRECTION_RTL) -1 else 1
                if (!recyclerView.canScrollHorizontally(direction)) {
                    block.invoke()
                }
            }
        }
    )
}

//fun NestedScrollView.onScrollEndsVertically(direction: Int = 1, block: () -> Unit) {
//    this.setOnScrollChangeListener(
//        NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            if (scrollY == ( v.getChildAt(0).measuredHeight - v.measuredHeight)) {
//                // here where the trick is going
//                block.invoke()
//            }
//        }
//    )
//}

fun NestedScrollView.onScrollEndsVertically(direction: Int = 1, block: () -> Unit) {
    viewTreeObserver.addOnScrollChangedListener {
        val view = getChildAt(childCount - 1) as View
        val diff: Int = (view.bottom - (height + this.scrollY))
        if (diff == 0) {
            // your pagination code
            block.invoke()
        }
    }
}

