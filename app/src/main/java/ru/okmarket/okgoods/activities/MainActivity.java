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
import ru.okmarket.okgoods.adapters.PagerAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.dialogs.SelectCityDialog;
import ru.okmarket.okgoods.fragments.GoodsFragment;
import ru.okmarket.okgoods.fragments.ShopMapFragment;
import ru.okmarket.okgoods.other.Preferences;

public class MainActivity extends AppCompatActivity implements GoodsFragment.OnFragmentInteractionListener, ShopMapFragment.OnFragmentInteractionListener, SelectCityDialog.OnFragmentInteractionListener
{
    private static final String TAG = "MainActivity";



    private static final int SETTINGS    = 1;
    private static final int SELECT_SHOP = 2;



    private MainDatabase    mMainDatabase    = null;
    private SQLiteDatabase  mDB              = null;
    private ViewPager       mPager           = null;
    private PagerAdapter    mPagerAdapter    = null;
    private GoodsFragment   mGoodsFragment   = null;
    private ShopMapFragment mShopMapFragment = null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mMainDatabase    = null;
        mDB              = null;
        mPager           = null;
        mPagerAdapter    = null;
        mGoodsFragment   = null;
        mShopMapFragment = null;



        mPager = (ViewPager)findViewById(R.id.pager);

        if (mPager != null)
        {
            mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

            mPager.setAdapter(mPagerAdapter);
        }



        mMainDatabase = new MainDatabase(MainActivity.this);
        mDB           = mMainDatabase.getWritableDatabase();



        if (savedInstanceState == null)
        {
            verifyContextPreferences();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            if (!prefs.contains(Preferences.SETTINGS_CITY))
            {
                SelectCityDialog dialog = new SelectCityDialog();
                dialog.show(getSupportFragmentManager(), "SelectCityDialog");
            }
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

        if (modified)
        {
            editor.apply();
        }
    }

    @Override
    public void onGoodsFragmentCreated(GoodsFragment fragment)
    {
        mGoodsFragment = fragment;

        setSupportActionBar(mGoodsFragment.getToolbar());
    }

    @Override
    public void onShopMapFragmentCreated(ShopMapFragment fragment)
    {
        mShopMapFragment = fragment;
    }

    @Override
    public void onShopMapSelectShopClicked()
    {
        Intent intent = new Intent(this, SelectShopActivity.class);
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
