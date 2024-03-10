package com.moreirasoft.materialstoryview.presentation

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.moreirasoft.materialstoryview.R
import com.moreirasoft.materialstoryview.callback.OnStoryChangedCallback
import com.moreirasoft.materialstoryview.callback.StoryCallbacks
import com.moreirasoft.materialstoryview.callback.StoryClickListeners
import com.moreirasoft.materialstoryview.callback.TouchCallbacks
import com.moreirasoft.materialstoryview.model.MaterialStoryViewHeaderInfo
import com.moreirasoft.materialstoryview.presentation.adapter.ViewPagerAdapter
import com.moreirasoft.materialstoryview.presentation.customviews.PullDismissLayout
import com.moreirasoft.materialstoryview.presentation.progress.StoriesProgressView

class StoryViewActivity private constructor() :
    DialogFragment(),
    StoriesProgressView.StoriesListener,
    StoryCallbacks,
    PullDismissLayout.Listener,
    TouchCallbacks {

    private val DEFAULT_DURATION: Int = 7000

    private var storiesList: ArrayList<MaterialStoryViewHeaderInfo> = ArrayList()

    private val storiesProgressView: StoriesProgressView by lazy {
        requireView().findViewById(R.id.storiesProgressView)
    }
    private val viewPager: ViewPager by lazy {
        requireView().findViewById(R.id.storiesViewPager)
    }
    private var counter = 0
    private var startingIndex = 0

    // Heading
    private val titleTextView: TextView by lazy {
        requireView().findViewById(R.id.title_textView)
    }
    private val subtitleTextView: TextView by lazy {
        requireView().findViewById(R.id.subtitle_textView)
    }
    private val titleCardView: CardView by lazy {
        requireView().findViewById(R.id.titleCardView)
    }
    private val titleImageView: ImageView by lazy {
        requireView().findViewById(R.id.imageView)
    }
    private val closeImageButton: ImageButton by lazy {
        requireView().findViewById(R.id.imageButton)
    }

    // Touch Events
    private var isDownClick = false
    private var elapsedTime: Long = 0
    private var timerThread: Thread? = null
    private var isPaused = false
    private var width = 0
    private var height = 0
    private var xValue = 0f
    private var yValue = 0f
    private var currentHeaderInfo = 0
    private var storyClickListeners: StoryClickListeners? = null
    private var onStoryChangedCallback: OnStoryChangedCallback? = null
    private var isRtl = false

    private var duration: Long = DEFAULT_DURATION.toLong()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return inflater.inflate(R.layout.dialog_stories, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displaymetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displaymetrics)
        width = displaymetrics.widthPixels
        height = displaymetrics.heightPixels
        readArguments()
        setupViews(view)
        setupStories()
    }

    private fun setupStories() {
        storiesProgressView.setStoriesCount(storiesList[currentHeaderInfo].stories.size)
        storiesProgressView.setStoryDuration(duration)
        viewPager.adapter =
            ViewPagerAdapter(
                storiesList[currentHeaderInfo].stories,
                context,
                this
            )
    }

    private fun readArguments() {
        assert(arguments != null)
        storiesList =
            ArrayList(requireArguments().getSerializable(IMAGES_KEY) as ArrayList<MaterialStoryViewHeaderInfo>?)
        duration = requireArguments().getLong(DURATION_KEY, DEFAULT_DURATION.toLong())
        currentHeaderInfo = requireArguments().getInt(STARTING_HEADER_TAG, 0)
        startingIndex = requireArguments().getInt(STARTING_INDEX_TAG, 0)
        if (startingIndex < 0) startingIndex = 0
        isRtl = requireArguments().getBoolean(IS_RTL_TAG, false)
    }

    private fun setupViews(view: View) {
        (view.findViewById<View>(R.id.pull_dismiss_layout) as PullDismissLayout).setListener(this)
        (view.findViewById<View>(R.id.pull_dismiss_layout) as PullDismissLayout).setmTouchCallbacks(
            this,
        )

        storiesProgressView.setStoriesListener(this)
        viewPager.setOnTouchListener { _: View?, _: MotionEvent? -> true }
        closeImageButton.setOnClickListener { _: View? -> dismissAllowingStateLoss() }
        if (storyClickListeners != null) {
            titleCardView.setOnClickListener { _: View? ->
                storyClickListeners!!.onTitleIconClickListener(
                    counter,
                )
            }
        }
        if (onStoryChangedCallback != null) {
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                    onStoryChangedCallback!!.storyChanged(storiesList[currentHeaderInfo], position)
                }

                override fun onPageSelected(position: Int) {}
                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
        if (isRtl) {
            storiesProgressView.layoutDirection = View.LAYOUT_DIRECTION_LTR
            storiesProgressView.rotation = 180f
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params
    }

    override fun onNext() {
        nextStory()
    }

    override fun onPrev() {
        previousStory()
    }

    override fun onComplete() {
        dismissAllowingStateLoss()
    }

    override fun startStories() {
        counter = startingIndex
        storiesProgressView.startStories(startingIndex)
        viewPager.setCurrentItem(startingIndex, false)
        updateHeading(storiesList[currentHeaderInfo])
        context?.let {
            storiesList[currentHeaderInfo].stories[counter].checkVisited(it)
            onStoryChangedCallback!!.storyStarted(storiesList[currentHeaderInfo], storiesList[currentHeaderInfo].stories[counter])
        }
    }

    override fun pauseStories() {
        storiesProgressView.pause()
    }

    private fun previousStory() {
        counter--
        if (counter < 0) {
            if (currentHeaderInfo - 1 < 0) {
                dismissAllowingStateLoss()
            } else {
                currentHeaderInfo--
                counter = 0
                updateHeading(storiesList[currentHeaderInfo])
                setupStories()
                viewPager.setCurrentItem(counter, false)
                storiesProgressView.startStories(counter)
            }
        } else {
            viewPager.setCurrentItem(counter, false)
            storiesProgressView.backTo(counter)
        }
    }

    override fun nextStory() {
        counter++
        if (counter >= storiesList[currentHeaderInfo].stories.size) {
            currentHeaderInfo++
            if (currentHeaderInfo >= storiesList.size) {
                dismissAllowingStateLoss()
            } else {
                counter = 0
                updateHeading(storiesList[currentHeaderInfo])
                setupStories()
                viewPager.setCurrentItem(counter, false)
                storiesProgressView.startStories(counter)
            }
        } else {
            viewPager.setCurrentItem(counter, false)
            storiesProgressView.startStories(counter)
        }
    }

    override fun onDescriptionClickListener(position: Int) {
        if (storyClickListeners == null) return
        storyClickListeners!!.onDescriptionClickListener(position)
    }

    override fun onActClickListener(position: Int) {
        activity?.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(storiesList[currentHeaderInfo].stories[counter].actUrl),
            ),
        )
    }

    override fun onDestroy() {
        timerThread = null
        storiesList.clear()
        storiesProgressView.destroy()
        super.onDestroy()
    }

    private fun updateHeading(materialStoryViewHeaderInfo: MaterialStoryViewHeaderInfo) {
        if (context == null) {
            return
        }

        Glide.with(requireContext())
            .load(materialStoryViewHeaderInfo.imageUrl)
            .into(titleImageView)

        titleTextView.text = materialStoryViewHeaderInfo.title
        subtitleTextView.text =
            DateUtils.getRelativeTimeSpanString(storiesList[currentHeaderInfo].stories[counter].date.time).toString()

    }

    private fun createTimer() {

        timerThread = Thread(
            Runnable {
                while (isDownClick) {
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    elapsedTime += 100




                    if (elapsedTime >= 500 && !isPaused) {
                        isPaused = true
                        if (activity == null) return@Runnable
                        requireActivity().runOnUiThread {
                            storiesProgressView.pause()
                        }
                    }
                }
                isPaused = false
                if (activity == null) return@Runnable
                if (elapsedTime < 500) return@Runnable
                requireActivity().runOnUiThread {
                    storiesProgressView.resume()
                }
            },
        )
    }

    private fun runTimer() {
        isDownClick = true
        createTimer()
        timerThread!!.start()
    }

    private fun stopTimer() {
        isDownClick = false
    }

    override fun onDismissed() {
        dismissAllowingStateLoss()
    }

    override fun onShouldInterceptTouchEvent(): Boolean {
        return false
    }

    override fun touchPull() {
        elapsedTime = 0
        stopTimer()
        storiesProgressView.pause()
    }

    override fun touchDown(xValue: Float, yValue: Float) {
        this.xValue = xValue
        this.yValue = yValue
        if (!isDownClick) {
            runTimer()
        }
    }

    override fun touchUp() {
        if (isDownClick && elapsedTime < 500) {
            stopTimer()
            if ((height - yValue).toInt() <= 0.8 * height) {
                if (!TextUtils.isEmpty(storiesList[currentHeaderInfo].stories[counter].title) && (height - yValue).toInt() >= 0.2 * height ||
                    TextUtils.isEmpty((storiesList[currentHeaderInfo].stories[counter].description))
                ) {
                    if (xValue.toInt() <= width / 2) {
                        // Left
                        if (isRtl) {
                            nextStory()
                        } else {
                            previousStory()
                        }
                    } else {
                        // Right
                        if (isRtl) {
                            previousStory()
                        } else {
                            nextStory()
                        }
                    }
                }
            }
        } else {
            stopTimer()
            storiesProgressView.resume()
        }
        elapsedTime = 0
    }

    fun setStoryClickListeners(storyClickListeners: StoryClickListeners?) {
        this.storyClickListeners = storyClickListeners
    }

    fun setOnStoryChangedCallback(onStoryChangedCallback: OnStoryChangedCallback?) {
        this.onStoryChangedCallback = onStoryChangedCallback
    }

    class Builder(private val fragmentManager: FragmentManager) {
        private val storyViewActivity = StoryViewActivity()
        private val bundle: Bundle = Bundle()
        private var storyClickListeners: StoryClickListeners? = null
        private var onStoryChangedCallback: OnStoryChangedCallback? = null

        fun setStoriesList(storiesList: ArrayList<MaterialStoryViewHeaderInfo>): Builder {
            bundle.putSerializable(IMAGES_KEY, storiesList)
            return this
        }

        fun setStoryDuration(duration: Long): Builder {
            bundle.putLong(DURATION_KEY, duration)
            return this
        }

        fun setStartingIndex(index: Int): Builder {
            bundle.putInt(STARTING_INDEX_TAG, index)
            return this
        }

        fun setStartingHeaderInfo(index: Int): Builder {
            bundle.putInt(STARTING_HEADER_TAG, index)
            return this
        }

        fun build(): Builder {
            storyViewActivity.arguments = bundle
            if (storyClickListeners != null) {
                storyViewActivity.setStoryClickListeners(storyClickListeners)
            }
            if (onStoryChangedCallback != null) {
                storyViewActivity.setOnStoryChangedCallback(onStoryChangedCallback)
            }
            return this
        }

        fun setOnStoryChangedCallback(onStoryChangedCallback: OnStoryChangedCallback?): Builder {
            this.onStoryChangedCallback = onStoryChangedCallback
            return this
        }

        fun setRtl(isRtl: Boolean): Builder {
            bundle.putBoolean(IS_RTL_TAG, isRtl)
            return this
        }

        fun setStoryClickListeners(storyClickListeners: StoryClickListeners?): Builder {
            this.storyClickListeners = storyClickListeners
            return this
        }

        fun show() {
            storyViewActivity.show(fragmentManager, TAG)
        }

        fun dismiss() {
            storyViewActivity.dismiss()
        }

        val fragment: Fragment?
            get() = storyViewActivity
    }

    companion object {
        private val TAG = StoryViewActivity::class.java.simpleName
        private const val IMAGES_KEY = "IMAGES"
        private const val DURATION_KEY = "DURATION"
        private const val STARTING_HEADER_TAG = "STARTING_HEADER_TAG"
        private const val STARTING_INDEX_TAG = "STARTING_INDEX"
        private const val IS_RTL_TAG = "IS_RTL"
    }
}
