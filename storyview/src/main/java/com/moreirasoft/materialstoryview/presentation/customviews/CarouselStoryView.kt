package com.moreirasoft.materialstoryview.presentation.customviews

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moreirasoft.materialstoryview.R
import com.moreirasoft.materialstoryview.core.StyleMaterialCarouselStoryView
import com.moreirasoft.materialstoryview.model.MaterialStory
import com.moreirasoft.materialstoryview.model.MaterialStoryViewHeaderInfo
import com.moreirasoft.materialstoryview.presentation.adapter.CarouselStoryViewAdapter

/**
 * Created by Welbert on 30/01/2024
 */
class CarouselStoryView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    private var styleMaterialCarouselStoryView: StyleMaterialCarouselStoryView? = null

    init {
        val ats = context.obtainStyledAttributes(attrs, R.styleable.MaterialCarouselStoryView, 0, 0)
        try {
            val miniStoryTextSize =
                ats.getDimensionPixelSize(
                    R.styleable.MaterialCarouselStoryView_miniStoryTextSize,
                    resources.getDimension(R.dimen.default_text_size).toInt(),
                )

            val miniStoryTextColor = ats.getColor(
                R.styleable.MaterialCarouselStoryView_miniStoryTextColor,
                Color.BLACK,
            )

            val miniVisitedIndicatorColor = ats.getColor(
                R.styleable.MaterialCarouselStoryView_miniStoryVisitedIndicatorColor,
                Color.GRAY,
            )

            val miniPendingIndicatorColor = ats.getColor(
                R.styleable.MaterialCarouselStoryView_miniStoryPendingIndicatorColor,
                resources.getColor(R.color.miniStoryPendingIndicatorColor, resources.newTheme()),
            )

            val storyDuration = ats.getInteger(
                R.styleable.MaterialCarouselStoryView_storyDuration,
                STORY_DURATION,
            )

            val miniStoryImageRadius = ats.getDimensionPixelOffset(
                R.styleable.MaterialCarouselStoryView_miniStoryImageRadius,
                STORY_IMAGE_RADIUS_IN_DP,
            )

            val miniStoryItemIndicatorWidth = ats.getDimensionPixelOffset(
                R.styleable.MaterialCarouselStoryView_miniStoryItemIndicatorWidth,
                STORY_INDICATOR_WIDTH_IN_DP
            )


            val miniStorySpaceBetweenImageAndIndicator = ats.getDimensionPixelOffset(
                R.styleable.MaterialCarouselStoryView_miniStorySpaceBetweenImageAndIndicator,
                SPACE_BETWEEN_IMAGE_AND_INDICATOR
            )



            styleMaterialCarouselStoryView = StyleMaterialCarouselStoryView(
                miniStoryTextSize = miniStoryTextSize.toFloat(),
                miniStoryTextColor = miniStoryTextColor,
                miniStoryImageRadius = miniStoryImageRadius,
                miniVisitedIndicatorColor = miniVisitedIndicatorColor,
                miniPendingIndicatorColor = miniPendingIndicatorColor,
                miniStoryItemIndicatorWidth = miniStoryItemIndicatorWidth,
                miniSpaceBetweenImageAndIndicator = miniStorySpaceBetweenImageAndIndicator,
                storyDuration = storyDuration,
            )
        } finally {
            ats.recycle()
        }
    }

    companion object {
        const val DEFAULT_MINI_TEXT_SIZE_IN_SP = 14
        const val STORY_IMAGE_RADIUS_IN_DP = 90
        const val STORY_INDICATOR_WIDTH_IN_DP = 2
        const val STORY_DURATION = 6000
        const val SPACE_BETWEEN_IMAGE_AND_INDICATOR = 4
        const val START_ANGLE = 270
        const val PENDING_INDICATOR_COLOR = "#009988"
        const val VISITED_INDICATOR_COLOR = "#33009988"
        var ANGEL_OF_GAP = 15
    }

    private lateinit var activity: Activity
    fun initWithActivity(
        activity: Activity
    ) {
        this.activity = activity
        layoutManager = LinearLayoutManager(activity, HORIZONTAL, false)
        adapter = styleMaterialCarouselStoryView?.let {
            CarouselStoryViewAdapter(
                activity,
                it,
            )
        }
    }

    fun addStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        if (adapter == null) {
            throw Exception("You should call initWithActivity before addStory")
        }
        materialStoryViewHeaderInfo.stories.forEach {
            cacheImage(it)
        }
        (adapter as CarouselStoryViewAdapter).addStory(materialStoryViewHeaderInfo)
    }

    private fun cacheImage(it: MaterialStory) {
        Glide.with(context)
            .load(it.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .submit()
    }

    fun addStories(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        if (adapter == null) {
            throw Exception("You should call initWithActivity before addStories")
        }
        materialStoryViewHeaderInfo.forEach {
            it.stories.forEach { cacheImage(it) }
        }

        (adapter as CarouselStoryViewAdapter).addStories(materialStoryViewHeaderInfo)
    }

    fun deleteStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        if (adapter == null) {
            throw Exception("You should call initWithActivity before deleteStory")
        }
        (adapter as CarouselStoryViewAdapter).deleteStory(materialStoryViewHeaderInfo)
    }

    fun deleteStories(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        if (adapter == null) {
            throw Exception("You should call initWithActivity before deleteStories")
        }
        (adapter as CarouselStoryViewAdapter).deleteStories(materialStoryViewHeaderInfo)
    }

    fun updateStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        if (adapter == null) {
            throw Exception("You should call initWithActivity before updateStory")
        }
        (adapter as CarouselStoryViewAdapter).updateStory(materialStoryViewHeaderInfo)
    }

    fun updateStories(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        if (adapter == null) {
            throw Exception("You should call initWithActivity before updateStories")
        }
        (adapter as CarouselStoryViewAdapter).updateStories(materialStoryViewHeaderInfo)
    }

    fun hasItem() : Boolean {
        return !isEmpty()
    }
    fun isEmpty() : Boolean {
        return adapter?.itemCount == 0
    }
    fun getItemCount(): Int {
        return if (adapter == null)
            0
        else
            (adapter as CarouselStoryViewAdapter).itemCount
    }

    fun clear() {
        if (adapter != null) {
            (adapter as CarouselStoryViewAdapter).clear()
        }

    }
}
