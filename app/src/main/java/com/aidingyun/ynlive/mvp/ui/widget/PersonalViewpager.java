package com.aidingyun.ynlive.mvp.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PersonalViewpager extends ViewPager {
    //是否可以进行滑动
    private boolean canScroll = true;//默认可以滑动
     public PersonalViewpager(Context context) { super(context); }
     public PersonalViewpager(Context context, AttributeSet attrs) {
         super(context, attrs); }
         @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         int height = 0; for (int i = 0; i < getChildCount(); i++) {
             View child = getChildAt(i);
             child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
             int h = child.getMeasuredHeight();
             if (h > height) height = h; }
             heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
         super.onMeasure(widthMeasureSpec, heightMeasureSpec); }
         public void setCanScroll(boolean canScroll) {
         this.canScroll = canScroll;
     }
         @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
         return canScroll;
     }
}
