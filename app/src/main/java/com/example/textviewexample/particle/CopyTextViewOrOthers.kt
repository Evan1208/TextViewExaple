package com.example.textviewexample.particle

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.example.textviewexample.particle.Particle.Companion.dp
import com.example.textviewexample.particle.Particle.Companion.getStatusBar
import kotlin.math.roundToInt


class CopyTextViewOrOthers: View {

    companion object {
        private var mBitmap:Bitmap ?= null
        private val mCanvas = Canvas()
    }

    private var mViewCopyPosition = -1f
    private var mBound = Rect()

    fun createBitmapFromView(pView: View): Bitmap? {
        pView.getGlobalVisibleRect(mBound)
        mBound.offset(0, -getStatusBar())
        pView.setLayerType(LAYER_TYPE_SOFTWARE, null)
        if( pView is ImageView) {
            val iDrawable = pView.drawable
            return if( iDrawable != null && iDrawable is BitmapDrawable) {
//                synchronized(mCanvas) {
//                    mCanvas.setBitmap(iDrawable.bitmap)
//                    mView.draw(mCanvas)
//                    mCanvas.setBitmap(null)
//                }
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

    //https://stackoverflow.com/questions/22610145/unsupportedexception-from-canvas-setbitmapbitmap
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val iBitmap = mBitmap
        if( canvas != null && iBitmap != null) {
            if( mViewCopyPosition == -1f) {
                canvas.drawBitmap(iBitmap, null, mBound, null)
                mViewCopyPosition = mBound.height() + iBitmap.height.toFloat()
            } else {
//                canvas.drawBitmap(iBitmap,  null, mBound, null)
                canvas.drawBitmap(iBitmap,  mBound.left.toFloat(), mBound.top.toFloat(), null)
                canvas.drawBitmap(iBitmap, 0f, mViewCopyPosition, null)
            }
        }
    }
}

