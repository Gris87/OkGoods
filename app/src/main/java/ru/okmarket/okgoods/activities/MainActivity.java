package ru.okmarket.okgoods.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Locale;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.SelectedGoodsAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.db.entities.SelectedGoodEntity;
import ru.okmarket.okgoods.db.entities.ShopEntity;
import ru.okmarket.okgoods.dialogs.SelectCityDialog;
import ru.okmarket.okgoods.fragments.ShopMapFragment;
import ru.okmarket.okgoods.other.Extras;
import ru.okmarket.okgoods.other.Preferences;
import ru.okmarket.okgoods.util.AnimationUtils;
import ru.okmarket.okgoods.util.AppLog;
import ru.okmarket.okgoods.widgets.DividerItemDecoration;
import ru.okmarket.okgoods.widgets.NoScrollableDrawerLayout;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, SelectCityDialog.OnFragmentInteractionListener, ShopMapFragment.OnFragmentInteractionListener, SelectedGoodsAdapter.OnItemClickListener, SelectedGoodsAdapter.OnBindViewHolderListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";



    private static final float EXPAND_ANIMATION_SPEED  = 0.5f;
    private static final int   FADE_ANIMATION_DURATION = 150;



    private static final int SETTINGS      = 1;
    private static final int SELECT_SHOP   = 2;
    private static final int HISTORY       = 3;
    private static final int GOODS_CATALOG = 4;



    private static final String SAVED_STATE_SELECTED_SHOP = "SELECTED_SHOP";
    private static final String SAVED_STATE_SELECTED_GOOD = "SELECTED_GOOD";



    private NoScrollableDrawerLayout        mDrawerLayout       = null;
    private ActionBarDrawerToggle           mDrawerToggle       = null;
    private SelectedGoodsAdapter            mAdapter            = null;
    private FrameLayout                     mShopMapView        = null;
    private ShopMapFragment                 mShopMapFragment    = null;
    private MainDatabase                    mMainDatabase       = null;
    private SQLiteDatabase                  mDB                 = null;
    private ShopEntity                      mSelectedShop       = null;
    private SelectedGoodsAdapter.ViewHolder mSelectedViewHolder = null;
    private SelectedGoodEntity              mSelectedGood       = null;



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



        setSupportActionBar(toolbar); // TODO: Check for Logo image in API < 21. Remove this logo with setDisplayShowHomeEnabled(false)
        setTitle(R.string.title_activity_main);



        mAdapter = new SelectedGoodsAdapter(this, mMainDatabase, mDB);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnBindViewHolderListener(this);

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
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            SharedPreferences contextSharedPreferences = getSharedPreferences(Preferences.CONTEXT_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = contextSharedPreferences.edit();

            if (!defaultSharedPreferences.contains(Preferences.SETTINGS_CITY))
            {
                editor.putString(Preferences.CONTEXT_LOCALE, Locale.getDefault().toString());
                editor.apply();

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
        outState.putParcelable(SAVED_STATE_SELECTED_GOOD, mSelectedGood);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        mSelectedShop = savedInstanceState.getParcelable(SAVED_STATE_SELECTED_SHOP);
        mSelectedGood = savedInstanceState.getParcelable(SAVED_STATE_SELECTED_GOOD);

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
            Intent intent = new Intent(this, GoodsCatalogActivity.class);
            startActivityForResult(intent, GOODS_CATALOG);

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
            AppLog.wtf(TAG, "Unknown view: " + String.valueOf(view));
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
        // noinspection StatementWithEmptyBody
        if (requestCode == GOODS_CATALOG)
        {
            // TODO: Implement it
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
    public void onCitySelected(String cityId)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(Preferences.SETTINGS_CITY, cityId);

        editor.apply();
    }

    @Override
    public void onShopMapSelectShopClicked()
    {
        Intent intent = new Intent(this, SelectShopActivity.class);
        intent.putExtra(Extras.SHOP, mSelectedShop);
        startActivityForResult(intent, SELECT_SHOP);
    }

    @Override
    public void onSelectedGoodClicked(SelectedGoodsAdapter.ViewHolder viewHolder, SelectedGoodEntity good)
    {
        if (good.equals(mSelectedGood))
        {
            selectSelectedGood(null, null);
        }
        else
        {
            selectSelectedGood(viewHolder, good);
        }
    }

    @Override
    public void onSelectedGoodBindViewHolder(SelectedGoodsAdapter.ViewHolder viewHolder, SelectedGoodEntity good)
    {
        if (mSelectedViewHolder == viewHolder)
        {
            mSelectedViewHolder = null;
        }

        if (good.equals(mSelectedGood))
        {
            mSelectedViewHolder = viewHolder;

            expandSelectedViewHolder(true);
        }
    }

    private void selectSelectedGood(SelectedGoodsAdapter.ViewHolder viewHolder, SelectedGoodEntity good)
    {
        if (mSelectedViewHolder != null)
        {
            collapseSelectedViewHolder();
        }

        mSelectedViewHolder = viewHolder;
        mSelectedGood       = good;

        if (mSelectedViewHolder != null)
        {
            expandSelectedViewHolder(false);
        }
    }

    private void expandSelectedViewHolder(boolean immediately)
    {
        if (immediately)
        {
            mSelectedViewHolder.mExpandedView.setVisibility(View.VISIBLE);
            mSelectedViewHolder.mCostTextView.setVisibility(View.GONE);

            mSelectedViewHolder.mExpandedView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        else
        {
            AnimationUtils.expand(mSelectedViewHolder.mExpandedView,  EXPAND_ANIMATION_SPEED);
            AnimationUtils.fadeOut(mSelectedViewHolder.mCostTextView, FADE_ANIMATION_DURATION);
        }

        mSelectedViewHolder.mGoodNameTextView.setHorizontallyScrolling(true);
        mSelectedViewHolder.mGoodNameTextView.setHorizontalFadingEdgeEnabled(true);
        mSelectedViewHolder.mGoodNameTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mSelectedViewHolder.mGoodNameTextView.setSelected(true);

        if (!TextUtils.isEmpty(mSelectedViewHolder.mCostTextView.getText()))
        {
            mSelectedViewHolder.mSecondCostTextView.setText(mSelectedViewHolder.mCostTextView.getText());
        }
        else
        {
            if (mSelectedGood.isOwn())
            {
                if (mSelectedGood.getGoodId() != MainDatabase.SPECIAL_ID_ROOT)
                {
                    mSelectedViewHolder.mSecondCostTextView.setText(R.string.own_good);
                }
                else
                {
                    mSelectedViewHolder.mSecondCostTextView.setText(R.string.own_category);
                }
            }
            else
            {
                mSelectedViewHolder.mSecondCostTextView.setText(R.string.category);
            }
        }
    }

    private void collapseSelectedViewHolder()
    {
        AnimationUtils.collapse(mSelectedViewHolder.mExpandedView, EXPAND_ANIMATION_SPEED);
        AnimationUtils.fadeIn(mSelectedViewHolder.mCostTextView,   FADE_ANIMATION_DURATION);

        mSelectedViewHolder.mGoodNameTextView.setHorizontallyScrolling(false);
        mSelectedViewHolder.mGoodNameTextView.setHorizontalFadingEdgeEnabled(false);
        mSelectedViewHolder.mGoodNameTextView.setEllipsize(TextUtils.TruncateAt.END);
        mSelectedViewHolder.mGoodNameTextView.setSelected(false);
    }
}
