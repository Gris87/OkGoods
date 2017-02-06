package ru.okmarket.okgoods.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.PhotoPagerAdapter;
import ru.okmarket.okgoods.other.ApplicationExtras;
import ru.okmarket.okgoods.widgets.PhotoViewPager;

@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
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

        ArrayList<String> urls          = intent.getStringArrayListExtra(ApplicationExtras.URLS);
        int               selectedIndex = intent.getIntExtra(            ApplicationExtras.SELECTED_INDEX, -1);



        PhotoViewPager viewPager = (PhotoViewPager)findViewById(R.id.photoViewPager);

        viewPager.setAdapter(PhotoPagerAdapter.newInstance(getSupportFragmentManager(), urls));
        viewPager.setCurrentItem(selectedIndex, false);
    }
}
