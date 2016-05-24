package ru.okmarket.okgoods.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.okmarket.okgoods.R;
import ru.yandex.yandexmapkit.MapView;

public class SelectShopActivity extends AppCompatActivity
{
    private static final String TAG = "SelectShopActivity";



    private MapView mMapView;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop);

        mMapView = (MapView)findViewById(R.id.map);

        mMapView.showJamsButton(true);
    }
}
