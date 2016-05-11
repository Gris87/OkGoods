package ru.okmarket.okgoods.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.PagerAdapter;
import ru.okmarket.okgoods.fragments.GoodsFragment;
import ru.okmarket.okgoods.fragments.ShopMapFragment;

public class MainActivity extends AppCompatActivity implements GoodsFragment.OnFragmentInteractionListener, ShopMapFragment.OnFragmentInteractionListener
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

        mPager           = null;
        mPagerAdapter    = null;
        mGoodsFragment   = null;
        mShopMapFragment = null;

        mPager = (ViewPager)findViewById(R.id.pager);

        if (mPager != null)
        {
            mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

            mPager.setAdapter(mPagerAdapter);
        }
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

    @Override
    public void onGoodsFragmentCreated(GoodsFragment fragment)
    {
        mGoodsFragment = fragment;

        setSupportActionBar(mGoodsFragment.getToolbar());
    }

    @Override
    public void onShopMapFragmentCreated(ShopMapFragment fragment)
    {
        mShopMapFragment = fragment;
    }
}
