package ru.okmarket.okgoods.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import ru.okmarket.okgoods.util.AppLog;

@SuppressWarnings("PublicConstructor")
public class CachedImageView extends FrameLayout implements View.OnTouchListener, View.OnClickListener, View.OnLongClickListener
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "CachedImageView";
    // endregion



    // region Dimensions
    private static final int     PROGRESS_VIEW_SIZE_DIP = 32;
    private static final int     ERROR_VIEW_SIZE_DIP    = 32;
    // endregion



    // region Animation
    private static final boolean USE_FADE_IN_ANIMATION  = true;
    private static final int     FADE_IN_DURATION       = 1000;
    // endregion
    // endregion



    // region Attributes
    private View                       mProgressView        = null;
    private View                       mErrorView           = null;
    private View                       mContentView         = null;
    private String                     mUrl                 = null;
    @SuppressWarnings("BooleanVariableAlwaysNegated")
    private boolean                    mCached              = false;
    private int                        mDefaultImageId      = 0;
    private int                        mErrorImageId        = 0;
    private ImageLoader                mImageLoader         = null;
    private ImageLoader.ImageContainer mImageContainer      = null;
    private OnTouchListener            mOnTouchListener     = null;
    private OnClickListener            mOnClickListener     = null;
    private OnLongClickListener        mOnLongClickListener = null;
    // endregion



    @Override
    public String toString()
    {
        return "CachedImageView{" +
                "mProgressView="          + mProgressView        +
                ", mErrorView="           + mErrorView           +
                ", mContentView="         + mContentView         +
                ", mUrl='"                + mUrl                 + '\'' +
                ", mCached="              + mCached              +
                ", mDefaultImageId="      + mDefaultImageId      +
                ", mErrorImageId="        + mErrorImageId        +
                ", mImageLoader="         + mImageLoader         +
                ", mImageContainer="      + mImageContainer      +
                ", mOnTouchListener="     + mOnTouchListener     +
                ", mOnClickListener="     + mOnClickListener     +
                ", mOnLongClickListener=" + mOnLongClickListener +
                '}';
    }

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

        //noinspection NumericCastThatLosesPrecision
        int progressViewSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PROGRESS_VIEW_SIZE_DIP, resources.getDisplayMetrics());
        //noinspection NumericCastThatLosesPrecision
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
        int width  = getWidth();
        int height = getHeight();

        boolean wrapWidth  = false;
        boolean wrapHeight = false;

        if (getLayoutParams() != null)
        {
            wrapWidth  = getLayoutParams().width  == LayoutParams.WRAP_CONTENT;
            wrapHeight = getLayoutParams().height == LayoutParams.WRAP_CONTENT;
        }

        //noinspection BooleanVariableAlwaysNegated
        boolean isFullyWrapContent = wrapWidth && wrapHeight;

        if (width == 0 && height == 0 && !isFullyWrapContent)
        {
            return;
        }

        if (TextUtils.isEmpty(mUrl))
        {
            if (mImageContainer != null)
            {
                mImageContainer.cancelRequest();
                mImageContainer = null;
            }

            setDefaultImageOrNull();
            showContentView();

            return;
        }

        if (mImageContainer != null && mImageContainer.getRequestUrl() != null)
        {
            if (mImageContainer.getRequestUrl().equals(mUrl))
            {
                return;
            }
            else
            {
                mImageContainer.cancelRequest();
            }
        }

        mCached = mImageLoader.isCached(mUrl, 0, 0);

        if (!mCached)
        {
            showProgressView();
        }

        mImageContainer = mImageLoader.get(mUrl, new ImageLoader.ImageListener()
        {
            @Override
            public void onResponse(final ImageLoader.ImageContainer response, boolean isImmediate)
            {
                if (isImmediate && isInLayoutPass)
                {
                    post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            onResponse(response, false);
                        }
                    });

                    return;
                }

                if (response.getBitmap() != null)
                {
                    setImageBitmap(response.getBitmap());

                    if (USE_FADE_IN_ANIMATION)
                    {
                        if (!mCached)
                        {
                            mContentView.setAlpha(0);
                            mContentView.animate().alpha(1).setDuration(FADE_IN_DURATION);
                        }
                    }

                    showContentView();
                }
                else
                if (mDefaultImageId != 0)
                {
                    setImageResource(mDefaultImageId);

                    showContentView();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                AppLog.w(TAG, "Failed to load image: " + mUrl);

                showErrorView();
            }
        });
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
        mOnTouchListener = listener;

        super.setOnTouchListener(this);

        mProgressView.setOnTouchListener(this);
        mErrorView.setOnTouchListener(this);
        mContentView.setOnTouchListener(this);
    }

    @Override
    public void setOnClickListener(OnClickListener listener)
    {
        mOnClickListener = listener;

        mContentView.setOnClickListener(this);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener listener)
    {
        mOnLongClickListener = listener;

        mContentView.setOnLongClickListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        return mOnTouchListener != null && mOnTouchListener.onTouch(this, event);
    }

    @Override
    public void onClick(View view)
    {
        if (mOnClickListener != null)
        {
            mOnClickListener.onClick(this);
        }
    }

    @Override
    public boolean onLongClick(View view)
    {
        return mOnLongClickListener != null && mOnLongClickListener.onLongClick(this);
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
