package com.moreirasoft.materialstoryview.core

import android.graphics.Color

/**
 * Created by Welbert on 01/02/2024
 */
internal data class StyleMaterialCarouselStoryView(
    var miniStoryTextSize: Float? = 0F,
    var miniStoryTextColor: Int? = 0,
    var miniVisitedIndicatorColor: Int = Color.GREEN,
    var miniPendingIndicatorColor: Int = Color.GRAY,
    var storyButtonActionBackgroundColor: Int = Color.GREEN,
    var miniStoryImageRadius: Int = 90,
    var miniSpaceBetweenImageAndIndicator: Int = 2,
    var miniStoryItemIndicatorWidth: Int = 2,
    var storyDuration: Int = 6000,

)
