package com.moreirasoft.materialstoryview.callback

import com.moreirasoft.materialstoryview.utils.MaterialStoryViewHeaderInfo

interface OnStoryChangedCallback {
    fun storyChanged(headerInfo: MaterialStoryViewHeaderInfo, storyPosition: Int)
}