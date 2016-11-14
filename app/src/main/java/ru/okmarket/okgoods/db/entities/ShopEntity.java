package ru.okmarket.okgoods.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public final class ShopEntity implements Parcelable
{
    @SuppressWarnings("unused")
    private static final String TAG = "ShopEntity";



    private int     mId;
    private int     mCityId;
    private String  mName;
    private boolean mIsHypermarket;
    private double  mLatitude;
    private double  mLongitude;
    private String  mPhone;
    private String  mWorkHours;
    private int     mSquare;
    private Date    mOpeningDate;
    private int     mParkingPlaces;
    private int     mNumberOfCashboxes;
    private int     mServicesSet;



    @Override
    public String toString()
    {
        return "ShopEntity{" +
                "mId="                  + mId                +
                ", mCityId="            + mCityId            +
                ", mName='"             + mName + '\''       +
                ", mIsHypermarket="     + mIsHypermarket     +
                ", mLatitude="          + mLatitude          +
                ", mLongitude="         + mLongitude         +
                ", mPhone='"            + mPhone + '\''      +
                ", mWorkHours='"        + mWorkHours + '\''  +
                ", mSquare="            + mSquare            +
                ", mOpeningDate="       + mOpeningDate       +
                ", mParkingPlaces="     + mParkingPlaces     +
                ", mNumberOfCashboxes=" + mNumberOfCashboxes +
                ", mServicesSet="       + mServicesSet       +
                '}';
    }

    private ShopEntity()
    {
        mId                = 0;
        mCityId            = 0;
        mName              = null;
        mIsHypermarket     = false;
        mLatitude          = 0;
        mLongitude         = 0;
        mPhone             = null;
        mWorkHours         = null;
        mSquare            = 0;
        mOpeningDate       = null;
        mParkingPlaces     = 0;
        mNumberOfCashboxes = 0;
        mServicesSet       = 0;
    }

    public static ShopEntity newInstance()
    {
        return new ShopEntity();
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

        if (!(object instanceof ShopEntity))
        {
            return false;
        }

        ShopEntity shop = (ShopEntity)object;

        return mId == shop.mId;
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

    @SuppressWarnings("unused")
    public int getCityId()
    {
        return mCityId;
    }

    public void setCityId(int cityId)
    {
        mCityId = cityId;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public boolean isHypermarket()
    {
        return mIsHypermarket;
    }

    public void setIsHypermarket(boolean isHypermarket)
    {
        mIsHypermarket = isHypermarket;
    }

    public double getLatitude()
    {
        return mLatitude;
    }

    public void setLatitude(double latitude)
    {
        mLatitude = latitude;
    }

    public double getLongitude()
    {
        return mLongitude;
    }

    public void setLongitude(double longitude)
    {
        mLongitude = longitude;
    }

    public String getPhone()
    {
        return mPhone;
    }

    public void setPhone(String phone)
    {
        mPhone = phone;
    }

    public String getWorkHours()
    {
        return mWorkHours;
    }

    public void setWorkHours(String workHours)
    {
        mWorkHours = workHours;
    }

    public int getSquare()
    {
        return mSquare;
    }

    public void setSquare(int square)
    {
        mSquare = square;
    }

    @SuppressWarnings("ReturnOfDateField")
    public Date getOpeningDate()
    {
        return mOpeningDate;
    }

    @SuppressWarnings("AssignmentToDateFieldFromParameter")
    public void setOpeningDate(Date openingDate)
    {
        mOpeningDate = openingDate;
    }

    public int getParkingPlaces()
    {
        return mParkingPlaces;
    }

    public void setParkingPlaces(int parkingPlaces)
    {
        mParkingPlaces = parkingPlaces;
    }

    public int getNumberOfCashboxes()
    {
        return mNumberOfCashboxes;
    }

    public void setNumberOfCashboxes(int numberOfCashboxes)
    {
        mNumberOfCashboxes = numberOfCashboxes;
    }

    public int getServicesSet()
    {
        return mServicesSet;
    }

    public void setServicesSet(int servicesSet)
    {
        mServicesSet = servicesSet;
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
        out.writeInt(mCityId);
        out.writeString(mName);
        //noinspection NumericCastThatLosesPrecision
        out.writeByte((byte)(mIsHypermarket ? 1 : 0));
        out.writeDouble(mLatitude);
        out.writeDouble(mLongitude);
        out.writeString(mPhone);
        out.writeString(mWorkHours);
        out.writeInt(mSquare);
        out.writeLong(mOpeningDate != null ? mOpeningDate.getTime() : -1);
        out.writeInt(mParkingPlaces);
        out.writeInt(mNumberOfCashboxes);
        out.writeInt(mServicesSet);
    }

    public static final Parcelable.Creator<ShopEntity> CREATOR = new Parcelable.Creator<ShopEntity>()
    {
        @Override
        public ShopEntity createFromParcel(Parcel in)
        {
            return new ShopEntity(in);
        }

        @Override
        public ShopEntity[] newArray(int size)
        {
            return new ShopEntity[size];
        }
    };

    private ShopEntity(Parcel in)
    {
        mId                = in.readInt();
        mCityId            = in.readInt();
        mName              = in.readString();
        mIsHypermarket     = in.readByte() == 1;
        mLatitude          = in.readDouble();
        mLongitude         = in.readDouble();
        mPhone             = in.readString();
        mWorkHours         = in.readString();
        mSquare            = in.readInt();
        long openingDate   = in.readLong();
        mOpeningDate       = openingDate == -1 ? null : new Date(openingDate);
        mParkingPlaces     = in.readInt();
        mNumberOfCashboxes = in.readInt();
        mServicesSet       = in.readInt();
    }
}
