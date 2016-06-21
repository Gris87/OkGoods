package ru.okmarket.okgoods.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.HistoryDetailsAdapter;
import ru.okmarket.okgoods.other.HistoryDetailsInfo;
import ru.okmarket.okgoods.widgets.DividerItemDecoration;

public class HistoryDetailsFragment extends Fragment
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryDetailsFragment";



    private HistoryDetailsAdapter mAdapter = null;



    public HistoryDetailsFragment()
    {
        // Nothing
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_history_details, container, false);



        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.historyDetailsRecyclerView);

        assert recyclerView != null;



        mAdapter = new HistoryDetailsAdapter(getActivity());

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public void setHistoryDetails(ArrayList<HistoryDetailsInfo> details)
    {
        mAdapter.setItems(details);
    }

    public ArrayList<HistoryDetailsInfo> getHistoryDetails()
    {
        return mAdapter.getItems();
    }
}
