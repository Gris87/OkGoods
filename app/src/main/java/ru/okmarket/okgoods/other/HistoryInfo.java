package ru.okmarket.okgoods.other;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class HistoryInfo implements Parcelable
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryInfo";



    private int     mId;
    private String  mShopName;
    private String  mDate;
    private int     mDuration;
    private double  mTotal;



    public HistoryInfo()
    {
        mId       = 0;
        mShopName = null;
        mDate     = null;
        mDuration = 0;
        mTotal    = 0;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.US, "{shopName = %s, date = %s, duration = %d, total = %f}"
                , String.valueOf(mShopName)
                , String.valueOf(mDate)
                , mDuration
                , mTotal
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

        if (!(object instanceof HistoryInfo))
        {
            return false;
        }

        HistoryInfo history = (HistoryInfo)object;

        return mId == history.mId;
    }

    public int getId()
    {
        return mId;
    }

    public void setId(int id)
    {
        mId = id;
    }

    public String getShopName()
    {
        return mShopName;
    }

    public void setShopName(String mShopName)
    {
        this.mShopName = mShopName;
    }

    public String getDate()
    {
        return mDate;
    }

    public void setDate(String date)
    {
        mDate = date;
    }

    public int getDuration()
    {
        return mDuration;
    }

    public void setDuration(int duration)
    {
        mDuration = duration;
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
        out.writeString(mShopName);
        out.writeString(mDate);
        out.writeInt(mDuration);
        out.writeDouble(mTotal);
    }

    public static final Parcelable.Creator<HistoryInfo> CREATOR = new Parcelable.Creator<HistoryInfo>()
    {
        @Override
        public HistoryInfo createFromParcel(Parcel in)
        {
            return new HistoryInfo(in);
        }

        @Override
        public HistoryInfo[] newArray(int size)
        {
            return new HistoryInfo[size];
        }
    };

    private HistoryInfo(Parcel in)
    {
        mId       = in.readInt();
        mShopName = in.readString();
        mDate     = in.readString();
        mDuration = in.readInt();
        mTotal    = in.readDouble();
    }
}