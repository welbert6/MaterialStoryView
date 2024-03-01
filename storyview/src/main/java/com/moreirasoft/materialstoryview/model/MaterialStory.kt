package com.moreirasoft.materialstoryview.model

import android.content.Context
import com.moreirasoft.materialstoryview.data.StoryPreference
import java.io.Serializable
import java.util.Date

data class MaterialStory(
    val date: Date,
    val imageUrl: String,
    val actText: String?,
    val actUrl: String?,
    val title: String?,
    val description: String?
) : Serializable {

    internal  fun getUniqueCode() : String{
        return  title+date.toString()+description+imageUrl
    }
    fun checkVisited(context: Context) {
        StoryPreference(context).setStoryVisited(this)
    }

    fun uncheckVisited(context: Context) {
        StoryPreference(context).setStoryNotVisited(this)
    }

    fun isStoryVisited(context: Context): Boolean {
        return StoryPreference(context).isStoryVisited(this)
    }
}
