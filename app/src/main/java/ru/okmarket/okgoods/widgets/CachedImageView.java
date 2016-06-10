package ru.okmarket.okgoods.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;

public class CachedImageView extends FrameLayout
{
    @SuppressWarnings("unused")
    private static final String TAG = "CachedImageView";



    private static final int PROGRESS_VIEW_SIZE_DIP = 32;
    private static final int ERROR_VIEW_SIZE_DIP    = 32;



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
        Resources resources = getContext().getResources();

        int progressViewSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PROGRESS_VIEW_SIZE_DIP, resources.getDisplayMetrics());
        int errorViewSize    = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ERROR_VIEW_SIZE_DIP,    resources.getDisplayMetrics());



        mProgressView = createProgressView();
        mErrorView    = createErrorView();
        mContentView  = createContentView();

        addView(mProgressView, new LayoutParams(progressViewSize,          progressViewSize,          Gravity.CENTER));
        addView(mErrorView,    new LayoutParams(errorViewSize,             errorViewSize,             Gravity.CENTER));
        addView(mContentView,  new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER));



        hideAll();
    }

    protected View createProgressView()
    {
        return new ProgressBar(getContext());
    }

    protected View createErrorView()
    {
        return new ImageView(getContext());
    }

    protected View createContentView()
    {
        return new ImageView(getContext());
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
        showProgressView();
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
    public void setOnTouchListener(OnTouchListener listener)
    {
        super.setOnTouchListener(listener);

        mProgressView.setOnTouchListener(listener);
        mErrorView.setOnTouchListener(listener);
        mContentView.setOnTouchListener(listener);
    }

    @Override
    public void setOnClickListener(OnClickListener listener)
    {
        mContentView.setOnClickListener(listener);
    }

    private void hideAll()
    {
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
    }

    private void showProgressView()
    {
        mProgressView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
    }

    private void showErrorView()
    {
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
    }

    private void showContentView()
    {
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
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
