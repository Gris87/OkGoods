package ru.okmarket.okgoods.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.okmarket.okgoods.R;

public class GoodsFragment extends Fragment
{
    private Toolbar mToolbar = null;



    public GoodsFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_goods, container, false);

        mToolbar = (Toolbar)rootView.findViewById(R.id.toolbar);

        return rootView;
    }

    public Toolbar getToolbar()
    {
        return mToolbar;
    }
}
