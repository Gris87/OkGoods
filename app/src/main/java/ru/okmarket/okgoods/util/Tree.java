package ru.okmarket.okgoods.util;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public final class Tree<T>
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "Tree";
    // endregion
    // endregion



    // region Attributes
    private T                  mData     = null;
    private int                mLevel    = 0;
    private Tree<T>            mParent   = null;
    private ArrayList<Tree<T>> mChildren = null;
    // endregion



    @Override
    public String toString()
    {
        return "Tree{" +
                "mData="       + mData     +
                ", mLevel="    + mLevel    +
                ", mParent="   + mParent   +
                ", mChildren=" + mChildren +
                '}';
    }

    private Tree(T data)
    {
        mData     = data;
        mLevel    = 0;
        mParent   = null;
        mChildren = new ArrayList<>(0);
    }

    public static <T> Tree<T> newInstance(T data)
    {
        return new Tree<>(data);
    }

    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    private Tree(T data, Tree<T> parent)
    {
        mData     = data;
        mLevel    = parent.mLevel + 1;
        mParent   = parent;
        mChildren = new ArrayList<>(0);

        //noinspection ThisEscapedInObjectConstruction
        mParent.mChildren.add(this);
    }

    @SuppressWarnings({"AccessingNonPublicFieldOfAnotherObject", "unused"})
    public void doBreadth(Operation<T> operation)
    {
        Queue<Tree<T>> queue = new LinkedList<>();
        queue.add(this);

        do
        {
            Tree<T> item = queue.poll();

            if (operation.filter(item))
            {
                operation.run(item);

                ArrayList<Tree<T>> children = item.mChildren;

                for (int i = 0; i < children.size(); ++i)
                {
                    queue.add(children.get(i));
                }
            }
        } while (!queue.isEmpty());
    }

    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public void doDepth(Operation<T> operation)
    {
        Stack<Tree<T>> stack = new Stack<>();
        stack.push(this);

        do
        {
            Tree<T> item = stack.pop();

            if (operation.filter(item))
            {
                operation.run(item);

                ArrayList<Tree<T>> children = item.mChildren;

                for (int i = children.size() - 1; i >= 0; --i)
                {
                    stack.push(children.get(i));
                }
            }
        } while (!stack.isEmpty());
    }

    @SuppressWarnings({"AccessingNonPublicFieldOfAnotherObject", "unused"})
    @Nullable
    public <R> R doBreadthForResult(OperationWithResult<T, R> operation)
    {
        R res = operation.init();

        Queue<Tree<T>> queue = new LinkedList<>();
        queue.add(this);

        do
        {
            Tree<T> item = queue.poll();

            if (operation.filter(item, res))
            {
                res = operation.run(item, res);

                ArrayList<Tree<T>> children = item.mChildren;

                for (int i = 0; i < children.size(); ++i)
                {
                    queue.add(children.get(i));
                }
            }
        } while (!queue.isEmpty());

        return res;
    }

    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    @Nullable
    public <R> R doDepthForResult(OperationWithResult<T, R> operation)
    {
        R res = operation.init();

        Stack<Tree<T>> stack = new Stack<>();
        stack.push(this);

        do
        {
            Tree<T> item = stack.pop();

            if (operation.filter(item, res))
            {
                res = operation.run(item, res);

                ArrayList<Tree<T>> children = item.mChildren;

                for (int i = children.size() - 1; i >= 0; --i)
                {
                    stack.push(children.get(i));
                }
            }
        } while (!stack.isEmpty());

        return res;
    }

    public Tree<T> addChild(T data)
    {
        return new Tree<>(data, this);
    }

    public void removeChild(int index)
    {
        mChildren.remove(index);
    }

    public Tree<T> getChild(int index)
    {
        return mChildren.get(index);
    }

    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public T get(int index)
    {
        return mChildren.get(index).mData;
    }

    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public void set(int index, T data)
    {
        mChildren.get(index).mData = data;
    }

    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    public ArrayList<T> getAll()
    {
        ArrayList<T> res = new ArrayList<>(0);

        for (int i = 0; i < mChildren.size(); ++i)
        {
            res.add(mChildren.get(i).mData);
        }

        return res;
    }

    @SuppressWarnings({"AccessingNonPublicFieldOfAnotherObject", "WeakerAccess"})
    public int indexOf(T data)
    {
        for (int i = 0; i < mChildren.size(); ++i)
        {
            if (mChildren.get(i).mData.equals(data))
            {
                return i;
            }
        }

        return -1;
    }

    public boolean contains(T data)
    {
        return indexOf(data) >= 0;
    }

    public int size()
    {
        return mChildren.size();
    }

    public T getData()
    {
        return mData;
    }

    public void setData(T data)
    {
        mData = data;
    }

    public int getLevel()
    {
        return mLevel;
    }

    public Tree<T> getParent()
    {
        return mParent;
    }

    @SuppressWarnings("unused")
    public ArrayList<Tree<T>> getChildren()
    {
        return mChildren;
    }



    @SuppressWarnings("PublicInnerClass")
    public abstract static class Operation<T>
    {
        protected Operation()
        {
            // Nothing
        }

        protected boolean filter(Tree<T> node)
        {
            return true;
        }

        protected abstract void run(Tree<T> node);
    }

    @SuppressWarnings("PublicInnerClass")
    public abstract static class OperationWithResult<T, R>
    {
        protected OperationWithResult()
        {
            // Nothing
        }

        protected boolean filter(Tree<T> node, R currentResult)
        {
            return true;
        }

        @Nullable
        protected abstract R init();

        @Nullable
        protected abstract R run(Tree<T> node, R currentResult);
    }
}
