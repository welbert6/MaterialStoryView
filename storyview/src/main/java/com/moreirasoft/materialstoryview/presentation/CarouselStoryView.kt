package com.moreirasoft.materialstoryview.presentation

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.moreirasoft.materialstoryview.R
import com.moreirasoft.materialstoryview.core.StyleMaterialCarouselStoryView
import com.moreirasoft.materialstoryview.presentation.adapter.CarouselStoryViewAdapter
import com.moreirasoft.materialstoryview.utils.MaterialStoryViewHeaderInfo

/**
 * Created by Welbert on 30/01/2024
 */
class CarouselStoryView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    private var styleMaterialCarouselStoryView: StyleMaterialCarouselStoryView? = null
    private var miniVisitedIndicatorColor = 0
    private var miniPendingIndicatorColor = 0
    private var miniStoryImageRadius = 0
    private var miniSpaceBetweenImageAndIndicator = 0
    private var miniStoryItemIndicatorWidth = 0

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
                6000,
            )

            val miniStoryImageRadius = ats.getDimensionPixelOffset(
                R.styleable.MaterialCarouselStoryView_miniStoryImageRadius,
                STORY_IMAGE_RADIUS_IN_DP,
            ).toFloat()

            styleMaterialCarouselStoryView = StyleMaterialCarouselStoryView(
                miniStoryTextSize = miniStoryTextSize.toFloat(),
                miniStoryTextColor = miniStoryTextColor,
                miniStoryImageRadius = miniStoryImageRadius.toInt(),
                miniVisitedIndicatorColor = miniVisitedIndicatorColor,
                miniPendingIndicatorColor = miniPendingIndicatorColor,
                storyDuration = storyDuration,
            )
        } finally {
            ats.recycle()
        }
    }
    companion object {
        const val DEFAULT_MINI_TEXT_SIZE_IN_SP = 14
        const val STORY_IMAGE_RADIUS_IN_DP = 90
        const val STORY_INDICATOR_WIDTH_IN_DP = 4
        const val SPACE_BETWEEN_IMAGE_AND_INDICATOR = 4
        const val START_ANGLE = 270
        const val PENDING_INDICATOR_COLOR = "#009988"
        const val VISITED_INDICATOR_COLOR = "#33009988"
        var ANGEL_OF_GAP = 15
    }

    private lateinit var activity: AppCompatActivity
    fun setItems(
        activity: AppCompatActivity,
        materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>,
    ) {
        this.activity = activity
        adapter = styleMaterialCarouselStoryView?.let {
            CarouselStoryViewAdapter(
                activity,
                materialStoryViewHeaderInfo,
                it,
            )
        }
    }

    fun addStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        (adapter as CarouselStoryViewAdapter).addStory(materialStoryViewHeaderInfo)
    }

    fun addStories(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        (adapter as CarouselStoryViewAdapter).addStorys(materialStoryViewHeaderInfo)
    }

    fun deleteStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        (adapter as CarouselStoryViewAdapter).deleteStory(materialStoryViewHeaderInfo)
    }

    fun deleteStories(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        (adapter as CarouselStoryViewAdapter).deleteStorys(materialStoryViewHeaderInfo)
    }

    fun updateStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        (adapter as CarouselStoryViewAdapter).updateStory(materialStoryViewHeaderInfo)
    }

    fun updateStories(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        (adapter as CarouselStoryViewAdapter).updateStorys(materialStoryViewHeaderInfo)
    }

    fun clear() {
        (adapter as CarouselStoryViewAdapter).clear()
    }
}
