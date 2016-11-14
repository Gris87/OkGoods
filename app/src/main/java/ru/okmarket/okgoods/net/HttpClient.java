package ru.okmarket.okgoods.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public final class HttpClient
{
    @SuppressWarnings("unused")
    private static final String TAG = "HttpClient";



    private static HttpClient sInstance = null;

    private RequestQueue mRequestQueue = null;
    private ImageLoader  mImageLoader  = null;



    @Override
    public String toString()
    {
        return "HttpClient{" +
                "mRequestQueue="  + mRequestQueue +
                ", mImageLoader=" + mImageLoader  +
                '}';
    }

    private HttpClient(Context context)
    {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        mImageLoader  = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache()
        {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(20);



            @Override
            public Bitmap getBitmap(String url)
            {
                return mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap)
            {
                mCache.put(url, bitmap);
            }
        });
    }

    public static HttpClient getInstance(Context context)
    {
        synchronized (HttpClient.class)
        {
            if (sInstance == null)
            {
                sInstance = new HttpClient(context);
            }

            return sInstance;
        }
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        mRequestQueue.add(req);
    }

    public void cancelAll(String tag)
    {
        mRequestQueue.cancelAll(tag);
    }

    public ImageLoader getImageLoader()
    {
        return mImageLoader;
    }
}