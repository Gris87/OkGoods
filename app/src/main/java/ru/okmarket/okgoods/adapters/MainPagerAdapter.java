package ru.okmarket.okgoods.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.okmarket.okgoods.fragments.GoodsFragment;
import ru.okmarket.okgoods.fragments.ShopMapFragment;

public class MainPagerAdapter extends FragmentPagerAdapter
{
    @SuppressWarnings("unused")
    private static final String TAG = "MainPagerAdapter";



    private static final int PAGE_GOODS    = 0;
    private static final int PAGE_SHOP_MAP = 1;
    private static final int PAGES_COUNT   = 2;



    private Fragment[] mFragments = null;



    public MainPagerAdapter(FragmentManager fragmentManager)
    {
        super(fragmentManager);

        mFragments = new Fragment[PAGES_COUNT];

        mFragments[PAGE_GOODS]    = new GoodsFragment();
        mFragments[PAGE_SHOP_MAP] = new ShopMapFragment();
    }

    @Override
    public Fragment getItem(int position)
    {
        return mFragments[position];
    }

    @Override
    public int getCount()
    {
        return PAGES_COUNT;
    }
}