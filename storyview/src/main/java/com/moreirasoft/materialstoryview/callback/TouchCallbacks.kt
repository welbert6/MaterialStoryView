package com.moreirasoft.materialstoryview.callback

interface TouchCallbacks {
    fun touchPull()
    fun touchDown(xValue: Float, yValue: Float)
    fun touchUp()
}