package ru.okmarket.okgoods.activities;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.ShopsAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.other.Preferences;
import ru.okmarket.okgoods.other.ShopInfo;
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

public class SelectShopActivity extends AppCompatActivity implements OnMyLocationListener, OnBalloonListener, AdapterView.OnItemClickListener
{
    private static final String TAG = "SelectShopActivity";



    private Toolbar               mToolbar           = null;
    private MapView               mMapView           = null;
    private Overlay               mShopsOverlay      = null;
    private Drawable              mOverlayDrawable   = null;
    private DrawerLayout          mDrawerLayout      = null;
    private ActionBarDrawerToggle mDrawerToggle      = null;
    private ListView              mShopsListView     = null;
    private ShopsAdapter          mShopsAdapter      = null;
    private RelativeLayout        mShopDetailsView   = null;
    private ShopInfo              mSelectedShop      = null;
    private boolean               mMovedToCurrentPos = false;



    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop);



        mToolbar         = (Toolbar)       findViewById(R.id.toolbar);
        mMapView         = (MapView)       findViewById(R.id.map);
        mDrawerLayout    = (DrawerLayout)  findViewById(R.id.drawer_layout);
        mShopsListView   = (ListView)      findViewById(R.id.shopsListView);
        mShopDetailsView = (RelativeLayout)findViewById(R.id.shopDetailsView);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        MainDatabase mainDatabase = new MainDatabase(this);
        SQLiteDatabase db = mainDatabase.getReadableDatabase();
        ArrayList<ShopInfo> shops = mainDatabase.getShops(db, mainDatabase.getCityId(prefs.getString(Preferences.SETTINGS_CITY, "MOSCOW")));
        db.close();



        setSupportActionBar(mToolbar);



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
                mToolbar,
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



        updateMapPoints();
        updateShopDetails();
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
            // TODO: Implement it

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
    }
}
