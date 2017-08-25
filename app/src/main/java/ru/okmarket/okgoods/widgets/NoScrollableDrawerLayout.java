package ru.okmarket.okgoods.widgets;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

@SuppressWarnings("PublicConstructor")
public class NoScrollableDrawerLayout extends DrawerLayout
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "NoScrollableDrawerLayout";
    // endregion
    // endregion



    // region Attributes
    private boolean mScrollEnabled = false;
    // endregion



    @Override
    public String toString()
    {
        return "NoScrollableDrawerLayout{" +
                "mScrollEnabled=" + mScrollEnabled +
                '}';
    }

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

    public void disableScroll()
    {
        mScrollEnabled = false;
    }

    public void enableScroll()
    {
        mScrollEnabled = true;
    }
}
