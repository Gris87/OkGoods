package ru.okmarket.okgoods.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.MainPagerAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.dialogs.SelectCityDialog;
import ru.okmarket.okgoods.fragments.GoodsFragment;
import ru.okmarket.okgoods.fragments.ShopMapFragment;
import ru.okmarket.okgoods.other.Extras;
import ru.okmarket.okgoods.other.Preferences;
import ru.okmarket.okgoods.other.ShopInfo;
import ru.okmarket.okgoods.util.AppLog;

public class MainActivity extends AppCompatActivity implements GoodsFragment.OnFragmentInteractionListener, ShopMapFragment.OnFragmentInteractionListener, SelectCityDialog.OnFragmentInteractionListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";



    private static final int SETTINGS    = 1;
    private static final int SELECT_SHOP = 2;



    private static final String SAVED_STATE_SELECTED_SHOP = "SELECTED_SHOP";



    private MainDatabase     mMainDatabase    = null;
    private SQLiteDatabase   mDB              = null;
    private ViewPager        mPager           = null;
    private MainPagerAdapter mPagerAdapter    = null;
    private GoodsFragment    mGoodsFragment   = null;
    private ShopMapFragment  mShopMapFragment = null;
    private ShopInfo         mSelectedShop    = null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mMainDatabase = new MainDatabase(MainActivity.this);
        mDB           = mMainDatabase.getWritableDatabase();



        setContentView(R.layout.activity_main);



        mPager = (ViewPager)findViewById(R.id.pager);

        if (mPager != null)
        {
            mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());

            mPager.setAdapter(mPagerAdapter);
        }



        if (savedInstanceState == null)
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            if (!prefs.contains(Preferences.SETTINGS_CITY))
            {
                SelectCityDialog dialog = new SelectCityDialog();
                dialog.show(getSupportFragmentManager(), "SelectCityDialog");
            }

            verifyContextPreferences();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mDB != null)
        {
            mDB.close();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVED_STATE_SELECTED_SHOP, mSelectedShop);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        mSelectedShop = savedInstanceState.getParcelable(SAVED_STATE_SELECTED_SHOP);

        updateSelectedShop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.menu_add_good)
        {
            return true;
        }

        if (id == R.id.menu_delete_goods)
        {
            return true;
        }

        if (id == R.id.menu_select_shop)
        {
            onShopMapSelectShopClicked();

            return true;
        }

        if (id == R.id.menu_history)
        {
            return true;
        }

        if (id == R.id.menu_update_db)
        {
            return true;
        }

        if (id == R.id.menu_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, SETTINGS);

            return true;
        }

        if (id == R.id.menu_exit)
        {
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // noinspection StatementWithEmptyBody
        if (requestCode == SETTINGS)
        {
            // Nothing
        }
        else
        if (requestCode == SELECT_SHOP)
        {
            // noinspection StatementWithEmptyBody
            if (resultCode == SelectShopActivity.NOTHING)
            {
                // Nothing
            }
            else
            if (resultCode == SelectShopActivity.SHOP_SELECTED)
            {
                mSelectedShop = data.getParcelableExtra(Extras.SHOP);



                SharedPreferences prefs = getSharedPreferences(Preferences.CONTEXT_FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putInt(Preferences.CONTEXT_SELECTED_SHOP, mSelectedShop.getId());

                editor.apply();



                updateSelectedShop();
            }
            else
            {
                AppLog.wtf(TAG, "Unknown result code: " + String.valueOf(resultCode));
            }
        }
        else
        {
            AppLog.wtf(TAG, "Unknown request code: " + String.valueOf(requestCode));
        }
    }

    private void verifyContextPreferences()
    {
        boolean modified = false;

        SharedPreferences prefs = getSharedPreferences(Preferences.CONTEXT_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (!prefs.getString(Preferences.CONTEXT_LOCALE, "").equals(Locale.getDefault().toString()))
        {
            mMainDatabase.recreateStaticTables(mDB);

            editor.putString(Preferences.CONTEXT_LOCALE, Locale.getDefault().toString());
            modified = true;
        }

        int selectedShopId = prefs.getInt(Preferences.CONTEXT_SELECTED_SHOP, 0);

        if (selectedShopId != 0)
        {
            mSelectedShop = mMainDatabase.getShop(mDB, selectedShopId);

            if (mSelectedShop != null)
            {
                updateSelectedShop();
            }
            else
            {
                editor.putInt(Preferences.CONTEXT_SELECTED_SHOP, 0);
                modified = true;
            }
        }

        if (modified)
        {
            editor.apply();
        }
    }

    private void updateSelectedShop()
    {
        if (mShopMapFragment != null)
        {
            if (mSelectedShop != null)
            {
                mShopMapFragment.setSelectedShopText(mSelectedShop.getName());
            }
            else
            {
                mShopMapFragment.resetSelectedShop();
            }
        }
    }

    @Override
    public void onGoodsFragmentCreated(GoodsFragment fragment)
    {
        if (mGoodsFragment == null)
        {
            mGoodsFragment = fragment;

            setSupportActionBar(mGoodsFragment.getToolbar());
        }
    }

    @Override
    public void onShopMapFragmentCreated(ShopMapFragment fragment)
    {
        if (mShopMapFragment == null)
        {
            mShopMapFragment = fragment;

            updateSelectedShop();
        }
    }

    @Override
    public void onShopMapSelectShopClicked()
    {
        Intent intent = new Intent(this, SelectShopActivity.class);
        intent.putExtra(Extras.SHOP, mSelectedShop);
        startActivityForResult(intent, SELECT_SHOP);
    }

    @Override
    public void onCitySelected(String cityId)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(Preferences.SETTINGS_CITY, cityId);

        editor.apply();
    }
}
