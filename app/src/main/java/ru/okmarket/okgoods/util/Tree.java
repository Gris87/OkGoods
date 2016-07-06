package ru.okmarket.okgoods.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Tree<T>
{
    @SuppressWarnings("unused")
    private static final String TAG = "Tree";



    private T                  mData;
    private int                mLevel;
    private Tree<T>            mParent;
    private ArrayList<Tree<T>> mChildren;



    public Tree(T data)
    {
        mData     = data;
        mLevel    = 0;
        mParent   = null;
        mChildren = new ArrayList<>();
    }

    private Tree(T data, Tree<T> parent)
    {
        mData     = data;
        mLevel    = parent.mLevel + 1;
        mParent   = parent;
        mChildren = new ArrayList<>();

        mParent.mChildren.add(this);
    }

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

    public T get(int index)
    {
        return mChildren.get(index).getData();
    }

    public void set(int index, T data)
    {
        mChildren.get(index).setData(data);
    }

    public ArrayList<T> getAll()
    {
        ArrayList<T> res = new ArrayList<>();

        for (int i = 0; i < mChildren.size(); ++i)
        {
            res.add(mChildren.get(i).getData());
        }

        return res;
    }

    public int indexOf(T data)
    {
        for (int i = 0; i < mChildren.size(); ++i)
        {
            if (mChildren.get(i).getData().equals(data))
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

    public ArrayList<Tree<T>> getChildren()
    {
        return mChildren;
    }



    public static abstract class Operation<T>
    {
        protected boolean filter(Tree<T> node)
        {
            return true;
        }

        protected abstract void run(Tree<T> node);
    }

    public static abstract class OperationWithResult<T, R>
    {
        protected boolean filter(Tree<T> node, R currentResult)
        {
            return true;
        }

        protected abstract R init();
        protected abstract R run(Tree<T> node, R currentResult);
    }
}
