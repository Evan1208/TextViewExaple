package com.example.textviewexample.particle

import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import kotlin.random.Random

class Particle {

    companion object {
        val Int.dp: Int
            get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

        const val mPartWh = 8

        fun getStatusBar(): Int {
            var statusBarHeight = 0
            val resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = Resources.getSystem().getDimensionPixelSize(resourceId)
            }
            return statusBarHeight
        }

        fun generateParticle(pColor: Int, pBound: Rect, pPoint: Point): Particle {
            val iRow = pPoint.y
            val iColumn = pPoint.x

            val iParticle = Particle()
            iParticle.mBound = pBound
            iParticle.mColor = pColor
            iParticle.mAlpha = 1f

            iParticle.mRadius = mPartWh.toFloat()
            iParticle.mCx = (pBound.left + (mPartWh * iColumn)).toFloat()
            iParticle.mCy = (pBound.top + (mPartWh * iRow)).toFloat()

//            Log.d("aaa", "iRow=$iRow, iColumn=$iColumn")
//            Log.d("aaa", "pBound.left=${pBound.left}, pBound.top=${pBound.top}")
//            Log.d("aaa", "mCx=${iParticle.mCx}, mCy=${iParticle.mCy}")

            return iParticle
        }

    }

    var mCx = 0.0f
    var mCy = 0.0f
    var mRadius = 0.0f

    var mBound: Rect = Rect()
    var mColor = 1
    var mAlpha = 0.0f

    fun advance(pFactor: Float) {
        mCx += pFactor * Random.nextInt(mBound.width()) * (Random.nextFloat() - 0.5f)
        mCy += pFactor * Random.nextInt(mBound.height()/2)

        mRadius -= pFactor * Random.nextInt(2)

        mAlpha = (1f - pFactor) * ( 1 + Random.nextFloat())
    }


}