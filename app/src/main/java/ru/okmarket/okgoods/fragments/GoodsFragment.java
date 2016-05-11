package ru.okmarket.okgoods.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.okmarket.okgoods.R;

public class GoodsFragment extends Fragment
{
    private OnFragmentInteractionListener mListener = null;
    private Toolbar                       mToolbar  = null;



    public GoodsFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_goods, container, false);

        mToolbar = (Toolbar)rootView.findViewById(R.id.toolbar);



        onFragmentCreated();

        return rootView;
    }

    public void onFragmentCreated()
    {
        if (mListener != null)
        {
            mListener.onGoodsFragmentCreated(this);
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

    public Toolbar getToolbar()
    {
        return mToolbar;
    }



    public interface OnFragmentInteractionListener
    {
        void onGoodsFragmentCreated(GoodsFragment fragment);
    }
}
