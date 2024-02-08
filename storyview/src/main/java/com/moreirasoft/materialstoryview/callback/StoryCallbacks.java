package com.moreirasoft.materialstoryview.callback;

public interface StoryCallbacks {

    void startStories();

    void pauseStories();

    void nextStory();

    void onDescriptionClickListener(int position);

    void onActClickListener(int position);
}
