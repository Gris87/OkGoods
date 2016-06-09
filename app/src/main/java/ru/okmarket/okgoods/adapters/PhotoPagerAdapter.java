package ru.okmarket.okgoods.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import ru.okmarket.okgoods.fragments.PhotoFragment;

public class PhotoPagerAdapter extends FragmentStatePagerAdapter
{
    @SuppressWarnings("unused")
    private static final String TAG = "PhotoPagerAdapter";



    private ArrayList<String> mUrls = null;



    public PhotoPagerAdapter(FragmentManager fm, ArrayList<String> urls)
    {
        super(fm);

        mUrls = urls;
    }

    @Override
    public Fragment getItem(int position)
    {
        return PhotoFragment.newInstance(mUrls.get(position));
    }

    @Override
    public int getCount()
    {
        return mUrls.size();
    }
}
