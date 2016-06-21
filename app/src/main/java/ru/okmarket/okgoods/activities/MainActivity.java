package ru.okmarket.okgoods.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Locale;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.SelectedGoodAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.dialogs.SelectCityDialog;
import ru.okmarket.okgoods.fragments.ShopMapFragment;
import ru.okmarket.okgoods.other.Extras;
import ru.okmarket.okgoods.other.Preferences;
import ru.okmarket.okgoods.other.ShopInfo;
import ru.okmarket.okgoods.util.AppLog;
import ru.okmarket.okgoods.widgets.DividerItemDecoration;
import ru.okmarket.okgoods.widgets.NoScrollableDrawerLayout;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, ShopMapFragment.OnFragmentInteractionListener, SelectCityDialog.OnFragmentInteractionListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";



    private static final int SETTINGS    = 1;
    private static final int SELECT_SHOP = 2;
    private static final int HISTORY     = 3;



    private static final String SAVED_STATE_SELECTED_SHOP = "SELECTED_SHOP";



    private NoScrollableDrawerLayout mDrawerLayout    = null;
    private ActionBarDrawerToggle    mDrawerToggle    = null;
    private SelectedGoodAdapter      mAdapter         = null;
    private FrameLayout              mShopMapView     = null;
    private ShopMapFragment          mShopMapFragment = null;
    private MainDatabase             mMainDatabase    = null;
    private SQLiteDatabase           mDB              = null;
    private ShopInfo                 mSelectedShop    = null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar           = (Toolbar)                 findViewById(R.id.toolbar);
        mDrawerLayout             = (NoScrollableDrawerLayout)findViewById(R.id.drawerLayout);
        RecyclerView recyclerView = (RecyclerView)            findViewById(R.id.selectedGoodsRecyclerView);
        mShopMapView              = (FrameLayout)             findViewById(R.id.shopMapView);
        mShopMapFragment          = (ShopMapFragment)         getSupportFragmentManager().findFragmentById(R.id.shopMapFragment);

        assert recyclerView != null;



        mMainDatabase = new MainDatabase(MainActivity.this);
        mDB           = mMainDatabase.getWritableDatabase();



        setSupportActionBar(toolbar);
        setTitle(R.string.title_activity_main);



        mAdapter = new SelectedGoodAdapter(this, mMainDatabase, mDB);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(mAdapter);



        mShopMapView.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels * 80 / 100;

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.show_map,
                R.string.hide_map
        );

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mShopMapView.setOnTouchListener(this);



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
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
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
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(mShopMapView))
        {
            mDrawerLayout.closeDrawer(mShopMapView);
        }
        else
        {
            super.onBackPressed();
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
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.menu_add_good)
        {
            return true;
        }

        if (id == R.id.menu_delete_goods)
        {
            return true;
        }

        if (id == R.id.menu_start)
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
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivityForResult(intent, HISTORY);

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
    public boolean onTouch(View view, MotionEvent event)
    {
        if (view == mShopMapView)
        {
            return true;
        }
        else
        {
            AppLog.wtf(TAG, "Unknown view");
        }

        return false;
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
        if (requestCode == HISTORY)
        {
            mAdapter.updateFromDatabase();
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
        if (mSelectedShop != null)
        {
            mShopMapFragment.setSelectedShopText(mSelectedShop.getName());
        }
        else
        {
            mShopMapFragment.resetSelectedShop();
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
