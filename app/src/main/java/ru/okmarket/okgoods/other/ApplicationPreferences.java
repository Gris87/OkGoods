package ru.okmarket.okgoods.other;

/**
 * Application preferences
 */
public final class ApplicationPreferences
{
    @SuppressWarnings("unused")
    private static final String TAG = "ApplicationPreferences";



    public static final String MAIN_SHARED_PREFERENCES    = "Application";
    public static final String CONTEXT_SHARED_PREFERENCES = "Context";



    public static final String CONTEXT_LOCALE         = "LOCALE";
    public static final String CONTEXT_SELECTED_SHOP  = "SELECTED_SHOP";
    public static final String CONTEXT_LAST_DB_UPDATE = "LAST_DB_UPDATE";



    /**
     * Disabled default constructor
     */
    private ApplicationPreferences()
    {
        // Nothing
    }
}
