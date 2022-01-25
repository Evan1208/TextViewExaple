package com.example.textviewexample.particle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView

class ExplosionField: View {

    companion object {
        private var mCanvas = Canvas()
    }

    private val mExplosionAnimator = arrayListOf<ExplosionAnimator>()
    private var mBitmap:Bitmap ?= null
    private var mIsDrawNow = false

    fun createBitmapFromView(pView: View): Bitmap? {
        Log.v("aaa","-----createBitmapFromView ININ---")
        synchronized(this) {
            if( mIsDrawNow) {
                return null
            }
        }
        Log.v("aaa","-----createBitmapFromView----$mIsDrawNow")
        mIsDrawNow = true


        pView.setLayerType(LAYER_TYPE_SOFTWARE, null)
        if( pView is ImageView) {
            val iDrawable = pView.drawable
            return if( iDrawable != null && iDrawable is BitmapDrawable) {
                synchronized(mCanvas) {
                    mCanvas.setBitmap(iDrawable.bitmap)
                    pView.draw(mCanvas)
                    mCanvas.setBitmap(null)
                }
                iDrawable.bitmap
            } else {
                null
            }
        } else {
            Bitmap.createBitmap(pView.width, pView.height, Bitmap.Config.ARGB_8888)?.let {
                synchronized(mCanvas) {
                    mCanvas.setBitmap(it)
                    pView.draw(mCanvas)
                    mCanvas.setBitmap(null)
                }
                val iExplosionAnimator = ExplosionAnimator()
                iExplosionAnimator.setView(pView, it)
                mExplosionAnimator.clear()
                mExplosionAnimator.add(iExplosionAnimator)
                mBitmap = it
                invalidate()
                return it
            } ?: kotlin.run {
                return null
            }
        }
    }

    private fun initView() {

    }


    constructor(mContext: Context): super(mContext) {
        initView()
    }

    constructor(mContext: Context, mAttrs: AttributeSet): super(mContext, mAttrs) {
        initView()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if( canvas != null ) {
            for( iExplosionAnimator in mExplosionAnimator) {
                iExplosionAnimator.draw(canvas)
            }
            Log.v("aaa","-----createBitmapFromView  onDraw----$mIsDrawNow")
            mIsDrawNow = false
        }

    }
}

