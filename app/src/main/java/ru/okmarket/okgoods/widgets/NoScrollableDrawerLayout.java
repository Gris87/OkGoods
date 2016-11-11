package ru.okmarket.okgoods.widgets;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollableDrawerLayout extends DrawerLayout
{
    @SuppressWarnings("unused")
    private static final String TAG = "NoScrollableDrawerLayout";



    private boolean mScrollEnabled = true;



    public NoScrollableDrawerLayout(Context context)
    {
        super(context);

        init();
    }

    public NoScrollableDrawerLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }

    public NoScrollableDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init()
    {
        mScrollEnabled = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return mScrollEnabled && super.onInterceptTouchEvent(ev);
    }

    @SuppressWarnings("unused")
    public void disableScroll()
    {
        mScrollEnabled = false;
    }

    @SuppressWarnings("unused")
    public void enableScroll()
    {
        mScrollEnabled = true;
    }
}
