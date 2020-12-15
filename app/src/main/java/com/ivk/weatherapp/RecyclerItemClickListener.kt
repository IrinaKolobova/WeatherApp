package com.ivk.weatherapp

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView


class RecyclerItemClickListener(
    context: Context,
    recyclerView: RecyclerView,
    private val listener: OnRecyclerClickListener
) : RecyclerView.SimpleOnItemTouchListener() {

    var isPressed: Boolean = false

    interface OnRecyclerClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongTouch(view: View, position: Int)
        //fun onItemDoubleClick(view: View, position: Int)
    }

    private val gestureDetector = GestureDetectorCompat(
        context,
        object : GestureDetector.SimpleOnGestureListener() {


            override fun onSingleTapUp(e: MotionEvent): Boolean {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if (childView != null) {
                    listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))
                }
                return super.onSingleTapUp(e)
            }

            override fun onLongPress(e: MotionEvent) {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if (childView != null) {
                    listener.onItemLongTouch(
                        childView,
                        recyclerView.getChildAdapterPosition(childView)
                    )
                    isPressed = true
                }
                super.onLongPress(e)
            }
        })

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        val childView = rv.findChildViewUnder(e.x, e.y)
        when (e.action) {
            MotionEvent.ACTION_DOWN -> if (childView != null) {
                listener.onItemClick(childView, rv.getChildAdapterPosition(childView))
            }
            MotionEvent.ACTION_UP -> if (childView != null && isPressed) {
                listener.onItemClick(childView, rv.getChildAdapterPosition(childView))
                isPressed = false
            }
        }
        super.onTouchEvent(rv, e)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val result = gestureDetector.onTouchEvent(e)
        return result
    }

}