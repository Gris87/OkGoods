package ru.okmarket.okgoods.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.okmarket.okgoods.R;

public class ShopMapFragment extends Fragment
{
    private OnFragmentInteractionListener mListener = null;



    public ShopMapFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_shop_map, container, false);



        onFragmentCreated();

        return rootView;
    }

    public void onFragmentCreated()
    {
        if (mListener != null)
        {
            mListener.onShopMapFragmentCreated(this);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener)context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        mListener = null;
    }



    public interface OnFragmentInteractionListener
    {
        void onShopMapFragmentCreated(ShopMapFragment fragment);
    }
}
