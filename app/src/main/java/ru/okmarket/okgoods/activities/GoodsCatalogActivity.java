package ru.okmarket.okgoods.activities;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.GoodsAdapter;
import ru.okmarket.okgoods.adapters.GoodsCategoriesAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.db.entities.GoodEntity;
import ru.okmarket.okgoods.db.entities.GoodsCategoryEntity;
import ru.okmarket.okgoods.util.AppLog;
import ru.okmarket.okgoods.util.Tree;
import ru.okmarket.okgoods.widgets.DividerItemDecoration;
import ru.okmarket.okgoods.widgets.NoScrollableDrawerLayout;

public class GoodsCatalogActivity extends AppCompatActivity implements View.OnTouchListener, GoodsCategoriesAdapter.OnItemClickListener, GoodsAdapter.OnCategoryClickListener, GoodsAdapter.OnGoodClickListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsCatalogActivity";



    // region Attributes
    private NoScrollableDrawerLayout  mDrawerLayout           = null;
    private ActionBarDrawerToggle     mDrawerToggle           = null;
    private FrameLayout               mGoodsCategoriesView    = null;
    private GoodsCategoriesAdapter    mGoodsCategoriesAdapter = null;
    private GoodsAdapter              mGoodsAdapter           = null;
    private MainDatabase              mMainDatabase           = null;
    private SQLiteDatabase            mDB                     = null;
    private Tree<GoodsCategoryEntity> mSelectedCategory       = null;
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

        assert goodsCategoriesRecyclerView != null;
        assert goodsRecyclerView           != null;
        // endregion



        Resources resources = getResources();

        int screenWidth    = resources.getDisplayMetrics().widthPixels;
        int maxColumnWidth = resources.getDimensionPixelSize(R.dimen.good_max_width);
        int columnCount    = screenWidth / maxColumnWidth;

        if (columnCount * maxColumnWidth > screenWidth)
        {
            --columnCount;
        }

        if (columnCount <= 0)
        {
            columnCount = 1;
        }



        setSupportActionBar(toolbar);



        // region DrawerLayout initialization
        mGoodsCategoriesView.getLayoutParams().width = screenWidth * 80 / 100;

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



        // region RecyclerViews initialization
        mMainDatabase = new MainDatabase(this);
        mDB           = mMainDatabase.getReadableDatabase();

        mGoodsCategoriesAdapter = new GoodsCategoriesAdapter(this, mMainDatabase.getGoodsCategoriesTree(mDB, 0));
        mGoodsAdapter           = new GoodsAdapter(this, screenWidth / columnCount);

        mGoodsCategoriesAdapter.setOnItemClickListener(this);
        mGoodsAdapter.setOnCategoryClickListener(this);
        mGoodsAdapter.setOnGoodClickListener(this);

        goodsCategoriesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        goodsCategoriesRecyclerView.setAdapter(mGoodsCategoriesAdapter);

        goodsRecyclerView.setAdapter(mGoodsAdapter);
        ((GridLayoutManager)goodsRecyclerView.getLayoutManager()).setSpanCount(columnCount);
        // endregion



        selectCategory(mGoodsCategoriesAdapter.getTree());
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
            if (mSelectedCategory.getParent() != null)
            {
                selectCategory(mSelectedCategory.getParent());
            }
            else
            {
                super.onBackPressed();
            }
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

    @Override
    public void onGoodsCategoryClicked(GoodsCategoriesAdapter.ViewHolder viewHolder, Tree<GoodsCategoryEntity> category)
    {
        mDrawerLayout.closeDrawer(mGoodsCategoriesView);

        selectCategory(category);
    }

    @Override
    public void onCategoryClicked(GoodsAdapter.ViewHolder viewHolder, GoodsCategoryEntity category)
    {
        for (int i = 0; i < mSelectedCategory.size(); ++i)
        {
            Tree<GoodsCategoryEntity> child = mSelectedCategory.getChild(i);

            if (child.getData() == category)
            {
                selectCategory(child);

                break;
            }
        }
    }

    @Override
    public void onGoodClicked(GoodsAdapter.ViewHolder viewHolder, GoodEntity good)
    {

    }

    private void selectCategory(Tree<GoodsCategoryEntity> category)
    {
        mSelectedCategory = category;

        mGoodsAdapter.setItems(mSelectedCategory.getAll(), mMainDatabase.getGoods(mDB, mSelectedCategory.getData().getId()));

        setTitle(mSelectedCategory.getData().getName());
    }
}
