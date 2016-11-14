package ru.okmarket.okgoods.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import ru.okmarket.okgoods.db.MainDatabase;

public final class HistoryDetailsEntity implements Parcelable
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryDetailsEntity";



    private int     mId;
    private int     mGoodId;
    private int     mCategoryId;
    private String  mName;
    private double  mCost;
    private double  mCount;
    private int     mEnabled;



    @Override
    public String toString()
    {
        return "HistoryDetailsEntity{" +
                "mId="           + mId         +
                ", mGoodId="     + mGoodId     +
                ", mCategoryId=" + mCategoryId +
                ", mName='"      + mName       + '\'' +
                ", mCost="       + mCost       +
                ", mCount="      + mCount      +
                ", mEnabled="    + mEnabled    +
                '}';
    }

    private HistoryDetailsEntity()
    {
        mId         = 0;
        mGoodId     = 0;
        mCategoryId = 0;
        mName       = null;
        mCost       = 0;
        mCount      = 0;
        mEnabled    = 0;
    }

    public static HistoryDetailsEntity newInstance()
    {
        return new HistoryDetailsEntity();
    }

    @SuppressWarnings({"NonFinalFieldReferenceInEquals", "AccessingNonPublicFieldOfAnotherObject"})
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

        if (!(object instanceof HistoryDetailsEntity))
        {
            return false;
        }

        HistoryDetailsEntity details = (HistoryDetailsEntity)object;

        return mId == details.mId;
    }

    @SuppressWarnings("NonFinalFieldReferencedInHashCode")
    @Override
    public int hashCode()
    {
        return mId;
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

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public int getEnabled()
    {
        return mEnabled;
    }

    public void setEnabled(int enabled)
    {
        mEnabled = enabled;
    }

    @SuppressWarnings("unused")
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

    public static final Parcelable.Creator<HistoryDetailsEntity> CREATOR = new Parcelable.Creator<HistoryDetailsEntity>()
    {
        @Override
        public HistoryDetailsEntity createFromParcel(Parcel in)
        {
            return new HistoryDetailsEntity(in);
        }

        @Override
        public HistoryDetailsEntity[] newArray(int size)
        {
            return new HistoryDetailsEntity[size];
        }
    };

    private HistoryDetailsEntity(Parcel in)
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
