package ru.okmarket.okgoods.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import ru.okmarket.okgoods.db.MainDatabase;

public final class SelectedGoodEntity implements Parcelable
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "SelectedGoodEntity";
    // endregion
    // endregion



    // region Attributes
    private int     mId         = 0;
    private int     mGoodId     = 0;
    private int     mCategoryId = 0;
    private String  mName       = null;
    private double  mCost       = 0;
    private double  mCount      = 0;
    private int     mEnabled    = 0;
    // endregion



    @Override
    public String toString()
    {
        return "SelectedGoodEntity{" +
                "mId="           + mId         +
                ", mGoodId="     + mGoodId     +
                ", mCategoryId=" + mCategoryId +
                ", mName='"      + mName       + '\'' +
                ", mCost="       + mCost       +
                ", mCount="      + mCount      +
                ", mEnabled="    + mEnabled    +
                '}';
    }

    private SelectedGoodEntity()
    {
        mId         = 0;
        mGoodId     = 0;
        mCategoryId = 0;
        mName       = null;
        mCost       = 0;
        mCount      = 0;
        mEnabled    = 0;
    }

    public static SelectedGoodEntity newInstance()
    {
        return new SelectedGoodEntity();
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

        if (!(object instanceof SelectedGoodEntity))
        {
            return false;
        }

        SelectedGoodEntity selectedGood = (SelectedGoodEntity)object;

        return mId == selectedGood.mId;
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
