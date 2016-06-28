package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.entities.ShopEntity;
import ru.okmarket.okgoods.other.ShopFilter;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "ShopsAdapter";



    private Context               mContext             = null;
    private ArrayList<ShopEntity> mOriginalShops       = null;
    private ArrayList<ShopEntity> mShops               = null;
    private ShopEntity            mNearestShop         = null;
    private ShopEntity            mSelectedShop        = null;
    private OnItemClickListener   mOnItemClickListener = null;



    public ShopsAdapter(Context context, ArrayList<ShopEntity> shops)
    {
        mContext       = context;
        mOriginalShops = shops;
        mShops         = new ArrayList<>();
        mNearestShop   = null;
        mSelectedShop  = null;

        filter(null);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final ShopEntity item = mShops.get(position);

        holder.mNameTextView.setText(item.getName());

        if (item == mNearestShop)
        {
            holder.mNearestShopImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.mNearestShopImageView.setVisibility(View.GONE);
        }

        if (mSelectedShop != null && mSelectedShop.equals(item))
        {
            // noinspection deprecation
            holder.mView.setBackgroundColor(mContext.getResources().getColor(R.color.selectedShop));
        }
        else
        {
            // noinspection deprecation
            holder.mView.setBackgroundColor(mContext.getResources().getColor(R.color.windowBackground));
        }

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onShopClicked(holder, item);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mShops.size();
    }

    public ArrayList<ShopEntity> getItems()
    {
        return mShops;
    }

    public void findNearestShop(double latitude, double longitude)
    {
        ShopEntity nearestShop = null;
        double     minDistance = Double.MAX_VALUE;

        for (int i = 0; i < mShops.size(); ++i)
        {
            ShopEntity shop = mShops.get(i);

            double distance = Math.sqrt((latitude - shop.getLatitude()) * (latitude - shop.getLatitude()) + (longitude - shop.getLongitude()) * (longitude - shop.getLongitude()));

            if (distance < minDistance)
            {
                nearestShop = shop;

                minDistance = distance;
            }
        }

        if (mNearestShop != nearestShop)
        {
            if (mNearestShop != null && mShops.size() > 0 && mShops.get(0) == mNearestShop)
            {
                mShops.remove(0);

                int index = 0;

                for (int i = 0; i < mOriginalShops.size() && index < mShops.size(); ++i)
                {
                    if (mOriginalShops.get(i) == mNearestShop)
                    {
                        break;
                    }

                    if (mOriginalShops.get(i) == mShops.get(index))
                    {
                        ++index;
                    }
                }

                mShops.add(index, mNearestShop);
            }

            mNearestShop = nearestShop;

            if (mNearestShop != null)
            {
                mShops.remove(mNearestShop);
                mShops.add(0, mNearestShop);
            }

            notifyDataSetChanged();
        }
    }

    public void filter(ShopFilter filter)
    {
        mShops.clear();

        for (int i = 0; i < mOriginalShops.size(); ++i)
        {
            ShopEntity shop = mOriginalShops.get(i);

            if (filter == null || filter.isFiltered(shop))
            {
                mShops.add(shop);
            }
        }

        notifyDataSetChanged();
    }

    public void setSelectedShop(ShopEntity shop)
    {
        mSelectedShop = shop;

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mOnItemClickListener = listener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public View      mView;
        public TextView  mNameTextView;
        public ImageView mNearestShopImageView;



        public ViewHolder(View view)
        {
            super(view);

            mView                 = view;
            mNameTextView         = (TextView) view.findViewById(R.id.nameTextView);
            mNearestShopImageView = (ImageView)view.findViewById(R.id.nearestShopImageView);
        }
    }



    public interface OnItemClickListener
    {
        void onShopClicked(ViewHolder viewHolder, ShopEntity shop);
    }
}
