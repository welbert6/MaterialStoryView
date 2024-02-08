package com.moreirasoft.materialstoryview.presentation

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.moreirasoft.materialstoryview.R
import org.jetbrains.annotations.Nullable

class StoryPlayerProgressView : View {
    private var mScreenWidth = 0
    private var mProgressHeight = 0
    private var mGapBetweenProgressBars = 0
    private var resources: Resources? = null
    private var mProgressPaint: Paint? = null
    private var singleProgressBarWidth = 0
    private var progressBarsCount = 0
    private lateinit var progressBarRightEdge: IntArray
    private var progressBarRectF: RectF? = null
    private var top = 0
    private var bottom = 0
    private var progressBarPrimaryColor = 0
    private var progressBarSecondaryColor = 0
    private val currentProgressIndex = 0
    private var singleStoryDisplayTime: Long = 0
    private var progressAnimator: ValueAnimator? = null
    private var isCancelled = false

    constructor(context: Context) : super(context) {
        setDefaults()
        init(context)
        prepareValues()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        init(context)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.StoryPlayerProgressView, 0, 0)
        try {
            mProgressHeight = getPxFromDp(
                ta.getDimension(
                    R.styleable.StoryPlayerProgressView_progressBarHeight,
                    PROGRESS_BAR_HEIGHT.toFloat(),
                ).toInt(),
            )
            mGapBetweenProgressBars = getPxFromDp(
                ta.getDimension(
                    R.styleable.StoryPlayerProgressView_gapBetweenProgressBar,
                    GAP_BETWEEN_PROGRESS_BARS.toFloat(),
                ).toInt(),
            )
            progressBarPrimaryColor = ta.getColor(
                R.styleable.StoryPlayerProgressView_progressBarPrimaryColor,
                Color.parseColor(
                    PROGRESS_PRIMARY_COLOR,
                ),
            )
            progressBarSecondaryColor = ta.getColor(
                R.styleable.StoryPlayerProgressView_progressBarSecondaryColor,
                Color.parseColor(
                    PROGRESS_SECONDARY_COLOR,
                ),
            )
            singleStoryDisplayTime = ta.getInt(
                R.styleable.StoryPlayerProgressView_singleStoryDisplayTime,
                SINGLE_STORY_DISPLAY_TIME,
            ).toLong()
        } finally {
            ta.recycle()
        }
        prepareValues()
    }

    private fun init(context: Context) {
        resources = context.resources
        mScreenWidth = resources!!.getDisplayMetrics().widthPixels
        mProgressPaint = Paint()
        mProgressPaint!!.isAntiAlias = true
    }

    private fun setDefaults() {
        mProgressHeight = getPxFromDp(PROGRESS_BAR_HEIGHT)
        mGapBetweenProgressBars = getPxFromDp(GAP_BETWEEN_PROGRESS_BARS)
        progressBarPrimaryColor = Color.parseColor(PROGRESS_PRIMARY_COLOR)
        progressBarSecondaryColor = Color.parseColor(PROGRESS_SECONDARY_COLOR)
        singleStoryDisplayTime = SINGLE_STORY_DISPLAY_TIME.toLong()
    }

    private fun prepareValues() {
        top = mGapBetweenProgressBars
        bottom = mGapBetweenProgressBars + mProgressHeight
        mProgressPaint!!.color = progressBarSecondaryColor
        mProgressPaint!!.style = Paint.Style.FILL
        mProgressPaint!!.strokeCap = Paint.Cap.ROUND
        progressBarRectF = RectF(0f, top.toFloat(), 0f, bottom.toFloat())
    }

    fun setProgressBarsCount(count: Int) {
        require(count >= 1) { "Count cannot be less than 1" }
        progressBarsCount = count
        progressBarRightEdge = IntArray(progressBarsCount)
        calculateWidthOfEachProgressBar(progressBarsCount)
        invalidate()
        startAnimating(0)
    }

    fun setProgressBarHeight(dpValue: Int) {
        mProgressHeight = getPxFromDp(dpValue)
        invalidate()
    }

    fun setGapBetweenProgressBars(dpValue: Int) {
        mGapBetweenProgressBars = getPxFromDp(dpValue)
        invalidate()
    }

    fun setProgressPrimaryColor(color: Int) {
        progressBarPrimaryColor = color
        invalidate()
    }

    fun setProgressSecondaryColor(color: Int) {
        progressBarSecondaryColor = color
        invalidate()
    }

    fun setSingleStoryDisplayTime(time: Int) {
        singleStoryDisplayTime = time.toLong()
        invalidate()
    }

    fun pauseProgress() {
        if (progressAnimator != null) {
            if (progressAnimator!!.isRunning) {
                progressAnimator!!.pause()
            }
        }
    }

    fun resumeProgress() {
        if (progressAnimator != null) {
            if (progressAnimator!!.isPaused) {
                progressAnimator!!.resume()
            }
        }
    }

    fun cancelAnimation() {
        if (progressAnimator != null) {
            progressAnimator!!.cancel()
            isCancelled = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until progressBarsCount) {
            val left =
                (mGapBetweenProgressBars + singleProgressBarWidth) * i + mGapBetweenProgressBars
            var right = (i + 1) * (mGapBetweenProgressBars + singleProgressBarWidth)
            mProgressPaint!!.color = progressBarSecondaryColor
            progressBarRectF!![left.toFloat(), top.toFloat(), right.toFloat()] = bottom.toFloat()
            canvas.drawRoundRect(
                progressBarRectF!!,
                mProgressHeight.toFloat(),
                mProgressHeight.toFloat(),
                mProgressPaint!!,
            )
            right = progressBarRightEdge[i]
            if (right > 0) {
                mProgressPaint!!.color = progressBarPrimaryColor
                progressBarRectF!![left.toFloat(), top.toFloat(), right.toFloat()] =
                    bottom.toFloat()
                canvas.drawRoundRect(
                    progressBarRectF!!,
                    mProgressHeight.toFloat(),
                    mProgressHeight.toFloat(),
                    mProgressPaint!!,
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = mScreenWidth - paddingStart + paddingEnd
        val height = paddingTop + paddingBottom + 2 * mGapBetweenProgressBars + mProgressHeight
        val w = resolveSizeAndState(width, widthMeasureSpec, 0)
        val h = resolveSizeAndState(height, heightMeasureSpec, 0)
        setMeasuredDimension(w, h)
    }

    private fun startAnimating(index: Int) {
        if (index >= progressBarsCount) {
            if (storyPlayerListener != null) {
                storyPlayerListener!!.onFinishedPlaying()
                return
            }
        }
        progressAnimator = ValueAnimator.ofInt(0, singleProgressBarWidth)
        progressAnimator!!.duration = singleStoryDisplayTime
        progressAnimator!!.addUpdateListener(
            AnimatorUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                progressBarRightEdge[index] =
                    (index + 1) * mGapBetweenProgressBars + index * singleProgressBarWidth + value
                invalidate()
            },
        )
        progressAnimator!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                if (!isCancelled) startAnimating(index + 1)
            }

            override fun onAnimationCancel(p0: Animator) {
                isCancelled = true
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })
        progressAnimator!!.start()
        if (storyPlayerListener != null) {
            storyPlayerListener!!.onStartedPlaying(index)
        }
    }

    private fun getPxFromDp(dpValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue.toFloat(),
            resources!!.displayMetrics,
        ).toInt()
    }

    private fun calculateWidthOfEachProgressBar(progressBarsCount: Int) {
        singleProgressBarWidth =
            (mScreenWidth - (progressBarsCount + 1) * mGapBetweenProgressBars) / progressBarsCount
    }

    private var storyPlayerListener: StoryPlayerListener? = null
    fun setStoryPlayerListener(storyPlayerListener: StoryPlayerListener?) {
        this.storyPlayerListener = storyPlayerListener
    }

    interface StoryPlayerListener {
        fun onStartedPlaying(index: Int)
        fun onFinishedPlaying()
    }

    companion object {
        const val PROGRESS_BAR_HEIGHT = 2
        const val GAP_BETWEEN_PROGRESS_BARS = 2
        const val SINGLE_STORY_DISPLAY_TIME = 1000
        const val PROGRESS_PRIMARY_COLOR = "#009988"
        const val PROGRESS_SECONDARY_COLOR = "#EEEEEE"
    }
}
