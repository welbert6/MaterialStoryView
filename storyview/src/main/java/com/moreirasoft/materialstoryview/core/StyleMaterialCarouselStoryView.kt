package com.moreirasoft.materialstoryview.core

/**
 * Created by Welbert on 01/02/2024
 */
data class StyleMaterialCarouselStoryView(
    var miniStoryTextSize: Float? = 0F,
    var miniStoryTextColor: Int? = 0,
    var miniVisitedIndicatorColor: Int? = 0,
    var miniPendingIndicatorColor: Int? = 0,
    var miniStoryImageRadius: Int = 90,
    var miniSpaceBetweenImageAndIndicator: Int? = 0,
    var miniStoryItemIndicatorWidth: Int? = 0,
    var storyDuration: Int = 6000,
)
