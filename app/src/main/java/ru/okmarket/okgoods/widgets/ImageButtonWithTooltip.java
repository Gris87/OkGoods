package ru.okmarket.okgoods.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

@SuppressWarnings("PublicConstructor")
public class ImageButtonWithTooltip extends AppCompatImageButton implements View.OnLongClickListener
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "ImageButtonWithTooltip";
    // endregion
    // endregion



    // region Attributes
    private OnLongClickListener mOnLongClickListener = null;
    // endregion



    @Override
    public String toString()
    {
        return "ImageButtonWithTooltip{" +
                "mOnLongClickListener=" + mOnLongClickListener +
                '}';
    }

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

        int[] screenPos    = new int[2];
        Rect  displayFrame = new Rect();

        getLocationOnScreen(screenPos);
        getWindowVisibleDisplayFrame(displayFrame);

        int centerX = screenPos[0] + getWidth()  / 2;
        int centerY = screenPos[1] + getHeight() / 2;

        if (ViewCompat.getLayoutDirection(view) == ViewCompat.LAYOUT_DIRECTION_LTR)
        {
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            centerX = screenWidth - centerX;
        }

        Toast toast = Toast.makeText(context, getContentDescription(), Toast.LENGTH_SHORT);

        if (centerY < displayFrame.centerY())
        {
            if (centerX < displayFrame.centerX())
            {
                toast.setGravity(Gravity.BOTTOM | GravityCompat.END, centerX, displayFrame.height() - centerY);
            }
            else
            {
                toast.setGravity(Gravity.BOTTOM | GravityCompat.START, displayFrame.width() - centerX, displayFrame.height() - centerY);
            }
        }
        else
        {
            if (centerX < displayFrame.centerX())
            {
                toast.setGravity(Gravity.TOP | GravityCompat.END, centerX, centerY);
            }
            else
            {
                toast.setGravity(Gravity.TOP | GravityCompat.START, displayFrame.width() - centerX, centerY);
            }
        }

        toast.show();



        if (mOnLongClickListener != null)
        {
            mOnLongClickListener.onLongClick(view);
        }



        return true;
    }
}
