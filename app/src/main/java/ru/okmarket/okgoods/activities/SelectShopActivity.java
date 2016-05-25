package ru.okmarket.okgoods.activities;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;

import ru.okmarket.okgoods.R;
import ru.yandex.yandexmapkit.MapView;

public class SelectShopActivity extends AppCompatActivity
{
    private static final String TAG = "SelectShopActivity";



    private MapView        mMapView          = null;
    private DrawerLayout   mDrawerLayout     = null;
    private ListView       mShopsListView    = null;
    private RelativeLayout mShopDetailstView = null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop);

        mMapView          = (MapView)        findViewById(R.id.map);
        mDrawerLayout     = (DrawerLayout)   findViewById(R.id.drawer_layout);
        mShopsListView    = (ListView)       findViewById(R.id.shopsListView);
        mShopDetailstView = (RelativeLayout) findViewById(R.id.shopDetailsView);

        mMapView.showBuiltInScreenButtons(true);

        int drawerWidth = getResources().getDisplayMetrics().widthPixels * 80 / 100;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams)mShopsListView.getLayoutParams();
        params.width = drawerWidth;
        mShopsListView.setLayoutParams(params);
        mShopDetailstView.setLayoutParams(params);
    }

    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END))
        {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }
        else
        {
            super.onBackPressed();
        }
    }
}
