package ru.okmarket.okgoods.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.HistoryDetailsAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.db.entities.HistoryDetailsEntity;
import ru.okmarket.okgoods.util.AnimationUtils;

@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
public class HistoryDetailsFragment extends Fragment implements HistoryDetailsAdapter.OnItemClickListener, HistoryDetailsAdapter.OnBindViewHolderListener
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryDetailsFragment";
    // endregion



    // region Animation
    private static final float EXPAND_ANIMATION_SPEED  = 0.5f;
    private static final int   FADE_ANIMATION_DURATION = 150;
    // endregion
    // endregion



    // region Attributes
    private TextView                                       mNoInformationTextView  = null;
    private RecyclerView                                   mRecyclerView           = null;
    private HistoryDetailsAdapter                          mAdapter                = null;
    private TextView                                       mTotalTextView          = null;
    private HistoryDetailsAdapter.HistoryDetailsViewHolder mSelectedViewHolder     = null;
    private HistoryDetailsEntity                           mSelectedHistoryDetails = null;
    private double                                         mTotal                  = 0;
    // endregion



    @Override
    public String toString()
    {
        return "HistoryDetailsFragment{" +
                "mNoInformationTextView="    + mNoInformationTextView  +
                ", mRecyclerView="           + mRecyclerView           +
                ", mAdapter="                + mAdapter                +
                ", mTotalTextView="          + mTotalTextView          +
                ", mSelectedViewHolder="     + mSelectedViewHolder     +
                ", mSelectedHistoryDetails=" + mSelectedHistoryDetails +
                ", mTotal="                  + mTotal                  +
                '}';
    }

    @SuppressWarnings("RedundantCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_history_details, container, false);



        mNoInformationTextView = (TextView)    rootView.findViewById(R.id.noInformationTextView);
        mRecyclerView          = (RecyclerView)rootView.findViewById(R.id.historyDetailsRecyclerView);
        mTotalTextView         = (TextView)    rootView.findViewById(R.id.totalTextView);



        mAdapter = HistoryDetailsAdapter.newInstance(getActivity());
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnBindViewHolderListener(this);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);



        return rootView;
    }

    public void setHistoryDetails(ArrayList<HistoryDetailsEntity> details)
    {
        mAdapter.setItems(details);

        if (!details.isEmpty())
        {
            mNoInformationTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(         View.VISIBLE);
        }
        else
        {
            mNoInformationTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(         View.GONE);
        }
    }

    public ArrayList<HistoryDetailsEntity> getHistoryDetails()
    {
        return mAdapter.getItems();
    }

    public void setSelectedHistoryDetails(HistoryDetailsEntity details)
    {
        mSelectedHistoryDetails = details;
    }

    public HistoryDetailsEntity getSelectedHistoryDetails()
    {
        return mSelectedHistoryDetails;
    }

    public void setTotal(double total)
    {
        mTotal = total;

        mTotalTextView.setText(getString(R.string.history_details_rub_currency, mTotal));
    }

    public double getTotal()
    {
        return mTotal;
    }

    @Override
    public void onHistoryDetailsClicked(HistoryDetailsAdapter.HistoryDetailsViewHolder holder, HistoryDetailsEntity details)
    {
        if (details.equals(mSelectedHistoryDetails))
        {
            selectHistoryDetails(null, null);
        }
        else
        {
            selectHistoryDetails(holder, details);
        }
    }

    @Override
    public void onHistoryDetailsBindViewHolder(HistoryDetailsAdapter.HistoryDetailsViewHolder holder, HistoryDetailsEntity details)
    {
        if (mSelectedViewHolder == holder)
        {
            mSelectedViewHolder = null;
        }

        if (details.equals(mSelectedHistoryDetails))
        {
            mSelectedViewHolder = holder;

            expandSelectedViewHolder(true);
        }
    }

    private void selectHistoryDetails(HistoryDetailsAdapter.HistoryDetailsViewHolder holder, HistoryDetailsEntity details)
    {
        //noinspection VariableNotUsedInsideIf
        if (mSelectedViewHolder != null)
        {
            collapseSelectedViewHolder();
        }

        mSelectedViewHolder     = holder;
        mSelectedHistoryDetails = details;

        //noinspection VariableNotUsedInsideIf
        if (mSelectedViewHolder != null)
        {
            expandSelectedViewHolder(false);
        }
    }

    private void expandSelectedViewHolder(boolean immediately)
    {
        if (immediately)
        {
            mSelectedViewHolder.getExpandedView().setVisibility(View.VISIBLE);
            mSelectedViewHolder.getCostTextView().setVisibility(View.GONE);

            mSelectedViewHolder.getExpandedView().getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        else
        {
            AnimationUtils.expand(mSelectedViewHolder.getExpandedView(),  EXPAND_ANIMATION_SPEED);
            AnimationUtils.fadeOut(mSelectedViewHolder.getCostTextView(), FADE_ANIMATION_DURATION);
        }



        mSelectedViewHolder.getGoodNameTextView().setHorizontallyScrolling(true);
        mSelectedViewHolder.getGoodNameTextView().setHorizontalFadingEdgeEnabled(true);
        mSelectedViewHolder.getGoodNameTextView().setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mSelectedViewHolder.getGoodNameTextView().setSelected(true);



        if (!TextUtils.isEmpty(mSelectedViewHolder.getCostTextView().getText()))
        {
            mSelectedViewHolder.getSecondCostTextView().setText(mSelectedViewHolder.getCostTextView().getText());
        }
        else
        {
            if (mSelectedHistoryDetails.isOwn())
            {
                if (mSelectedHistoryDetails.getGoodId() != MainDatabase.SPECIAL_ID_ROOT)
                {
                    mSelectedViewHolder.getSecondCostTextView().setText(R.string.history_details_own_good);
                }
                else
                {
                    mSelectedViewHolder.getSecondCostTextView().setText(R.string.history_details_own_category);
                }
            }
            else
            {
                mSelectedViewHolder.getSecondCostTextView().setText(R.string.history_details_category);
            }
        }
    }

    private void collapseSelectedViewHolder()
    {
        AnimationUtils.collapse(mSelectedViewHolder.getExpandedView(), EXPAND_ANIMATION_SPEED);
        AnimationUtils.fadeIn(mSelectedViewHolder.getCostTextView(),   FADE_ANIMATION_DURATION);



        mSelectedViewHolder.getGoodNameTextView().setHorizontallyScrolling(false);
        mSelectedViewHolder.getGoodNameTextView().setHorizontalFadingEdgeEnabled(false);
        mSelectedViewHolder.getGoodNameTextView().setEllipsize(TextUtils.TruncateAt.END);
        mSelectedViewHolder.getGoodNameTextView().setSelected(false);
    }
}
