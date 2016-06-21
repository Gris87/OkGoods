package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.other.HistoryInfo;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryAdapter";



    private Context                mContext             = null;
    private ArrayList<HistoryInfo> mItems               = null;
    private OnItemClickListener    mOnItemClickListener = null;



    public HistoryAdapter(Context context, ArrayList<HistoryInfo> items)
    {
        mContext             = context;
        mItems               = items;
        mOnItemClickListener = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final HistoryInfo item = mItems.get(position);

        holder.mShopNameTextView.setText(item.getShopName());
        holder.mDateTextView.setText(    item.getDate());
        holder.mDurationTextView.setText(mContext.getString(R.string.time,         item.getDurationString()));
        holder.mTotalTextView.setText(   mContext.getString(R.string.rub_currency, item.getTotal()));

        holder.mView.setOnClickListener(new View.OnClickListener()
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

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mOnItemClickListener = listener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public View     mView;
        public TextView mShopNameTextView;
        public TextView mDateTextView;
        public TextView mDurationTextView;
        public TextView mTotalTextView;



        public ViewHolder(View view)
        {
            super(view);

            mView             = view;
            mShopNameTextView = (TextView)view.findViewById(R.id.shopNameTextView);
            mDateTextView     = (TextView)view.findViewById(R.id.dateTextView);
            mDurationTextView = (TextView)view.findViewById(R.id.durationTextView);
            mTotalTextView    = (TextView)view.findViewById(R.id.totalTextView);
        }
    }



    public interface OnItemClickListener
    {
        void onHistoryClicked(ViewHolder viewHolder, HistoryInfo history);
    }
}
