package com.moreirasoft.materialstoryview.model

import android.content.Context
import com.moreirasoft.materialstoryview.data.StoryPreference
import com.moreirasoft.materialstoryview.utils.MaterialStoryViewHeaderInfo
import java.util.Date

data class MaterialStory(
    val date: Date,
    val imageUrl: String,
    val actText: String?,
    val actUrl: String?,
    val title: String?,
    val description: String?,
    var materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo? = null,
){
    fun checkVisited(context: Context) {
        StoryPreference(context).setStoryVisited(imageUrl)
    }

    fun uncheckVisited(context: Context) {
        StoryPreference(context).setStoryNotVisited(imageUrl)
    }

    fun isStoryVisited(context: Context): Boolean {
        return StoryPreference(context).isStoryVisited(imageUrl)
    }
}
