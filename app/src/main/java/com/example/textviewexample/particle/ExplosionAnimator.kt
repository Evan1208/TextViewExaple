package com.example.textviewexample.particle

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.*
import android.util.Log
import android.view.View

@SuppressLint("Recycle", "SoonBlockedPrivateApi")
class ExplosionAnimator : ValueAnimator() {
    companion object {
        private const val mTime1500 = 1500L
    }

    init {
        setFloatValues(0.0f, 1.0f)
        duration = mTime1500
        try {
            //某些機型的durationScale 出現未重置問題, 必須重製後才會正常
            //https://blog.csdn.net/u011387817/article/details/78628956
            val iField = ValueAnimator::class.java.getDeclaredField("sDurationScale")
            iField.isAccessible = true
            iField.setFloat(null, 1f)
        } catch (e: java.lang.Exception) {
        }
    }

    private val mPaint = Paint()
    private val mParticles = arrayListOf<ArrayList<Particle>>()
    private var mShowView:View? = null

    fun setView(pView: View, pBitmap: Bitmap, explosionField: View) {
        val iBound = Rect()
        mShowView = explosionField
        pView.getGlobalVisibleRect(iBound)
        iBound.offset(0, -Particle.getStatusBar())
        generateParticles(pBitmap, iBound)
    }


    private fun generateParticles(pBitmap: Bitmap, pBound: Rect) {
//        Log.v("aaa","-------------")
        mParticles.clear()
        val iW = pBound.width()
        val iH = pBound.height()

        var iPW = iW / Particle.mPartWh
        var iPH = iH / Particle.mPartWh
        if( iW % Particle.mPartWh != 0) {
            iPW++
        }
        if( iH % Particle.mPartWh != 0) {
            iPH++
        }

        val iFirstArray = arrayListOf<ArrayList<Particle>>()
        var iSecondArray: java.util.ArrayList<Particle>

        var iPoint: Point
        var iColor: Int = 0
//        Log.v("aaa", "iW=$iW, iH=$iH")
//        Log.v("aaa", "iW=${pBitmap.width}, iH=${pBitmap.height}")
//        Log.v("aaa", "iPW=$iPW, iPH=$iPH")
        for( i in 0 .. iPH) { // 行
            iSecondArray = arrayListOf()
            for( j in 0 .. iPW) {//列
                val iY = if( i * Particle.mPartWh >= iH) {
                    iH-1
                } else {
                    i * Particle.mPartWh
                }
                val iX = if( j * Particle.mPartWh >= iW) {
                    iW-1
                } else {
                    j * Particle.mPartWh
                }
//                Log.v("aaa", "Y=$iY, X=$iX")
                iColor = pBitmap.getPixel(iX, iY)
                iPoint = Point(j, i)
                iSecondArray.add(Particle.generateParticle(iColor, pBound, iPoint))
            }
//            Log.d("aaa","xxxxxxxxxxxxxxxxxx")
            iFirstArray.add(iSecondArray)
        }
        mParticles.addAll(iFirstArray)
    }

    fun drawJustPutTheParticle(pCanvas: Canvas) {
        for( iParticle in mParticles) {
            for( iiParticle in iParticle) {
                mPaint.color = iiParticle.mColor
                pCanvas.drawCircle(iiParticle.mCx, iiParticle.mCy, iiParticle.mRadius, mPaint)
            }
        }
    }

    fun draw(pCanvas: Canvas) {
        Log.v("aaa","isStarted=$isStarted")
//        if( !isStarted) {
//            return
//        }
        Log.v("aaa","isStarted=${animatedValue.toString().toFloat()}")
        for( iParticle in mParticles) {
            for( iiParticle in iParticle) {
                iiParticle.advance(animatedValue.toString().toFloat())
                mPaint.color = iiParticle.mColor
                mPaint.alpha = (Color.alpha(iiParticle.mColor) * iiParticle.mAlpha).toInt()
                pCanvas.drawCircle(iiParticle.mCx, iiParticle.mCy, iiParticle.mRadius, mPaint)
            }
        }
        mShowView?.invalidate()
    }

    override fun start() {
        super.start()
        mShowView?.invalidate()
    }
}