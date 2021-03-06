package ru.okmarket.okgoods.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import ru.okmarket.okgoods.other.ApplicationExtras;
import ru.okmarket.okgoods.other.ApplicationPreferences;
import ru.okmarket.okgoods.other.ApplicationSettings;
import ru.okmarket.okgoods.util.AnimationUtils;
import ru.okmarket.okgoods.util.AppLog;
import ru.okmarket.okgoods.widgets.NoScrollableDrawerLayout;

@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
public class MainActivity extends AppCompatActivity implements View.OnTouchListener, SelectCityDialog.OnFragmentInteractionListener, ShopMapFragment.OnFragmentInteractionListener, SelectedGoodsAdapter.OnItemClickListener, SelectedGoodsAdapter.OnBindViewHolderListener
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";
    // endregion



    // region Animation
    private static final float EXPAND_ANIMATION_SPEED  = 0.5f;
    private static final int   FADE_ANIMATION_DURATION = 150;
    // endregion



    // region Activities ID
    private static final int SETTINGS      = 1;
    private static final int SELECT_SHOP   = 2;
    private static final int HISTORY       = 3;
    private static final int GOODS_CATALOG = 4;
    // endregion



    // region Save state constants
    private static final String SAVED_STATE_SELECTED_SHOP = "SELECTED_SHOP";
    private static final String SAVED_STATE_SELECTED_GOOD = "SELECTED_GOOD";
    // endregion
    // endregion



    // region Attributes
    private NoScrollableDrawerLayout                    mDrawerLayout       = null;
    private ActionBarDrawerToggle                       mDrawerToggle       = null;
    private SelectedGoodsAdapter                        mAdapter            = null;
    private FrameLayout                                 mShopMapView        = null;
    private ShopMapFragment                             mShopMapFragment    = null;
    private MainDatabase                                mMainDatabase       = null;
    private SQLiteDatabase                              mDB                 = null;
    private ShopEntity                                  mSelectedShop       = null;
    private SelectedGoodsAdapter.SelectedGoodViewHolder mSelectedViewHolder = null;
    private SelectedGoodEntity                          mSelectedGood       = null;
    // endregion



    @Override
    public String toString()
    {
        return "MainActivity{" +
                "mDrawerLayout="         + mDrawerLayout       +
                ", mDrawerToggle="       + mDrawerToggle       +
                ", mAdapter="            + mAdapter            +
                ", mShopMapView="        + mShopMapView        +
                ", mShopMapFragment="    + mShopMapFragment    +
                ", mMainDatabase="       + mMainDatabase       +
                ", mDB="                 + mDB                 +
                ", mSelectedShop="       + mSelectedShop       +
                ", mSelectedViewHolder=" + mSelectedViewHolder +
                ", mSelectedGood="       + mSelectedGood       +
                '}';
    }

    @SuppressWarnings("RedundantCast")
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



        mMainDatabase = MainDatabase.newInstance(this);
        mDB           = mMainDatabase.getWritableDatabase();



        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.activity_main_title);



        mAdapter = SelectedGoodsAdapter.newInstance(this, mDB);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnBindViewHolderListener(this);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);



        mShopMapView.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels * 80 / 100;

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.main_show_map,
                R.string.main_hide_map
        );

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mShopMapView.setOnTouchListener(this);



        if (savedInstanceState == null)
        {
            SharedPreferences defaultSharedPreferences = getSharedPreferences(ApplicationPreferences.MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE);

            SharedPreferences contextSharedPreferences = getSharedPreferences(ApplicationPreferences.CONTEXT_SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = contextSharedPreferences.edit();

            if (!defaultSharedPreferences.contains(getString(R.string.pref_key_city)))
            {
                editor.putString(ApplicationPreferences.CONTEXT_LOCALE, Locale.getDefault().toString());
                editor.apply();

                SelectCityDialog dialog = SelectCityDialog.newInstance();
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
            // TODO: Implement it

            return true;
        }

        if (id == R.id.menu_start)
        {
            // TODO: Implement it

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
            // TODO: Implement it

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
            AppLog.wtf(TAG, "Unknown view: " + view);
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
            ApplicationSettings.update(this);
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
                mSelectedShop = data.getParcelableExtra(ApplicationExtras.SHOP);



                SharedPreferences prefs = getSharedPreferences(ApplicationPreferences.CONTEXT_SHARED_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putInt(ApplicationPreferences.CONTEXT_SELECTED_SHOP, mSelectedShop.getId());

                editor.apply();



                updateSelectedShop();
            }
            else
            {
                AppLog.wtf(TAG, "Unknown result code: " + resultCode);
            }
        }
        else
        if (requestCode == HISTORY)
        {
            mAdapter.updateFromDatabase();
        }
        else
        if (requestCode == GOODS_CATALOG)
        {
            mAdapter.updateFromDatabase();
        }
        else
        {
            AppLog.wtf(TAG, "Unknown request code: " + requestCode);
        }
    }

    private void verifyContextPreferences()
    {
        boolean modified = false;

        SharedPreferences prefs = getSharedPreferences(ApplicationPreferences.CONTEXT_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (!prefs.getString(ApplicationPreferences.CONTEXT_LOCALE, "").equals(Locale.getDefault().toString()))
        {
            mMainDatabase.recreateStaticTables(mDB);

            editor.putString(ApplicationPreferences.CONTEXT_LOCALE, Locale.getDefault().toString());
            modified = true;
        }

        int selectedShopId = prefs.getInt(ApplicationPreferences.CONTEXT_SELECTED_SHOP, 0);

        if (selectedShopId != 0)
        {
            mSelectedShop = MainDatabase.getShop(mDB, selectedShopId);

            //noinspection VariableNotUsedInsideIf
            if (mSelectedShop != null)
            {
                updateSelectedShop();
            }
            else
            {
                editor.putInt(ApplicationPreferences.CONTEXT_SELECTED_SHOP, 0);
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
        SharedPreferences prefs = getSharedPreferences(ApplicationPreferences.MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(getString(R.string.pref_key_city), cityId);

        editor.apply();
    }

    @Override
    public void onShopMapSelectShopClicked()
    {
        Intent intent = new Intent(this, SelectShopActivity.class);
        intent.putExtra(ApplicationExtras.SHOP, mSelectedShop);
        startActivityForResult(intent, SELECT_SHOP);
    }

    @Override
    public void onSelectedGoodClicked(SelectedGoodsAdapter.SelectedGoodViewHolder holder, SelectedGoodEntity good)
    {
        if (good.equals(mSelectedGood))
        {
            selectSelectedGood(null, null);
        }
        else
        {
            selectSelectedGood(holder, good);
        }
    }

    @Override
    public void onSelectedGoodBindViewHolder(SelectedGoodsAdapter.SelectedGoodViewHolder holder, SelectedGoodEntity good)
    {
        if (mSelectedViewHolder == holder)
        {
            mSelectedViewHolder = null;
        }

        if (good.equals(mSelectedGood))
        {
            mSelectedViewHolder = holder;

            expandSelectedViewHolder(true);
        }
    }

    private void selectSelectedGood(SelectedGoodsAdapter.SelectedGoodViewHolder holder, SelectedGoodEntity good)
    {
        //noinspection VariableNotUsedInsideIf
        if (mSelectedViewHolder != null)
        {
            collapseSelectedViewHolder();
        }

        mSelectedViewHolder = holder;
        mSelectedGood       = good;

        //noinspection VariableNotUsedInsideIf
        if (mSelectedViewHolder != null)
        {
            expandSelectedViewHolder(false);
        }
    }

    private void expandSelectedViewHolder(boolean immediately)
    {
        if (immediately)
        {
            mSelectedViewHolder.getExpandedView().setVisibility(View.VISIBLE);
            mSelectedViewHolder.getCostTextView().setVisibility(View.GONE);

            mSelectedViewHolder.getExpandedView().getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        else
        {
            AnimationUtils.expand(mSelectedViewHolder.getExpandedView(),  EXPAND_ANIMATION_SPEED);
            AnimationUtils.fadeOut(mSelectedViewHolder.getCostTextView(), FADE_ANIMATION_DURATION);
        }



        mSelectedViewHolder.getGoodNameTextView().setHorizontallyScrolling(true);
        mSelectedViewHolder.getGoodNameTextView().setHorizontalFadingEdgeEnabled(true);
        mSelectedViewHolder.getGoodNameTextView().setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mSelectedViewHolder.getGoodNameTextView().setSelected(true);



        if (!TextUtils.isEmpty(mSelectedViewHolder.getCostTextView().getText()))
        {
            mSelectedViewHolder.getSecondCostTextView().setText(mSelectedViewHolder.getCostTextView().getText());
        }
        else
        {
            if (mSelectedGood.isOwn())
            {
                if (mSelectedGood.getGoodId() != MainDatabase.SPECIAL_ID_ROOT)
                {
                    mSelectedViewHolder.getSecondCostTextView().setText(R.string.main_own_good);
                }
                else
                {
                    mSelectedViewHolder.getSecondCostTextView().setText(R.string.main_own_category);
                }
            }
            else
            {
                mSelectedViewHolder.getSecondCostTextView().setText(R.string.main_category);
            }
        }
    }

    private void collapseSelectedViewHolder()
    {
        AnimationUtils.collapse(mSelectedViewHolder.getExpandedView(), EXPAND_ANIMATION_SPEED);
        AnimationUtils.fadeIn(mSelectedViewHolder.getCostTextView(),   FADE_ANIMATION_DURATION);



        mSelectedViewHolder.getGoodNameTextView().setHorizontallyScrolling(false);
        mSelectedViewHolder.getGoodNameTextView().setHorizontalFadingEdgeEnabled(false);
        mSelectedViewHolder.getGoodNameTextView().setEllipsize(TextUtils.TruncateAt.END);
        mSelectedViewHolder.getGoodNameTextView().setSelected(false);
    }
}
