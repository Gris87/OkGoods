package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.entities.GoodsCategoryEntity;
import ru.okmarket.okgoods.util.Tree;

public class GoodsCategoriesAdapter extends RecyclerView.Adapter<GoodsCategoriesAdapter.ViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsCategoriesAdapter";



    private Context                   mContext             = null;
    private Tree<GoodsCategoryEntity> mItems               = null;
    private OnItemClickListener       mOnItemClickListener = null;



    public GoodsCategoriesAdapter(Context context, Tree<GoodsCategoryEntity> tree)
    {
        mContext             = context;
        mItems               = tree;
        mOnItemClickListener = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_goods_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        final GoodsCategoryEntity item = mItems.get(position);

        holder.mNameTextView.setText(item.getName());

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onGoodsCategoryClicked(holder, item);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    public ArrayList<GoodsCategoryEntity> getItems()
    {
        ArrayList<GoodsCategoryEntity> res = new ArrayList<>();

        ArrayList<Tree<GoodsCategoryEntity>> children = mItems.getChildren();

        for (int i = 0; i < children.size(); ++i)
        {
            res.add(children.get(i).getData());
        }

        return res;
    }

    public Tree<GoodsCategoryEntity> getTree()
    {
        return mItems;
    }

    public void setTree(Tree<GoodsCategoryEntity> tree)
    {
        mItems = tree;

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mOnItemClickListener = listener;
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



    public interface OnItemClickListener
    {
        void onGoodsCategoryClicked(ViewHolder viewHolder, GoodsCategoryEntity category);
    }
}
