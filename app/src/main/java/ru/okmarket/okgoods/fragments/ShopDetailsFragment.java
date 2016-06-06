package ru.okmarket.okgoods.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.other.ShopInfo;
import ru.okmarket.okgoods.util.AppLog;
import ru.okmarket.okgoods.widgets.ImageViewWithTooltip;

public class ShopDetailsFragment extends Fragment implements View.OnTouchListener, View.OnClickListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "ShopDetailsFragment";



    private OnFragmentInteractionListener mListener                             = null;
    private TextView                      mNameTextView                         = null;
    private TextView                      mPhoneTextView                        = null;
    private TextView                      mWorkHoursTextView                    = null;
    private TextView                      mSquareTextView                       = null;
    private TextView                      mOpeningDateTextView                  = null;
    private TextView                      mParkingPlacesTextView                = null;
    private TextView                      mNumberOfCashboxesTextView            = null;
    private View                          mServicesScrollView                   = null;
    private ImageViewWithTooltip          mServiceClearingSettlementImageView   = null;
    private ImageViewWithTooltip          mServiceCosmeticsImageView            = null;
    private ImageViewWithTooltip          mServicePlaygroundImageView           = null;
    private ImageViewWithTooltip          mServiceFishIslandImageView           = null;
    private ImageViewWithTooltip          mServiceBakeryImageView               = null;
    private ImageViewWithTooltip          mServiceCookeryImageView              = null;
    private ImageViewWithTooltip          mServiceTaxiOrderingImageView         = null;
    private ImageViewWithTooltip          mServicePharmacyImageView             = null;
    private ImageViewWithTooltip          mServiceOrderingFoodImageView         = null;
    private ImageViewWithTooltip          mServiceDegustationImageView          = null;
    private ImageViewWithTooltip          mServiceCafeImageView                 = null;
    private ImageViewWithTooltip          mServiceGiftCardsImageView            = null;
    private ImageViewWithTooltip          mServiceParkingImageView              = null;
    private ImageViewWithTooltip          mServicePointOfIssuingOrdersImageView = null;
    private View                          mPhotosScrollView                     = null;
    private LinearLayout                  mPhotosLinearLayout                   = null;
    private Button                        mCancelButton                         = null;
    private Button                        mOkButton                             = null;
    private GetShopPhotosTask             mGetShopPhotosTask                    = null;



    public ShopDetailsFragment()
    {
        // Nothing
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_shop_details, container, false);



        mNameTextView                         = (TextView)            rootView.findViewById(R.id.nameTextView);
        mPhoneTextView                        = (TextView)            rootView.findViewById(R.id.phoneTextView);
        mWorkHoursTextView                    = (TextView)            rootView.findViewById(R.id.workHoursTextView);
        mSquareTextView                       = (TextView)            rootView.findViewById(R.id.squareTextView);
        mOpeningDateTextView                  = (TextView)            rootView.findViewById(R.id.openingDateTextView);
        mParkingPlacesTextView                = (TextView)            rootView.findViewById(R.id.parkingPlacesTextView);
        mNumberOfCashboxesTextView            = (TextView)            rootView.findViewById(R.id.numberOfCashboxesTextView);
        mServicesScrollView                   =                       rootView.findViewById(R.id.servicesScrollView);
        mServiceClearingSettlementImageView   = (ImageViewWithTooltip)rootView.findViewById(R.id.serviceClearingSettlementImageView);
        mServiceCosmeticsImageView            = (ImageViewWithTooltip)rootView.findViewById(R.id.serviceCosmeticsImageView);
        mServicePlaygroundImageView           = (ImageViewWithTooltip)rootView.findViewById(R.id.servicePlaygroundImageView);
        mServiceFishIslandImageView           = (ImageViewWithTooltip)rootView.findViewById(R.id.serviceFishIslandImageView);
        mServiceBakeryImageView               = (ImageViewWithTooltip)rootView.findViewById(R.id.serviceBakeryImageView);
        mServiceCookeryImageView              = (ImageViewWithTooltip)rootView.findViewById(R.id.serviceCookeryImageView);
        mServiceTaxiOrderingImageView         = (ImageViewWithTooltip)rootView.findViewById(R.id.serviceTaxiOrderingImageView);
        mServicePharmacyImageView             = (ImageViewWithTooltip)rootView.findViewById(R.id.servicePharmacyImageView);
        mServiceOrderingFoodImageView         = (ImageViewWithTooltip)rootView.findViewById(R.id.serviceOrderingFoodImageView);
        mServiceDegustationImageView          = (ImageViewWithTooltip)rootView.findViewById(R.id.serviceDegustationImageView);
        mServiceCafeImageView                 = (ImageViewWithTooltip)rootView.findViewById(R.id.serviceCafeImageView);
        mServiceGiftCardsImageView            = (ImageViewWithTooltip)rootView.findViewById(R.id.serviceGiftCardsImageView);
        mServiceParkingImageView              = (ImageViewWithTooltip)rootView.findViewById(R.id.serviceParkingImageView);
        mServicePointOfIssuingOrdersImageView = (ImageViewWithTooltip)rootView.findViewById(R.id.servicePointOfIssuingOrdersImageView);
        mPhotosScrollView                     =                       rootView.findViewById(R.id.photosScrollView);
        mPhotosLinearLayout                   = (LinearLayout)        rootView.findViewById(R.id.photosLinearLayout);
        mCancelButton                         = (Button)              rootView.findViewById(R.id.cancelButton);
        mOkButton                             = (Button)              rootView.findViewById(R.id.okButton);

        mGetShopPhotosTask = null;



        mServicesScrollView.setOnTouchListener(this);
        mPhotosScrollView.setOnTouchListener(this);

        mCancelButton.setOnClickListener(this);
        mOkButton.setOnClickListener(this);



        return rootView;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mGetShopPhotosTask != null)
        {
            mGetShopPhotosTask.cancel(true);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        if (
            view == mServicesScrollView
            ||
            view == mPhotosScrollView
           )
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                onDisableScroll();
            }
            else
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                onEnableScroll();
            }
        }
        else
        {
            AppLog.wtf(TAG, "Unknown view");
        }

        return false;
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
        else
        {
            AppLog.wtf(TAG, "Unknown view");
        }
    }

    public void onDisableScroll()
    {
        if (mListener != null)
        {
            mListener.onShopDetailsDisableScroll();
        }
    }

    public void onEnableScroll()
    {
        if (mListener != null)
        {
            mListener.onShopDetailsEnableScroll();
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



            if (mGetShopPhotosTask != null)
            {
                mGetShopPhotosTask.cancel(true);
            }

            mGetShopPhotosTask = new GetShopPhotosTask();
            mGetShopPhotosTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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



            if (mGetShopPhotosTask != null)
            {
                mGetShopPhotosTask.cancel(true);
                mGetShopPhotosTask = null;
            }
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



    private class GetShopPhotosTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {

            }
            catch (Exception e)
            {
                // Nothing
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            if (result)
            {
                // TODO: Implement it
            }

            mGetShopPhotosTask = null;
        }
    }



    public interface OnFragmentInteractionListener
    {
        void onShopDetailsDisableScroll();
        void onShopDetailsEnableScroll();
        void onShopDetailsCancelClicked();
        void onShopDetailsOkClicked();
    }
}
