package ru.okmarket.okgoods.other;

import android.content.Context;
import android.content.SharedPreferences;

import ru.okmarket.okgoods.R;

/**
 * Application settings
 */
public final class ApplicationSettings
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "ApplicationSettings";
    // endregion
    // endregion



    // region Attributes
    private static String  sCity              = null;
    private static boolean sShowNotifications = false;
    // endregion



    /**
     * Disabled default constructor
     */
    private ApplicationSettings()
    {
        // Nothing
    }

    /**
     * Updates application settings from SharedPreferences
     * @param context    context
     */
    public static void update(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(ApplicationPreferences.MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        sCity              = prefs.getString( context.getString(R.string.pref_key_city),               context.getResources().getString( R.string.pref_default_city));
        sShowNotifications = prefs.getBoolean(context.getString(R.string.pref_key_show_notifications), context.getResources().getBoolean(R.bool.pref_default_show_notifications));
    }

    /**
     * Gets city
     * @return city
     */
    public static String getCity()
    {
        return sCity;
    }

    /**
     * Returns true if user wants to show notifications, otherwise false
     * @return true if user wants to show notifications, otherwise false
     */
    @SuppressWarnings("unused")
    public static boolean isShowNotifications()
    {
        return sShowNotifications;
    }
}
