package ru.okmarket.okgoods.util;

import java.util.ArrayList;

public class Tree<T>
{
    @SuppressWarnings("unused")
    private static final String TAG = "Tree";



    private T                  mData;
    private Tree<T>            mParent;
    private ArrayList<Tree<T>> mChildren;



    public Tree(T data)
    {
        mData     = data;
        mParent   = null;
        mChildren = new ArrayList<>();
    }

    private Tree(T data, Tree<T> parent)
    {
        mData     = data;
        mParent   = parent;
        mChildren = new ArrayList<>();

        mParent.mChildren.add(this);
    }

    public Tree<T> addChild(T data)
    {
        return new Tree<T>(data, this);
    }

    public Tree<T> getChild(int index)
    {
        return mChildren.get(index);
    }

    public T get(int index)
    {
        return mChildren.get(index).getData();
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

    public Tree<T> getParent()
    {
        return mParent;
    }

    public ArrayList<Tree<T>> getChildren()
    {
        return mChildren;
    }
}
