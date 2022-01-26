package com.example.textviewexample.particle

import android.animation.Animator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView

class ExplosionField: View {

    companion object {
        private var mCanvas = Canvas()
    }

    private val mExplosionAnimator = arrayListOf<ExplosionAnimator>()
    private val mExplosionView = arrayListOf<View>()
    private var mBitmap:Bitmap ?= null
    private var mIsDrawNow = false

    fun createBitmapFromView(pView: View): Bitmap? {
//        Log.v("aaa","-----createBitmapFromView ININ---")
        synchronized(this) {
            if( mIsDrawNow) {
                return null
            }
        }
//        Log.v("aaa","-----createBitmapFromView----$mIsDrawNow")
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
                iExplosionAnimator.setView(pView, it, this)

                if( mExplosionView.isNotEmpty()) {
                    val iIndex = mExplosionView.indexOf(pView)
                    if( iIndex != -1) {
                        mExplosionView.remove(pView)
                        val iiExplosionAnimator = mExplosionAnimator[iIndex]
                        mExplosionAnimator.removeAt(iIndex)
                        iiExplosionAnimator.cancel()
                    }
                }
                mExplosionView.add(pView)
                mExplosionAnimator.add(iExplosionAnimator)
                iExplosionAnimator.addListener(object : Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {
                        Log.v("aaa","onAnimationStart")
                        pView.animate().alpha(0f).setDuration(150).start()
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        Log.v("aaa","onAnimationEnd")
                        pView.animate().alpha(1f).setDuration(150).start()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                })
                iExplosionAnimator.interpolator = AccelerateInterpolator(1.5f)
                iExplosionAnimator.start()
                mBitmap = it
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
        Log.v("aaa","bbbbb")
        if( canvas != null ) {
            for( iExplosionAnimator in mExplosionAnimator) {
                iExplosionAnimator.draw(canvas)
            }
//            Log.v("aaa","-----createBitmapFromView  onDraw----$mIsDrawNow")
            mIsDrawNow = false
        }

    }
}

