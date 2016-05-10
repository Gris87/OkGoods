package ru.okmarket.okgoods.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.okmarket.okgoods.R;

public class ShopMapFragment extends Fragment
{
    public ShopMapFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_shop_map, container, false);
    }
}
