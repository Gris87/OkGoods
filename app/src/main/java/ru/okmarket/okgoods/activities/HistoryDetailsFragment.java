package ru.okmarket.okgoods.activities;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.activities.dummy.DummyContent;

public class HistoryDetailsFragment extends Fragment
{
    public static final String ARG_ITEM_ID = "item_id";



    private DummyContent.DummyItem mItem;



    public HistoryDetailsFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID))
        {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_history_details, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null)
        {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
        }

        return rootView;
    }
}
