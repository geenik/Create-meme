package com.example.create_meme

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.customview.widget.ViewDragHelper
import kotlin.math.log

class DraggableCoordinatorLayout (context: Context?, attrs: AttributeSet? = null)
    : CoordinatorLayout(context!!, attrs) {

    /** A listener to use when a child view is being dragged  */
    interface ViewDragListener {
        fun onViewCaptured(view: View, i: Int)
        fun onViewReleased(view: View, v: Float, v1: Float)
    }


    private val viewDragHelper: ViewDragHelper
     val draggableChildren: MutableList<View> = ArrayList()
    private var viewDragListener: ViewDragListener? = null

    fun addDraggableChild(child: View) {
        require(!(child.parent !== this))
        draggableChildren.add(child)
    }

    fun removeDraggableChild(child: View) {
        require(!(child.parent !== this))
        draggableChildren.remove(child)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(ev)
        return super.onTouchEvent(ev)
    }
    private val dragCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(view: View, i: Int): Boolean {
            return view.visibility == VISIBLE && viewIsDraggableChild(view)
        }

        override fun onViewCaptured(view: View, i: Int) {
            if (viewDragListener != null) {
                viewDragListener!!.onViewCaptured(view, i)
            }
        }

        override fun onViewReleased(view: View, v: Float, v1: Float) {
            if (viewDragListener != null) {
                viewDragListener!!.onViewReleased(view, v, v1)
            }
        }

        override fun getViewHorizontalDragRange(view: View): Int {
            return view.width
        }


        override fun getViewVerticalDragRange(view: View): Int {
            return view.height
        }

        override fun clampViewPositionHorizontal(view: View, left: Int, dx: Int): Int {
            val leftBound = paddingLeft
            val rightBound = width - view.width - paddingRight
            return left.coerceAtLeast(leftBound).coerceAtMost(rightBound)
        }

        override fun clampViewPositionVertical(view: View, top: Int, dy: Int): Int {
            val topBound = paddingTop
            val bottomBound = height - view.height - paddingBottom
            return top.coerceAtLeast(topBound).coerceAtMost(bottomBound)
        }
    }

    private fun viewIsDraggableChild(view: View): Boolean {
        return (draggableChildren.isEmpty() || draggableChildren.contains(view) )
    }

    fun setViewDragListener(viewDragListener: ViewDragListener?) {
        this.viewDragListener = viewDragListener
    }

    init {
        viewDragHelper = ViewDragHelper.create(this, dragCallback)
    }
}