package com.moreirasoft.materialstoryview.callback

import com.moreirasoft.materialstoryview.model.MaterialStory
import com.moreirasoft.materialstoryview.model.MaterialStoryViewHeaderInfo

interface OnStoryChangedCallback {
    fun storyStarted(headerInfo: MaterialStoryViewHeaderInfo, story: MaterialStory)
    fun storyChanged(headerInfo: MaterialStoryViewHeaderInfo, storyPosition: Int)
}