package ru.okmarket.okgoods.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public final class HistoryEntity implements Parcelable
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryEntity";



    private int     mId       = 0;
    private int     mShopId   = 0;
    private String  mShopName = null;
    private String  mDate     = null;
    private int     mDuration = 0;
    private double  mTotal    = 0;



    @Override
    public String toString()
    {
        return "HistoryEntity{" +
                "mId="          + mId              +
                ", mShopId="    + mShopId          +
                ", mShopName='" + mShopName + '\'' +
                ", mDate='"     + mDate + '\''     +
                ", mDuration="  + mDuration        +
                ", mTotal="     + mTotal           +
                '}';
    }

    private HistoryEntity()
    {
        mId       = 0;
        mShopId   = 0;
        mShopName = null;
        mDate     = null;
        mDuration = 0;
        mTotal    = 0;
    }

    public static HistoryEntity newInstance()
    {
        return new HistoryEntity();
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

        if (!(object instanceof HistoryEntity))
        {
            return false;
        }

        HistoryEntity history = (HistoryEntity)object;

        return mId == history.mId;
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

    public int getShopId()
    {
        return mShopId;
    }

    public void setShopId(int shopId)
    {
        mShopId = shopId;
    }

    public String getShopName()
    {
        return mShopName;
    }

    public void setShopName(String shopName)
    {
        mShopName = shopName;
    }

    public String getDate()
    {
        return mDate;
    }

    public void setDate(String date)
    {
        mDate = date;
    }

    @SuppressWarnings("unused")
    public int getDuration()
    {
        return mDuration;
    }

    public void setDuration(int duration)
    {
        mDuration = duration;
    }

    public String getDurationString()
    {
        int duration = mDuration;

        int milliseconds = duration                  % 1000;
        duration         = (duration - milliseconds) / 1000;

        int seconds = duration             % 60;
        duration    = (duration - seconds) / 60;

        int minutes = duration             % 60;
        duration    = (duration - minutes) / 60;

        int hours = duration;

        return String.format(Locale.US, "%1$02d:%2$02d:%3$02d", hours, minutes, seconds);
    }

    public double getTotal()
    {
        return mTotal;
    }

    public void setTotal(double total)
    {
        mTotal = total;
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
        out.writeInt(mShopId);
        out.writeString(mShopName);
        out.writeString(mDate);
        out.writeInt(mDuration);
        out.writeDouble(mTotal);
    }

    public static final Parcelable.Creator<HistoryEntity> CREATOR = new Parcelable.Creator<HistoryEntity>()
    {
        @Override
        public HistoryEntity createFromParcel(Parcel in)
        {
            return new HistoryEntity(in);
        }

        @Override
        public HistoryEntity[] newArray(int size)
        {
            return new HistoryEntity[size];
        }
    };

    private HistoryEntity(Parcel in)
    {
        mId       = in.readInt();
        mShopId   = in.readInt();
        mShopName = in.readString();
        mDate     = in.readString();
        mDuration = in.readInt();
        mTotal    = in.readDouble();
    }
}
