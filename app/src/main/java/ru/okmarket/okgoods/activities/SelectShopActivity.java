package ru.okmarket.okgoods.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.ShopsAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.dialogs.ShopFilterDialog;
import ru.okmarket.okgoods.fragments.ShopDetailsFragment;
import ru.okmarket.okgoods.other.Extras;
import ru.okmarket.okgoods.other.Preferences;
import ru.okmarket.okgoods.other.ShopFilter;
import ru.okmarket.okgoods.other.ShopInfo;
import ru.okmarket.okgoods.util.AppLog;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.overlay.location.MyLocationItem;
import ru.yandex.yandexmapkit.overlay.location.MyLocationOverlay;
import ru.yandex.yandexmapkit.overlay.location.OnMyLocationListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class SelectShopActivity extends AppCompatActivity implements OnMyLocationListener, OnBalloonListener, AdapterView.OnItemClickListener, View.OnTouchListener, ShopDetailsFragment.OnFragmentInteractionListener, ShopFilterDialog.OnFragmentInteractionListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "SelectShopActivity";



    public static final int SHOP_SELECTED = 1;



    private static final String SAVED_STATE_MAP_CENTER           = "MAP_CENTER";
    private static final String SAVED_STATE_MAP_ZOOM             = "MAP_ZOOM";
    private static final String SAVED_STATE_SHOP_FILTER          = "SHOP_FILTER";
    private static final String SAVED_STATE_SELECTED_SHOP        = "SELECTED_SHOP";
    private static final String SAVED_STATE_MOVED_TO_CURRENT_POS = "MOVED_TO_CURRENT_POS";



    private MapView               mMapView             = null;
    private Overlay               mShopsOverlay        = null;
    private Drawable              mOverlayDrawable     = null;
    private DrawerLayout          mDrawerLayout        = null;
    private ActionBarDrawerToggle mDrawerToggle        = null;
    private ListView              mShopsListView       = null;
    private ShopsAdapter          mShopsAdapter        = null;
    private FrameLayout           mShopDetailsView     = null;
    private ShopDetailsFragment   mShopDetailsFragment = null;
    private ShopFilter            mShopFilter          = null;
    private ShopInfo              mSelectedShop        = null;
    private boolean               mMovedToCurrentPos   = false;



    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop);



        Toolbar toolbar      = (Toolbar)            findViewById(R.id.toolbar);
        mMapView             = (MapView)            findViewById(R.id.map);
        mDrawerLayout        = (DrawerLayout)       findViewById(R.id.drawer_layout);
        mShopsListView       = (ListView)           findViewById(R.id.shopsListView);
        mShopDetailsView     = (FrameLayout)        findViewById(R.id.shopDetailsView);
        mShopDetailsFragment = (ShopDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.shopDetailsFragment);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        MainDatabase mainDatabase = new MainDatabase(this);
        SQLiteDatabase db = mainDatabase.getReadableDatabase();
        ArrayList<ShopInfo> shops = mainDatabase.getShops(db, mainDatabase.getCityId(prefs.getString(Preferences.SETTINGS_CITY, "MOSCOW")));
        db.close();



        setSupportActionBar(toolbar);



        MapController     mapController     = mMapView.getMapController();
        OverlayManager    overlayManager    = mapController.getOverlayManager();
        MyLocationOverlay myLocationOverlay = overlayManager.getMyLocation();

        mMapView.showBuiltInScreenButtons(true);
        mShopsOverlay = new Overlay(mapController);
        mOverlayDrawable = getResources().getDrawable(R.drawable.ok_overlay);

        overlayManager.addOverlay(mShopsOverlay);
        myLocationOverlay.setAutoScroll(false);
        myLocationOverlay.addMyLocationListener(this);



        int drawerWidth = getResources().getDisplayMetrics().widthPixels * 80 / 100;
        mShopsListView.getLayoutParams().width   = drawerWidth;
        mShopDetailsView.getLayoutParams().width = drawerWidth;

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.show_list,
                R.string.hide_list
        )
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);

                if (drawerView == mShopsListView)
                {
                    mDrawerLayout.closeDrawer(mShopDetailsView);
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);



        mShopsAdapter = new ShopsAdapter(this, shops);
        mShopsListView.setAdapter(mShopsAdapter);
        mShopsListView.setOnItemClickListener(this);



        mShopDetailsView.setOnTouchListener(this);



        mShopFilter = new ShopFilter();



        updateMapPoints();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        MapController mapController = mMapView.getMapController();

        outState.putParcelable(SAVED_STATE_MAP_CENTER,           mapController.getMapCenter());
        outState.putFloat(     SAVED_STATE_MAP_ZOOM,             mapController.getZoomCurrent());
        outState.putParcelable(SAVED_STATE_SHOP_FILTER,          mShopFilter);
        outState.putParcelable(SAVED_STATE_SELECTED_SHOP,        mSelectedShop);
        outState.putBoolean(   SAVED_STATE_MOVED_TO_CURRENT_POS, mMovedToCurrentPos);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        GeoPoint mapCenter = savedInstanceState.getParcelable(SAVED_STATE_MAP_CENTER);

        if (mapCenter != null)
        {
            MapController mapController = mMapView.getMapController();

            mapController.setPositionNoAnimationTo(mapCenter);
            mapController.setZoomCurrent(savedInstanceState.getFloat(SAVED_STATE_MAP_ZOOM));
        }

        mShopFilter = savedInstanceState.getParcelable(SAVED_STATE_SHOP_FILTER);

        mSelectedShop = savedInstanceState.getParcelable(SAVED_STATE_SELECTED_SHOP);
        updateShopDetails();

        mMovedToCurrentPos = savedInstanceState.getBoolean(SAVED_STATE_MOVED_TO_CURRENT_POS);
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
        if (mDrawerLayout.isDrawerOpen(mShopsListView))
        {
            mDrawerLayout.closeDrawer(mShopsListView);
        }
        else
        if (mDrawerLayout.isDrawerOpen(mShopDetailsView))
        {
            mDrawerLayout.closeDrawer(mShopDetailsView);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_select_shop, menu);

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

        if (id == R.id.menu_filter)
        {
            ShopFilterDialog dialog = ShopFilterDialog.newInstance(mShopFilter);
            dialog.show(getSupportFragmentManager(), "ShopFilterDialog");

            return true;
        }

        if (id == R.id.menu_shop_details)
        {
            toggleShopDetails();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_MENU)
        {
            toggleShopDetails();

            return true;
        }
        else
        {
            return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onMyLocationChange(MyLocationItem myLocationItem)
    {
        GeoPoint geoPoint = myLocationItem.getGeoPoint();

        if (!mMovedToCurrentPos)
        {
            mMovedToCurrentPos = true;

            mMapView.getMapController().setPositionNoAnimationTo(geoPoint);
        }

        mShopsAdapter.findNearestShop(geoPoint.getLat(), geoPoint.getLon());
    }

    @Override
    public void onBalloonViewClick(BalloonItem balloonItem, View view)
    {
        // Nothing
    }

    @Override
    public void onBalloonShow(BalloonItem balloonItem)
    {
        int itemId = Integer.parseInt(String.valueOf(balloonItem.getText()));

        mSelectedShop = (ShopInfo)mShopsAdapter.getItem(itemId);
        updateShopDetails();

        mDrawerLayout.openDrawer(mShopDetailsView);

        balloonItem.setVisible(false);
    }

    @Override
    public void onBalloonHide(BalloonItem balloonItem)
    {
        // Nothing
    }

    @Override
    public void onBalloonAnimationStart(BalloonItem balloonItem)
    {
        // Nothing
    }

    @Override
    public void onBalloonAnimationEnd(BalloonItem balloonItem)
    {
        // Nothing
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (parent == mShopsListView)
        {
            mSelectedShop = (ShopInfo)mShopsAdapter.getItem(position);
            updateShopDetails();

            mMapView.getMapController().setPositionAnimationTo(new GeoPoint(mSelectedShop.getLatitude(), mSelectedShop.getLongitude()));

            mDrawerLayout.closeDrawer(mShopsListView);
            mDrawerLayout.openDrawer(mShopDetailsView);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        if (view == mShopDetailsView)
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
    public void onShopDetailsCancelClicked()
    {
        mDrawerLayout.closeDrawer(mShopDetailsView);
    }

    @Override
    public void onShopDetailsOkClicked()
    {
        Intent intent = new Intent();
        intent.putExtra(Extras.SHOP_ID, mSelectedShop.getId());
        setResult(SHOP_SELECTED, intent);

        finish();
    }

    @Override
    public void onShopFilterApplied(ShopFilter filter)
    {
        mShopFilter = filter;
        mShopsAdapter.filter(mShopFilter);
        updateMapPoints();
    }

    private void updateMapPoints()
    {
        mShopsOverlay.clearOverlayItems();

        for (int i = 0; i < mShopsAdapter.getCount(); ++i)
        {
            ShopInfo shop = (ShopInfo)mShopsAdapter.getItem(i);

            if (shop.getLatitude() != 0 || shop.getLongitude() != 0)
            {
                GeoPoint geoPoint = new GeoPoint(shop.getLatitude(), shop.getLongitude());
                OverlayItem overlayItem = new OverlayItem(geoPoint, mOverlayDrawable);
                BalloonItem balloonItem = new BalloonItem(this, geoPoint);
                balloonItem.setText(String.valueOf(i));
                balloonItem.setOnBalloonListener(this);
                overlayItem.setBalloonItem(balloonItem);

                mShopsOverlay.addOverlayItem(overlayItem);
            }
        }

        mSelectedShop = null;
        updateShopDetails();
    }

    private void toggleShopDetails()
    {
        mDrawerLayout.closeDrawer(mShopsListView);

        if (mDrawerLayout.isDrawerOpen(mShopDetailsView))
        {
            mDrawerLayout.closeDrawer(mShopDetailsView);
        }
        else
        {
            mDrawerLayout.openDrawer(mShopDetailsView);
        }
    }

    private void updateShopDetails()
    {
        mShopDetailsFragment.updateUI(mSelectedShop);
    }
}
