package com.moreirasoft.materialstoryview.presentation

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.moreirasoft.materialstoryview.R
import com.moreirasoft.materialstoryview.data.StoryPreference
import com.moreirasoft.materialstoryview.callback.OnStoryChangedCallback
import com.moreirasoft.materialstoryview.callback.StoryClickListeners
import com.moreirasoft.materialstoryview.utils.MaterialStoryViewHeaderInfo

/**
 * Created by Welbert on 01/02/2024
 */

class StoryView : View {
    private var currentItem: Int = 0
    private var mDuration: Int = 6000
    private var currentHeaderInfo: Int = 0
    private var storyPreference: StoryPreference? = null
    private var mStoryImageRadiusInPx = 40
    private var mStoryIndicatorWidthInPx = 0
    private var mSpaceBetweenImageAndIndicator = 0
    private var mPendingIndicatorColor = 0
    private var mVisitedIndicatorColor = 0
    private var mViewWidth = 0
    private var mViewHeight = 0
    private var mIndicatoryOffset = 0
    private var mIndicatorImageOffset = 0
    private var resources: Resources? = null
    private var storyImageUris: ArrayList<MaterialStoryViewHeaderInfo>? = null
    private var indicatorCount = 0
    private var indicatorSweepAngle = 0
    private var mIndicatorImageBitmap: Bitmap? = null
    private var mIndicatorImageRect: Rect? = null
    private var mContext: AppCompatActivity? = null
    var onStoryChangedCallback: OnStoryChangedCallback? = null

    constructor(context: Context) : super(context) {
        init(context)
        setDefaults()
    }

    private fun init(context: Context) {
        storyPreference = StoryPreference(context)
        resources = context.resources
        storyImageUris = ArrayList()

        if (isInEditMode) {
            val icon = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.exemplastoryb,
            )
            mIndicatorImageBitmap = icon
            invalidate()
        }
    }

    fun addOnStoryChangedCallback(onStoryChangedCallback: OnStoryChangedCallback) {
        this.onStoryChangedCallback = onStoryChangedCallback
    }

    private fun setDefaults() {
        mStoryImageRadiusInPx = getPxFromDp(STORY_IMAGE_RADIUS_IN_DP)
        mStoryIndicatorWidthInPx = getPxFromDp(STORY_INDICATOR_WIDTH_IN_DP)
        mSpaceBetweenImageAndIndicator = getPxFromDp(SPACE_BETWEEN_IMAGE_AND_INDICATOR)
        mPendingIndicatorColor = Color.parseColor(PENDING_INDICATOR_COLOR)
        mVisitedIndicatorColor = Color.parseColor(VISITED_INDICATOR_COLOR)
        prepareValues()
        if (isInEditMode) {
            indicatorCount = 1
            calculateSweepAngle(indicatorCount)
            invalidate()
            loadFirstImageBitamp()
        }
    }

    private fun getPxFromDp(dpValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue.toFloat(),
            resources!!.displayMetrics,
        ).toInt()
    }

    private fun prepareValues() {
        mViewHeight =
            2 * (mStoryIndicatorWidthInPx + mSpaceBetweenImageAndIndicator + mStoryImageRadiusInPx)
        mViewWidth = mViewHeight
        mIndicatoryOffset = mStoryIndicatorWidthInPx / 2
        mIndicatorImageOffset = mStoryIndicatorWidthInPx + mSpaceBetweenImageAndIndicator
        mIndicatorImageRect = Rect(
            mIndicatorImageOffset,
            mIndicatorImageOffset,
            mViewWidth - mIndicatorImageOffset,
            mViewHeight - mIndicatorImageOffset,
        )
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.StoryView, 0, 0)
        try {
            mStoryImageRadiusInPx = getPxFromDp(
                mStoryImageRadiusInPx,
            )
            mStoryIndicatorWidthInPx = getPxFromDp(
                ta.getDimension(
                    R.styleable.StoryView_storyItemIndicatorWidth,
                    STORY_INDICATOR_WIDTH_IN_DP.toFloat(),
                ).toInt(),
            )
            mSpaceBetweenImageAndIndicator = getPxFromDp(
                ta.getDimension(
                    R.styleable.StoryView_spaceBetweenImageAndIndicator,
                    SPACE_BETWEEN_IMAGE_AND_INDICATOR.toFloat(),
                ).toInt(),
            )
            mPendingIndicatorColor = ta.getColor(
                R.styleable.StoryView_pendingIndicatorColor,
                Color.parseColor(
                    PENDING_INDICATOR_COLOR,
                ),
            )
            mVisitedIndicatorColor = ta.getColor(
                R.styleable.StoryView_visitedIndicatorColor,
                Color.parseColor(
                    VISITED_INDICATOR_COLOR,
                ),
            )
        } finally {
            ta.recycle()
        }
        prepareValues()
    }

    fun setActivityContext(activityContext: AppCompatActivity?) {
        mContext = activityContext
    }

    fun resetStoryVisits() {
        storyPreference!!.clearStoryPreferences()
    }

    fun setImageUris(
        currentHeaderInfo: Int,
        currentItem: Int,
        imageUris: ArrayList<MaterialStoryViewHeaderInfo>,
    ) {
        this.currentHeaderInfo = currentHeaderInfo
        this.currentItem = currentItem
        storyImageUris = imageUris
        indicatorCount = imageUris[currentHeaderInfo].storys.size
        calculateSweepAngle(indicatorCount)
        loadFirstImageBitamp()
    }

    private fun calculateSweepAngle(itemCounts: Int) {
        indicatorSweepAngle = 360 / itemCounts - ANGEL_OF_GAP / 2
    }

    private fun loadFirstImageBitamp() {
        if (isInEditMode) {
            val icon = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.exemplastoryb,
            )
            mIndicatorImageBitmap = icon
            invalidate()
        } else {
            Glide.with(this)
                .asBitmap()
                .circleCrop()
                .load(storyImageUris!![currentHeaderInfo].imageUrl)
                .into(object : SimpleTarget<Bitmap?>() {

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?,
                    ) {
                        mIndicatorImageBitmap = resource
                        invalidate()
                    }
                })
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            navigateToStoryPlayerPage()
            return true
        }
        return true
    }

    private fun navigateToStoryPlayerPage() {
        if (mContext == null) {
            throw RuntimeException("Activity Context MUST not be null. You need to call StoryView.setActivityContext(activity) first")
        } else {
            StoryViewActivity.Builder(mContext?.supportFragmentManager!!)
                .setStoriesList(storyImageUris!!)
                .setStoryDuration(mDuration.toLong())
                .setStoryClickListeners(object : StoryClickListeners {
                    override fun onDescriptionClickListener(position: Int) {
                        Toast.makeText(
                            mContext,
                            "Clicked: " + storyImageUris!![position],
                            Toast.LENGTH_SHORT,
                        ).show()
                    }

                    override fun onTitleIconClickListener(position: Int) {}
                })
                .setOnStoryChangedCallback(object : OnStoryChangedCallback {
                    override fun storyChanged(
                        headerInfo: MaterialStoryViewHeaderInfo,
                        storyPosition: Int,
                    ) {
                        onStoryChangedCallback?.storyChanged(headerInfo, storyPosition)
                    }
                })
                .setStartingHeaderInfo(currentHeaderInfo)
                .setStartingIndex(currentItem)
                .build()
                .show()
        }
    }

    fun setImageRadius(radius: Int) {
        mStoryImageRadiusInPx = radius
        prepareValues()
        invalidate()
    }

    fun setDuration(duration: Int) {
        mDuration = duration
    }

    fun setIndicatorVisitedColor(indicatorVisitedColor: Int) {
        mVisitedIndicatorColor = indicatorVisitedColor
        invalidate()
    }

    fun setPendingIndicatorColor(pendingIndicatorColor: Int) {
        mPendingIndicatorColor = pendingIndicatorColor
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
        paint.color = mPendingIndicatorColor
        paint.strokeWidth = mStoryIndicatorWidthInPx.toFloat()
        var startAngle = START_ANGLE + ANGEL_OF_GAP / 2
        for (i in 0 until indicatorCount) {
            paint.color = getIndicatorColor(i)
            canvas.drawArc(
                mIndicatoryOffset.toFloat(),
                mIndicatoryOffset.toFloat(),
                (mViewWidth - mIndicatoryOffset).toFloat(),
                (mViewHeight - mIndicatoryOffset).toFloat(),
                startAngle.toFloat(),
                if (storyImageUris?.get(currentHeaderInfo)?.storys.isNullOrEmpty() ||
                    storyImageUris?.get(currentHeaderInfo)!!.storys.size == 1
                ) {
                    360f
                } else {
                    (indicatorSweepAngle - ANGEL_OF_GAP / 2).toFloat()
                },
                false,
                paint,
            )
            startAngle += indicatorSweepAngle + ANGEL_OF_GAP / 2
        }
        if (mIndicatorImageBitmap != null) {
            canvas.drawBitmap(mIndicatorImageBitmap!!, null, mIndicatorImageRect!!, null)
        }
    }

    private fun getIndicatorColor(index: Int): Int {
        return if (storyPreference!!.isStoryVisited(storyImageUris!![currentHeaderInfo].storys[index].imageUrl)) mVisitedIndicatorColor else mPendingIndicatorColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = paddingStart + paddingEnd + mViewWidth
        val height = paddingTop + paddingBottom + mViewHeight
        val w = resolveSizeAndState(width, widthMeasureSpec, 0)
        val h = resolveSizeAndState(height, heightMeasureSpec, 0)
        setMeasuredDimension(w, h)
    }

    companion object {
        const val STORY_IMAGE_RADIUS_IN_DP = 90
        const val STORY_INDICATOR_WIDTH_IN_DP = 4
        const val SPACE_BETWEEN_IMAGE_AND_INDICATOR = 4
        const val START_ANGLE = 270
        const val PENDING_INDICATOR_COLOR = "#009988"
        const val VISITED_INDICATOR_COLOR = "#33009988"
        var ANGEL_OF_GAP = 15
    }
}
