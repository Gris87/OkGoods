package ru.okmarket.okgoods.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.okmarket.okgoods.R;

public class GoodsCatalogActivity extends AppCompatActivity
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsCatalogActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_catalog);
    }
}
