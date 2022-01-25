package com.example.textviewexample

import android.content.Context
import android.graphics.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil

import android.text.style.*

import android.annotation.SuppressLint
import android.text.*
import android.util.AttributeSet

import android.widget.EditText
import com.example.textviewexample.databinding.ActivityPageOneBinding
import android.text.style.LeadingMarginSpan
import android.view.View
import android.view.Window
import com.example.textviewexample.particle.Particle.Companion.dp


class PageOneActivity : AppCompatActivity() {

    private val mBinding: ActivityPageOneBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_page_one)
    }



    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var iString = SpannableString("Text with underline span")
        iString.setSpan(Square(this), 10, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        mBinding.mTwo.text = iString

        //測試過若只有一個點點  無法做累加動作
//        var iAllText:CharSequence = ""
//        val iArraySpn = arrayListOf<SpannableString>()
//        val iSize = 10
//        var iIndex = 0
//        do {
//            iString = SpannableString("Text with underline span $iIndex \naaddkdfk")
//            iString.setSpan(BulletSpan(15, Color.RED), 0, 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            iAllText = TextUtils.concat(iAllText, iString)
////            iArraySpn.add(iString)
//            iIndex++
//        }while (iSize > iIndex)

        iString = SpannableString("Text with underline span  \naaddkdfk")
        iString.setSpan(BulletSpan(15, Color.RED), 0, 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.mThree.text = iString


        val bullets = arrayOf("1.", "2.", "3.", "4.")
        val itemContents = arrayOf(
            "那一天，閉目在經殿香霧中，驀然聽見，你誦經中的真言；",
            "那一月，我搖動所有的經筒，不為超度，只為觸摸你的指尖；",
            "那一年，磕長頭匍匐在山路，不為覲見，只為貼著你的溫暖；",
            "那一世，轉山轉水轉佛塔呀，不為修來生，只為途中與你相見。"
        )

        var iAllText: CharSequence? = ""
        for (i in bullets.indices) {
            val aBullet = bullets[i]
            val t = itemContents[i].trim()

            // 註意此處的換行, 如果沒有換行符, 則系統當做只有一個項目處理
            val spannableString = SpannableString(t+"\n")
            spannableString.setSpan(object : LeadingMarginSpan {
                override fun getLeadingMargin(first: Boolean): Int {

                    // 項目符號和正文的縮進距離, 單位 px
                    // 我們可以根據 first 來改變第1行和其余行的縮進距離
                    return 20.dp
                }

                override fun drawLeadingMargin(
                    c: Canvas,
                    p: Paint,
                    x: Int,
                    dir: Int,
                    top: Int,
                    baseline: Int,
                    bottom: Int,
                    text: CharSequence?,
                    start: Int,
                    end: Int,
                    first: Boolean,
                    layout: Layout?
                ) {

                    // 只對第1行文本添加項添加符號
                    if (first) {
                        val orgStyle = p.style
                        p.style = Paint.Style.FILL
                        c.drawText(aBullet, 0f, bottom - p.descent(), p)
                        p.style = orgStyle
                    }
                }
            }, 0, t.length, 0)
            iAllText = TextUtils.concat(iAllText, spannableString)
        }

        mBinding.mFour.text = iAllText


        mBinding.mFour.setOnClickListener {
            mBinding.mFive.createBitmapFromView(it)
        }

        mBinding.mThree.setOnClickListener {
            mBinding.mFive.createBitmapFromView(it)
        }

//        mBinding.mThree.setOnClickListener {
//            mBinding.mSix.createBitmapFromView(it)
//        }


    }

}

@SuppressLint("AppCompatCustomView")
class LinedEditText: EditText {


    constructor(context: Context):super(context)

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    @RequiresApi(Build.VERSION_CODES.M)
    private fun init() {
//        paint.style = Paint.Style.STROKE
//        paint.color = resources.getColor(
//            R.color.black, null
//        )
        paint.strokeWidth = (lineHeight / 10).toFloat()
        paint.strokeCap = Paint.Cap.ROUND
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas?) {
        if( canvas != null) {
            init()
            val startX = paddingLeft.toFloat()
            val stopX = (width - paddingRight).toFloat()
            val offsetY = (paddingTop
                    - paint.fontMetrics.top
                    + paint.strokeWidth / 2)
            for (i in 0 until lineCount) {
                val y = offsetY + lineHeight * i
                canvas.drawLine(
                    startX, y, stopX, y, paint
                )
            }
        }

        super.onDraw(canvas)
    }

}

class Square(private val mContext: Context): ReplacementSpan() {

    private var mWidth = 0

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        mWidth = paint.measureText(text, start, end).toInt()
        return mWidth
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        paint.color = mContext.resources.getColor(
            R.color.c0000ff, null
        )
        paint.style = Paint.Style.STROKE
        canvas.drawRect(x, top.toFloat(), x + mWidth, bottom.toFloat(), paint)
    }

}