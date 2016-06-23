package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.other.SelectedGoodInfo;

public class SelectedGoodAdapter extends RecyclerView.Adapter<SelectedGoodAdapter.ViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "SelectedGoodAdapter";



    private Context                     mContext                  = null;
    private MainDatabase                mMainDatabase             = null;
    private SQLiteDatabase              mDB                       = null;
    private ArrayList<SelectedGoodInfo> mItems                    = null;
    private OnItemClickListener         mOnItemClickListener      = null;
    private OnBindViewHolderListener    mOnBindViewHolderListener = null;



    public SelectedGoodAdapter(Context context, MainDatabase mainDatabase, SQLiteDatabase db)
    {
        mContext                  = context;
        mMainDatabase             = mainDatabase;
        mDB                       = db;
        mItems                    = new ArrayList<>();
        mOnItemClickListener      = null;
        mOnBindViewHolderListener = null;

        updateFromDatabase();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_selected_good, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final SelectedGoodInfo item = mItems.get(position);

        holder.mGoodNameTextView.setText(item.getName());
        holder.mGoodNameTextView.setHorizontallyScrolling(false);
        holder.mGoodNameTextView.setHorizontalFadingEdgeEnabled(false);
        holder.mGoodNameTextView.setEllipsize(TextUtils.TruncateAt.END);
        holder.mGoodNameTextView.setMarqueeRepeatLimit(-1);
        holder.mGoodNameTextView.setSelected(false);

        if (item.getGoodId() > 0 && item.getCost() > 0 && item.getCount() > 0)
        {
            holder.mCostTextView.setVisibility(View.VISIBLE);
            holder.mCostTextView.setAlpha(1);
            holder.mCostTextView.setText(mContext.getString(R.string.rub_currency_count, item.getCost(), item.getCount()));
        }
        else
        {
            holder.mCostTextView.setVisibility(View.GONE);
        }

        holder.mExpandedView.setVisibility(View.GONE);

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onSelectedGoodClicked(holder, item);
                }
            }
        });

        if (mOnBindViewHolderListener != null)
        {
            mOnBindViewHolderListener.onSelectedGoodBindViewHolder(holder, item);
        }
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    public void updateFromDatabase()
    {
        mItems = mMainDatabase.getSelectedGoods(mDB);

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



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public View     mView;
        public TextView mGoodNameTextView;
        public TextView mCostTextView;
        public View     mExpandedView;
        public TextView mSecondCostTextView;



        public ViewHolder(View view)
        {
            super(view);

            mView               = view;
            mGoodNameTextView   = (TextView)view.findViewById(R.id.goodNameTextView);
            mCostTextView       = (TextView)view.findViewById(R.id.costTextView);
            mExpandedView       =           view.findViewById(R.id.expandedView);
            mSecondCostTextView = (TextView)view.findViewById(R.id.secondCostTextView);
        }
    }



    public interface OnItemClickListener
    {
        void onSelectedGoodClicked(ViewHolder viewHolder, SelectedGoodInfo good);
    }

    public interface OnBindViewHolderListener
    {
        void onSelectedGoodBindViewHolder(ViewHolder viewHolder, SelectedGoodInfo good);
    }
}
