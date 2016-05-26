package ru.okmarket.okgoods.other;

import java.util.Date;

public class ShopInfo
{
    private static final String TAG = "ShopInfo";



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



    public ShopInfo()
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

    @Override
    public String toString()
    {
        return mName;
    }

    public int getId()
    {
        return mId;
    }

    public void setId(int id)
    {
        mId = id;
    }

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

    public Date getOpeningDate()
    {
        return mOpeningDate;
    }

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
}
