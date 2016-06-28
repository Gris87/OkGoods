package ru.okmarket.okgoods.other;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

import ru.okmarket.okgoods.db.entities.ShopInfo;

public class ShopFilter implements Parcelable
{
    @SuppressWarnings("unused")
    private static final String TAG = "ShopFilter";


    private boolean mIsHypermarket;
    private boolean mIsSupermarket;
    private boolean mIsAllDay;
    private int     mServicesSet;



    public ShopFilter()
    {
        mIsHypermarket = false;
        mIsSupermarket = false;
        mIsAllDay      = false;
        mServicesSet   = 0;
    }

    public ShopFilter(ShopFilter another)
    {
        mIsHypermarket = another.mIsHypermarket;
        mIsSupermarket = another.mIsSupermarket;
        mIsAllDay      = another.mIsAllDay;
        mServicesSet   = another.mServicesSet;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.US, "{isHypermarket = %1$d, isSupermarket = %2$d, isAllDay = %3$d, servicesSet = %4$d}"
                                        , mIsHypermarket ? 1 : 0
                                        , mIsSupermarket ? 1 : 0
                                        , mIsAllDay      ? 1 : 0
                                        , mServicesSet
        );
    }

    public boolean isHypermarket()
    {
        return mIsHypermarket;
    }

    public void setIsHypermarket(boolean isHypermarket)
    {
        mIsHypermarket = isHypermarket;
    }

    public boolean isSupermarket()
    {
        return mIsSupermarket;
    }

    public void setIsSupermarket(boolean isSupermarket)
    {
        mIsSupermarket = isSupermarket;
    }

    public boolean isAllDay()
    {
        return mIsAllDay;
    }

    public void setIsAllDay(boolean isAllDay)
    {
        mIsAllDay = isAllDay;
    }

    public int getServicesSet()
    {
        return mServicesSet;
    }

    public void setServicesSet(int servicesSet)
    {
        mServicesSet = servicesSet;
    }

    public boolean isFiltered(ShopInfo shop)
    {
        if (mIsHypermarket)
        {
            if (!mIsSupermarket)
            {
                if (!shop.isHypermarket())
                {
                    return false;
                }
            }
        }
        else
        {
            if (mIsSupermarket)
            {
                if (shop.isHypermarket())
                {
                    return false;
                }
            }
        }

        return (!mIsAllDay        || shop.getWorkHours().equals("0:00 - 24:00"))
               &&
               (mServicesSet == 0 || (shop.getServicesSet() & mServicesSet) == mServicesSet);

    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeByte(mIsHypermarket ? (byte)1 : (byte)0);
        out.writeByte(mIsSupermarket ? (byte)1 : (byte)0);
        out.writeByte(mIsAllDay      ? (byte)1 : (byte)0);
        out.writeInt(mServicesSet);
    }

    public static final Parcelable.Creator<ShopFilter> CREATOR = new Parcelable.Creator<ShopFilter>()
    {
        @Override
        public ShopFilter createFromParcel(Parcel in)
        {
            return new ShopFilter(in);
        }

        @Override
        public ShopFilter[] newArray(int size)
        {
            return new ShopFilter[size];
        }
    };

    private ShopFilter(Parcel in)
    {
        mIsHypermarket = (in.readByte() == (byte)1);
        mIsSupermarket = (in.readByte() == (byte)1);
        mIsAllDay      = (in.readByte() == (byte)1);
        mServicesSet   = in.readInt();
    }
}
