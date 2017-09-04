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
import ru.okmarket.okgoods.db.entities.SelectedGoodEntity;
import ru.okmarket.okgoods.widgets.GlowView;

public final class SelectedGoodsAdapter extends RecyclerView.Adapter<SelectedGoodsAdapter.SelectedGoodViewHolder>
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "SelectedGoodsAdapter";
    // endregion
    // endregion



    // region Attributes
    private Context                       mContext                  = null;
    private SQLiteDatabase                mDB                       = null;
    private ArrayList<SelectedGoodEntity> mItems                    = null;
    private OnItemClickListener           mOnItemClickListener      = null;
    private OnBindViewHolderListener      mOnBindViewHolderListener = null;
    // endregion



    @Override
    public String toString()
    {
        return "SelectedGoodsAdapter{" +
                "mContext="                    + mContext                  +
                ", mDB="                       + mDB                       +
                ", mItems="                    + mItems                    +
                ", mOnItemClickListener="      + mOnItemClickListener      +
                ", mOnBindViewHolderListener=" + mOnBindViewHolderListener +
                '}';
    }

    private SelectedGoodsAdapter(Context context, SQLiteDatabase db)
    {
        mContext                  = context;
        mDB                       = db;
        mItems                    = new ArrayList<>(0);
        mOnItemClickListener      = null;
        mOnBindViewHolderListener = null;

        updateFromDatabase();
    }

    public static SelectedGoodsAdapter newInstance(Context context, SQLiteDatabase db)
    {
        return new SelectedGoodsAdapter(context, db);
    }

    @Override
    public SelectedGoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_selected_good, parent, false);

        return SelectedGoodViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(final SelectedGoodViewHolder holder, int position)
    {
        final SelectedGoodEntity item = mItems.get(position);

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
            holder.getCostTextView().setText(mContext.getString(R.string.main_rub_currency_count, item.getCost(), item.getCount()));
        }
        else
        {
            holder.getCostTextView().setVisibility(View.GONE);
            holder.getCostTextView().setText("");
        }

        holder.getExpandedView().setVisibility(View.GONE);

        holder.getGlowView().setOnClickListener(new View.OnClickListener()
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

    @SuppressWarnings("unused")
    public ArrayList<SelectedGoodEntity> getItems()
    {
        return mItems;
    }

    @SuppressWarnings("unused")
    public void setItems(ArrayList<SelectedGoodEntity> items)
    {
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        mItems = items;

        notifyDataSetChanged();
    }

    public void updateFromDatabase()
    {
        mItems = MainDatabase.getSelectedGoods(mDB, MainDatabase.LIMIT_UNLIMITED);

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
    public static final class SelectedGoodViewHolder extends RecyclerView.ViewHolder
    {
        // region Attributes
        private GlowView mGlowView           = null;
        private TextView mGoodNameTextView   = null;
        private TextView mCostTextView       = null;
        private View     mExpandedView       = null;
        private TextView mSecondCostTextView = null;
        // endregion



        @Override
        public String toString()
        {
            return "SelectedGoodViewHolder{" +
                    "mGlowView="             + mGlowView           +
                    ", mGoodNameTextView="   + mGoodNameTextView   +
                    ", mCostTextView="       + mCostTextView       +
                    ", mExpandedView="       + mExpandedView       +
                    ", mSecondCostTextView=" + mSecondCostTextView +
                    '}';
        }

        @SuppressWarnings("RedundantCast")
        private SelectedGoodViewHolder(View view)
        {
            super(view);

            mGlowView           = (GlowView)view.findViewById(R.id.glowView);
            mGoodNameTextView   = (TextView)view.findViewById(R.id.goodNameTextView);
            mCostTextView       = (TextView)view.findViewById(R.id.costTextView);
            mExpandedView       =           view.findViewById(R.id.expandedView);
            mSecondCostTextView = (TextView)view.findViewById(R.id.secondCostTextView);
        }

        public static SelectedGoodViewHolder newInstance(View view)
        {
            return new SelectedGoodViewHolder(view);
        }

        public GlowView getGlowView()
        {
            return mGlowView;
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
        void onSelectedGoodClicked(SelectedGoodViewHolder holder, SelectedGoodEntity good);
    }

    @SuppressWarnings("PublicInnerClass")
    public interface OnBindViewHolderListener
    {
        void onSelectedGoodBindViewHolder(SelectedGoodViewHolder holder, SelectedGoodEntity good);
    }
}
