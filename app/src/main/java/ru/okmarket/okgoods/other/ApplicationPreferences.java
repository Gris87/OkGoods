package ru.okmarket.okgoods.other;

/**
 * Application preferences
 */
public final class ApplicationPreferences
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "ApplicationPreferences";
    // endregion



    // region File names
    public static final String MAIN_SHARED_PREFERENCES    = "Application";
    public static final String CONTEXT_SHARED_PREFERENCES = "Context";
    // endregion



    // region Preferences
    public static final String CONTEXT_LOCALE         = "LOCALE";
    public static final String CONTEXT_SELECTED_SHOP  = "SELECTED_SHOP";
    public static final String CONTEXT_LAST_DB_UPDATE = "LAST_DB_UPDATE";
    // endregion
    // endregion



    /**
     * Disabled default constructor
     */
    private ApplicationPreferences()
    {
        // Nothing
    }
}
