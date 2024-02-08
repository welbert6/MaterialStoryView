package com.moreirasoft.materialstoryview.data

import android.content.Context
import android.content.SharedPreferences

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

    fun setStoryVisited(uri: String?) {
        editor.putBoolean(uri, true).apply()
    }

    fun setStoryNotVisited(uri: String?) {
        editor.putBoolean(uri, false).apply()
    }

    fun isStoryVisited(uri: String?): Boolean {
        return preferences.getBoolean(uri, false)
    }

    companion object {
        private lateinit var preferences: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        private const val PREF_NAME = "storyview_cache_pref"
    }
}
