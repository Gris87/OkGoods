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
import ru.okmarket.okgoods.widgets.GlowView;
import ru.okmarket.okgoods.widgets.ImageButtonWithTooltip;

public final class GoodsCategoriesAdapter extends RecyclerView.Adapter<GoodsCategoriesAdapter.GoodsCategoryViewHolder>
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsCategoriesAdapter";
    // endregion
    // endregion



    // region Attributes
    private Context                              mContext             = null;
    private Tree<GoodsCategoryEntity>            mTree                = null;
    private ArrayList<Tree<GoodsCategoryEntity>> mItems               = null;
    private OnItemClickListener                  mOnItemClickListener = null;
    // endregion



    @Override
    public String toString()
    {
        return "GoodsCategoriesAdapter{" +
                "mContext="               + mContext             +
                ", mTree="                + mTree                +
                ", mItems="               + mItems               +
                ", mOnItemClickListener=" + mOnItemClickListener +
                '}';
    }

    private GoodsCategoriesAdapter(Context context, Tree<GoodsCategoryEntity> tree)
    {
        mContext             = context;
        mTree                = tree;
        mItems               = new ArrayList<>(0);
        mOnItemClickListener = null;

        invalidate();
    }

    public static GoodsCategoriesAdapter newInstance(Context context, Tree<GoodsCategoryEntity> tree)
    {
        return new GoodsCategoriesAdapter(context, tree);
    }

    @Override
    public GoodsCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_goods_category, parent, false);

        return GoodsCategoryViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(final GoodsCategoryViewHolder holder, int position)
    {
        final Tree<GoodsCategoryEntity> node      = mItems.get(position);
        final GoodsCategoryEntity       item      = node.getData();
        Resources                       resources = mContext.getResources();

        int margin      = resources.getDimensionPixelSize(R.dimen.common_margin);
        int indentation = resources.getDimensionPixelSize(R.dimen.expand_category_indentation);
        int buttonSize  = resources.getDimensionPixelSize(R.dimen.expand_category_button_size);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(buttonSize, buttonSize);
        layoutParams.setMargins(margin + indentation * (node.getLevel() - 1), margin, 0, margin);
        holder.getExpandCategoryButton().setLayoutParams(layoutParams);

        holder.getNameTextView().setText(item.getName());
        holder.getNameTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 18 - (node.getLevel() << 1));

        if (node.size() > 0)
        {
            holder.getExpandCategoryButton().setVisibility(View.VISIBLE);

            if (item.isExpanded())
            {
                holder.getExpandCategoryButton().setImageResource(R.drawable.collapse);
            }
            else
            {
                holder.getExpandCategoryButton().setImageResource(R.drawable.expand);
            }

            holder.getExpandCategoryButton().setOnClickListener(new View.OnClickListener()
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
                                return new ArrayList<>(0);
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



                        if (children != null)
                        {
                            mItems.addAll(holder.getAdapterPosition() + 1, children);
                        }
                    }
                    else
                    {
                        Integer childCount = node.doDepthForResult(new Tree.OperationWithResult<GoodsCategoryEntity, Integer>()
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

                        if (childCount != null)
                        {
                            for (int i = childCount; i >= 1; --i)
                            {
                                mItems.remove(holder.getAdapterPosition() + i);
                            }

                            item.setExpanded(false);
                        }
                    }

                    notifyDataSetChanged();
                }
            });
        }
        else
        {
            holder.getExpandCategoryButton().setVisibility(View.INVISIBLE);
        }

        holder.getGlowView().setOnClickListener(new View.OnClickListener()
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

    public void invalidate()
    {
        mItems.clear();

        mTree.doDepth(new Tree.Operation<GoodsCategoryEntity>()
        {
            @Override
            protected boolean filter(Tree<GoodsCategoryEntity> node)
            {
                return node == mTree || node.getParent().getData().isExpanded();
            }

            @Override
            protected void run(Tree<GoodsCategoryEntity> node)
            {
                mItems.add(node);
            }
        });

        notifyDataSetChanged();
    }



    @SuppressWarnings({"PublicInnerClass", "WeakerAccess"})
    public static final class GoodsCategoryViewHolder extends RecyclerView.ViewHolder
    {
        // region Attributes
        private GlowView               mGlowView             = null;
        private ImageButtonWithTooltip mExpandCategoryButton = null;
        private TextView               mNameTextView         = null;
        // endregion



        @Override
        public String toString()
        {
            return "GoodsCategoryViewHolder{" +
                    "mGlowView="               + mGlowView             +
                    ", mExpandCategoryButton=" + mExpandCategoryButton +
                    ", mNameTextView="         + mNameTextView         +
                    '}';
        }

        @SuppressWarnings("RedundantCast")
        private GoodsCategoryViewHolder(View view)
        {
            super(view);

            mGlowView             = (GlowView)view.findViewById(R.id.glowView);
            mExpandCategoryButton = (ImageButtonWithTooltip)view.findViewById(R.id.expandCategoryButton);
            mNameTextView         = (TextView)              view.findViewById(R.id.nameTextView);
        }

        public static GoodsCategoryViewHolder newInstance(View view)
        {
            return new GoodsCategoryViewHolder(view);
        }

        public GlowView getGlowView()
        {
            return mGlowView;
        }

        public ImageButtonWithTooltip getExpandCategoryButton()
        {
            return mExpandCategoryButton;
        }

        public TextView getNameTextView()
        {
            return mNameTextView;
        }
    }



    @SuppressWarnings("PublicInnerClass")
    public interface OnItemClickListener
    {
        void onGoodsCategoryClicked(GoodsCategoryViewHolder holder, Tree<GoodsCategoryEntity> category);
    }
}
