package ru.okmarket.okgoods.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.other.ShopInfo;

public class ShopDetailsFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "ShopDetailsFragment";



    private OnFragmentInteractionListener mListener                             = null;
    private TextView                      mNameTextView                         = null;
    private TextView                      mPhoneTextView                        = null;
    private TextView                      mWorkHoursTextView                    = null;
    private TextView                      mSquareTextView                       = null;
    private TextView                      mOpeningDateTextView                  = null;
    private TextView                      mParkingPlacesTextView                = null;
    private TextView                      mNumberOfCashboxesTextView            = null;
    private ImageView                     mServiceClearingSettlementImageView   = null;
    private ImageView                     mServiceCosmeticsImageView            = null;
    private ImageView                     mServicePlaygroundImageView           = null;
    private ImageView                     mServiceFishIslandImageView           = null;
    private ImageView                     mServiceBakeryImageView               = null;
    private ImageView                     mServiceCookeryImageView              = null;
    private ImageView                     mServiceTaxiOrderingImageView         = null;
    private ImageView                     mServicePharmacyImageView             = null;
    private ImageView                     mServiceOrderingFoodImageView         = null;
    private ImageView                     mServiceDegustationImageView          = null;
    private ImageView                     mServiceCafeImageView                 = null;
    private ImageView                     mServiceGiftCardsImageView            = null;
    private ImageView                     mServiceParkingImageView              = null;
    private ImageView                     mServicePointOfIssuingOrdersImageView = null;
    private LinearLayout                  mPhotosLinearLayout                   = null;
    private Button                        mCancelButton                         = null;
    private Button                        mOkButton                             = null;



    public ShopDetailsFragment()
    {
        // Nothing
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_shop_details, container, false);

        mNameTextView                         = (TextView)    rootView.findViewById(R.id.nameTextView);
        mPhoneTextView                        = (TextView)    rootView.findViewById(R.id.phoneTextView);
        mWorkHoursTextView                    = (TextView)    rootView.findViewById(R.id.workHoursTextView);
        mSquareTextView                       = (TextView)    rootView.findViewById(R.id.squareTextView);
        mOpeningDateTextView                  = (TextView)    rootView.findViewById(R.id.openingDateTextView);
        mParkingPlacesTextView                = (TextView)    rootView.findViewById(R.id.parkingPlacesTextView);
        mNumberOfCashboxesTextView            = (TextView)    rootView.findViewById(R.id.numberOfCashboxesTextView);
        mServiceClearingSettlementImageView   = (ImageView)   rootView.findViewById(R.id.serviceClearingSettlementImageView);
        mServiceCosmeticsImageView            = (ImageView)   rootView.findViewById(R.id.serviceCosmeticsImageView);
        mServicePlaygroundImageView           = (ImageView)   rootView.findViewById(R.id.servicePlaygroundImageView);
        mServiceFishIslandImageView           = (ImageView)   rootView.findViewById(R.id.serviceFishIslandImageView);
        mServiceBakeryImageView               = (ImageView)   rootView.findViewById(R.id.serviceBakeryImageView);
        mServiceCookeryImageView              = (ImageView)   rootView.findViewById(R.id.serviceCookeryImageView);
        mServiceTaxiOrderingImageView         = (ImageView)   rootView.findViewById(R.id.serviceTaxiOrderingImageView);
        mServicePharmacyImageView             = (ImageView)   rootView.findViewById(R.id.servicePharmacyImageView);
        mServiceOrderingFoodImageView         = (ImageView)   rootView.findViewById(R.id.serviceOrderingFoodImageView);
        mServiceDegustationImageView          = (ImageView)   rootView.findViewById(R.id.serviceDegustationImageView);
        mServiceCafeImageView                 = (ImageView)   rootView.findViewById(R.id.serviceCafeImageView);
        mServiceGiftCardsImageView            = (ImageView)   rootView.findViewById(R.id.serviceGiftCardsImageView);
        mServiceParkingImageView              = (ImageView)   rootView.findViewById(R.id.serviceParkingImageView);
        mServicePointOfIssuingOrdersImageView = (ImageView)   rootView.findViewById(R.id.servicePointOfIssuingOrdersImageView);
        mPhotosLinearLayout                   = (LinearLayout)rootView.findViewById(R.id.photosLinearLayout);
        mCancelButton                         = (Button)      rootView.findViewById(R.id.cancelButton);
        mOkButton                             = (Button)      rootView.findViewById(R.id.okButton);

        mCancelButton.setOnClickListener(this);
        mOkButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if (view == mCancelButton)
        {
            onCancelClicked();
        }
        else
        if (view == mOkButton)
        {
            onOkClicked();
        }
    }

    public void onCancelClicked()
    {
        if (mListener != null)
        {
            mListener.onShopDetailsCancelClicked();
        }
    }

    public void onOkClicked()
    {
        if (mListener != null)
        {
            mListener.onShopDetailsOkClicked();
        }
    }

    public void updateUI(ShopInfo shop)
    {
        if (shop != null)
        {
            setName(             shop.getName());
            setPhone(            shop.getPhone());
            setWorkHours(        shop.getWorkHours());
            setSquare(           shop.getSquare());
            setOpeningDate(      shop.getOpeningDate());
            setParkingPlaces(    shop.getParkingPlaces());
            setNumberOfCashboxes(shop.getNumberOfCashboxes());

            mServiceClearingSettlementImageView.setVisibility(  (shop.getServicesSet() & MainDatabase.SERVICE_CLEARING_SETTLEMENT_MASK)     != 0 ? View.VISIBLE : View.GONE);
            mServiceCosmeticsImageView.setVisibility(           (shop.getServicesSet() & MainDatabase.SERVICE_COSMETICS_MASK)               != 0 ? View.VISIBLE : View.GONE);
            mServicePlaygroundImageView.setVisibility(          (shop.getServicesSet() & MainDatabase.SERVICE_PLAYGROUND_MASK)              != 0 ? View.VISIBLE : View.GONE);
            mServiceFishIslandImageView.setVisibility(          (shop.getServicesSet() & MainDatabase.SERVICE_FISH_ISLAND_MASK)             != 0 ? View.VISIBLE : View.GONE);
            mServiceBakeryImageView.setVisibility(              (shop.getServicesSet() & MainDatabase.SERVICE_BAKERY_MASK)                  != 0 ? View.VISIBLE : View.GONE);
            mServiceCookeryImageView.setVisibility(             (shop.getServicesSet() & MainDatabase.SERVICE_COOKERY_MASK)                 != 0 ? View.VISIBLE : View.GONE);
            mServiceTaxiOrderingImageView.setVisibility(        (shop.getServicesSet() & MainDatabase.SERVICE_TAXI_ORDERING_MASK)           != 0 ? View.VISIBLE : View.GONE);
            mServicePharmacyImageView.setVisibility(            (shop.getServicesSet() & MainDatabase.SERVICE_PHARMACY_MASK)                != 0 ? View.VISIBLE : View.GONE);
            mServiceOrderingFoodImageView.setVisibility(        (shop.getServicesSet() & MainDatabase.SERVICE_ORDERING_FOOD_MASK)           != 0 ? View.VISIBLE : View.GONE);
            mServiceDegustationImageView.setVisibility(         (shop.getServicesSet() & MainDatabase.SERVICE_DEGUSTATION_MASK)             != 0 ? View.VISIBLE : View.GONE);
            mServiceCafeImageView.setVisibility(                (shop.getServicesSet() & MainDatabase.SERVICE_CAFE_MASK)                    != 0 ? View.VISIBLE : View.GONE);
            mServiceGiftCardsImageView.setVisibility(           (shop.getServicesSet() & MainDatabase.SERVICE_GIFT_CARDS_MASK)              != 0 ? View.VISIBLE : View.GONE);
            mServiceParkingImageView.setVisibility(             (shop.getServicesSet() & MainDatabase.SERVICE_PARKING_MASK)                 != 0 ? View.VISIBLE : View.GONE);
            mServicePointOfIssuingOrdersImageView.setVisibility((shop.getServicesSet() & MainDatabase.SERVICE_POINT_OF_ISSUING_ORDERS_MASK) != 0 ? View.VISIBLE : View.GONE);

            mPhotosLinearLayout.removeAllViews();

            mOkButton.setEnabled(true);
        }
        else
        {
            setName(             null);
            setPhone(            null);
            setWorkHours(        null);
            setSquare(           0);
            setOpeningDate(      null);
            setParkingPlaces(    0);
            setNumberOfCashboxes(0);

            mServiceClearingSettlementImageView.setVisibility(  View.GONE);
            mServiceCosmeticsImageView.setVisibility(           View.GONE);
            mServicePlaygroundImageView.setVisibility(          View.GONE);
            mServiceFishIslandImageView.setVisibility(          View.GONE);
            mServiceBakeryImageView.setVisibility(              View.GONE);
            mServiceCookeryImageView.setVisibility(             View.GONE);
            mServiceTaxiOrderingImageView.setVisibility(        View.GONE);
            mServicePharmacyImageView.setVisibility(            View.GONE);
            mServiceOrderingFoodImageView.setVisibility(        View.GONE);
            mServiceDegustationImageView.setVisibility(         View.GONE);
            mServiceCafeImageView.setVisibility(                View.GONE);
            mServiceGiftCardsImageView.setVisibility(           View.GONE);
            mServiceParkingImageView.setVisibility(             View.GONE);
            mServicePointOfIssuingOrdersImageView.setVisibility(View.GONE);

            mPhotosLinearLayout.removeAllViews();

            mOkButton.setEnabled(false);
        }
    }

    private void setName(String name)
    {
        if (!TextUtils.isEmpty(name))
        {
            mNameTextView.setText(name);
        }
        else
        {
            mNameTextView.setText(R.string.undefined_shop);
        }
    }

    private void setPhone(String phone)
    {
        if (!TextUtils.isEmpty(phone))
        {
            int index = phone.indexOf(',');

            if (index >= 0)
            {
                phone = phone.substring(0, index).trim();
            }

            mPhoneTextView.setText(phone);
        }
        else
        {
            mPhoneTextView.setText(R.string.no_value);
        }
    }

    private void setWorkHours(String workHours)
    {
        if (!TextUtils.isEmpty(workHours))
        {
            if (workHours.equals("0:00 - 24:00"))
            {
                mWorkHoursTextView.setText(R.string.all_day);
            }
            else
            {
                mWorkHoursTextView.setText(workHours);
            }
        }
        else
        {
            mWorkHoursTextView.setText(R.string.no_value);
        }
    }

    private void setSquare(int square)
    {
        if (square > 0)
        {
            mSquareTextView.setText(String.format(getContext().getString(R.string.square_value), square));
        }
        else
        {
            mSquareTextView.setText(R.string.no_value);
        }
    }

    private void setOpeningDate(Date openingDate)
    {
        if (openingDate != null)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

            mOpeningDateTextView.setText(dateFormat.format(openingDate));
        }
        else
        {
            mOpeningDateTextView.setText(R.string.no_value);
        }
    }

    private void setParkingPlaces(int parkingPlaces)
    {
        if (parkingPlaces > 0)
        {
            mParkingPlacesTextView.setText(String.valueOf(parkingPlaces));
        }
        else
        {
            mParkingPlacesTextView.setText(R.string.no_value);
        }
    }

    private void setNumberOfCashboxes(int numberOfCashboxes)
    {
        if (numberOfCashboxes > 0)
        {
            mNumberOfCashboxesTextView.setText(String.valueOf(numberOfCashboxes));
        }
        else
        {
            mNumberOfCashboxesTextView.setText(R.string.no_value);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener)context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        mListener = null;
    }



    public interface OnFragmentInteractionListener
    {
        void onShopDetailsCancelClicked();
        void onShopDetailsOkClicked();
    }
}
