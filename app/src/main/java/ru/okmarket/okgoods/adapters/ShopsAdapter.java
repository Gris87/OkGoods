package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.other.ShopFilter;
import ru.okmarket.okgoods.other.ShopInfo;

public class ShopsAdapter extends BaseAdapter
{
    private static final String TAG = "ShopsAdapter";



    private Context             mContext       = null;
    private ArrayList<ShopInfo> mOriginalShops = null;
    private ArrayList<ShopInfo> mShops         = null;
    private ShopInfo            mNearestShop   = null;



    private static class ViewHolder
    {
        TextView mName;
        TextView mNearest;
    }



    public ShopsAdapter(Context context, ArrayList<ShopInfo> shops)
    {
        mContext       = context;
        mOriginalShops = shops;
        mShops         = new ArrayList<>();
        mNearestShop   = null;

        filter(null);
    }

    @Override
    public int getCount()
    {
        return mShops.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position >= 0 && position < mShops.size() ? mShops.get(position) : null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    private View newView(Context context, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);

        View resView = inflater.inflate(R.layout.list_item_shops, parent, false);

        ViewHolder holder = new ViewHolder();

        holder.mName    = (TextView)resView.findViewById(R.id.nameTextView);
        holder.mNearest = (TextView)resView.findViewById(R.id.nearestShopTextView);

        resView.setTag(holder);

        return resView;
    }

    private void bindView(int position, View view)
    {
        ShopInfo shop = mShops.get(position);

        ViewHolder holder = (ViewHolder)view.getTag();

        holder.mName.setText(shop.getName());

        if (shop == mNearestShop)
        {
            holder.mNearest.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.mNearest.setVisibility(View.GONE);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;

        if (convertView != null)
        {
            view = convertView;
        }
        else
        {
            view = newView(mContext, parent);
        }

        bindView(position, view);

        return view;
    }

    public void findNearestShop(double latitude, double longitude)
    {
        ShopInfo nearestShop = null;
        double   minDistance = Double.MAX_VALUE;

        for (int i = 0; i < mShops.size(); ++i)
        {
            ShopInfo shop = mShops.get(i);

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
            ShopInfo shop = mOriginalShops.get(i);

            if (filter == null || filter.isFiltered(shop))
            {
                mShops.add(shop);
            }
        }

        notifyDataSetChanged();
    }
}
