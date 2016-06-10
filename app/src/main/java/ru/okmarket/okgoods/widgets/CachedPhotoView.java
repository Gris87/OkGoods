package ru.okmarket.okgoods.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import uk.co.senab.photoview.PhotoView;

public class CachedPhotoView extends CachedImageView
{
    @SuppressWarnings("unused")
    private static final String TAG = "CachedPhotoView";



    public CachedPhotoView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public CachedPhotoView(Context context)
    {
        super(context);
    }

    public CachedPhotoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected View createContentView()
    {
        PhotoView res = new PhotoView(getContext());

        return res;
    }
}
