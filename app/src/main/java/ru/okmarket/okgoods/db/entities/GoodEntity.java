package ru.okmarket.okgoods.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

import ru.okmarket.okgoods.db.MainDatabase;

public class GoodEntity implements Parcelable
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodEntity";



    private int     mId;
    private int     mCategoryId;
    private String  mName;
    private double  mCost;
    private double  mUnit;
    private int     mUnitType;
    private int     mUpdateTime;
    private int     mEnabled;



    public GoodEntity()
    {
        mId         = 0;
        mCategoryId = 0;
        mName       = null;
        mCost       = 0;
        mUnit       = 0;
        mUnitType   = MainDatabase.UNIT_TYPE_NOTHING;
        mUpdateTime = 0;
        mEnabled    = 0;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.US, "{id = %1$d, categoryId = %2$d, name = %3$s, cost = %4$.2f, unit = %5$.2f, unitType = %6$d, updateTime = %7$d, enabled = %8$d}"
                , mId
                , mCategoryId
                , String.valueOf(mName)
                , mCost
                , mUnit
                , mUnitType
                , mUpdateTime
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

        if (!(object instanceof GoodEntity))
        {
            return false;
        }

        GoodEntity good = (GoodEntity)object;

        return mId == good.mId;
    }

    public int getId()
    {
        return mId;
    }

    public void setId(int id)
    {
        mId = id;
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

    public double getUnit()
    {
        return mUnit;
    }

    public void setUnit(double unit)
    {
        mUnit = unit;
    }

    public int getUnitType()
    {
        return mUnitType;
    }

    public void setUnitType(int unitType)
    {
        mUnitType = unitType;
    }

    public int getUpdateTime()
    {
        return mUpdateTime;
    }

    public void setUpdateTime(int updateTime)
    {
        mUpdateTime = updateTime;
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
        out.writeInt(mCategoryId);
        out.writeString(mName);
        out.writeDouble(mCost);
        out.writeDouble(mUnit);
        out.writeInt(mUnitType);
        out.writeInt(mUpdateTime);
        out.writeInt(mEnabled);
    }

    public static final Parcelable.Creator<GoodEntity> CREATOR = new Parcelable.Creator<GoodEntity>()
    {
        @Override
        public GoodEntity createFromParcel(Parcel in)
        {
            return new GoodEntity(in);
        }

        @Override
        public GoodEntity[] newArray(int size)
        {
            return new GoodEntity[size];
        }
    };

    private GoodEntity(Parcel in)
    {
        mId         = in.readInt();
        mCategoryId = in.readInt();
        mName       = in.readString();
        mCost       = in.readDouble();
        mUnit       = in.readDouble();
        mUnitType   = in.readInt();
        mUpdateTime = in.readInt();
        mEnabled    = in.readInt();
    }
}
