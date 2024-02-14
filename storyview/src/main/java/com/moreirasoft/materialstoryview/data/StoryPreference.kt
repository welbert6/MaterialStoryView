package com.moreirasoft.materialstoryview.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.moreirasoft.materialstoryview.model.MaterialStory

/**
 * Created by Welbert on 01/02/2024
 */
internal class StoryPreference(context: Context) {
    init {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = preferences.edit()
        editor.apply()
    }

    fun clearStoryPreferences() {
        editor.clear()
        editor.apply()
    }

    fun setStoryVisited(materialStory :MaterialStory) {
        editor.putBoolean(materialStory.getUniqueCode(), true).apply()
        Log.v("ISSUEVISITED", "Story = "+materialStory.getUniqueCode()+ " set to true")

    }

    fun setStoryNotVisited(materialStory: MaterialStory) {
        editor.putBoolean(materialStory.getUniqueCode(), false).apply()
    }

    fun isStoryVisited(materialStory: MaterialStory): Boolean {
        return preferences.getBoolean(materialStory.getUniqueCode(), false)
    }

    companion object {
        private lateinit var preferences: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        private const val PREF_NAME = "storyview_cache_pref"
    }
}
