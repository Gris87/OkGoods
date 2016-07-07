package ru.okmarket.okgoods.activities;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.GoodsAdapter;
import ru.okmarket.okgoods.adapters.GoodsCategoriesAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.db.entities.GoodEntity;
import ru.okmarket.okgoods.db.entities.GoodsCategoryEntity;
import ru.okmarket.okgoods.net.HttpClient;
import ru.okmarket.okgoods.net.Web;
import ru.okmarket.okgoods.util.AppLog;
import ru.okmarket.okgoods.util.Tree;
import ru.okmarket.okgoods.util.Utils;
import ru.okmarket.okgoods.widgets.DividerItemDecoration;
import ru.okmarket.okgoods.widgets.NoScrollableDrawerLayout;

public class GoodsCatalogActivity extends AppCompatActivity implements View.OnTouchListener, GoodsCategoriesAdapter.OnItemClickListener, GoodsAdapter.OnCategoryClickListener, GoodsAdapter.OnGoodClickListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsCatalogActivity";



    private static final String SAVED_STATE_TREE              = "TREE";
    private static final String SAVED_STATE_SELECTED_CATEGORY = "SELECTED_CATEGORY";



    // region Attributes
    private NoScrollableDrawerLayout  mDrawerLayout           = null;
    private ActionBarDrawerToggle     mDrawerToggle           = null;
    private ProgressBar               mLoadingProgressBar     = null;
    private FrameLayout               mGoodsCategoriesView    = null;
    private GoodsCategoriesAdapter    mGoodsCategoriesAdapter = null;
    private GoodsAdapter              mGoodsAdapter           = null;
    private MainDatabase              mMainDatabase           = null;
    private SQLiteDatabase            mDB                     = null;
    private Tree<GoodsCategoryEntity> mSelectedCategory       = null;
    private GoodsLoadingTask          mGoodsLoadingTask       = null;
    private HttpClient                mHttpClient             = null;
    private int                       mRequestsInProgress     = 0;
    // endregion



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_catalog);



        // region Searching for views
        Toolbar toolbar                          = (Toolbar)                 findViewById(R.id.toolbar);
        mDrawerLayout                            = (NoScrollableDrawerLayout)findViewById(R.id.drawerLayout);
        mLoadingProgressBar                      = (ProgressBar)             findViewById(R.id.loadingProgressBar);
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

        mGoodsCategoriesAdapter = new GoodsCategoriesAdapter(this, mMainDatabase.getGoodsCategoriesTree(mDB, MainDatabase.SPECIAL_ID_ROOT));
        mGoodsAdapter           = new GoodsAdapter(this, screenWidth / columnCount);

        mGoodsCategoriesAdapter.setOnItemClickListener(this);
        mGoodsAdapter.setOnCategoryClickListener(this);
        mGoodsAdapter.setOnGoodClickListener(this);

        goodsCategoriesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        goodsCategoriesRecyclerView.setAdapter(mGoodsCategoriesAdapter);

        goodsRecyclerView.setAdapter(mGoodsAdapter);
        ((GridLayoutManager)goodsRecyclerView.getLayoutManager()).setSpanCount(columnCount);
        // endregion



        mHttpClient = HttpClient.getInstance(this);

        mHttpClient.cancelAll(TAG);
        mRequestsInProgress = 0;

        //noinspection deprecation
        mLoadingProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.progressBarInToolbar), android.graphics.PorterDuff.Mode.SRC_IN);
        mLoadingProgressBar.setVisibility(View.GONE);

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

        if (mGoodsLoadingTask != null)
        {
            mGoodsLoadingTask.cancel(true);
        }

        if (mRequestsInProgress > 0)
        {
            mHttpClient.cancelAll(TAG);
        }

        if (mDB != null)
        {
            mDB.close();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        ArrayList<GoodsCategoryEntity> allNodes = mGoodsCategoriesAdapter.getTree().doDepthForResult(new Tree.OperationWithResult<GoodsCategoryEntity, ArrayList<GoodsCategoryEntity>>()
        {
            @Override
            protected ArrayList<GoodsCategoryEntity> init()
            {
                return new ArrayList<>();
            }

            @Override
            protected ArrayList<GoodsCategoryEntity> run(Tree<GoodsCategoryEntity> node, ArrayList<GoodsCategoryEntity> currentResult)
            {
                currentResult.add(node.getData());

                return currentResult;
            }
        });

        outState.putParcelableArrayList(SAVED_STATE_TREE,              allNodes);
        outState.putInt(                SAVED_STATE_SELECTED_CATEGORY, mSelectedCategory.getData().getId());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        final ArrayList<GoodsCategoryEntity> allNodes           = savedInstanceState.getParcelableArrayList(SAVED_STATE_TREE);
        final int                            selectedCategoryId = savedInstanceState.getInt(                SAVED_STATE_SELECTED_CATEGORY, MainDatabase.SPECIAL_ID_NONE);

        if (allNodes != null && allNodes.size() > 0)
        {
            Tree<GoodsCategoryEntity> tree = Utils.buildCategoriesTreeFromList(allNodes, allNodes.get(0));

            Tree<GoodsCategoryEntity> foundCategory = tree.doDepthForResult(new Tree.OperationWithResult<GoodsCategoryEntity, Tree<GoodsCategoryEntity>>()
            {
                @Override
                protected boolean filter(Tree<GoodsCategoryEntity> node, Tree<GoodsCategoryEntity> currentResult)
                {
                    return currentResult == null;
                }

                @Override
                protected Tree<GoodsCategoryEntity> init()
                {
                    return null;
                }

                @Override
                protected Tree<GoodsCategoryEntity> run(Tree<GoodsCategoryEntity> node, Tree<GoodsCategoryEntity> currentResult)
                {
                    if (node.getData().getId() == selectedCategoryId)
                    {
                        return node;
                    }

                    return null;
                }
            });

            if (foundCategory != null)
            {
                mGoodsCategoriesAdapter.setTree(tree);
                selectCategory(foundCategory);
            }
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
                mSelectedCategory.getData().setExpanded(false);
                mGoodsCategoriesAdapter.invalidate();

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
            AppLog.wtf(TAG, "Unknown view: " + String.valueOf(view));
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
                category.setExpanded(true);
                mGoodsCategoriesAdapter.invalidate();

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

        setTitle(mSelectedCategory.getData().getName());



        mGoodsAdapter.setItems(mSelectedCategory.getAll(), mMainDatabase.getGoods(mDB, mSelectedCategory.getData().getId(), true));

        if (mGoodsLoadingTask != null)
        {
            mGoodsLoadingTask.cancel(true);
        }

        mGoodsLoadingTask = new GoodsLoadingTask(mSelectedCategory.getData().getId());
        mGoodsLoadingTask.execute();

        if (mRequestsInProgress > 0)
        {
            mHttpClient.cancelAll(TAG);
            mRequestsInProgress = 0;

            mLoadingProgressBar.setVisibility(View.GONE);
        }
    }

    private void loadCompleted(ArrayList<GoodsCategoryEntity> webCategories, ArrayList<GoodEntity> webGoods)
    {
        if (webCategories.size() > 0 || webGoods.size() > 0)
        {
            ArrayList<GoodsCategoryEntity> categoriesInDB = mMainDatabase.getGoodsCategories(mDB, mSelectedCategory.getData().getId(), true);

            for (int i = 0; i < categoriesInDB.size(); ++i)
            {
                GoodsCategoryEntity category = categoriesInDB.get(i);

                int index = webCategories.indexOf(category);

                if (index >= 0)
                {
                    GoodsCategoryEntity webCategory = webCategories.get(index);

                    category.setName(     webCategory.getName());
                    category.setImageName(webCategory.getImageName());
                    category.setEnabled(  webCategory.getEnabled());
                }
                else
                {
                    category.setEnabled(MainDatabase.DISABLED);
                }

                mMainDatabase.updateGoodsCategory(mDB, category);
            }

            for (int i = 0; i < webCategories.size(); ++i)
            {
                GoodsCategoryEntity category = webCategories.get(i);

                if (!categoriesInDB.contains(category))
                {
                    categoriesInDB.add(category);
                    mMainDatabase.insertGoodsCategory(mDB, category);
                }
            }

            for (int i = 0; i < mSelectedCategory.size(); ++i)
            {
                GoodsCategoryEntity category = mSelectedCategory.get(i);

                if (!category.isOwn())
                {
                    int index = categoriesInDB.indexOf(category);

                    if (index >= 0)
                    {
                        GoodsCategoryEntity categoryInDB = categoriesInDB.get(index);

                        if (categoryInDB.isEnabled())
                        {
                            mSelectedCategory.set(i, categoryInDB);
                        }
                        else
                        {
                            mSelectedCategory.removeChild(i);
                            --i;
                        }
                    }
                    else
                    {
                        AppLog.wtf(TAG, "Failed to find category in database: " + category.getName());
                    }
                }
            }

            for (int i = 0; i < categoriesInDB.size(); ++i)
            {
                GoodsCategoryEntity category = categoriesInDB.get(i);

                if (!mSelectedCategory.contains(category))
                {
                    mSelectedCategory.addChild(category);
                }
            }

            mSelectedCategory.getData().setUpdateTime(System.currentTimeMillis());
            mMainDatabase.updateGoodsCategoryUpdateTime(mDB, mSelectedCategory.getData());



            ArrayList<GoodEntity> goods = mGoodsAdapter.getGoods();



            mGoodsCategoriesAdapter.invalidate();
            mGoodsAdapter.setItems(mSelectedCategory.getAll(), goods);
        }

        mLoadingProgressBar.setVisibility(View.GONE);
    }



    private class GoodsLoadingTask extends AsyncTask<Void, Void, ArrayList<GoodEntity>>
    {
        private int mCategoryId = 0;



        public GoodsLoadingTask(int categoryId)
        {
            mCategoryId = categoryId;
        }

        @Override
        protected ArrayList<GoodEntity> doInBackground(Void... params)
        {
            ArrayList<GoodEntity> res = null;

            try
            {
                res = mMainDatabase.getGoods(mDB, mCategoryId, false);
            }
            catch (Exception e)
            {
                // Nothing
            }

            if (isCancelled())
            {
                return null;
            }

            return res;
        }

        @Override
        protected void onPostExecute(final ArrayList<GoodEntity> goods)
        {
            if (goods != null && mCategoryId == mSelectedCategory.getData().getId())
            {
                mGoodsLoadingTask = null;

                mGoodsAdapter.setGoods(goods);

                if (
                    mCategoryId >= MainDatabase.SPECIAL_ID_ROOT
                    &&
                    System.currentTimeMillis() - mSelectedCategory.getData().getUpdateTime() > 300000 // 5 minutes = 5 * 60 * 1000
                   )
                {
                    mLoadingProgressBar.setVisibility(View.VISIBLE);

                    final ArrayList<GoodsCategoryEntity> webCategories = new ArrayList<>();
                    final ArrayList<GoodEntity>          webGoods      = new ArrayList<>();

                    for (int i = 0; i < Web.OKEY_DOSTAVKA_RU_SHOPS.length; ++i)
                    {
                        StringRequest request = new StringRequest(Request.Method.GET, Web.getCatalogUrl(Web.OKEY_DOSTAVKA_RU_SHOPS[i], Web.OKEY_DOSTAVKA_RU_SHOP_IDS[i], mCategoryId)
                                , new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        Web.getCatalogItemsFromResponse(response, mCategoryId, webCategories, webGoods);

                                        --mRequestsInProgress;

                                        if (mRequestsInProgress == 0)
                                        {
                                            loadCompleted(webCategories, webGoods);
                                        }
                                    }
                                }
                                , new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error)
                                    {
                                        AppLog.w(TAG, "Failed to get goods catalog: " + String.valueOf(mSelectedCategory.getData().getName()));

                                        --mRequestsInProgress;

                                        if (mRequestsInProgress == 0)
                                        {
                                            loadCompleted(webCategories, webGoods);
                                        }
                                    }
                                }
                        );

                        request.setTag(TAG);

                        mHttpClient.addToRequestQueue(request);
                        ++mRequestsInProgress;
                    }
                }
            }
        }
    }
}
