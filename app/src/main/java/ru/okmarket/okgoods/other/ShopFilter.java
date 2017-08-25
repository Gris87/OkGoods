package ru.okmarket.okgoods.other;

import android.os.Parcel;
import android.os.Parcelable;

import ru.okmarket.okgoods.db.entities.ShopEntity;

public final class ShopFilter implements Parcelable
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "ShopFilter";
    // endregion
    // endregion


    // region Attributes
    private boolean mIsHypermarket = false;
    private boolean mIsSupermarket = false;
    private boolean mIsAllDay      = false;
    private int     mServicesSet   = 0;
    // endregion



    @Override
    public String toString()
    {
        return "ShopFilter{" +
                "mIsHypermarket="   + mIsHypermarket +
                ", mIsSupermarket=" + mIsSupermarket +
                ", mIsAllDay="      + mIsAllDay      +
                ", mServicesSet="   + mServicesSet   +
                '}';
    }

    private ShopFilter()
    {
        mIsHypermarket = false;
        mIsSupermarket = false;
        mIsAllDay      = false;
        mServicesSet   = 0;
    }

    @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
    private ShopFilter(ShopFilter another)
    {
        mIsHypermarket = another.mIsHypermarket;
        mIsSupermarket = another.mIsSupermarket;
        mIsAllDay      = another.mIsAllDay;
        mServicesSet   = another.mServicesSet;
    }

    public static ShopFilter newInstance()
    {
        return new ShopFilter();
    }

    public static ShopFilter newInstance(ShopFilter another)
    {
        return new ShopFilter(another);
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

    public boolean isFiltered(ShopEntity shop)
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

    @SuppressWarnings("NumericCastThatLosesPrecision")
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeByte((byte) (mIsHypermarket ? 1 : 0));
        out.writeByte((byte) (mIsSupermarket ? 1 : 0));
        out.writeByte((byte) (mIsAllDay ? 1 : 0));
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

    @SuppressWarnings("UnnecessaryParentheses")
    private ShopFilter(Parcel in)
    {
        mIsHypermarket = (in.readByte() == (byte)1);
        mIsSupermarket = (in.readByte() == (byte)1);
        mIsAllDay      = (in.readByte() == (byte)1);
        mServicesSet   = in.readInt();
    }
}
