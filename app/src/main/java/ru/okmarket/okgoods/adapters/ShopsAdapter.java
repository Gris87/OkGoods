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

public final class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ShopViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "ShopsAdapter";



    private Context               mContext             = null;
    private ArrayList<ShopEntity> mOriginalShops       = null;
    private ArrayList<ShopEntity> mShops               = null;
    private ShopEntity            mNearestShop         = null;
    private ShopEntity            mSelectedShop        = null;
    private OnItemClickListener   mOnItemClickListener = null;



    @Override
    public String toString()
    {
        return "ShopsAdapter{" +
                "mContext="               + mContext             +
                ", mOriginalShops="       + mOriginalShops       +
                ", mShops="               + mShops               +
                ", mNearestShop="         + mNearestShop         +
                ", mSelectedShop="        + mSelectedShop        +
                ", mOnItemClickListener=" + mOnItemClickListener +
                '}';
    }

    private ShopsAdapter(Context context, ArrayList<ShopEntity> shops)
    {
        mContext             = context;
        mOriginalShops       = shops;
        mShops               = new ArrayList<>(0);
        mNearestShop         = null;
        mSelectedShop        = null;
        mOnItemClickListener = null;

        filter(null);
    }

    public static ShopsAdapter newInstance(Context context, ArrayList<ShopEntity> shops)
    {
        return new ShopsAdapter(context, shops);
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop, parent, false);

        return ShopViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(final ShopViewHolder holder, int position)
    {
        final ShopEntity item = mShops.get(position);

        holder.getNameTextView().setText(item.getName());

        if (item == mNearestShop)
        {
            holder.getNearestShopImageView().setVisibility(View.VISIBLE);
        }
        else
        {
            holder.getNearestShopImageView().setVisibility(View.GONE);
        }

        if (mSelectedShop != null && mSelectedShop.equals(item))
        {
            // noinspection deprecation
            holder.getView().setBackgroundColor(mContext.getResources().getColor(R.color.selectedShop));
        }
        else
        {
            // noinspection deprecation
            holder.getView().setBackgroundColor(mContext.getResources().getColor(R.color.windowBackground));
        }

        holder.getView().setOnClickListener(new View.OnClickListener()
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

    @SuppressWarnings("unused")
    public ArrayList<ShopEntity> getItems()
    {
        return mShops;
    }

    @SuppressWarnings("unused")
    public void setItems(ArrayList<ShopEntity> items)
    {
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        mOriginalShops = items;

        filter(null);
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

    public void findNearestShop(double latitude, double longitude)
    {
        ShopEntity nearestShop = null;
        double     minDistance = Double.MAX_VALUE;

        for (int i = 0; i < mShops.size(); ++i)
        {
            ShopEntity shop = mShops.get(i);

            double dx = latitude  - shop.getLatitude();
            double dy = longitude - shop.getLongitude();

            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < minDistance)
            {
                nearestShop = shop;

                minDistance = distance;
            }
        }

        if (mNearestShop != nearestShop)
        {
            if (mNearestShop != null && !mShops.isEmpty() && mShops.get(0) == mNearestShop)
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



    @SuppressWarnings({"PublicInnerClass", "WeakerAccess"})
    public static final class ShopViewHolder extends RecyclerView.ViewHolder
    {
        private View      mView                 = null;
        private TextView  mNameTextView         = null;
        private ImageView mNearestShopImageView = null;



        @Override
        public String toString()
        {
            return "ShopViewHolder{" +
                    "mView="                   + mView                 +
                    ", mNameTextView="         + mNameTextView         +
                    ", mNearestShopImageView=" + mNearestShopImageView +
                    '}';
        }

        private ShopViewHolder(View view)
        {
            super(view);

            mView                 = view;
            mNameTextView         = (TextView) view.findViewById(R.id.nameTextView);
            mNearestShopImageView = (ImageView)view.findViewById(R.id.nearestShopImageView);
        }

        public static ShopViewHolder newInstance(View view)
        {
            return new ShopViewHolder(view);
        }

        public View getView()
        {
            return mView;
        }

        public TextView getNameTextView()
        {
            return mNameTextView;
        }

        public ImageView getNearestShopImageView()
        {
            return mNearestShopImageView;
        }
    }



    @SuppressWarnings("PublicInnerClass")
    public interface OnItemClickListener
    {
        void onShopClicked(ShopViewHolder holder, ShopEntity shop);
    }
}
