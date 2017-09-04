package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.entities.HistoryEntity;
import ru.okmarket.okgoods.widgets.GlowView;

public final class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryAdapter";
    // endregion
    // endregion



    // region Attributes
    private Context                  mContext             = null;
    private ArrayList<HistoryEntity> mItems               = null;
    private OnItemClickListener      mOnItemClickListener = null;
    // endregion



    @Override
    public String toString()
    {
        return "HistoryAdapter{" +
                "mContext="               + mContext             +
                ", mItems="               + mItems               +
                ", mOnItemClickListener=" + mOnItemClickListener +
                '}';
    }

    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    private HistoryAdapter(Context context, ArrayList<HistoryEntity> items)
    {
        mContext             = context;
        mItems               = items;
        mOnItemClickListener = null;
    }

    public static HistoryAdapter newInstance(Context context, ArrayList<HistoryEntity> items)
    {
        return new HistoryAdapter(context, items);
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);

        return HistoryViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, int position)
    {
        final HistoryEntity item = mItems.get(position);

        holder.getShopNameTextView().setText(item.getShopName());
        holder.getDateTextView().setText(    item.getDate());
        holder.getDurationTextView().setText(mContext.getString(R.string.history_time,         item.getDurationString()));
        holder.getTotalTextView().setText(   mContext.getString(R.string.history_rub_currency, item.getTotal()));

        holder.getGlowView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onHistoryClicked(holder, item);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    @SuppressWarnings("unused")
    public ArrayList<HistoryEntity> getItems()
    {
        return mItems;
    }

    @SuppressWarnings("unused")
    public void setItems(ArrayList<HistoryEntity> items)
    {
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        mItems = items;

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mOnItemClickListener = listener;
    }



    @SuppressWarnings({"PublicInnerClass", "WeakerAccess"})
    public static final class HistoryViewHolder extends RecyclerView.ViewHolder
    {
        // region Attributes
        private GlowView mGlowView         = null;
        private TextView mShopNameTextView = null;
        private TextView mDateTextView     = null;
        private TextView mDurationTextView = null;
        private TextView mTotalTextView    = null;
        // endregion



        @Override
        public String toString()
        {
            return "HistoryViewHolder{" +
                    "mGlowView="           + mGlowView         +
                    ", mShopNameTextView=" + mShopNameTextView +
                    ", mDateTextView="     + mDateTextView     +
                    ", mDurationTextView=" + mDurationTextView +
                    ", mTotalTextView="    + mTotalTextView    +
                    '}';
        }

        @SuppressWarnings("RedundantCast")
        private HistoryViewHolder(View view)
        {
            super(view);

            mGlowView         = (GlowView)view.findViewById(R.id.glowView);
            mShopNameTextView = (TextView)view.findViewById(R.id.shopNameTextView);
            mDateTextView     = (TextView)view.findViewById(R.id.dateTextView);
            mDurationTextView = (TextView)view.findViewById(R.id.durationTextView);
            mTotalTextView    = (TextView)view.findViewById(R.id.totalTextView);
        }

        public static HistoryViewHolder newInstance(View view)
        {
            return new HistoryViewHolder(view);
        }

        public GlowView getGlowView()
        {
            return mGlowView;
        }

        public TextView getShopNameTextView()
        {
            return mShopNameTextView;
        }

        public TextView getDateTextView()
        {
            return mDateTextView;
        }

        public TextView getDurationTextView()
        {
            return mDurationTextView;
        }

        public TextView getTotalTextView()
        {
            return mTotalTextView;
        }
    }



    @SuppressWarnings("PublicInnerClass")
    public interface OnItemClickListener
    {
        void onHistoryClicked(HistoryViewHolder holder, HistoryEntity history);
    }
}
