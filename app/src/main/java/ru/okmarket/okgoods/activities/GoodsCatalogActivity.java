package ru.okmarket.okgoods.activities;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
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
    public String toString()
    {
        return "GoodsCatalogActivity{" +
                "mDrawerLayout="             + mDrawerLayout           +
                ", mDrawerToggle="           + mDrawerToggle           +
                ", mLoadingProgressBar="     + mLoadingProgressBar     +
                ", mGoodsCategoriesView="    + mGoodsCategoriesView    +
                ", mGoodsCategoriesAdapter=" + mGoodsCategoriesAdapter +
                ", mGoodsAdapter="           + mGoodsAdapter           +
                ", mMainDatabase="           + mMainDatabase           +
                ", mDB="                     + mDB                     +
                ", mSelectedCategory="       + mSelectedCategory       +
                ", mGoodsLoadingTask="       + mGoodsLoadingTask       +
                ", mHttpClient="             + mHttpClient             +
                ", mRequestsInProgress="     + mRequestsInProgress     +
                '}';
    }

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
        mMainDatabase = MainDatabase.newInstance(this);
        mDB           = mMainDatabase.getReadableDatabase();

        mGoodsCategoriesAdapter = GoodsCategoriesAdapter.newInstance(this, mMainDatabase.getGoodsCategoriesTree(mDB, MainDatabase.SPECIAL_ID_ROOT));
        mGoodsAdapter           = GoodsAdapter.newInstance(this, screenWidth / columnCount);

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
        mLoadingProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.progressBarInToolbar), PorterDuff.Mode.SRC_IN);
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
                return new ArrayList<>(0);
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

        ArrayList<GoodsCategoryEntity> allNodes           = savedInstanceState.getParcelableArrayList(SAVED_STATE_TREE);
        final int                      selectedCategoryId = savedInstanceState.getInt(                SAVED_STATE_SELECTED_CATEGORY, MainDatabase.SPECIAL_ID_NONE);

        if (allNodes != null && !allNodes.isEmpty())
        {
            Tree<GoodsCategoryEntity> tree = Utils.buildCategoriesTreeFromList(allNodes, allNodes.get(0));

            Tree<GoodsCategoryEntity> foundCategory = tree.doDepthForResult(new Tree.OperationWithResult<GoodsCategoryEntity, Tree<GoodsCategoryEntity>>()
            {
                @Override
                protected boolean filter(Tree<GoodsCategoryEntity> node, Tree<GoodsCategoryEntity> currentResult)
                {
                    return currentResult == null;
                }

                @Nullable
                @Override
                protected Tree<GoodsCategoryEntity> init()
                {
                    return null;
                }

                @Nullable
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
            AppLog.wtf(TAG, "Unknown view: " + view);
        }

        return false;
    }

    @Override
    public void onGoodsCategoryClicked(GoodsCategoriesAdapter.GoodsCategoryViewHolder holder, Tree<GoodsCategoryEntity> category)
    {
        mDrawerLayout.closeDrawer(mGoodsCategoriesView);

        selectCategory(category);
    }

    @Override
    public void onCategoryClicked(GoodsAdapter.GoodViewHolder holder, GoodsCategoryEntity category)
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
    public void onGoodClicked(GoodsAdapter.GoodViewHolder holder, GoodEntity good)
    {
        // TODO: Implement it
    }

    private void selectCategory(Tree<GoodsCategoryEntity> category)
    {
        mSelectedCategory = category;

        setTitle(mSelectedCategory.getData().getName());



        mGoodsAdapter.setItems(mSelectedCategory.getAll(), mMainDatabase.getGoods(mDB, mSelectedCategory.getData().getId(), MainDatabase.ALLOW_DISABLED_NO, MainDatabase.LIMIT_STANDARD));

        if (mGoodsLoadingTask != null)
        {
            mGoodsLoadingTask.cancel(true);
        }

        mGoodsLoadingTask = new GoodsLoadingTask(this);
        mGoodsLoadingTask.execute();

        if (mRequestsInProgress > 0)
        {
            mHttpClient.cancelAll(TAG);
            mRequestsInProgress = 0;

            mLoadingProgressBar.setVisibility(View.GONE);
        }
    }

    private void loadPartiallyCompleted(ArrayList<GoodsCategoryEntity> webCategories, ArrayList<GoodEntity> webGoods)
    {
        if (!webCategories.isEmpty() || !webGoods.isEmpty())
        {
            ArrayList<GoodsCategoryEntity> categoriesInDB = mMainDatabase.getGoodsCategories(mDB, mSelectedCategory.getData().getId(), MainDatabase.ALLOW_DISABLED_YES);

            for (int i = 0; i < categoriesInDB.size(); ++i)
            {
                GoodsCategoryEntity category = categoriesInDB.get(i);

                int index = webCategories.indexOf(category);

                if (index >= 0)
                {
                    GoodsCategoryEntity webCategory = webCategories.get(index);

                    category.setName(     webCategory.getName());
                    category.setImageName(webCategory.getImageName());
                    category.setPriority( webCategory.getPriority());
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

                            //noinspection AssignmentToForLoopParameter
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



            ArrayList<GoodEntity> goodsInDB = mMainDatabase.getGoods(mDB, mSelectedCategory.getData().getId(), MainDatabase.ALLOW_DISABLED_YES, MainDatabase.LIMIT_UNLIMITED);

            for (int i = 0; i < goodsInDB.size(); ++i)
            {
                GoodEntity good = goodsInDB.get(i);

                int index = webGoods.indexOf(good);

                if (index >= 0)
                {
                    GoodEntity webGood = webGoods.get(index);

                    good.setName(    webGood.getName());
                    good.setImageId( webGood.getImageId());
                    good.setCost(    webGood.getCost());

                    if (good.getUnitType() != MainDatabase.UNIT_TYPE_LITER)
                    {
                        good.setUnit(    webGood.getUnit());
                        good.setUnitType(webGood.getUnitType());
                    }

                    good.setCountIncrement(webGood.getCountIncrement());
                    good.setCountType(     webGood.getCountType());
                    good.setAttrs(         Utils.mergeJSONObjects(good.getAttrs(),        webGood.getAttrs()));
                    good.setAttrsDetails(  Utils.mergeJSONObjects(good.getAttrsDetails(), webGood.getAttrsDetails()));
                    good.setPriority(      webGood.getPriority());
                    good.setEnabled(       webGood.getEnabled());
                }
                else
                {
                    good.setEnabled(MainDatabase.DISABLED);
                }

                mMainDatabase.updateGood(mDB, good);
            }

            for (int i = 0; i < webGoods.size(); ++i)
            {
                GoodEntity category = webGoods.get(i);

                if (!goodsInDB.contains(category))
                {
                    goodsInDB.add(category);
                    mMainDatabase.insertGood(mDB, category);
                }
            }

            ArrayList<GoodEntity> goods = mGoodsAdapter.getGoods();

            for (int i = 0; i < goods.size(); ++i)
            {
                GoodEntity good = goods.get(i);

                if (!good.isOwn())
                {
                    int index = goodsInDB.indexOf(good);

                    if (index >= 0)
                    {
                        GoodEntity goodInDB = goodsInDB.get(index);

                        if (goodInDB.isEnabled())
                        {
                            goods.set(i, goodInDB);
                        }
                        else
                        {
                            goods.remove(i);

                            //noinspection AssignmentToForLoopParameter
                            --i;
                        }
                    }
                    else
                    {
                        AppLog.wtf(TAG, "Failed to find good in database: " + good.getName());
                    }
                }
            }

            for (int i = 0; i < goodsInDB.size(); ++i)
            {
                GoodEntity good = goodsInDB.get(i);

                if (!goods.contains(good))
                {
                    goods.add(good);
                }
            }



            mGoodsCategoriesAdapter.invalidate();
            mGoodsAdapter.setItems(mSelectedCategory.getAll(), goods);
        }
    }

    private void loadCompleted(ArrayList<GoodsCategoryEntity> webCategories, ArrayList<GoodEntity> webGoods)
    {
        if (!webCategories.isEmpty() || !webGoods.isEmpty())
        {
            mSelectedCategory.getData().setUpdateTime(System.currentTimeMillis());
            mMainDatabase.updateGoodsCategoryUpdateTime(mDB, mSelectedCategory.getData());
        }

        mLoadingProgressBar.setVisibility(View.GONE);
    }



    private static final class GoodsLoadingTask extends AsyncTask<Void, Void, ArrayList<GoodEntity>>
    {
        @SuppressWarnings("FieldNotUsedInToString")
        private GoodsCatalogActivity mGoodsCatalogActivity = null;
        private int                  mCategoryId           = 0;




        @Override
        public String toString()
        {
            return "GoodsLoadingTask{" +
                    "mCategoryId=" + mCategoryId +
                    '}';
        }

        @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
        private GoodsLoadingTask(GoodsCatalogActivity goodsCatalogActivity)
        {
            mGoodsCatalogActivity = goodsCatalogActivity;
            mCategoryId           = mGoodsCatalogActivity.mSelectedCategory.getData().getId();
        }

        @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
        @Override
        protected ArrayList<GoodEntity> doInBackground(Void... params)
        {
            ArrayList<GoodEntity> res = null;

            try
            {
                res = mGoodsCatalogActivity.mMainDatabase.getGoods(mGoodsCatalogActivity.mDB, mCategoryId, MainDatabase.ALLOW_DISABLED_NO, MainDatabase.LIMIT_UNLIMITED);
            }
            catch (Exception ignored)
            {
                // Nothing
            }

            if (isCancelled())
            {
                return null;
            }

            return res;
        }

        @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
        @Override
        protected void onPostExecute(ArrayList<GoodEntity> goods)
        {
            if (goods != null && mCategoryId == mGoodsCatalogActivity.mSelectedCategory.getData().getId())
            {
                mGoodsCatalogActivity.mGoodsLoadingTask = null;

                mGoodsCatalogActivity.mGoodsAdapter.setGoods(goods);

                if (
                    mCategoryId >= MainDatabase.SPECIAL_ID_ROOT
                    &&
                    System.currentTimeMillis() - mGoodsCatalogActivity.mSelectedCategory.getData().getUpdateTime() > 900000 // 15 minutes = 15 * 60 * 1000 = 900000
                   )
                {
                    mGoodsCatalogActivity.mLoadingProgressBar.setVisibility(View.VISIBLE);

                    final ArrayList<GoodsCategoryEntity> webCategories = new ArrayList<>(0);
                    final ArrayList<GoodEntity>          webGoods      = new ArrayList<>(0);

                    for (int i = 0; i < Web.OKEY_DOSTAVKA_RU_SHOPS.length; ++i)
                    {
                        StringRequest request = new StringRequest(Request.Method.GET, Web.getCatalogUrl(Web.OKEY_DOSTAVKA_RU_SHOPS[i], Web.OKEY_DOSTAVKA_RU_SHOP_IDS[i], mCategoryId, true)
                                , new Response.Listener<String>()
                                {
                                    private String mShop   = null;
                                    private int    mShopId = 0;



                                    @SuppressWarnings({"WeakerAccess", "ReturnOfThis"})
                                    public Response.Listener<String> init(String shop, int shopId)
                                    {
                                        mShop   = shop;
                                        mShopId = shopId;

                                        return this;
                                    }



                                    @Override
                                    public void onResponse(String response)
                                    {
                                        boolean hasPages = Web.getCatalogItemsFromResponse(response, webCategories, webGoods, mShop, mShopId, mCategoryId, true);
                                        mGoodsCatalogActivity.loadPartiallyCompleted(webCategories, webGoods);

                                        --mGoodsCatalogActivity.mRequestsInProgress;

                                        if (hasPages)
                                        {
                                            StringRequest request2 = new StringRequest(Request.Method.GET, Web.getCatalogUrl(mShop, mShopId, mCategoryId, false)
                                                    , new Response.Listener<String>()
                                                    {
                                                        private String mInnerShop   = null;
                                                        private int    mInnerShopId = 0;



                                                        @SuppressWarnings({"WeakerAccess", "ReturnOfThis"})
                                                        public Response.Listener<String> init(String shop, int shopId)
                                                        {
                                                            mInnerShop   = shop;
                                                            mInnerShopId = shopId;

                                                            return this;
                                                        }



                                                        @Override
                                                        public void onResponse(String innerResponse)
                                                        {
                                                            Web.getCatalogItemsFromResponse(innerResponse, webCategories, webGoods, mInnerShop, mInnerShopId, mCategoryId, false);
                                                            mGoodsCatalogActivity.loadPartiallyCompleted(webCategories, webGoods);

                                                            --mGoodsCatalogActivity.mRequestsInProgress;

                                                            if (mGoodsCatalogActivity.mRequestsInProgress == 0)
                                                            {
                                                                mGoodsCatalogActivity.loadCompleted(webCategories, webGoods);
                                                            }
                                                        }
                                                    }
                                                    .init(mShop, mShopId)
                                                    , new Response.ErrorListener()
                                                    {
                                                        private String mInnerShop   = null;
                                                        private int    mInnerShopId = 0;



                                                        @SuppressWarnings({"WeakerAccess", "ReturnOfThis"})
                                                        public Response.ErrorListener init(String shop, int shopId)
                                                        {
                                                            mInnerShop   = shop;
                                                            mInnerShopId = shopId;

                                                            return this;
                                                        }



                                                        @Override
                                                        public void onErrorResponse(VolleyError error)
                                                        {
                                                            AppLog.w(TAG, "Failed to get goods catalog: " + Web.getCatalogUrl(mInnerShop, mInnerShopId, mCategoryId, false));

                                                            --mGoodsCatalogActivity.mRequestsInProgress;

                                                            if (mGoodsCatalogActivity.mRequestsInProgress == 0)
                                                            {
                                                                mGoodsCatalogActivity.loadCompleted(webCategories, webGoods);
                                                            }
                                                        }
                                                    }
                                                    .init(mShop, mShopId)
                                            );

                                            request2.setTag(TAG);

                                            mGoodsCatalogActivity.mHttpClient.addToRequestQueue(request2);
                                            ++mGoodsCatalogActivity.mRequestsInProgress;
                                        }
                                        else
                                        {
                                            if (mGoodsCatalogActivity.mRequestsInProgress == 0)
                                            {
                                                mGoodsCatalogActivity.loadCompleted(webCategories, webGoods);
                                            }
                                        }
                                    }
                                }
                                .init(Web.OKEY_DOSTAVKA_RU_SHOPS[i], Web.OKEY_DOSTAVKA_RU_SHOP_IDS[i])
                                , new Response.ErrorListener()
                                {
                                    private String mShop   = null;
                                    private int    mShopId = 0;



                                    @SuppressWarnings({"WeakerAccess", "ReturnOfThis"})
                                    public Response.ErrorListener init(String shop, int shopId)
                                    {
                                        mShop   = shop;
                                        mShopId = shopId;

                                        return this;
                                    }



                                    @Override
                                    public void onErrorResponse(VolleyError error)
                                    {
                                        AppLog.w(TAG, "Failed to get goods catalog: " + Web.getCatalogUrl(mShop, mShopId, mCategoryId, true));

                                        --mGoodsCatalogActivity.mRequestsInProgress;

                                        if (mGoodsCatalogActivity.mRequestsInProgress == 0)
                                        {
                                            mGoodsCatalogActivity.loadCompleted(webCategories, webGoods);
                                        }
                                    }
                                }
                                .init(Web.OKEY_DOSTAVKA_RU_SHOPS[i], Web.OKEY_DOSTAVKA_RU_SHOP_IDS[i])
                        );

                        request.setTag(TAG);

                        mGoodsCatalogActivity.mHttpClient.addToRequestQueue(request);
                        ++mGoodsCatalogActivity.mRequestsInProgress;
                    }
                }
            }
        }
    }
}
