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

public final class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryAdapter";



    private Context                  mContext             = null;
    private ArrayList<HistoryEntity> mItems               = null;
    private OnItemClickListener      mOnItemClickListener = null;



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

        holder.getView().setOnClickListener(new View.OnClickListener()
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
        private View     mView             = null;
        private TextView mShopNameTextView = null;
        private TextView mDateTextView     = null;
        private TextView mDurationTextView = null;
        private TextView mTotalTextView    = null;



        @Override
        public String toString()
        {
            return "HistoryViewHolder{" +
                    "mView="               + mView             +
                    ", mShopNameTextView=" + mShopNameTextView +
                    ", mDateTextView="     + mDateTextView     +
                    ", mDurationTextView=" + mDurationTextView +
                    ", mTotalTextView="    + mTotalTextView    +
                    '}';
        }

        private HistoryViewHolder(View view)
        {
            super(view);

            mView             = view;
            mShopNameTextView = (TextView)view.findViewById(R.id.shopNameTextView);
            mDateTextView     = (TextView)view.findViewById(R.id.dateTextView);
            mDurationTextView = (TextView)view.findViewById(R.id.durationTextView);
            mTotalTextView    = (TextView)view.findViewById(R.id.totalTextView);
        }

        public static HistoryViewHolder newInstance(View view)
        {
            return new HistoryViewHolder(view);
        }

        public View getView()
        {
            return mView;
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
