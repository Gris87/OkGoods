package ru.okmarket.okgoods.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.List;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.other.ApplicationPreferences;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
public class SettingsActivity extends PreferenceActivity
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "SettingsActivity";
    // endregion
    // endregion



    // region sBindPreferenceSummaryToValueListener
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    @SuppressWarnings("ConstantNamingConvention")
    private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener()
    {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value)
        {
            String stringValue = value.toString();

            if (preference instanceof ListPreference)
            {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            }
            else
            {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }

            return true;
        }
    };
    // endregion

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference)
    {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                preference.getContext().getSharedPreferences(ApplicationPreferences.MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                        .getString(preference.getKey(), ""));
    }



    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    @SuppressWarnings("deprecation")
    private void setupSimplePreferencesScreen()
    {
        if (isSimplePreferences())
        {
            // In the simplified UI, fragments are not used at all and we instead
            // use the older PreferenceActivity APIs.

            // Change preference file name
            PreferenceManager prefManager = getPreferenceManager();
            prefManager.setSharedPreferencesName(ApplicationPreferences.MAIN_SHARED_PREFERENCES);
            prefManager.setSharedPreferencesMode(MODE_PRIVATE);

            // Create empty PreferenceScreen
            setPreferenceScreen(prefManager.createPreferenceScreen(this));

            // Add 'general' preferences, and a corresponding header.
            PreferenceCategory fakeHeader = new PreferenceCategory(this);
            fakeHeader.setTitle(R.string.settings_pref_header_general);
            getPreferenceScreen().addPreference(fakeHeader);
            addPreferencesFromResource(R.xml.pref_general);

            // Add 'notifications' preferences, and a corresponding header.
            fakeHeader = new PreferenceCategory(this);
            fakeHeader.setTitle(R.string.settings_pref_header_notifications);
            getPreferenceScreen().addPreference(fakeHeader);
            addPreferencesFromResource(R.xml.pref_notification);



            MainDatabase mainDatabase = MainDatabase.newInstance(this);
            SQLiteDatabase db         = mainDatabase.getReadableDatabase();

            ListPreference cities = (ListPreference)findPreference(getString(R.string.pref_key_city));
            cities.setEntries(MainDatabase.getCities(db));
            cities.setEntryValues(MainDatabase.CITIES);

            db.close();



            bindPreferenceSummaryToValue(cities);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane()
    {
        return isXLargeTablet();
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private boolean isXLargeTablet()
    {
        return (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if the device doesn't have newer APIs like {@link PreferenceFragment},
     * or the device doesn't have an extra-large screen. In these cases, a single-pane
     * "simplified" settings UI should be shown.
     */
    private boolean isSimplePreferences()
    {
        return !isXLargeTablet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBuildHeaders(List<Header> target)
    {
        if (!isSimplePreferences())
        {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return fragmentName.equals(PreferenceFragment.class.getName())
               ||
               fragmentName.equals(GeneralPreferenceFragment.class.getName())
               ||
               fragmentName.equals(NotificationPreferenceFragment.class.getName());
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @SuppressWarnings({"PublicInnerClass", "ClassWithoutConstructor", "PublicConstructor"})
    public static class GeneralPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);



            // Change preference file name
            PreferenceManager prefManager = getPreferenceManager();
            prefManager.setSharedPreferencesName(ApplicationPreferences.MAIN_SHARED_PREFERENCES);
            prefManager.setSharedPreferencesMode(MODE_PRIVATE);

            addPreferencesFromResource(R.xml.pref_general);



            MainDatabase mainDatabase = MainDatabase.newInstance(getActivity());
            SQLiteDatabase db         = mainDatabase.getReadableDatabase();

            ListPreference cities = (ListPreference)findPreference(getString(R.string.pref_key_city));
            cities.setEntries(MainDatabase.getCities(db));
            cities.setEntryValues(MainDatabase.CITIES);

            db.close();



            bindPreferenceSummaryToValue(cities);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @SuppressWarnings({"PublicInnerClass", "ClassWithoutConstructor", "PublicConstructor"})
    public static class NotificationPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);



            // Change preference file name
            PreferenceManager prefManager = getPreferenceManager();
            prefManager.setSharedPreferencesName(ApplicationPreferences.MAIN_SHARED_PREFERENCES);
            prefManager.setSharedPreferencesMode(MODE_PRIVATE);

            addPreferencesFromResource(R.xml.pref_notification);
        }
    }
}
