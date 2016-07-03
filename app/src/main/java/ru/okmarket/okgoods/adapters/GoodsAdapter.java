package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.entities.GoodEntity;
import ru.okmarket.okgoods.db.entities.GoodsCategoryEntity;
import ru.okmarket.okgoods.net.HttpClient;
import ru.okmarket.okgoods.net.Web;
import ru.okmarket.okgoods.widgets.CachedImageView;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsAdapter";



    private Context                        mContext                 = null;
    private ArrayList<GoodsCategoryEntity> mCategories              = null;
    private ArrayList<GoodEntity>          mGoods                   = null;
    private HttpClient                     mHttpClient              = null;
    private OnCategoryClickListener        mOnCategoryClickListener = null;
    private OnGoodClickListener            mOnGoodClickListener     = null;
    private int                            mWidth                   = 0;



    public GoodsAdapter(Context context, int width)
    {
        mContext             = context;
        mCategories          = new ArrayList<>();
        mGoods               = new ArrayList<>();
        mHttpClient          = HttpClient.getInstance(mContext);
        mOnGoodClickListener = null;
        mWidth               = width;
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

            holder.mCategoryView.setVisibility(View.VISIBLE);
            holder.mGoodView.setVisibility(    View.GONE);

            int imageWidth  = mWidth;
            int imageHeight = mWidth * 2 / 3;

            holder.mCategoryImageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));

            ((ImageView)holder.mCategoryImageView.getContentView()).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.mCategoryImageView.setImageUrl(Web.getCategoryPhotoUrl(item.getImageUrl()), mHttpClient.getImageLoader());
            holder.mCategoryNameTextView.setText(item.getName());

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

            holder.mCategoryView.setVisibility(View.GONE);
            holder.mGoodView.setVisibility(    View.VISIBLE);

            holder.mGoodNameTextView.setText(item.getName());

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
        public View            mView;
        public View            mCategoryView;
        public View            mGoodView;
        public CachedImageView mCategoryImageView;
        public TextView        mCategoryNameTextView;
        public TextView        mGoodNameTextView;



        public ViewHolder(View view)
        {
            super(view);

            mView                 = view;
            mCategoryView         =                  view.findViewById(R.id.categoryView);
            mGoodView             =                  view.findViewById(R.id.goodView);
            mCategoryImageView    = (CachedImageView)view.findViewById(R.id.categoryImageView);
            mCategoryNameTextView = (TextView)       view.findViewById(R.id.categoryNameTextView);
            mGoodNameTextView     = (TextView)       view.findViewById(R.id.goodNameTextView);
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
