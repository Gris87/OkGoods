package ru.okmarket.okgoods.db.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import ru.okmarket.okgoods.db.MainDatabase;

public final class GoodsCategoryEntity implements Parcelable
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsCategoryEntity";
    // endregion
    // endregion



    // region Attributes
    private int     mId         = 0;
    private int     mParentId   = 0;
    private String  mName       = null;
    private String  mImageName  = null;
    private int     mPriority   = 0;
    private long    mUpdateTime = 0;
    private int     mEnabled    = 0;
    private boolean mExpanded   = false;
    // endregion



    @Override
    public String toString()
    {
        return "GoodsCategoryEntity{" +
                "mId="           + mId         +
                ", mParentId="   + mParentId   +
                ", mName='"      + mName       + '\'' +
                ", mImageName='" + mImageName  + '\'' +
                ", mPriority="   + mPriority   +
                ", mUpdateTime=" + mUpdateTime +
                ", mEnabled="    + mEnabled    +
                ", mExpanded="   + mExpanded   +
                '}';
    }

    private GoodsCategoryEntity()
    {
        mId         = 0;
        mParentId   = 0;
        mName       = null;
        mImageName  = null;
        mPriority   = 0;
        mUpdateTime = 0;
        mEnabled    = 0;
        mExpanded   = false;
    }

    public static GoodsCategoryEntity newInstance()
    {
        return new GoodsCategoryEntity();
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

        if (!(object instanceof GoodsCategoryEntity))
        {
            return false;
        }

        GoodsCategoryEntity category = (GoodsCategoryEntity)object;

        return mId == category.mId;
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

    public int getParentId()
    {
        return mParentId;
    }

    public void setParentId(int id)
    {
        mParentId = id;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public String getImageName()
    {
        return mImageName;
    }

    public void setImageName(String imageName)
    {
        mImageName = imageName;
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

    public boolean isExpanded()
    {
        return mExpanded;
    }

    public void setExpanded(boolean expanded)
    {
        mExpanded = expanded;
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
        out.writeInt(mParentId);
        out.writeString(mName      != null ? mName      : "");
        out.writeString(mImageName != null ? mImageName : "");
        out.writeInt(mPriority);
        out.writeLong(mUpdateTime);
        out.writeInt(mEnabled);
        //noinspection NumericCastThatLosesPrecision
        out.writeByte((byte)(mExpanded ? 1 : 0));
    }

    public static final Parcelable.Creator<GoodsCategoryEntity> CREATOR = new Parcelable.Creator<GoodsCategoryEntity>()
    {
        @Override
        public GoodsCategoryEntity createFromParcel(Parcel in)
        {
            return new GoodsCategoryEntity(in);
        }

        @Override
        public GoodsCategoryEntity[] newArray(int size)
        {
            return new GoodsCategoryEntity[size];
        }
    };

    private GoodsCategoryEntity(Parcel in)
    {
        mId         = in.readInt();
        mParentId   = in.readInt();
        mName       = in.readString();
        mImageName  = in.readString();
        mPriority   = in.readInt();
        mUpdateTime = in.readLong();
        mEnabled    = in.readInt();
        mExpanded   = in.readByte() == 1;

        if (TextUtils.isEmpty(mName))
        {
            mName = null;
        }

        if (TextUtils.isEmpty(mImageName))
        {
            mImageName = null;
        }
    }
}
