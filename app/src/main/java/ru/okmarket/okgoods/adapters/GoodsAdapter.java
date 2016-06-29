package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.entities.GoodEntity;
import ru.okmarket.okgoods.db.entities.GoodsCategoryEntity;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsAdapter";



    private Context                        mContext                 = null;
    private ArrayList<GoodsCategoryEntity> mCategories              = null;
    private ArrayList<GoodEntity>          mGoods                   = null;
    private OnCategoryClickListener        mOnCategoryClickListener = null;
    private OnGoodClickListener            mOnGoodClickListener     = null;



    public GoodsAdapter(Context context, ArrayList<GoodsCategoryEntity> categories, ArrayList<GoodEntity> goods)
    {
        mContext             = context;
        mCategories          = categories;
        mGoods               = goods;
        mOnGoodClickListener = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_good, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        if (position < mCategories.size())
        {
            final GoodsCategoryEntity item = mCategories.get(position);

            holder.mNameTextView.setText(item.getName());

            holder.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (mOnGoodClickListener != null)
                    {
                        mOnCategoryClickListener.onCategoryClicked(holder, item);
                    }
                }
            });
        }
        else
        {
            final GoodEntity item = mGoods.get(position - mCategories.size());

            holder.mNameTextView.setText(item.getName());

            holder.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (mOnGoodClickListener != null)
                    {
                        mOnGoodClickListener.onGoodClicked(holder, item);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return mCategories.size() + mGoods.size();
    }

    public ArrayList<GoodsCategoryEntity> getCategories()
    {
        return mCategories;
    }

    public ArrayList<GoodEntity> getGoods()
    {
        return mGoods;
    }

    public void setItems(ArrayList<GoodsCategoryEntity> categories, ArrayList<GoodEntity> goods)
    {
        mCategories = categories;
        mGoods      = goods;

        notifyDataSetChanged();
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener)
    {
        mOnCategoryClickListener = listener;
    }

    public void setOnGoodClickListener(OnGoodClickListener listener)
    {
        mOnGoodClickListener = listener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public View     mView;
        public TextView mNameTextView;



        public ViewHolder(View view)
        {
            super(view);

            mView         = view;
            mNameTextView = (TextView)view.findViewById(R.id.nameTextView);
        }
    }



    public interface OnCategoryClickListener
    {
        void onCategoryClicked(ViewHolder viewHolder, GoodsCategoryEntity category);
    }

    public interface OnGoodClickListener
    {
        void onGoodClicked(ViewHolder viewHolder, GoodEntity good);
    }
}
