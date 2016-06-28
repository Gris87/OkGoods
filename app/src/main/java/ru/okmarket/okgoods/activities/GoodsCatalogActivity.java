package ru.okmarket.okgoods.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.util.AppLog;
import ru.okmarket.okgoods.widgets.NoScrollableDrawerLayout;

public class GoodsCatalogActivity extends AppCompatActivity implements View.OnTouchListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsCatalogActivity";



    // region Attributes
    private NoScrollableDrawerLayout mDrawerLayout         = null;
    private ActionBarDrawerToggle    mDrawerToggle         = null;
    private FrameLayout              mGoodsCatalogMenuView = null;
    // endregion



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_catalog);



        // region Searching for views
        Toolbar toolbar                           = (Toolbar)                 findViewById(R.id.toolbar);
        mDrawerLayout                             = (NoScrollableDrawerLayout)findViewById(R.id.drawerLayout);
        RecyclerView goodsCatalogRecyclerView     = (RecyclerView)            findViewById(R.id.goodsCatalogRecyclerView);
        mGoodsCatalogMenuView                     = (FrameLayout)             findViewById(R.id.goodsCatalogMenuView);
        RecyclerView goodsCatalogMenuRecyclerView = (RecyclerView)            findViewById(R.id.goodsCatalogMenuRecyclerView);
        // endregion



        setSupportActionBar(toolbar);



        // region DrawerLayout initialization
        mGoodsCatalogMenuView.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels * 80 / 100;

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.show_catalog,
                R.string.hide_catalog
        );

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mGoodsCatalogMenuView.setOnTouchListener(this);
        // endregion
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
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
        if (mDrawerLayout.isDrawerOpen(mGoodsCatalogMenuView))
        {
            mDrawerLayout.closeDrawer(mGoodsCatalogMenuView);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        if (view == mGoodsCatalogMenuView)
        {
            return true;
        }
        else
        {
            AppLog.wtf(TAG, "Unknown view");
        }

        return false;
    }
}
