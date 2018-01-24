//The app MoodTracker use this class for the vertical swipe

package com.arthur.MoodTracker.VerticalView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class VerticalViewPager extends ViewPager {
    public VerticalViewPager(Context context) {
        super(context);
        init();
    }
    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    //Transform the ViewPager into a VerticalViewPager useful for the swipe
    private void init() {
        setPageTransformer(true, new VerticalPageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer{

        //Stop the horizontal slide
        @Override
        public void transformPage(View view, float position) {
            view.setTranslationX(view.getWidth()* -position);
        //Active the vertical slide
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
        }
    }

    //Change the X axis by the Y axis to enable a vertical scroll
    private MotionEvent swapXY(MotionEvent ev){
        float width = getWidth();
        float height = getHeight();

        float newX = ev.getY();
        float newY = ev.getX();

        ev.setLocation(newX,newY);

        return ev;
    }
    // Detect when the user touch the screen
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev);
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapXY(ev));
    }
 }
