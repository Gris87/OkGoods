package ru.okmarket.okgoods.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import ru.okmarket.okgoods.fragments.PhotoFragment;

public final class PhotoPagerAdapter extends FragmentStatePagerAdapter
{
    @SuppressWarnings("unused")
    private static final String TAG = "PhotoPagerAdapter";



    private ArrayList<String> mUrls = null;



    @Override
    public String toString()
    {
        return "PhotoPagerAdapter{" +
                "mUrls=" + mUrls +
                '}';
    }

    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    private PhotoPagerAdapter(FragmentManager fm, ArrayList<String> urls)
    {
        super(fm);

        mUrls = urls;
    }

    public static PhotoPagerAdapter newInstance(FragmentManager fm, ArrayList<String> urls)
    {
        return new PhotoPagerAdapter(fm, urls);
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
