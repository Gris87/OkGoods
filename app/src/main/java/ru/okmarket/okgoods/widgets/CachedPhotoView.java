package ru.okmarket.okgoods.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;

@SuppressWarnings("PublicConstructor")
public class CachedPhotoView extends CachedImageView
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "CachedPhotoView";
    // endregion
    // endregion



    public CachedPhotoView(Context context)
    {
        super(context);
    }

    public CachedPhotoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CachedPhotoView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createContentView()
    {
        return new PhotoView(getContext());
    }
}
