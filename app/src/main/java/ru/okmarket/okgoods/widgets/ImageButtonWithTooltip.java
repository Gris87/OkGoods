package ru.okmarket.okgoods.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class ImageButtonWithTooltip extends ImageButton implements View.OnLongClickListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "ImageButtonWithTooltip";



    private OnLongClickListener mOnLongClickListener = null;



    public ImageButtonWithTooltip(Context context)
    {
        super(context);

        init();
    }

    public ImageButtonWithTooltip(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }

    public ImageButtonWithTooltip(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init()
    {
        super.setOnLongClickListener(this);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener listener)
    {
        mOnLongClickListener = listener;
    }

    @Override
    public boolean onLongClick(View view)
    {
        Context context = getContext();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        int[] screenPos = new int[2];

        getLocationOnScreen(screenPos);

        int centerX =                               screenPos[0] + getWidth()  / 2;
        int centerY = displayMetrics.heightPixels - screenPos[1] + getHeight() / 2;

        if (ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_LTR)
        {
            final int screenWidth = displayMetrics.widthPixels;
            centerX = screenWidth - centerX;
        }

        Toast toast = Toast.makeText(context, getContentDescription(), Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.BOTTOM | Gravity.START, centerX, centerY);
        toast.show();



        if (mOnLongClickListener != null)
        {
            mOnLongClickListener.onLongClick(view);
        }

        return true;
    }
}
