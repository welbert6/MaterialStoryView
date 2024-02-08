package com.moreirasoft.materialstoryview.presentation.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.moreirasoft.materialstoryview.R
import com.moreirasoft.materialstoryview.data.StoryPreference
import com.moreirasoft.materialstoryview.callback.OnStoryChangedCallback
import com.moreirasoft.materialstoryview.core.StyleMaterialCarouselStoryView
import com.moreirasoft.materialstoryview.presentation.StoryView
import com.moreirasoft.materialstoryview.utils.MaterialStoryViewHeaderInfo

/**
 * Created by Welbert on 28/01/2024
 */
class CarouselStoryViewAdapter(
    private val activity: AppCompatActivity,
    private val listStoryViewHeader: ArrayList<MaterialStoryViewHeaderInfo>,
    private val styleMaterialCarouselStoryView: StyleMaterialCarouselStoryView,
) : RecyclerView.Adapter<CarouselStoryViewAdapter.CarouselStoryViewViewHolder>(),
    OnStoryChangedCallback {

    private lateinit var context: Context

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

            styleMaterialCarouselStoryView.miniPendingIndicatorColor?.let {
                this.storyView.setPendingIndicatorColor(it)
            }

            styleMaterialCarouselStoryView.miniVisitedIndicatorColor?.let {
                this.storyView.setIndicatorVisitedColor(it)
            }
            this.storyView.setImageRadius(styleMaterialCarouselStoryView.miniStoryImageRadius)
            this.storyView.setDuration(styleMaterialCarouselStoryView.storyDuration)
            this.storyView.setActivityContext(activity)
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
        headerInfoStory.storys.forEach {
            it.materialStoryViewHeaderInfo = headerInfoStory
            if (indexToStart == -1 && !StoryPreference(activity).isStoryVisited(it.imageUrl)) {
                indexToStart = headerInfoStory.storys.indexOf(it)
                return@forEach
            }
        }
        holder.storyView.setImageUris(position, indexToStart, listStoryViewHeader)
        holder.miniStoryTitle.text = headerInfoStory.title
    }

    override fun storyChanged(headerInfo: MaterialStoryViewHeaderInfo, storyPosition: Int) {
        var hasNoMoreStoryToSee = true
        headerInfo.storys.forEach {
            if (!it.isStoryVisited(context)) {
                hasNoMoreStoryToSee = false
                return@forEach
            }
        }
        if (hasNoMoreStoryToSee && headerInfo.storys[storyPosition].isStoryVisited(context)) {
            val indexToRemove = listStoryViewHeader.indexOf(headerInfo)
            listStoryViewHeader.removeAt(indexToRemove)
            listStoryViewHeader.add(headerInfo)
            notifyDataSetChanged()
        }
    }

    fun addStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        listStoryViewHeader.add(materialStoryViewHeaderInfo)
        notifyItemInserted(listStoryViewHeader.indexOf(materialStoryViewHeaderInfo))
    }

    fun addStorys(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        materialStoryViewHeaderInfo.forEach {
            addStory(it)
        }
    }

    fun deleteStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        listStoryViewHeader.remove(materialStoryViewHeaderInfo)
        notifyItemRemoved(listStoryViewHeader.indexOf(materialStoryViewHeaderInfo))
    }

    fun deleteStorys(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        materialStoryViewHeaderInfo.forEach {
            deleteStory(it)
        }
    }

    fun updateStory(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        val indexOf = listStoryViewHeader.indexOf(materialStoryViewHeaderInfo)
        listStoryViewHeader[indexOf] = materialStoryViewHeaderInfo
        notifyItemChanged(indexOf)
    }

    fun updateStorys(materialStoryViewHeaderInfo: ArrayList<MaterialStoryViewHeaderInfo>) {
        materialStoryViewHeaderInfo.forEach {
            updateStory(it)
        }
    }

    fun clear() {
        listStoryViewHeader.clear()
        notifyDataSetChanged()
    }
}
