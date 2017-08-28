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

public final class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodViewHolder>
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsAdapter";
    // endregion
    // endregion



    // region Attributes
    private Context                        mContext                 = null;
    private ArrayList<GoodsCategoryEntity> mCategories              = null;
    private ArrayList<GoodEntity>          mGoods                   = null;
    private HttpClient                     mHttpClient              = null;
    private OnCategoryClickListener        mOnCategoryClickListener = null;
    private OnGoodClickListener            mOnGoodClickListener     = null;
    private LinearLayout.LayoutParams      mImageLayoutParams       = null;
    // endregion



    @Override
    public String toString()
    {
        return "GoodsAdapter{" +
                "mContext="                   + mContext                 +
                ", mCategories="              + mCategories              +
                ", mGoods="                   + mGoods                   +
                ", mHttpClient="              + mHttpClient              +
                ", mOnCategoryClickListener=" + mOnCategoryClickListener +
                ", mOnGoodClickListener="     + mOnGoodClickListener     +
                ", mImageLayoutParams="       + mImageLayoutParams       +
                '}';
    }

    private GoodsAdapter(Context context, int width)
    {
        mContext             = context;
        mCategories          = new ArrayList<>(0);
        mGoods               = new ArrayList<>(0);
        mHttpClient          = HttpClient.getInstance(mContext);
        mOnGoodClickListener = null;
        mImageLayoutParams   = new LinearLayout.LayoutParams(width, (width << 1) / 3);
    }

    public static GoodsAdapter newInstance(Context context, int width)
    {
        return new GoodsAdapter(context, width);
    }

    @Override
    public GoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_good, parent, false);

        return GoodViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(final GoodViewHolder holder, int position)
    {
        if (position < mCategories.size())
        {
            final GoodsCategoryEntity item = mCategories.get(position);

            holder.getCategoryView().setVisibility(View.VISIBLE);
            holder.getGoodView().setVisibility(    View.GONE);

            holder.getCategoryImageView().setLayoutParams(mImageLayoutParams);
            holder.getCategoryImageView().setErrorImageResId(R.drawable.download_error);
            ((ImageView) holder.getCategoryImageView().getContentView()).setScaleType(ImageView.ScaleType.FIT_CENTER);

            if (!TextUtils.isEmpty(item.getImageName()))
            {
                holder.getCategoryImageView().setDefaultImageResId(0);
                holder.getCategoryImageView().setImageUrl(Web.getCategoryPhotoUrl(item.getImageName()), mHttpClient.getImageLoader());
            }
            else
            {
                holder.getCategoryImageView().setDefaultImageResId(R.drawable.no_image);
                holder.getCategoryImageView().setImageUrl("", mHttpClient.getImageLoader());
            }

            holder.getCategoryNameTextView().setText(item.getName());

            holder.getView().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (mOnCategoryClickListener != null)
                    {
                        mOnCategoryClickListener.onCategoryClicked(holder, item);
                    }
                }
            });
        }
        else
        {
            final GoodEntity item = mGoods.get(position - mCategories.size());

            holder.getCategoryView().setVisibility(View.GONE);
            holder.getGoodView().setVisibility(    View.VISIBLE);

            holder.getGoodImageView().setLayoutParams(mImageLayoutParams);
            holder.getGoodImageView().setErrorImageResId(R.drawable.download_error);
            ((ImageView) holder.getGoodImageView().getContentView()).setScaleType(ImageView.ScaleType.FIT_CENTER);

            if (item.getImageId() > 0)
            {
                holder.getGoodImageView().setDefaultImageResId(0);
                holder.getGoodImageView().setImageUrl(Web.getGoodPhotoThumbnailUrl(item.getImageId()), mHttpClient.getImageLoader());
            }
            else
            {
                holder.getGoodImageView().setDefaultImageResId(R.drawable.no_image);
                holder.getGoodImageView().setImageUrl("", mHttpClient.getImageLoader());
            }

            holder.getGoodNameTextView().setText(item.getName());

            holder.getView().setOnClickListener(new View.OnClickListener()
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

    @SuppressWarnings("unused")
    public ArrayList<GoodsCategoryEntity> getCategories()
    {
        return mCategories;
    }

    @SuppressWarnings("unused")
    public void setCategories(ArrayList<GoodsCategoryEntity> categories)
    {
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        mCategories = categories;

        notifyDataSetChanged();
    }

    public ArrayList<GoodEntity> getGoods()
    {
        return mGoods;
    }

    public void setGoods(ArrayList<GoodEntity> goods)
    {
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        mGoods = goods;

        notifyDataSetChanged();
    }

    public void setItems(ArrayList<GoodsCategoryEntity> categories, ArrayList<GoodEntity> goods)
    {
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        mCategories = categories;
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
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



    @SuppressWarnings({"PublicInnerClass", "WeakerAccess"})
    public static final class GoodViewHolder extends RecyclerView.ViewHolder
    {
        // region Attributes
        private View            mView                 = null;
        private View            mCategoryView         = null;
        private View            mGoodView             = null;
        private CachedImageView mCategoryImageView    = null;
        private TextView        mCategoryNameTextView = null;
        private CachedImageView mGoodImageView        = null;
        private TextView        mGoodNameTextView     = null;
        // endregion



        @Override
        public String toString()
        {
            return "GoodViewHolder{" +
                    "mView="                   + mView                 +
                    ", mCategoryView="         + mCategoryView         +
                    ", mGoodView="             + mGoodView             +
                    ", mCategoryImageView="    + mCategoryImageView    +
                    ", mCategoryNameTextView=" + mCategoryNameTextView +
                    ", mGoodImageView="        + mGoodImageView        +
                    ", mGoodNameTextView="     + mGoodNameTextView     +
                    '}';
        }

        @SuppressWarnings("RedundantCast")
        private GoodViewHolder(View view)
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

        public static GoodViewHolder newInstance(View view)
        {
            return new GoodViewHolder(view);
        }

        public View getView()
        {
            return mView;
        }

        public View getCategoryView()
        {
            return mCategoryView;
        }

        public View getGoodView()
        {
            return mGoodView;
        }

        public CachedImageView getCategoryImageView()
        {
            return mCategoryImageView;
        }

        public TextView getCategoryNameTextView()
        {
            return mCategoryNameTextView;
        }

        public CachedImageView getGoodImageView()
        {
            return mGoodImageView;
        }

        public TextView getGoodNameTextView()
        {
            return mGoodNameTextView;
        }
    }



    @SuppressWarnings("PublicInnerClass")
    public interface OnCategoryClickListener
    {
        void onCategoryClicked(GoodViewHolder holder, GoodsCategoryEntity category);
    }

    @SuppressWarnings("PublicInnerClass")
    public interface OnGoodClickListener
    {
        void onGoodClicked(GoodViewHolder holder, GoodEntity good);
    }
}
