package ru.okmarket.okgoods.activities;

import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.GoodsAdapter;
import ru.okmarket.okgoods.adapters.GoodsCategoriesAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.util.AppLog;
import ru.okmarket.okgoods.widgets.DividerItemDecoration;
import ru.okmarket.okgoods.widgets.NoScrollableDrawerLayout;

public class GoodsCatalogActivity extends AppCompatActivity implements View.OnTouchListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsCatalogActivity";



    // region Attributes
    private NoScrollableDrawerLayout mDrawerLayout           = null;
    private ActionBarDrawerToggle    mDrawerToggle           = null;
    private FrameLayout              mGoodsCategoriesView    = null;
    private GoodsCategoriesAdapter   mGoodsCategoriesAdapter = null;
    private GoodsAdapter             mGoodsAdapter           = null;
    private MainDatabase             mMainDatabase           = null;
    private SQLiteDatabase           mDB                     = null;
    // endregion



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_catalog);



        // region Searching for views
        Toolbar toolbar                          = (Toolbar)                 findViewById(R.id.toolbar);
        mDrawerLayout                            = (NoScrollableDrawerLayout)findViewById(R.id.drawerLayout);
        mGoodsCategoriesView                     = (FrameLayout)             findViewById(R.id.goodsCategoriesView);
        RecyclerView goodsCategoriesRecyclerView = (RecyclerView)            findViewById(R.id.goodsCategoriesRecyclerView);
        RecyclerView goodsRecyclerView           = (RecyclerView)            findViewById(R.id.goodsRecyclerView);
        // endregion

        assert goodsCategoriesRecyclerView != null;
        assert goodsRecyclerView != null;



        setSupportActionBar(toolbar);



        // region DrawerLayout initialization
        mGoodsCategoriesView.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels * 80 / 100;

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.show_catalog,
                R.string.hide_catalog
        );

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mGoodsCategoriesView.setOnTouchListener(this);
        // endregion



        mMainDatabase = new MainDatabase(this);
        mDB           = mMainDatabase.getReadableDatabase();

        mGoodsCategoriesAdapter = new GoodsCategoriesAdapter(this, mMainDatabase.getGoodsCategoriesTree(mDB, 0));
        mGoodsAdapter           = new GoodsAdapter(this, mGoodsCategoriesAdapter.getTopLevelItems(), mMainDatabase.getGoods(mDB, 0));

        goodsCategoriesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        goodsCategoriesRecyclerView.setAdapter(mGoodsCategoriesAdapter);

        goodsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        goodsRecyclerView.setAdapter(mGoodsAdapter);
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
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(mGoodsCategoriesView))
        {
            mDrawerLayout.closeDrawer(mGoodsCategoriesView);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        if (view == mGoodsCategoriesView)
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
