package ru.okmarket.okgoods.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.PagerAdapter;
import ru.okmarket.okgoods.fragments.GoodsFragment;
import ru.okmarket.okgoods.fragments.ShopMapFragment;

public class MainActivity extends AppCompatActivity
{
    private ViewPager       mPager           = null;
    private PagerAdapter    mPagerAdapter    = null;
    private GoodsFragment   mGoodsFragment   = null;
    private ShopMapFragment mShopMapFragment = null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        mPager = (ViewPager)findViewById(R.id.pager);

        if (mPager != null)
        {
            mPagerAdapter = new PagerAdapter(fragmentManager);

            mPager.setAdapter(mPagerAdapter);

            mGoodsFragment   = (GoodsFragment)   mPagerAdapter.getItem(PagerAdapter.PAGE_GOODS);
            mShopMapFragment = (ShopMapFragment) mPagerAdapter.getItem(PagerAdapter.PAGE_SHOP_MAP);
        }
        else
        {
            mGoodsFragment   = (GoodsFragment)   fragmentManager.findFragmentById(R.id.goodsFragment);
            mShopMapFragment = (ShopMapFragment) fragmentManager.findFragmentById(R.id.shopMapFragment);
        }

        setSupportActionBar(mGoodsFragment.getToolbar());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.menu_add_good)
        {
            return true;
        }

        if (id == R.id.menu_delete_goods)
        {
            return true;
        }

        if (id == R.id.menu_history)
        {
            return true;
        }

        if (id == R.id.menu_update_db)
        {
            return true;
        }

        if (id == R.id.menu_exit)
        {
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
