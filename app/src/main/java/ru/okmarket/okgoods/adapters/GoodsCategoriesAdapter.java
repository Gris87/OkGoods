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
import ru.okmarket.okgoods.widgets.ImageButtonWithTooltip;

public class GoodsCategoriesAdapter extends RecyclerView.Adapter<GoodsCategoriesAdapter.ViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsCategoriesAdapter";



    private Context                              mContext             = null;
    private Tree<GoodsCategoryEntity>            mTree                = null;
    private ArrayList<Tree<GoodsCategoryEntity>> mItems               = null;
    private OnItemClickListener                  mOnItemClickListener = null;



    public GoodsCategoriesAdapter(Context context, Tree<GoodsCategoryEntity> tree)
    {
        mContext             = context;
        mTree                = tree;
        mItems               = new ArrayList<>();
        mOnItemClickListener = null;

        invalidate();
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
        final Tree<GoodsCategoryEntity> node = mItems.get(position);
        final GoodsCategoryEntity       item = node.getData();

        holder.mNameTextView.setText(item.getName());

        if (node.size() > 0)
        {
            holder.mExpandCategoryButton.setVisibility(View.VISIBLE);

            if (item.isExpanded())
            {
                holder.mExpandCategoryButton.setImageResource(R.drawable.collapse);
            }
            else
            {
                holder.mExpandCategoryButton.setImageResource(R.drawable.expand);
            }

            holder.mExpandCategoryButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (!item.isExpanded())
                    {
                        item.setExpanded(true);

                        mItems.addAll(holder.getAdapterPosition() + 1, node.getChildren());
                    }
                    else
                    {
                        item.setExpanded(false);

                        for (int i = node.size() - 1; i >= 0; --i)
                        {
                            mItems.remove(holder.getAdapterPosition() + 1 + i);
                        }
                    }

                    notifyDataSetChanged();
                }
            });
        }
        else
        {
            holder.mExpandCategoryButton.setVisibility(View.INVISIBLE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onGoodsCategoryClicked(holder, node);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    public ArrayList<GoodsCategoryEntity> getTopLevelItems()
    {
        ArrayList<GoodsCategoryEntity> res = new ArrayList<>();

        ArrayList<Tree<GoodsCategoryEntity>> children = mTree.getChildren();

        for (int i = 0; i < children.size(); ++i)
        {
            res.add(children.get(i).getData());
        }

        return res;
    }

    public Tree<GoodsCategoryEntity> getTree()
    {
        return mTree;
    }

    public void setTree(Tree<GoodsCategoryEntity> tree)
    {
        mTree = tree;

        invalidate();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mOnItemClickListener = listener;
    }

    private void invalidate()
    {
        mItems.clear();

        ArrayList<Tree<GoodsCategoryEntity>> children = mTree.getChildren();

        for (int i = 0; i < children.size(); ++i)
        {
            Tree<GoodsCategoryEntity> child = children.get(i);

            mItems.add(child);
            addItemsIfExpanded(child);
        }
    }

    private void addItemsIfExpanded(Tree<GoodsCategoryEntity> tree)
    {
        if (tree.getData().isExpanded())
        {
            ArrayList<Tree<GoodsCategoryEntity>> children = tree.getChildren();

            for (int i = 0; i < children.size(); ++i)
            {
                Tree<GoodsCategoryEntity> child = children.get(i);

                mItems.add(child);
                addItemsIfExpanded(child);
            }
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public View                   mView;
        public ImageButtonWithTooltip mExpandCategoryButton;
        public TextView               mNameTextView;



        public ViewHolder(View view)
        {
            super(view);

            mView                 = view;
            mExpandCategoryButton = (ImageButtonWithTooltip)view.findViewById(R.id.expandCategoryButton);
            mNameTextView         = (TextView)              view.findViewById(R.id.nameTextView);
        }
    }



    public interface OnItemClickListener
    {
        void onGoodsCategoryClicked(ViewHolder viewHolder, Tree<GoodsCategoryEntity> category);
    }
}
