package com.moreirasoft.materialstoryview.presentation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.colormoon.readmoretextview.ReadMoreTextView;
import com.moreirasoft.materialstoryview.R;
import com.moreirasoft.materialstoryview.callback.StoryCallbacks;
import com.moreirasoft.materialstoryview.model.MaterialStory;
import com.moreirasoft.materialstoryview.presentation.StoryViewActivity;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private final ArrayList<MaterialStory> images;
    private final StoryViewActivity activity;
    private final StoryCallbacks storyCallbacks;
    private boolean storiesStarted = false;

    public ViewPagerAdapter(ArrayList<MaterialStory> images, StoryViewActivity activity, StoryCallbacks storyCallbacks) {
        this.images = images;
        this.activity = activity;
        this.storyCallbacks = storyCallbacks;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, final int position) {

        LayoutInflater inflater = LayoutInflater.from(activity.getContext());

        MaterialStory currentStory = images.get(position);

        final View view = inflater.inflate(R.layout.layout_story_item, collection, false);

        final ImageView mImageView = view.findViewById(R.id.mImageView);

        final boolean hasTitle = !TextUtils.isEmpty(currentStory.getTitle());
        final boolean hasDescription = !TextUtils.isEmpty(currentStory.getDescription());

        if (hasTitle) {
            TextView textView = view.findViewById(R.id.txtStoryTitle);
            textView.setVisibility(View.VISIBLE);
            textView.setText(currentStory.getTitle());
            textView.setOnClickListener(v -> storyCallbacks.onDescriptionClickListener(position));
        }

        if (hasDescription) {
            final ReadMoreTextView textView = view.findViewById(R.id.txtStoryDescription);
            textView.setVisibility(View.VISIBLE);
            textView.setText(currentStory.getDescription());
            textView.setCollapsedText(activity.getString(R.string.msv_read_more));
            textView.setExpandedText(activity.getString(R.string.msv_read_more));
            textView.setCollapsedTextColor(R.color.gray_scale);
            textView.setExpandedTextColor(R.color.gray_scale);
            textView.setOnClickListener(v -> storyCallbacks.onDescriptionClickListener(position));
        }

        view.findViewById(R.id.layoutDescription).setVisibility((hasDescription || hasTitle) ? View.VISIBLE : View.GONE);


        if (!TextUtils.isEmpty(currentStory.getActText())) {
            TextView button = view.findViewById(R.id.actTitle);
            button.setBackgroundTintList(ColorStateList.valueOf(activity.getArguments().getInt("storyButtonActionBackgroundColor",Color.GREEN)));
            button.setVisibility(View.VISIBLE);
            button.setText(currentStory.getActText());
            button.setOnClickListener(v -> storyCallbacks.onActClickListener(position));
        }

        Glide.with(activity).load(currentStory.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                storyCallbacks.nextStory();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                try {
                    if (!storiesStarted) {
                        storiesStarted = true;
                        storyCallbacks.startStories();
                    }
                } catch (Exception ignored) {

                }

                return false;
            }
        }).into(mImageView);

        collection.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);
    }
}
