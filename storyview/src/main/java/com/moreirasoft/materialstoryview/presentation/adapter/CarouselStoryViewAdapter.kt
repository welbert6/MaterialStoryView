package com.moreirasoft.materialstoryview.presentation.adapter

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.moreirasoft.materialstoryview.R
import com.moreirasoft.materialstoryview.callback.OnStoryChangedCallback
import com.moreirasoft.materialstoryview.core.StyleMaterialCarouselStoryView
import com.moreirasoft.materialstoryview.model.MaterialStory
import com.moreirasoft.materialstoryview.model.MaterialStoryViewHeaderInfo
import com.moreirasoft.materialstoryview.presentation.customviews.StoryView


/**
 * Created by Welbert on 28/01/2024
 */
internal class CarouselStoryViewAdapter(
    private val activity: Activity,
    private val styleMaterialCarouselStoryView: StyleMaterialCarouselStoryView,
) : RecyclerView.Adapter<CarouselStoryViewAdapter.CarouselStoryViewViewHolder>(),
    OnStoryChangedCallback {

    private lateinit var context: Context
    private val listStoryViewHeader = ArrayList<MaterialStoryViewHeaderInfo>()

    inner class CarouselStoryViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val storyView: StoryView = itemView.findViewById(R.id.storyView)
        val miniStoryTitle: TextView = itemView.findViewById(R.id.miniStoryTitle)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CarouselStoryViewViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_mini_story, parent, false)
        return CarouselStoryViewViewHolder(view).apply {
            styleMaterialCarouselStoryView.miniStoryTextSize?.let {
                this.miniStoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, it)
            }

            styleMaterialCarouselStoryView.miniStoryTextColor?.let {
                this.miniStoryTitle.setTextColor(it)
            }

            styleMaterialCarouselStoryView.miniPendingIndicatorColor.let {
                this.storyView.setPendingIndicatorColor(it)
            }

            styleMaterialCarouselStoryView.miniVisitedIndicatorColor.let {
                this.storyView.setIndicatorVisitedColor(it)
            }
            this.storyView.setSpaceBetweenImageAndIndicator(styleMaterialCarouselStoryView.miniSpaceBetweenImageAndIndicator)
            this.storyView.setIndicatorMiniStoryImageWidth(styleMaterialCarouselStoryView.miniStoryItemIndicatorWidth)
            this.storyView.setImageRadius(styleMaterialCarouselStoryView.miniStoryImageRadius)
            this.storyView.setDuration(styleMaterialCarouselStoryView.storyDuration)
            this.storyView.setActivityContext(activity as AppCompatActivity)
            this.storyView.setButtonActionBackgroundColor(styleMaterialCarouselStoryView.storyButtonActionBackgroundColor)
            this.itemView.invalidate()
            this.storyView.addOnStoryChangedCallback(this@CarouselStoryViewAdapter)
        }
    }

    override fun getItemCount(): Int {
        return listStoryViewHeader.size
    }

    override fun onBindViewHolder(holder: CarouselStoryViewViewHolder, position: Int) {
        val headerInfoStory = listStoryViewHeader[position]
        var indexToStart = -1

        headerInfoStory.stories.forEach {

            if (indexToStart == -1 && !it.isStoryVisited(context)) {
                indexToStart = headerInfoStory.stories.indexOf(it)
                return@forEach
            }
        }
        holder.storyView.setImageUris(position, indexToStart, listStoryViewHeader)
        holder.miniStoryTitle.text = headerInfoStory.title
        holder.storyView.invalidate()
    }

    override fun storyStarted(headerInfo: MaterialStoryViewHeaderInfo, story: MaterialStory) {
        notifyItemChanged(listStoryViewHeader.indexOf(headerInfo))
    }

    override fun storyChanged(headerInfo: MaterialStoryViewHeaderInfo, storyPosition: Int) {
        var hasNoMoreStoryToSee = true
        headerInfo.stories.forEach {
            if (!it.isStoryVisited(context)) {
                hasNoMoreStoryToSee = false
                return@forEach
            }
        }
        if (hasNoMoreStoryToSee && headerInfo.stories[storyPosition].isStoryVisited(context)) {
            val indexToRemove = listStoryViewHeader.indexOf(headerInfo)
            listStoryViewHeader.removeAt(indexToRemove)
            listStoryViewHeader.add(headerInfo)
            notifyDataSetChanged()
        }

    }

    fun addStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        if (materialStoryViewHeaderInfo.stories.isNotEmpty()){
            listStoryViewHeader.add(materialStoryViewHeaderInfo)
            notifyItemInserted(listStoryViewHeader.indexOf(materialStoryViewHeaderInfo))
        }
    }

    fun addStories(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        materialStoryViewHeaderInfo.forEach {
            addStory(it)
        }
    }

    fun deleteStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        listStoryViewHeader.remove(materialStoryViewHeaderInfo)
        notifyItemRemoved(listStoryViewHeader.indexOf(materialStoryViewHeaderInfo))
    }

    fun deleteStories(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        materialStoryViewHeaderInfo.forEach {
            deleteStory(it)
        }
    }

    fun updateStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        val indexOf = listStoryViewHeader.indexOf(materialStoryViewHeaderInfo)
        listStoryViewHeader[indexOf] = materialStoryViewHeaderInfo
        notifyItemChanged(indexOf)
    }

    fun updateStories(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        materialStoryViewHeaderInfo.forEach {
            updateStory(it)
        }
    }

    fun clear() {
        listStoryViewHeader.clear()
        notifyDataSetChanged()
    }
}
