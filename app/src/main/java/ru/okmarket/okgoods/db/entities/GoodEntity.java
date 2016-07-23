package ru.okmarket.okgoods.db.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.Locale;

import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.util.AppLog;

public class GoodEntity implements Parcelable
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodEntity";



    public static final String ATTRIBUTE_BRAND = "brand";



    private int        mId;
    private int        mCategoryId;
    private String     mName;
    private int        mImageId;
    private double     mCost;
    private double     mUnit;
    private int        mUnitType;
    private double     mCountIncrement;
    private int        mCountType;
    private JSONObject mAttrs;
    private JSONObject mAttrsDetails;
    private int        mPriority;
    private long       mUpdateTime;
    private int        mEnabled;



    public GoodEntity()
    {
        mId             = 0;
        mCategoryId     = 0;
        mName           = null;
        mImageId        = 0;
        mCost           = 0;
        mUnit           = 0;
        mUnitType       = MainDatabase.UNIT_TYPE_NOTHING;
        mCountIncrement = 0;
        mCountType      = MainDatabase.UNIT_TYPE_NOTHING;
        mAttrs          = null;
        mAttrsDetails   = null;
        mPriority       = 0;
        mUpdateTime     = 0;
        mEnabled        = 0;
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

    public int getImageId()
    {
        return mImageId;
    }

    public void setImageId(int imageId)
    {
        mImageId = imageId;
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

    public double getCountIncrement()
    {
        return mCountIncrement;
    }

    public void setCountIncrement(double countIncrement)
    {
        mCountIncrement = countIncrement;
    }

    public int getCountType()
    {
        return mCountType;
    }

    public void setCountType(int countType)
    {
        mCountType = countType;
    }

    public JSONObject getAttrs()
    {
        return mAttrs;
    }

    public void setAttrs(JSONObject attrs)
    {
        mAttrs = attrs;
    }

    public JSONObject getAttrsDetails()
    {
        return mAttrsDetails;
    }

    public void setAttrsDetails(JSONObject attrsDetails)
    {
        mAttrsDetails = attrsDetails;
    }

    public int getPriority()
    {
        return mPriority;
    }

    public void setPriority(int priority)
    {
        mPriority = priority;
    }

    public long getUpdateTime()
    {
        return mUpdateTime;
    }

    public void setUpdateTime(long updateTime)
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
        out.writeString(mName != null ? mName : "");
        out.writeInt(mImageId);
        out.writeDouble(mCost);
        out.writeDouble(mUnit);
        out.writeInt(mUnitType);
        out.writeDouble(mCountIncrement);
        out.writeInt(mCountType);
        out.writeString(mAttrs        != null ? mAttrs.toString()        : "");
        out.writeString(mAttrsDetails != null ? mAttrsDetails.toString() : "");
        out.writeInt(mPriority);
        out.writeLong(mUpdateTime);
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
        mId                 = in.readInt();
        mCategoryId         = in.readInt();
        mName               = in.readString();
        mImageId            = in.readInt();
        mCost               = in.readDouble();
        mUnit               = in.readDouble();
        mUnitType           = in.readInt();
        mCountIncrement     = in.readDouble();
        mCountType          = in.readInt();
        String attrs        = in.readString();
        String attrsDetails = in.readString();
        mPriority           = in.readInt();
        mUpdateTime         = in.readLong();
        mEnabled            = in.readInt();

        if (TextUtils.isEmpty(mName))
        {
            mName = null;
        }

        try
        {
            mAttrs        = !TextUtils.isEmpty(attrs)        ? new JSONObject(attrs)        : null;
            mAttrsDetails = !TextUtils.isEmpty(attrsDetails) ? new JSONObject(attrsDetails) : null;
        }
        catch (Exception e)
        {
            AppLog.e(TAG, "Failed to parse JSON", e);

            mAttrs        = null;
            mAttrsDetails = null;
        }
    }
}
