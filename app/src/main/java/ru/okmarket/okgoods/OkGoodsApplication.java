package ru.okmarket.okgoods;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.Map;

import ru.okmarket.okgoods.other.ApplicationSettings;

/**
 * OkGoods application
 */
@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
public class OkGoodsApplication extends Application
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "OkGoodsApplication";
    // endregion



    // region Attributes
    private static Tracker sTracker = null;
    // endregion
    // endregion



    /** {@inheritDoc} */
    @Override
    public void onCreate()
    {
        super.onCreate();

        //noinspection AssignmentToStaticFieldFromInstanceMethod
        sTracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.tracker_config);

        ApplicationSettings.update(this);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return Google Analytics tracker
     */
    public static Tracker getDefaultTracker()
    {
        return sTracker;
    }

    public static void sendToDefaultTracker(Map<String, String> message)
    {
        if (sTracker != null)
        {
            sTracker.send(message);
        }
    }
}
