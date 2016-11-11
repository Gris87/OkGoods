package ru.okmarket.okgoods.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PhotoViewPager extends ViewPager
{
    @SuppressWarnings("unused")
    private static final String TAG = "PhotoViewPager";



    public PhotoViewPager(Context context)
    {
        super(context);

        init();
    }

    public PhotoViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }

    private void init()
    {
        // Nothing
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        try
        {
            return super.onInterceptTouchEvent(event);
        }
        catch (Exception e)
        {
            // Nothing
        }

        return false;
    }
}
