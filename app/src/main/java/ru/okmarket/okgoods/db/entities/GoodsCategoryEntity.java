package ru.okmarket.okgoods.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

import ru.okmarket.okgoods.db.MainDatabase;

public class GoodsCategoryEntity implements Parcelable
{
    @SuppressWarnings("unused")
    private static final String TAG = "GoodsCategoryEntity";



    private int     mId;
    private int     mParentId;
    private String  mName;
    private int     mUpdateTime;
    private int     mEnabled;



    public GoodsCategoryEntity()
    {
        mId         = 0;
        mParentId   = 0;
        mName       = null;
        mUpdateTime = 0;
        mEnabled    = 0;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.US, "{id = %1$d, parentId = %2$d, name = %3$s, updateTime = %4$d, enabled = %5$d}"
                , mId
                , mParentId
                , String.valueOf(mName)
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

        if (!(object instanceof GoodsCategoryEntity))
        {
            return false;
        }

        GoodsCategoryEntity category = (GoodsCategoryEntity)object;

        return mId == category.mId;
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
        out.writeInt(mParentId);
        out.writeString(mName);
        out.writeInt(mUpdateTime);
        out.writeInt(mEnabled);
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
        mUpdateTime = in.readInt();
        mEnabled    = in.readInt();
    }
}
