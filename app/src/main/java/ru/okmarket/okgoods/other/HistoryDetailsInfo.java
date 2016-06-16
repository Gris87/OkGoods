package ru.okmarket.okgoods.other;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class HistoryDetailsInfo implements Parcelable
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryDetailsInfo";



    private int     mId;
    private int     mGoodId;
    private String  mGoodName;
    private double  mCost;
    private double  mCount;



    public HistoryDetailsInfo()
    {
        mId       = 0;
        mGoodId   = 0;
        mGoodName = null;
        mCost     = 0;
        mCount    = 0;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.US, "{id = %d, goodName = %s, cost = %f, count = %f}"
                , mId
                , String.valueOf(mGoodName)
                , mCost
                , mCount
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

        if (!(object instanceof HistoryDetailsInfo))
        {
            return false;
        }

        HistoryDetailsInfo historyDetails = (HistoryDetailsInfo)object;

        return mId == historyDetails.mId;
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

    public String getGoodName() 
    {
        return mGoodName;
    }

    public void setGoodName(String goodName)
    {
        mGoodName = goodName;
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
        out.writeString(mGoodName);
        out.writeDouble(mCost);
        out.writeDouble(mCount);
    }

    public static final Parcelable.Creator<HistoryDetailsInfo> CREATOR = new Parcelable.Creator<HistoryDetailsInfo>()
    {
        @Override
        public HistoryDetailsInfo createFromParcel(Parcel in)
        {
            return new HistoryDetailsInfo(in);
        }

        @Override
        public HistoryDetailsInfo[] newArray(int size)
        {
            return new HistoryDetailsInfo[size];
        }
    };

    private HistoryDetailsInfo(Parcel in)
    {
        mId       = in.readInt();
        mGoodId   = in.readInt();
        mGoodName = in.readString();
        mCost     = in.readDouble();
        mCount    = in.readDouble();
    }
}
