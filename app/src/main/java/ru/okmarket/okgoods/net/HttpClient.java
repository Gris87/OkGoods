package ru.okmarket.okgoods.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class HttpClient
{
    @SuppressWarnings("unused")
    private static final String TAG = "HttpClient";



    private static HttpClient sInstance = null;



    private Context      mContext      = null;
    private RequestQueue mRequestQueue = null;
    private ImageLoader  mImageLoader  = null;



    private HttpClient(Context context)
    {
        mContext      = context;
        mRequestQueue = getRequestQueue();
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

    public static synchronized HttpClient getInstance(Context context)
    {
        if (sInstance == null)
        {
            sInstance = new HttpClient(context);
        }

        return sInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader()
    {
        return mImageLoader;
    }
}