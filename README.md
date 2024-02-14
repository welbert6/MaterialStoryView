# StoryView

## Screenshots
![Screenshots](images/screenshots_7.png)
![Screenshots](images/screenshots.jpg)

## Introduction

MaterialStoryView is an Android library for implementing a story carousel, similar to those found on social networks like Instagram and Facebook. This library makes it easy to add a story viewing component to your Android application, allowing for quick and simple integration. 

✅ Saves story as seen in SharedPreferences: Ensures users don’t repeatedly see the same story, enhancing user experience.

✅ Action button on story: Allows for direct interactions, such as visiting a link or performing a specific action, right from the story.

✅ Navigation between stories by tapping on the right and left side of the screen: Makes navigating through stories easy, letting users advance or go back with a simple tap.

✅ Supports RTL and LTR: Accommodates users of languages that are read from right to left (RTL) and left to right (LTR), ensuring a global and inclusive user experience.

✅ Supports stories with and without titles: Offers flexibility to display stories both with titles for context and without titles for a cleaner design.

✅ 100% Customization: Allows full customization of the story appearance, including duration, indicator color, image radius size, text color, etc., to perfectly match your application’s visual identity.


## Setup

#### 1. Add the gradle dependency

Add JitPack repository to your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency:
```
dependencies {
	       	        implementation 'com.github.welbert6:MaterialStoryView:1.0.0'
	}
```
#### 2. Usage 

Add CarouselStoryView to your XML layout:
```xml 
<com.moreirasoft.materialstoryview.presentation.customviews.CarouselStoryView
    android:id="@+id/carouselStoryView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:layout_margin="10dp"
    app:miniStoryTextColor="#000"
    app:miniStoryImageRadius="36dp"
    app:miniStorySpaceBetweenImageAndIndicator="2dp"
    app:miniStoryItemIndicatorWidth="3dp"
    app:storyDuration="6000" />

```
#### Initialize and configure CarouselStoryView in your Activity:


```java
val carrosselStoryView: CarouselStoryView = findViewById(R.id.carouselStoryView)

//Ensure to call init With Activity to init lib
carrosselStoryView.initWithActivity(this)

// You can addStory or addStories with you have a list of stories 
carrosselStoryView.addStory(getCopasaStories())
carrosselStoryView.addStory(getCemigStories())
carrosselStoryView.addStory(getCminStories())
carrosselStoryView.addStories(getModelStorys())
````

#### Customization


MaterialStoryView offers various customization options through XML attributes, allowing for modifications of:

    Text size and color
    Colors of visited and pending story indicators
    Story image radius
    Story item indicator width
    Story duration

For more details on customization options, refer to the available options in the declare-styleable XML attributes section.

<declare-styleable name="MaterialCarouselStoryView">
        <attr name="miniStoryTextSize" format="dimension"/> 
        <attr name="miniStoryTextColor" format="color"/>
        <attr name="miniStoryTextFont" format="reference"/>
        <attr name="miniStoryVisitedIndicatorColor" format="color"/>
        <attr name="miniStoryPendingIndicatorColor" format="color"/>
        <attr name="miniStoryImageRadius" format="dimension"/>
        <attr name="miniStorySpaceBetweenImageAndIndicator" format="dimension"/>
        <attr name="miniStoryItemIndicatorWidth" format="dimension"/>
        <attr name="storyProgressBarPrimaryColor" format="color"/>
        <attr name="storyProgressBarSecondaryColor" format="color"/>
        <attr name="storyProgressBarHeight" format="dimension"/>
        <attr name="storyGapBetweenProgressBar" format="dimension"/>
        <attr name="storySingleStoryDisplayTime" format="integer"/>
        <attr name="storyDuration" format="integer"/>
    </declare-styleable>

   - miniStoryTextSize (Mini Story Text Size): Sets the text size of mini story captions, allowing customization of text scale for better readability.
   - miniStoryTextColor (Mini Story Text Color): Specifies the text color for mini story captions, offering the ability to match the text color with the app theme.
   - miniStoryTextFont (Mini Story Text Font): Allows setting the text font for mini stories through a reference, enabling typography customization to align with the visual identity.
   - miniStoryVisitedIndicatorColor (Mini Story Visited Indicator Color): Defines the indicator color for stories that have been viewed by the user, helping distinguish between new and reviewed content.
   - miniStoryPendingIndicatorColor (Mini Story Pending Indicator Color): Specifies the indicator color for stories that have not yet been viewed, facilitating the identification of new content.
   - miniStoryImageRadius (Mini Story Image Radius): Determines the radius of images in mini stories, allowing the corners of images to be rounded for a softer look.
   - miniStorySpaceBetweenImageAndIndicator (Space Between Mini Story Image and Indicator): Sets the space

 
 ## Credit 

 [shts/StoriesProgressView](https://github.com/OMARIHAMZA/StoryView): This Library was used to display the progress of the stories


## Developed By
#### Welbert Moreira
* [LinkedIn](https://www.linkedin.com/in/welbertim/)

Feel free to contribute improvements, bug fixes, or new features. Your contribution is welcome!


## License
```
MIT License

Copyright (c) 2024 Welbert Moreira 
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
