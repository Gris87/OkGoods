package ru.okmarket.okgoods.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.PhotoPagerAdapter;
import ru.okmarket.okgoods.other.Extras;
import ru.okmarket.okgoods.widgets.PhotoViewPager;

public class PhotoViewerActivity extends FragmentActivity
{
    @SuppressWarnings("unused")
    private static final String TAG = "PhotoViewerActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);



        Intent intent = getIntent();

        ArrayList<String> urls          = intent.getStringArrayListExtra(Extras.URLS);
        int               selectedIndex = intent.getIntExtra(            Extras.SELECTED_INDEX, -1);



        PhotoViewPager viewPager = (PhotoViewPager)findViewById(R.id.photoViewPager);

        viewPager.setAdapter(new PhotoPagerAdapter(getSupportFragmentManager(), urls));
        viewPager.setCurrentItem(selectedIndex, false);
    }
}
