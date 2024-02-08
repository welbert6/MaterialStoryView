package com.moreirasoft.materialstoryview.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.moreirasoft.materialstoryview.R;
import com.moreirasoft.materialstoryview.callback.StoryCallbacks;
import com.moreirasoft.materialstoryview.model.MaterialStory;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private final ArrayList<MaterialStory> images;
    private final Context context;
    private final StoryCallbacks storyCallbacks;
    private boolean storiesStarted = false;

    public ViewPagerAdapter(ArrayList<MaterialStory> images, Context context, StoryCallbacks storyCallbacks) {
        this.images = images;
        this.context = context;
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

        LayoutInflater inflater = LayoutInflater.from(context);

        MaterialStory currentStory = images.get(position);

        final View view = inflater.inflate(R.layout.layout_story_item, collection, false);

        final ImageView mImageView = view.findViewById(R.id.mImageView);

        if (!TextUtils.isEmpty(currentStory.getTitle())) {
            TextView textView = view.findViewById(R.id.txtStoryTitle);
            textView.setVisibility(View.VISIBLE);
            textView.setText(currentStory.getTitle());
            textView.setOnClickListener(v -> storyCallbacks.onDescriptionClickListener(position));
        }

        if (!TextUtils.isEmpty(currentStory.getDescription())) {
            TextView textView = view.findViewById(R.id.txtStoryDescription);
            textView.setVisibility(View.VISIBLE);
            textView.setText(currentStory.getDescription());
            textView.setOnClickListener(v -> storyCallbacks.onDescriptionClickListener(position));
        }


        if (!TextUtils.isEmpty(currentStory.getActText())) {
            TextView button = view.findViewById(R.id.actTitle);
            button.setVisibility(View.VISIBLE);
            button.setText(currentStory.getActText());
            button.setOnClickListener(v-> storyCallbacks.onActClickListener(position));
        }

        Glide.with(context)
                .load(currentStory.getImageUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        storyCallbacks.nextStory();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource != null) {
                            PaletteExtraction pe = new PaletteExtraction(view.findViewById(R.id.relativeLayout),
                                    ((BitmapDrawable) resource).getBitmap());
                            pe.execute();
                        }
                        if (!storiesStarted) {
                            storiesStarted = true;
                            storyCallbacks.startStories();
                        }
                        return false;
                    }
                })
                .into(mImageView);

        collection.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);
    }
}
