package com.ivk.weatherapp

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "RecyclerItemClickListen"

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
                Log.d(TAG, "onLongPress: starts")
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if (childView != null) {
                    listener.onItemLongTouch(
                        childView,
                        recyclerView.getChildAdapterPosition(childView)
                    )
                    isPressed = true
                }
                super.onLongPress(e)
                Log.d(TAG, "onLongPress: finished")
            }

            /*override fun onDoubleTap(e: MotionEvent): Boolean {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                if (childView != null) {
                    listener.onItemDoubleClick(childView, recyclerView.getChildAdapterPosition(childView))
                }
                return super.onDoubleTap(e)
            }*/
        })


    /*    Button mSpeak = (Button)findViewById(R.id.speakbutton);
        mSpeak.setOnLongClickListener(speakHoldListener);
        mSpeak.setOnTouchListener(speakTouchListener);
    }

    private View.OnLongClickListener speakHoldListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View pView) {
            // Do something when your hold starts here.
            isSpeakButtonLongPressed = true;
            return true;
        }
    }
    @Override
    public boolean onTouch(View pView, MotionEvent pEvent) {
        pView.onTouchEvent(pEvent);
        // We're only interested in when the button is released.
        if (pEvent.getAction() == MotionEvent.ACTION_UP) {
            // We're only interested in anything if our speak button is currently pressed.
            if (isSpeakButtonLongPressed) {
                // Do something when the button is released.
                isSpeakButtonLongPressed = false;
            }
        }
        return false;
    }*/


    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        Log.d(TAG, "onTouchEvent: starts")
        val childView = rv.findChildViewUnder(e.x, e.y)
        when (e.action) {
            MotionEvent.ACTION_DOWN -> if (childView != null) {
                Log.d(TAG, "onTouchEvent: MotionEvent.ACTION_DOWN")
                listener.onItemClick(childView, rv.getChildAdapterPosition(childView))
                //isPressed = true
            }
            MotionEvent.ACTION_UP -> if (childView != null && isPressed) {
                Log.d(TAG, "onTouchEvent: MotionEvent.ACTION_UP")
                listener.onItemClick(childView, rv.getChildAdapterPosition(childView))
                isPressed = false
            }
        }
        super.onTouchEvent(rv, e)
        Log.d(TAG, "onTouchEvent: finished")
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, "onInterceptTouchEvent: starts $e")
        val result = gestureDetector.onTouchEvent(e)
        Log.d(TAG, "onInterceptTouchEvent: returning $result")
        return result
    }

}