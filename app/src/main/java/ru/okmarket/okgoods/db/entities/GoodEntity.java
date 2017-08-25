package ru.okmarket.okgoods.db.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONObject;

import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.util.AppLog;

public final class GoodEntity implements Parcelable
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "GoodEntity";
    // endregion



    // region Attribute constants
    public static final String ATTRIBUTE_BRAND = "brand";
    // endregion
    // endregion



    // region Attributes
    private int        mId             = 0;
    private int        mCategoryId     = 0;
    private String     mName           = null;
    private int        mImageId        = 0;
    private double     mCost           = 0;
    private double     mUnit           = 0;
    private int        mUnitType       = 0;
    private double     mCountIncrement = 0;
    private int        mCountType      = 0;
    private JSONObject mAttrs          = null;
    private JSONObject mAttrsDetails   = null;
    private int        mPriority       = 0;
    private long       mUpdateTime     = 0;
    private int        mEnabled        = 0;
    // endregion



    @Override
    public String toString()
    {
        return "GoodEntity{" +
                "mId="               + mId             +
                ", mCategoryId="     + mCategoryId     +
                ", mName='"          + mName           + '\'' +
                ", mImageId="        + mImageId        +
                ", mCost="           + mCost           +
                ", mUnit="           + mUnit           +
                ", mUnitType="       + mUnitType       +
                ", mCountIncrement=" + mCountIncrement +
                ", mCountType="      + mCountType      +
                ", mAttrs="          + mAttrs          +
                ", mAttrsDetails="   + mAttrsDetails   +
                ", mPriority="       + mPriority       +
                ", mUpdateTime="     + mUpdateTime     +
                ", mEnabled="        + mEnabled        +
                '}';
    }

    private GoodEntity()
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

    public static GoodEntity newInstance()
    {
        return new GoodEntity();
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

        if (!(object instanceof GoodEntity))
        {
            return false;
        }

        GoodEntity good = (GoodEntity)object;

        return mId == good.mId;
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
            mAttrs        = TextUtils.isEmpty(attrs)        ? null : new JSONObject(attrs);
            mAttrsDetails = TextUtils.isEmpty(attrsDetails) ? null : new JSONObject(attrsDetails);
        }
        catch (Exception e)
        {
            AppLog.e(TAG, "Failed to parse JSON", e);

            mAttrs        = null;
            mAttrsDetails = null;
        }
    }
}
