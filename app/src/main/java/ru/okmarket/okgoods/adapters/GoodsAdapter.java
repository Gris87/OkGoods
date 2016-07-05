package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
    private LinearLayout.LayoutParams      mImageLayoutParams       = null;



    public GoodsAdapter(Context context, int width)
    {
        mContext             = context;
        mCategories          = new ArrayList<>();
        mGoods               = new ArrayList<>();
        mHttpClient          = HttpClient.getInstance(mContext);
        mOnGoodClickListener = null;
        mImageLayoutParams   = new LinearLayout.LayoutParams(width, width * 2 / 3);
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

            holder.mCategoryImageView.setLayoutParams(mImageLayoutParams);
            holder.mCategoryImageView.setErrorImageResId(R.drawable.download_error);
            ((ImageView)holder.mCategoryImageView.getContentView()).setScaleType(ImageView.ScaleType.FIT_CENTER);

            if (!TextUtils.isEmpty(item.getImageName()))
            {
                holder.mCategoryImageView.setDefaultImageResId(0);
                holder.mCategoryImageView.setImageUrl(Web.getCategoryPhotoUrl(item.getImageName()), mHttpClient.getImageLoader());
            }
            else
            {
                holder.mCategoryImageView.setDefaultImageResId(R.drawable.download_error);
                holder.mCategoryImageView.setImageUrl("", mHttpClient.getImageLoader());
            }

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

            holder.mGoodImageView.setLayoutParams(mImageLayoutParams);
            holder.mGoodImageView.setErrorImageResId(R.drawable.download_error);
            ((ImageView)holder.mGoodImageView.getContentView()).setScaleType(ImageView.ScaleType.FIT_CENTER);

            if (item.getImageId() > 0)
            {
                holder.mGoodImageView.setDefaultImageResId(0);
                holder.mGoodImageView.setImageUrl(Web.getGoodPhotoThumbnailUrl(item.getImageId()), mHttpClient.getImageLoader());
            }
            else
            {
                holder.mGoodImageView.setDefaultImageResId(R.drawable.download_error);
                holder.mGoodImageView.setImageUrl("", mHttpClient.getImageLoader());
            }

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
        public CachedImageView mGoodImageView;
        public TextView        mGoodNameTextView;



        public ViewHolder(View view)
        {
            super(view);

            mView                 = view;
            mCategoryView         =                  view.findViewById(R.id.categoryView);
            mGoodView             =                  view.findViewById(R.id.goodView);
            mCategoryImageView    = (CachedImageView)view.findViewById(R.id.categoryImageView);
            mCategoryNameTextView = (TextView)       view.findViewById(R.id.categoryNameTextView);
            mGoodImageView        = (CachedImageView)view.findViewById(R.id.goodImageView);
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
