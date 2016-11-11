package ru.okmarket.okgoods.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

import ru.okmarket.okgoods.db.MainDatabase;

public class SelectedGoodEntity implements Parcelable
{
    @SuppressWarnings("unused")
    private static final String TAG = "SelectedGoodEntity";



    private int     mId;
    private int     mGoodId;
    private int     mCategoryId;
    private String  mName;
    private double  mCost;
    private double  mCount;
    private int     mEnabled;



    public SelectedGoodEntity()
    {
        mId         = 0;
        mGoodId     = 0;
        mCategoryId = 0;
        mName       = null;
        mCost       = 0;
        mCount      = 0;
        mEnabled    = 0;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.US, "{id = %1$d, name = %2$s, cost = %3$.2f, count = %4$.2f, enabled = %5$d}"
                , mId
                , String.valueOf(mName)
                , mCost
                , mCount
                , mEnabled
        );
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == null)
        {
            return false;
        }

        if (object == this)
        {
            return true;
        }

        if (!(object instanceof SelectedGoodEntity))
        {
            return false;
        }

        SelectedGoodEntity selectedGood = (SelectedGoodEntity)object;

        return mId == selectedGood.mId;
    }

    public int getId()
    {
        return mId;
    }

    public void setId(int id)
    {
        mId = id;
    }

    public int getGoodId()
    {
        return mGoodId;
    }

    public void setGoodId(int goodId)
    {
        mGoodId = goodId;
    }

    public int getCategoryId()
    {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId)
    {
        mCategoryId = categoryId;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public double getCost()
    {
        return mCost;
    }

    public void setCost(double cost)
    {
        mCost = cost;
    }

    public double getCount()
    {
        return mCount;
    }

    public void setCount(double count)
    {
        mCount = count;
    }

    public int getEnabled()
    {
        return mEnabled;
    }

    public void setEnabled(int enabled)
    {
        mEnabled = enabled;
    }

    public boolean isEnabled()
    {
        return mEnabled != MainDatabase.DISABLED;
    }

    public boolean isOwn()
    {
        return mEnabled == MainDatabase.FORCE_ENABLED;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(mId);
        out.writeInt(mGoodId);
        out.writeInt(mCategoryId);
        out.writeString(mName);
        out.writeDouble(mCost);
        out.writeDouble(mCount);
        out.writeInt(mEnabled);
    }

    public static final Parcelable.Creator<SelectedGoodEntity> CREATOR = new Parcelable.Creator<SelectedGoodEntity>()
    {
        @Override
        public SelectedGoodEntity createFromParcel(Parcel in)
        {
            return new SelectedGoodEntity(in);
        }

        @Override
        public SelectedGoodEntity[] newArray(int size)
        {
            return new SelectedGoodEntity[size];
        }
    };

    private SelectedGoodEntity(Parcel in)
    {
        mId         = in.readInt();
        mGoodId     = in.readInt();
        mCategoryId = in.readInt();
        mName       = in.readString();
        mCost       = in.readDouble();
        mCount      = in.readDouble();
        mEnabled    = in.readInt();
    }
}
