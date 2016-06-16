package ru.okmarket.okgoods.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.other.HistoryDetailsInfo;

public class HistoryDetailsFragment extends Fragment
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryDetailsFragment";



    public HistoryDetailsFragment()
    {
        // Nothing
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_history_details, container, false);



        return rootView;
    }

    public void setHistoryDetails(ArrayList<HistoryDetailsInfo> details)
    {

    }
}
