package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        final Tree<GoodsCategoryEntity> node      = mItems.get(position);
        final GoodsCategoryEntity       item      = node.getData();
        Resources                       resources = mContext.getResources();

        int margin      = resources.getDimensionPixelSize(R.dimen.common_margin);
        int indentation = resources.getDimensionPixelSize(R.dimen.expand_category_indentation);
        int button_size = resources.getDimensionPixelSize(R.dimen.expand_category_button_size);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(button_size, button_size);
        layoutParams.setMargins(margin + indentation * (node.getLevel() - 1), margin, 0, margin);
        holder.mExpandCategoryButton.setLayoutParams(layoutParams);

        holder.mNameTextView.setText(item.getName());
        holder.mNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12 - node.getLevel());

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

                        ArrayList<Tree<GoodsCategoryEntity>> children = node.doDepthForResult(new Tree.OperationWithResult<GoodsCategoryEntity, ArrayList<Tree<GoodsCategoryEntity>>>()
                        {
                            @Override
                            protected boolean filter(Tree<GoodsCategoryEntity> recursiveNode, ArrayList<Tree<GoodsCategoryEntity>> currentResult)
                            {
                                return recursiveNode == node || recursiveNode.getParent().getData().isExpanded();
                            }

                            @Override
                            protected ArrayList<Tree<GoodsCategoryEntity>> init()
                            {
                                return new ArrayList<>();
                            }

                            @Override
                            protected ArrayList<Tree<GoodsCategoryEntity>> run(Tree<GoodsCategoryEntity> recursiveNode, ArrayList<Tree<GoodsCategoryEntity>> currentResult)
                            {
                                if (recursiveNode != node)
                                {
                                    currentResult.add(recursiveNode);
                                }

                                return currentResult;
                            }
                        });

                        mItems.addAll(holder.getAdapterPosition() + 1, children);
                    }
                    else
                    {
                        int childCount = node.doDepthForResult(new Tree.OperationWithResult<GoodsCategoryEntity, Integer>()
                        {
                            @Override
                            protected boolean filter(Tree<GoodsCategoryEntity> recursiveNode, Integer currentResult)
                            {
                                return recursiveNode.getData().isExpanded();
                            }

                            @Override
                            protected Integer init()
                            {
                                return 0;
                            }

                            @Override
                            protected Integer run(Tree<GoodsCategoryEntity> recursiveNode, Integer currentResult)
                            {
                                return currentResult + recursiveNode.size();
                            }
                        });

                        for (int i = childCount; i >= 1; --i)
                        {
                            mItems.remove(holder.getAdapterPosition() + i);
                        }

                        item.setExpanded(false);
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

    public Tree<GoodsCategoryEntity> getTree()
    {
        return mTree;
    }

    public void setTree(Tree<GoodsCategoryEntity> tree)
    {
        mTree = tree;

        invalidate();
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mOnItemClickListener = listener;
    }

    private void invalidate()
    {
        mItems.clear();

        mTree.doDepth(new Tree.Operation<GoodsCategoryEntity>()
        {
            @Override
            protected boolean filter(Tree<GoodsCategoryEntity> node)
            {
                return node == mTree || node.getParent().getData() == null || node.getParent().getData().isExpanded();
            }

            @Override
            protected void run(Tree<GoodsCategoryEntity> node)
            {
                if (node != mTree)
                {
                    mItems.add(node);
                }
            }
        });

        notifyDataSetChanged();
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
