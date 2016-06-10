package ru.okmarket.okgoods.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;

public class CachedImageView extends FrameLayout
{
    @SuppressWarnings("unused")
    private static final String TAG = "CachedImageView";



    private View                       mProgressView   = null;
    private View                       mErrorView      = null;
    private View                       mContentView    = null;
    private String                     mUrl            = null;
    private int                        mDefaultImageId = 0;
    private int                        mErrorImageId   = 0;
    private ImageLoader                mImageLoader    = null;
    private ImageLoader.ImageContainer mImageContainer = null;



    public CachedImageView(Context context)
    {
        super(context);

        init();
    }

    public CachedImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }

    public CachedImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init()
    {
        mProgressView = createProgressView();
        mErrorView    = createErrorView();
        mContentView  = createContentView();

        addView(mProgressView);
        addView(mErrorView);
        addView(mContentView);
    }

    protected View createProgressView()
    {
        ProgressBar res = new ProgressBar(getContext());

        return res;
    }

    protected View createErrorView()
    {
        ImageView res = new ImageView(getContext());

        return res;
    }

    protected View createContentView()
    {
        ImageView res = new ImageView(getContext());

        return res;
    }

    public void setImageUrl(String url, ImageLoader imageLoader)
    {
        mUrl         = url;
        mImageLoader = imageLoader;

        loadImageIfNecessary(false);
    }

    protected void setImageBitmap(Bitmap bitmap)
    {
        ((ImageView)mContentView).setImageBitmap(bitmap);
    }

    protected void setImageResource(@DrawableRes int resId)
    {
        ((ImageView)mContentView).setImageResource(resId);
    }

    private void loadImageIfNecessary(final boolean isInLayoutPass)
    {

    }

    private void setDefaultImageOrNull()
    {
        if (mDefaultImageId != 0)
        {
            setImageResource(mDefaultImageId);
        }
        else
        {
            setImageBitmap(null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        loadImageIfNecessary(true);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        if (mImageContainer != null)
        {
            mImageContainer.cancelRequest();
            mImageContainer = null;

            setImageBitmap(null);
        }

        super.onDetachedFromWindow();
    }

    @Override
    protected void drawableStateChanged()
    {
        super.drawableStateChanged();

        invalidate();
    }

    @Override
    public void setOnClickListener(OnClickListener listener)
    {
        mContentView.setOnClickListener(listener);
    }

    @SuppressWarnings("unused")
    public View getProgressView()
    {
        return mProgressView;
    }

    @SuppressWarnings("unused")
    public View getErrorView()
    {
        return mErrorView;
    }

    @SuppressWarnings("unused")
    public View getContentView()
    {
        return mContentView;
    }

    @SuppressWarnings("unused")
    public void setDefaultImageResId(int defaultImageId)
    {
        mDefaultImageId = defaultImageId;
    }

    @SuppressWarnings("unused")
    public int getDefaultImageResId()
    {
        return mDefaultImageId;
    }

    @SuppressWarnings("unused")
    public void setErrorImageResId(int errorImageId)
    {
        mErrorImageId = errorImageId;

        ((ImageView)mErrorView).setImageResource(mErrorImageId);
    }

    @SuppressWarnings("unused")
    public int getErrorImageResId()
    {
        return mErrorImageId;
    }
}
