package ru.okmarket.okgoods.activities;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import ru.yandex.yandexmapkit.MapView;

public class SelectShopActivity extends AppCompatActivity
{
    private static final String TAG = "SelectShopActivity";



    private MapView        mMapView         = null;
    private DrawerLayout   mDrawerLayout    = null;
    private ListView       mShopsListView   = null;
    private RelativeLayout mShopDetailsView = null;
    private ShopsAdapter   mShopsAdapter    = null;



    @Override
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



        mMapView.showBuiltInScreenButtons(true);

        int drawerWidth = getResources().getDisplayMetrics().widthPixels * 80 / 100;
        mShopsListView.getLayoutParams().width    = drawerWidth;
        mShopDetailsView.getLayoutParams().width = drawerWidth;

        mShopsAdapter = new ShopsAdapter(this, shops);
        mShopsListView.setAdapter(mShopsAdapter);
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
