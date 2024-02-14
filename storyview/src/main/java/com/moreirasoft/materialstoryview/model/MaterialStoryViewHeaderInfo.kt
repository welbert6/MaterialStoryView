package com.moreirasoft.materialstoryview.model

import java.io.Serializable

data class MaterialStoryViewHeaderInfo(
    val title: String,
    val imageUrl: String,
    val stories: ArrayList<MaterialStory>,
) : Serializable
