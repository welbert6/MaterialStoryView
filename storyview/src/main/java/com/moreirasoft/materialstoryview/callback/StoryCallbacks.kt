package com.moreirasoft.materialstoryview.callback

interface StoryCallbacks {
    fun startStories()
    fun pauseStories()
    fun nextStory()
    fun onDescriptionClickListener(position: Int)
    fun onActClickListener(position: Int)
}