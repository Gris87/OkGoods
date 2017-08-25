package ru.okmarket.okgoods.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.db.entities.ShopEntity;
import ru.okmarket.okgoods.net.HttpClient;
import ru.okmarket.okgoods.net.Web;
import ru.okmarket.okgoods.util.AppLog;
import ru.okmarket.okgoods.widgets.CachedImageView;
import ru.okmarket.okgoods.widgets.ImageViewWithTooltip;

@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
public class ShopDetailsFragment extends Fragment implements View.OnTouchListener, View.OnClickListener
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "ShopDetailsFragment";
    // endregion
    // endregion



    // region Attributes
    private OnFragmentInteractionListener mListener                             = null;
    private TextView                      mNameTextView                         = null;
    private TextView                      mPhoneTextView                        = null;
    private TextView                      mWorkHoursTextView                    = null;
    private TextView                      mSquareTextView                       = null;
    private TextView                      mOpeningDateTextView                  = null;
    private TextView                      mParkingPlacesTextView                = null;
    private TextView                      mNumberOfCashboxesTextView            = null;
    private TextView                      mServicesTextView                     = null;
    private HorizontalScrollView          mServicesHorizontalScrollView         = null;
    private ScrollView                    mServicesVerticalScrollView           = null;
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
    private TextView                      mPhotosTextView                       = null;
    private ProgressBar                   mPhotosProgressBar                    = null;
    private HorizontalScrollView          mPhotosHorizontalScrollView           = null;
    private ScrollView                    mPhotosVerticalScrollView             = null;
    private LinearLayout                  mPhotosLinearLayout                   = null;
    private Button                        mCancelButton                         = null;
    private Button                        mOkButton                             = null;
    private HttpClient                    mHttpClient                           = null;
    // endregion



    @Override
    public String toString()
    {
        return "ShopDetailsFragment{" +
                "mListener="                               + mListener                             +
                ", mNameTextView="                         + mNameTextView                         +
                ", mPhoneTextView="                        + mPhoneTextView                        +
                ", mWorkHoursTextView="                    + mWorkHoursTextView                    +
                ", mSquareTextView="                       + mSquareTextView                       +
                ", mOpeningDateTextView="                  + mOpeningDateTextView                  +
                ", mParkingPlacesTextView="                + mParkingPlacesTextView                +
                ", mNumberOfCashboxesTextView="            + mNumberOfCashboxesTextView            +
                ", mServicesTextView="                     + mServicesTextView                     +
                ", mServicesHorizontalScrollView="         + mServicesHorizontalScrollView         +
                ", mServicesVerticalScrollView="           + mServicesVerticalScrollView           +
                ", mServiceClearingSettlementImageView="   + mServiceClearingSettlementImageView   +
                ", mServiceCosmeticsImageView="            + mServiceCosmeticsImageView            +
                ", mServicePlaygroundImageView="           + mServicePlaygroundImageView           +
                ", mServiceFishIslandImageView="           + mServiceFishIslandImageView           +
                ", mServiceBakeryImageView="               + mServiceBakeryImageView               +
                ", mServiceCookeryImageView="              + mServiceCookeryImageView              +
                ", mServiceTaxiOrderingImageView="         + mServiceTaxiOrderingImageView         +
                ", mServicePharmacyImageView="             + mServicePharmacyImageView             +
                ", mServiceOrderingFoodImageView="         + mServiceOrderingFoodImageView         +
                ", mServiceDegustationImageView="          + mServiceDegustationImageView          +
                ", mServiceCafeImageView="                 + mServiceCafeImageView                 +
                ", mServiceGiftCardsImageView="            + mServiceGiftCardsImageView            +
                ", mServiceParkingImageView="              + mServiceParkingImageView              +
                ", mServicePointOfIssuingOrdersImageView=" + mServicePointOfIssuingOrdersImageView +
                ", mPhotosTextView="                       + mPhotosTextView                       +
                ", mPhotosProgressBar="                    + mPhotosProgressBar                    +
                ", mPhotosHorizontalScrollView="           + mPhotosHorizontalScrollView           +
                ", mPhotosVerticalScrollView="             + mPhotosVerticalScrollView             +
                ", mPhotosLinearLayout="                   + mPhotosLinearLayout                   +
                ", mCancelButton="                         + mCancelButton                         +
                ", mOkButton="                             + mOkButton                             +
                ", mHttpClient="                           + mHttpClient                           +
                '}';
    }

    @SuppressWarnings("RedundantCast")
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
        mServicesTextView                     = (TextView)            rootView.findViewById(R.id.servicesTextView);
        mServicesHorizontalScrollView         = (HorizontalScrollView)rootView.findViewById(R.id.servicesHorizontalScrollView);
        mServicesVerticalScrollView           = (ScrollView)          rootView.findViewById(R.id.servicesVerticalScrollView);
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
        mPhotosTextView                       = (TextView)            rootView.findViewById(R.id.photosTextView);
        mPhotosProgressBar                    = (ProgressBar)         rootView.findViewById(R.id.photosProgressBar);
        mPhotosHorizontalScrollView           = (HorizontalScrollView)rootView.findViewById(R.id.photosHorizontalScrollView);
        mPhotosVerticalScrollView             = (ScrollView)          rootView.findViewById(R.id.photosVerticalScrollView);
        mPhotosLinearLayout                   = (LinearLayout)        rootView.findViewById(R.id.photosLinearLayout);
        mCancelButton                         = (Button)              rootView.findViewById(R.id.cancelButton);
        mOkButton                             = (Button)              rootView.findViewById(R.id.okButton);



        mHttpClient = HttpClient.getInstance(getActivity());



        mServicesHorizontalScrollView.setOnTouchListener(this);
        mServicesVerticalScrollView.setOnTouchListener(this);
        mServiceClearingSettlementImageView.setOnTouchListener(this);
        mServiceCosmeticsImageView.setOnTouchListener(this);
        mServicePlaygroundImageView.setOnTouchListener(this);
        mServiceFishIslandImageView.setOnTouchListener(this);
        mServiceBakeryImageView.setOnTouchListener(this);
        mServiceCookeryImageView.setOnTouchListener(this);
        mServiceTaxiOrderingImageView.setOnTouchListener(this);
        mServicePharmacyImageView.setOnTouchListener(this);
        mServiceOrderingFoodImageView.setOnTouchListener(this);
        mServiceDegustationImageView.setOnTouchListener(this);
        mServiceCafeImageView.setOnTouchListener(this);
        mServiceGiftCardsImageView.setOnTouchListener(this);
        mServiceParkingImageView.setOnTouchListener(this);
        mServicePointOfIssuingOrdersImageView.setOnTouchListener(this);
        mPhotosHorizontalScrollView.setOnTouchListener(this);
        mPhotosVerticalScrollView.setOnTouchListener(this);



        mCancelButton.setOnClickListener(this);
        mOkButton.setOnClickListener(this);



        return rootView;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mHttpClient.cancelAll(TAG);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event)
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
            AppLog.wtf(TAG, "Unknown view: " + view);
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

    public void onPhotoClicked(ArrayList<String> urls, int selectedIndex)
    {
        if (mListener != null)
        {
            mListener.onShopDetailsPhotoClicked(urls, selectedIndex);
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

    public void updateUI(ShopEntity shop)
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

            mServicesTextView.setVisibility(            View.VISIBLE);
            mServicesHorizontalScrollView.setVisibility(View.VISIBLE);
            mServicesVerticalScrollView.setVisibility(  View.VISIBLE);
            mPhotosTextView.setVisibility(              View.VISIBLE);
            mPhotosProgressBar.setVisibility(           View.VISIBLE);
            mPhotosHorizontalScrollView.setVisibility(  View.GONE);
            mPhotosVerticalScrollView.setVisibility(    View.GONE);

            mServiceClearingSettlementImageView.setVisibility(  (shop.getServicesSet() & MainDatabase.SERVICE_CLEARING_SETTLEMENT_MASK)     == 0 ? View.GONE : View.VISIBLE);
            mServiceCosmeticsImageView.setVisibility(           (shop.getServicesSet() & MainDatabase.SERVICE_COSMETICS_MASK)               == 0 ? View.GONE : View.VISIBLE);
            mServicePlaygroundImageView.setVisibility(          (shop.getServicesSet() & MainDatabase.SERVICE_PLAYGROUND_MASK)              == 0 ? View.GONE : View.VISIBLE);
            mServiceFishIslandImageView.setVisibility(          (shop.getServicesSet() & MainDatabase.SERVICE_FISH_ISLAND_MASK)             == 0 ? View.GONE : View.VISIBLE);
            mServiceBakeryImageView.setVisibility(              (shop.getServicesSet() & MainDatabase.SERVICE_BAKERY_MASK)                  == 0 ? View.GONE : View.VISIBLE);
            mServiceCookeryImageView.setVisibility(             (shop.getServicesSet() & MainDatabase.SERVICE_COOKERY_MASK)                 == 0 ? View.GONE : View.VISIBLE);
            mServiceTaxiOrderingImageView.setVisibility(        (shop.getServicesSet() & MainDatabase.SERVICE_TAXI_ORDERING_MASK)           == 0 ? View.GONE : View.VISIBLE);
            mServicePharmacyImageView.setVisibility(            (shop.getServicesSet() & MainDatabase.SERVICE_PHARMACY_MASK)                == 0 ? View.GONE : View.VISIBLE);
            mServiceOrderingFoodImageView.setVisibility(        (shop.getServicesSet() & MainDatabase.SERVICE_ORDERING_FOOD_MASK)           == 0 ? View.GONE : View.VISIBLE);
            mServiceDegustationImageView.setVisibility(         (shop.getServicesSet() & MainDatabase.SERVICE_DEGUSTATION_MASK)             == 0 ? View.GONE : View.VISIBLE);
            mServiceCafeImageView.setVisibility(                (shop.getServicesSet() & MainDatabase.SERVICE_CAFE_MASK)                    == 0 ? View.GONE : View.VISIBLE);
            mServiceGiftCardsImageView.setVisibility(           (shop.getServicesSet() & MainDatabase.SERVICE_GIFT_CARDS_MASK)              == 0 ? View.GONE : View.VISIBLE);
            mServiceParkingImageView.setVisibility(             (shop.getServicesSet() & MainDatabase.SERVICE_PARKING_MASK)                 == 0 ? View.GONE : View.VISIBLE);
            mServicePointOfIssuingOrdersImageView.setVisibility((shop.getServicesSet() & MainDatabase.SERVICE_POINT_OF_ISSUING_ORDERS_MASK) == 0 ? View.GONE : View.VISIBLE);

            mPhotosLinearLayout.removeAllViews();

            mOkButton.setEnabled(true);



            mHttpClient.cancelAll(TAG);



            StringRequest request = new StringRequest(Request.Method.GET, Web.getShopUrl(shop.getId())
                    , new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            mPhotosProgressBar.setVisibility(         View.GONE);
                            mPhotosHorizontalScrollView.setVisibility(View.VISIBLE);
                            mPhotosVerticalScrollView.setVisibility(  View.VISIBLE);



                            Resources resources = getResources();

                            int height = resources.getDimensionPixelSize(R.dimen.shop_photo_size);
                            int width  = (height << 2) / 3;
                            int margin = resources.getDimensionPixelSize(R.dimen.common_margin);



                            final ArrayList<String> urls = Web.getShopPhotosUrlsFromResponse(response);

                            for (int i = 0; i < urls.size(); ++i)
                            {
                                CachedImageView imageView = new CachedImageView(getActivity());
                                imageView.setErrorImageResId(R.drawable.download_error);
                                imageView.setImageUrl(urls.get(i), mHttpClient.getImageLoader());

                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);

                                if (i > 0)
                                {
                                    if (mPhotosLinearLayout.getOrientation() == LinearLayout.HORIZONTAL)
                                    {
                                        layoutParams.setMargins(margin, 0, 0, 0);
                                    }
                                    else
                                    {
                                        layoutParams.setMargins(0, margin, 0, 0);
                                    }
                                }

                                imageView.setOnTouchListener(ShopDetailsFragment.this);
                                imageView.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        onPhotoClicked(urls, mPhotosLinearLayout.indexOfChild(view));
                                    }
                                });

                                mPhotosLinearLayout.addView(imageView, layoutParams);
                            }
                        }
                    }
                    ,  new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            AppLog.w(TAG, "Failed to get photos for shop: " + mNameTextView.getText());

                            mPhotosProgressBar.setVisibility(View.GONE);
                        }
                    }
            );

            request.setTag(TAG);

            mHttpClient.addToRequestQueue(request);
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

            mServicesTextView.setVisibility(            View.GONE);
            mServicesHorizontalScrollView.setVisibility(View.GONE);
            mServicesVerticalScrollView.setVisibility(  View.GONE);
            mPhotosTextView.setVisibility(              View.GONE);
            mPhotosProgressBar.setVisibility(           View.GONE);
            mPhotosHorizontalScrollView.setVisibility(  View.GONE);
            mPhotosVerticalScrollView.setVisibility(    View.GONE);

            mPhotosLinearLayout.removeAllViews();

            mOkButton.setEnabled(false);



            mHttpClient.cancelAll(TAG);
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
            mNameTextView.setText(R.string.shop_details_undefined_shop);
        }
    }

    private void setPhone(String phone)
    {
        String res = phone;

        if (!TextUtils.isEmpty(res))
        {
            int index = res.indexOf(',');

            if (index >= 0)
            {
                res = res.substring(0, index).trim();
            }

            mPhoneTextView.setText(res);
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
                mWorkHoursTextView.setText(R.string.shop_details_all_day);
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
            mSquareTextView.setText(getString(R.string.shop_details_square_value, square));
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
            //noinspection ProhibitedExceptionThrown
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        mListener = null;
    }



    @SuppressWarnings("PublicInnerClass")
    public interface OnFragmentInteractionListener
    {
        void onShopDetailsDisableScroll();
        void onShopDetailsEnableScroll();
        void onShopDetailsPhotoClicked(ArrayList<String> urls, int selectedIndex);
        void onShopDetailsCancelClicked();
        void onShopDetailsOkClicked();
    }
}
