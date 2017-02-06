package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.db.entities.HistoryDetailsEntity;

public final class HistoryDetailsAdapter extends RecyclerView.Adapter<HistoryDetailsAdapter.HistoryDetailsViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryDetailsAdapter";



    private Context                         mContext                  = null;
    private ArrayList<HistoryDetailsEntity> mItems                    = null;
    private OnItemClickListener             mOnItemClickListener      = null;
    private OnBindViewHolderListener        mOnBindViewHolderListener = null;



    @Override
    public String toString()
    {
        return "HistoryDetailsAdapter{" +
                "mContext="                    + mContext                  +
                ", mItems="                    + mItems                    +
                ", mOnItemClickListener="      + mOnItemClickListener      +
                ", mOnBindViewHolderListener=" + mOnBindViewHolderListener +
                '}';
    }

    private HistoryDetailsAdapter(Context context)
    {
        mContext                  = context;
        mItems                    = new ArrayList<>(0);
        mOnItemClickListener      = null;
        mOnBindViewHolderListener = null;
    }

    public static HistoryDetailsAdapter newInstance(Context context)
    {
        return new HistoryDetailsAdapter(context);
    }

    @Override
    public HistoryDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history_details, parent, false);

        return HistoryDetailsViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(final HistoryDetailsViewHolder holder, int position)
    {
        final HistoryDetailsEntity item = mItems.get(position);

        holder.getGoodNameTextView().setText(item.getName());
        holder.getGoodNameTextView().setHorizontallyScrolling(false);
        holder.getGoodNameTextView().setHorizontalFadingEdgeEnabled(false);
        holder.getGoodNameTextView().setEllipsize(TextUtils.TruncateAt.END);
        holder.getGoodNameTextView().setMarqueeRepeatLimit(-1);
        holder.getGoodNameTextView().setSelected(false);

        if (item.getGoodId() != MainDatabase.SPECIAL_ID_ROOT && item.getCost() > 0 && item.getCount() > 0)
        {
            holder.getCostTextView().setVisibility(View.VISIBLE);
            holder.getCostTextView().setAlpha(1);
            holder.getCostTextView().setText(mContext.getString(R.string.history_details_rub_currency_count, item.getCost(), item.getCount()));
        }
        else
        {
            holder.getCostTextView().setVisibility(View.GONE);
            holder.getCostTextView().setText("");
        }

        holder.getExpandedView().setVisibility(View.GONE);

        holder.getView().setOnClickListener(new View.OnClickListener()
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

        if (mOnBindViewHolderListener != null)
        {
            mOnBindViewHolderListener.onHistoryDetailsBindViewHolder(holder, item);
        }
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    public ArrayList<HistoryDetailsEntity> getItems()
    {
        return mItems;
    }

    public void setItems(ArrayList<HistoryDetailsEntity> items)
    {
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        mItems = items;

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mOnItemClickListener = listener;
    }

    public void setOnBindViewHolderListener(OnBindViewHolderListener listener)
    {
        mOnBindViewHolderListener = listener;
    }



    @SuppressWarnings({"PublicInnerClass", "WeakerAccess"})
    public static final class HistoryDetailsViewHolder extends RecyclerView.ViewHolder
    {
        private View     mView               = null;
        private TextView mGoodNameTextView   = null;
        private TextView mCostTextView       = null;
        private View     mExpandedView       = null;
        private TextView mSecondCostTextView = null;



        @Override
        public String toString()
        {
            return "HistoryDetailsViewHolder{" +
                    "mView="                 + mView               +
                    ", mGoodNameTextView="   + mGoodNameTextView   +
                    ", mCostTextView="       + mCostTextView       +
                    ", mExpandedView="       + mExpandedView       +
                    ", mSecondCostTextView=" + mSecondCostTextView +
                    '}';
        }

        private HistoryDetailsViewHolder(View view)
        {
            super(view);

            mView               = view;
            mGoodNameTextView   = (TextView)view.findViewById(R.id.goodNameTextView);
            mCostTextView       = (TextView)view.findViewById(R.id.costTextView);
            mExpandedView       =           view.findViewById(R.id.expandedView);
            mSecondCostTextView = (TextView)view.findViewById(R.id.secondCostTextView);
        }

        public static HistoryDetailsViewHolder newInstance(View view)
        {
            return new HistoryDetailsViewHolder(view);
        }

        public View getView()
        {
            return mView;
        }

        public TextView getGoodNameTextView()
        {
            return mGoodNameTextView;
        }

        public TextView getCostTextView()
        {
            return mCostTextView;
        }

        public View getExpandedView()
        {
            return mExpandedView;
        }

        @SuppressWarnings("unused")
        public TextView getSecondCostTextView()
        {
            return mSecondCostTextView;
        }
    }



    @SuppressWarnings("PublicInnerClass")
    public interface OnItemClickListener
    {
        void onHistoryDetailsClicked(HistoryDetailsViewHolder holder, HistoryDetailsEntity details);
    }

    @SuppressWarnings("PublicInnerClass")
    public interface OnBindViewHolderListener
    {
        void onHistoryDetailsBindViewHolder(HistoryDetailsViewHolder holder, HistoryDetailsEntity details);
    }
}
