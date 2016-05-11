package ru.okmarket.okgoods.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class ImageButtonWithTooltip extends ImageButton implements View.OnLongClickListener
{
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
    public boolean onLongClick(View v)
    {
        int[] screenPos    = new int[2];
        Rect  displayFrame = new Rect();

        getLocationOnScreen(screenPos);
        getWindowVisibleDisplayFrame(displayFrame);

        Context context = getContext();

        int width   = getWidth();
        int height  = getHeight();
        int centerY = screenPos[1] + height / 2;
        int centerX = screenPos[0] + width  / 2;

        if (ViewCompat.getLayoutDirection(v) == ViewCompat.LAYOUT_DIRECTION_LTR)
        {
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            centerX = screenWidth - centerX;
        }

        Toast toast = Toast.makeText(context, getContentDescription(), Toast.LENGTH_SHORT);

        if (centerY < displayFrame.height())
        {
            toast.setGravity(Gravity.TOP | GravityCompat.END, centerX, height);
        }
        else
        {
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
        }

        toast.show();



        if (mOnLongClickListener != null)
        {
            mOnLongClickListener.onLongClick(v);
        }

        return true;
    }
}
