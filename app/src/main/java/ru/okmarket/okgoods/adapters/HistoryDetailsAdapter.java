package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.other.HistoryDetailsInfo;

public class HistoryDetailsAdapter extends RecyclerView.Adapter<HistoryDetailsAdapter.ViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryDetailsAdapter";



    private Context                       mContext             = null;
    private ArrayList<HistoryDetailsInfo> mItems               = null;
    private OnItemClickListener           mOnItemClickListener = null;



    public HistoryDetailsAdapter(Context context)
    {
        mContext             = context;
        mItems               = new ArrayList<>();
        mOnItemClickListener = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history_details, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final HistoryDetailsInfo item = mItems.get(position);

        holder.mGoodNameTextView.setText(item.getName());

        if (item.getGoodId() > 0 && item.getCost() > 0 && item.getCount() > 0)
        {
            holder.mCostTextView.setVisibility(View.VISIBLE);
            holder.mCostTextView.setText(mContext.getString(R.string.rub_currency_count, item.getCost(), item.getCount()));
        }
        else
        {
            holder.mCostTextView.setVisibility(View.GONE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onHistoryDetailsClicked(holder, item);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    public void setItems(ArrayList<HistoryDetailsInfo> items)
    {
        mItems = items;

        notifyDataSetChanged();
    }

    public ArrayList<HistoryDetailsInfo> getItems()
    {
        return mItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mOnItemClickListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public View     mView;
        public TextView mGoodNameTextView;
        public TextView mCostTextView;



        public ViewHolder(View view)
        {
            super(view);

            mView             = view;
            mGoodNameTextView = (TextView)view.findViewById(R.id.goodNameTextView);
            mCostTextView     = (TextView)view.findViewById(R.id.costTextView);
        }
    }



    public interface OnItemClickListener
    {
        void onHistoryDetailsClicked(ViewHolder viewHolder, HistoryDetailsInfo details);
    }
}
