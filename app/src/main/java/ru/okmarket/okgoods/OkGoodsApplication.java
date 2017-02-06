package ru.okmarket.okgoods;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import ru.okmarket.okgoods.other.ApplicationSettings;

/**
 * OkGoods application
 */
@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
public class OkGoodsApplication extends Application
{
    @SuppressWarnings("unused")
    private static final String TAG = "OkGoodsApplication";



    private Tracker mTracker = null;



    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "OkGoodsApplication{" +
                "mTracker=" + mTracker +
                '}';
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate()
    {
        super.onCreate();

        mTracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.tracker_config);

        ApplicationSettings.update(this);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return Google Analytics tracker
     */
    public Tracker getDefaultTracker()
    {
        return mTracker;
    }
}
