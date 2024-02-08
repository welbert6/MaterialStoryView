package com.moreirasoft.materialstoryview.utils

import com.moreirasoft.materialstoryview.model.MaterialStory
import java.io.Serializable

data class MaterialStoryViewHeaderInfo(
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val storys: ArrayList<MaterialStory>,
) : Serializable
