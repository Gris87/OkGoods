package ru.okmarket.okgoods.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.HistoryDetailsAdapter;
import ru.okmarket.okgoods.other.HistoryDetailsInfo;
import ru.okmarket.okgoods.util.AnimationUtils;
import ru.okmarket.okgoods.widgets.DividerItemDecoration;

public class HistoryDetailsFragment extends Fragment implements HistoryDetailsAdapter.OnItemClickListener, HistoryDetailsAdapter.OnBindViewHolderListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryDetailsFragment";



    private static final float EXPAND_ANIMATION_SPEED  = 0.5f;
    private static final int   FADE_ANIMATION_DURATION = 150;



    private HistoryDetailsAdapter            mAdapter                    = null;
    private HistoryDetailsAdapter.ViewHolder mSelectedViewHolder         = null;
    private HistoryDetailsInfo               mSelectedHistoryDetailsInfo = null;



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
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnBindViewHolderListener(this);

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

    @Override
    public void onHistoryDetailsClicked(HistoryDetailsAdapter.ViewHolder viewHolder, HistoryDetailsInfo details)
    {
        if (mSelectedHistoryDetailsInfo == details)
        {
            selectHistoryDetails(null, null);
        }
        else
        {
            selectHistoryDetails(viewHolder, details);
        }
    }

    @Override
    public void onHistoryDetailsBindViewHolder(HistoryDetailsAdapter.ViewHolder viewHolder, HistoryDetailsInfo details)
    {
        if (mSelectedViewHolder == viewHolder)
        {
            mSelectedViewHolder = null;
        }

        if (mSelectedHistoryDetailsInfo == details)
        {
            mSelectedViewHolder = viewHolder;

            expandSelectedViewHolder(true);
        }
    }

    private void selectHistoryDetails(HistoryDetailsAdapter.ViewHolder viewHolder, HistoryDetailsInfo details)
    {
        if (mSelectedViewHolder != null)
        {
            collapseSelectedViewHolder();
        }

        mSelectedViewHolder         = viewHolder;
        mSelectedHistoryDetailsInfo = details;

        if (mSelectedViewHolder != null)
        {
            expandSelectedViewHolder(false);
        }
    }

    private void expandSelectedViewHolder(boolean immediately)
    {
        if (immediately)
        {
            mSelectedViewHolder.mExpandedView.setVisibility(View.VISIBLE);
            mSelectedViewHolder.mCostTextView.setVisibility(View.GONE);

            mSelectedViewHolder.mExpandedView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        else
        {
            AnimationUtils.expand(mSelectedViewHolder.mExpandedView,  EXPAND_ANIMATION_SPEED);
            AnimationUtils.fadeOut(mSelectedViewHolder.mCostTextView, FADE_ANIMATION_DURATION);
        }

        mSelectedViewHolder.mGoodNameTextView.setHorizontallyScrolling(true);
        mSelectedViewHolder.mGoodNameTextView.setHorizontalFadingEdgeEnabled(true);
        mSelectedViewHolder.mGoodNameTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mSelectedViewHolder.mGoodNameTextView.setSelected(true);

        if (!TextUtils.isEmpty(mSelectedViewHolder.mCostTextView.getText()))
        {
            mSelectedViewHolder.mSecondCostTextView.setText(mSelectedViewHolder.mCostTextView.getText());
        }
        else
        {
            if (mSelectedHistoryDetailsInfo.isOwn())
            {
                if (mSelectedHistoryDetailsInfo.getGoodId() > 0)
                {
                    mSelectedViewHolder.mSecondCostTextView.setText(R.string.own_good);
                }
                else
                {
                    mSelectedViewHolder.mSecondCostTextView.setText(R.string.own_category);
                }
            }
            else
            {
                mSelectedViewHolder.mSecondCostTextView.setText(R.string.category);
            }
        }
    }

    private void collapseSelectedViewHolder()
    {
        AnimationUtils.collapse(mSelectedViewHolder.mExpandedView, EXPAND_ANIMATION_SPEED);
        AnimationUtils.fadeIn(mSelectedViewHolder.mCostTextView,   FADE_ANIMATION_DURATION);

        mSelectedViewHolder.mGoodNameTextView.setHorizontallyScrolling(false);
        mSelectedViewHolder.mGoodNameTextView.setHorizontalFadingEdgeEnabled(false);
        mSelectedViewHolder.mGoodNameTextView.setEllipsize(TextUtils.TruncateAt.END);
        mSelectedViewHolder.mGoodNameTextView.setSelected(false);
    }
}
