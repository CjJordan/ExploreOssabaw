package org.ossabawisland.exploreossabaw;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * A custom view pager that disables horizontal panning between tabs
 * in order to preserve map panning functionality
 * @author Matt Antonelli
 */
public class OssabawViewPager extends ViewPager {

	private boolean isPagingEnabled;

    public OssabawViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isPagingEnabled = false; // Disable page sliding to preserve map scrolling
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.isPagingEnabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.isPagingEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

}
