package ru.okmarket.okgoods.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.ShopsAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.other.Preferences;
import ru.okmarket.okgoods.other.ShopInfo;
import ru.okmarket.okgoods.util.AppLog;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class SelectShopActivity extends AppCompatActivity
{
    private static final String TAG = "SelectShopActivity";



    private MapView        mMapView         = null;
    private Overlay        mShopsOverlay    = null;
    private Drawable       mOverlayDrawable = null;
    private DrawerLayout   mDrawerLayout    = null;
    private ListView       mShopsListView   = null;
    private RelativeLayout mShopDetailsView = null;
    private ShopsAdapter   mShopsAdapter    = null;



    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop);



        mMapView         = (MapView)        findViewById(R.id.map);
        mDrawerLayout    = (DrawerLayout)   findViewById(R.id.drawer_layout);
        mShopsListView   = (ListView)       findViewById(R.id.shopsListView);
        mShopDetailsView = (RelativeLayout) findViewById(R.id.shopDetailsView);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        MainDatabase mainDatabase = new MainDatabase(this);
        SQLiteDatabase db = mainDatabase.getReadableDatabase();
        ArrayList<ShopInfo> shops = mainDatabase.getShops(db, mainDatabase.getCityId(prefs.getString(Preferences.SETTINGS_CITY, "MOSCOW")));
        db.close();



        MapController  mapController  = mMapView.getMapController();
        OverlayManager overlayManager = mapController.getOverlayManager();

        mMapView.showBuiltInScreenButtons(true);
        mShopsOverlay = new Overlay(mapController);
        mOverlayDrawable = getResources().getDrawable(R.drawable.ok_overlay);

        overlayManager.addOverlay(mShopsOverlay);



        int drawerWidth = getResources().getDisplayMetrics().widthPixels * 80 / 100;
        mShopsListView.getLayoutParams().width   = drawerWidth;
        mShopDetailsView.getLayoutParams().width = drawerWidth;



        mShopsAdapter = new ShopsAdapter(this, shops);
        mShopsListView.setAdapter(mShopsAdapter);



        moveMapToCurrentLocation();
        updateMapPoints();
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.menu_filter)
        {
            return true;
        }

        if (id == R.id.menu_shop_details)
        {
            toggleShopDetails();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void moveMapToCurrentLocation()
    {
        try
        {
            MapController mapController = mMapView.getMapController();

            LocationManager  locationManager  = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener()
            {
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {}

                @Override
                public void onLocationChanged(Location location) {}
            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            mapController.setPositionAnimationTo(new GeoPoint((int) location.getLatitude() * 1000000, (int) location.getLongitude() * 1000000));
        }
        catch (SecurityException e)
        {
            AppLog.e(TAG, "Failed to get current location", e);
        }
    }

    private void updateMapPoints()
    {
        mShopsOverlay.clearOverlayItems();

        for (int i = 0; i < mShopsAdapter.getCount(); ++i)
        {
            ShopInfo shop = (ShopInfo)mShopsAdapter.getItem(i);

            GeoPoint geoPoint = new GeoPoint(shop.getLatitude(), shop.getLongitude());
            OverlayItem overlayItem = new OverlayItem(geoPoint, mOverlayDrawable);
            BalloonItem balloonItem = new BalloonItem(this, geoPoint);
            balloonItem.setText(shop.getName());
            overlayItem.setBalloonItem(balloonItem);

            mShopsOverlay.addOverlayItem(overlayItem);
        }
    }

    private void toggleShopDetails()
    {
        if (mDrawerLayout.isDrawerOpen(mShopDetailsView))
        {
            mDrawerLayout.closeDrawer(mShopDetailsView);
        }
        else
        {
            mDrawerLayout.openDrawer(mShopDetailsView);
        }
    }
}
