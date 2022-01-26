package com.example.textviewexample

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.textviewexample.databinding.ActivityMainBinding
import android.text.Spanned

import android.text.SpannableString
import android.text.TextPaint
import android.text.style.*
import android.animation.ValueAnimator

import android.animation.FloatEvaluator

import android.animation.ObjectAnimator
import android.animation.ObjectAnimator.ofFloat
import android.content.Intent
import android.text.format.DateUtils
import android.util.Property

import android.view.animation.LinearInterpolator





class MainActivity : AppCompatActivity() {

    private val mBinding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAnimation(mBinding.mOne.compoundDrawables)
        setAnimation(mBinding.mTwo.compoundDrawables)
        setAnimation(mBinding.mThree.compoundDrawables)
        setAnimation(mBinding.mFour.compoundDrawables)
        setAnimation(mBinding.mSix.compoundDrawables)
        setAnimation(mBinding.mSeven.compoundDrawables)
        setAnimation(mBinding.mEight.compoundDrawables)



        var iShader: Shader = LinearGradient(
            10f, 10f, 40f, mBinding.mTwelve.textSize,
            Color.RED, Color.BLUE,
            Shader.TileMode.CLAMP
        )
        mBinding.mTwelve.paint.shader = iShader

        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.pix
        )

        iShader = BitmapShader(
            bitmap,
            Shader.TileMode.REPEAT,
            Shader.TileMode.REPEAT
        )

        mBinding.mThirteen.paint.shader = iShader


        var iString = SpannableString("Text with underline span")
        iString.setSpan(UnderlineSpan(), 10, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.mFourteen.text = iString

        iString = SpannableString("Text with StrikethroughSpan span")
        iString.setSpan(StrikethroughSpan(), 10, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.mFifteen.text = iString

        iString = SpannableString("Text with SubscriptSpan span")
        iString.setSpan(SubscriptSpan(), 10, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.mSixteen.text = iString

        iString = SpannableString("Text with SuperscriptSpan span")
        iString.setSpan(SuperscriptSpan(), 10, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.mEighteen.text = iString

        iString = SpannableString("Text with BackgroundColorSpan span")
        iString.setSpan(BackgroundColorSpan(Color.GREEN), 10, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.mNineteen.text = iString

        iString = SpannableString("Text with ForegroundColorSpan span")
        iString.setSpan(ForegroundColorSpan(Color.RED), 10, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.mTwenty.text = iString


        val iSubstring = "RainBow".lowercase()
        val iText = "It is RainBow Span!"
        var iSpannableString = SpannableString(iText)
        val iStart: Int = iText.lowercase().indexOf(iSubstring)
        val iEnd: Int = iStart + iSubstring.length
        iSpannableString.setSpan(
            RainbowSpan(this), iStart, iEnd, 0
        )

        mBinding.mTwentyOne.text = iSpannableString


        val iAnimatedColorSpan = AnimatedColorSpan(this)
        iSpannableString = SpannableString(iText)
        iSpannableString.setSpan(
            iAnimatedColorSpan, iStart, iEnd, 0
        )

        val objectAnimator: ObjectAnimator = ofFloat(
            iAnimatedColorSpan,
            iAnimatedColorSpan.ANIMATED_COLOR_SPAN_FLOAT_PROPERTY, 0f, 100f
        )
        objectAnimator.setEvaluator(
            FloatEvaluator()
        )
        objectAnimator.addUpdateListener { mBinding.mTwentyTwo.text = iSpannableString }

        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.duration = DateUtils.MINUTE_IN_MILLIS * 3
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.start()



        mBinding.mNextPage.setOnClickListener {
            val iTent = Intent(this, PageOneActivity::class.java)
            startActivity(iTent)
        }
    }



    private fun setAnimation(pArrayDrawable: Array<Drawable>) {
//        Log.v("aaa","ArrayDrawable size=${pArrayDrawable.size}")
        for( iDrawable in pArrayDrawable) {
            if( iDrawable is Animatable) {
                iDrawable.start()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
class AnimatedColorSpan(pContext: Context): CharacterStyle(){
    private val mArray = IntArray(7)
    private var mShader: Shader? = null
    private val mMatrix = Matrix()
    private var mTranslateXPercentage = 0f

    init {
        mArray[0] = pContext.getColor(R.color.cff0000)
        mArray[1] = pContext.getColor(R.color.cFF7f00)
        mArray[2] = pContext.getColor(R.color.cFFff00)
        mArray[3] = pContext.getColor(R.color.c00ff00)
        mArray[4] = pContext.getColor(R.color.c0000ff)
        mArray[5] = pContext.getColor(R.color.c4b0082)
        mArray[6] = pContext.getColor(R.color.c9400d3)
    }

    fun setTranslateXPercentage(percentage: Float) {
        mTranslateXPercentage = percentage
    }

    fun getTranslateXPercentage(): Float {
        return mTranslateXPercentage
    }

    override fun updateDrawState(tp: TextPaint?) {
        if (tp != null) {
            tp.style = Paint.Style.FILL
            val width: Float = tp.textSize * mArray.size
            if (mShader == null) {
                mShader = LinearGradient(0f, 0f, 0f, width, mArray, null, Shader.TileMode.MIRROR)
            }
            if( mShader != null) {
                mMatrix.reset()
                mMatrix.setRotate(90f)
                mMatrix.postTranslate(width * mTranslateXPercentage, 0f)
                mShader!!.setLocalMatrix(mMatrix)
                tp.shader = mShader
            }
        }
    }

    val ANIMATED_COLOR_SPAN_FLOAT_PROPERTY = object : Property<AnimatedColorSpan, Float>(Float::class.java,
        "ANIMATED_COLOR_SPAN_FLOAT_PROPERTY") {

        override fun set(span: AnimatedColorSpan?, value: Float?) {
            if( span != null && value != null) {
                span.setTranslateXPercentage(value)
            }
        }

        override fun get(span: AnimatedColorSpan): Float {
            return span.getTranslateXPercentage()
        }

    }

}


@RequiresApi(Build.VERSION_CODES.M)
class RainbowSpan(pContext: Context): CharacterStyle(), UpdateAppearance {
    private val mArray = IntArray(7)

    init {
        mArray[0] = pContext.getColor(R.color.cff0000)
        mArray[1] = pContext.getColor(R.color.cFF7f00)
        mArray[2] = pContext.getColor(R.color.cFFff00)
        mArray[3] = pContext.getColor(R.color.c00ff00)
        mArray[4] = pContext.getColor(R.color.c0000ff)
        mArray[5] = pContext.getColor(R.color.c4b0082)
        mArray[6] = pContext.getColor(R.color.c9400d3)
    }

    override fun updateDrawState(tp: TextPaint?) {
        if (tp != null) {
            tp.style = Paint.Style.FILL
            val shader: Shader = LinearGradient(
                0f, 0f, 0f,
                tp.textSize * mArray.size,
                mArray, null,
                Shader.TileMode.MIRROR
            )
            val matrix = Matrix()
            matrix.setRotate(90f)
            shader.setLocalMatrix(matrix)
            tp.shader = shader
        }

    }

}
