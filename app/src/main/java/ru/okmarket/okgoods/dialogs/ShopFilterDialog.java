package ru.okmarket.okgoods.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.other.ShopFilter;
import ru.okmarket.okgoods.util.AppLog;
import ru.okmarket.okgoods.widgets.ImageButtonWithTooltip;

public class ShopFilterDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "ShopFilterDialog";



    private static final String ARG_SHOP_FILTER = "SHOP_FILTER";



    private OnFragmentInteractionListener mListener                          = null;
    private CheckBox                      mSupermarketCheckBox               = null;
    private CheckBox                      mHypermarketCheckBox               = null;
    private CheckBox                      mAllDayCheckBox                    = null;
    private ImageButtonWithTooltip        mServiceClearingSettlementButton   = null;
    private ImageButtonWithTooltip        mServiceCosmeticsButton            = null;
    private ImageButtonWithTooltip        mServicePlaygroundButton           = null;
    private ImageButtonWithTooltip        mServiceFishIslandButton           = null;
    private ImageButtonWithTooltip        mServiceBakeryButton               = null;
    private ImageButtonWithTooltip        mServiceCookeryButton              = null;
    private ImageButtonWithTooltip        mServiceTaxiOrderingButton         = null;
    private ImageButtonWithTooltip        mServicePharmacyButton             = null;
    private ImageButtonWithTooltip        mServiceDegustationButton          = null;
    private ImageButtonWithTooltip        mServiceOrderingFoodButton         = null;
    private ImageButtonWithTooltip        mServiceCafeButton                 = null;
    private ImageButtonWithTooltip        mServiceGiftCardsButton            = null;
    private ImageButtonWithTooltip        mServiceParkingButton              = null;
    private ImageButtonWithTooltip        mServicePointOfIssuingOrdersButton = null;
    private ShopFilter                    mShopFilter                        = null;



    public static ShopFilterDialog newInstance(ShopFilter filter)
    {
        ShopFilterDialog fragment = new ShopFilterDialog();

        Bundle args = new Bundle();
        args.putParcelable(ARG_SHOP_FILTER, new ShopFilter(filter));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mShopFilter = getArguments().getParcelable(ARG_SHOP_FILTER);
    }

    @Override
    @NonNull
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.dialog_shop_filter, null, false);

        mSupermarketCheckBox               = (CheckBox)              rootView.findViewById(R.id.supermarketCheckBox);
        mHypermarketCheckBox               = (CheckBox)              rootView.findViewById(R.id.hypermarketCheckBox);
        mAllDayCheckBox                    = (CheckBox)              rootView.findViewById(R.id.allDayCheckBox);
        mServiceClearingSettlementButton   = (ImageButtonWithTooltip)rootView.findViewById(R.id.serviceClearingSettlementButton);
        mServiceCosmeticsButton            = (ImageButtonWithTooltip)rootView.findViewById(R.id.serviceCosmeticsButton);
        mServicePlaygroundButton           = (ImageButtonWithTooltip)rootView.findViewById(R.id.servicePlaygroundButton);
        mServiceFishIslandButton           = (ImageButtonWithTooltip)rootView.findViewById(R.id.serviceFishIslandButton);
        mServiceBakeryButton               = (ImageButtonWithTooltip)rootView.findViewById(R.id.serviceBakeryButton);
        mServiceCookeryButton              = (ImageButtonWithTooltip)rootView.findViewById(R.id.serviceCookeryButton);
        mServiceTaxiOrderingButton         = (ImageButtonWithTooltip)rootView.findViewById(R.id.serviceTaxiOrderingButton);
        mServicePharmacyButton             = (ImageButtonWithTooltip)rootView.findViewById(R.id.servicePharmacyButton);
        mServiceDegustationButton          = (ImageButtonWithTooltip)rootView.findViewById(R.id.serviceDegustationButton);
        mServiceOrderingFoodButton         = (ImageButtonWithTooltip)rootView.findViewById(R.id.serviceOrderingFoodButton);
        mServiceCafeButton                 = (ImageButtonWithTooltip)rootView.findViewById(R.id.serviceCafeButton);
        mServiceGiftCardsButton            = (ImageButtonWithTooltip)rootView.findViewById(R.id.serviceGiftCardsButton);
        mServiceParkingButton              = (ImageButtonWithTooltip)rootView.findViewById(R.id.serviceParkingButton);
        mServicePointOfIssuingOrdersButton = (ImageButtonWithTooltip)rootView.findViewById(R.id.servicePointOfIssuingOrdersButton);



        updateUI();



        mSupermarketCheckBox.setOnCheckedChangeListener(this);
        mHypermarketCheckBox.setOnCheckedChangeListener(this);
        mAllDayCheckBox.setOnCheckedChangeListener(this);
        mServiceClearingSettlementButton.setOnClickListener(this);
        mServiceCosmeticsButton.setOnClickListener(this);
        mServicePlaygroundButton.setOnClickListener(this);
        mServiceFishIslandButton.setOnClickListener(this);
        mServiceBakeryButton.setOnClickListener(this);
        mServiceCookeryButton.setOnClickListener(this);
        mServiceTaxiOrderingButton.setOnClickListener(this);
        mServicePharmacyButton.setOnClickListener(this);
        mServiceDegustationButton.setOnClickListener(this);
        mServiceOrderingFoodButton.setOnClickListener(this);
        mServiceCafeButton.setOnClickListener(this);
        mServiceGiftCardsButton.setOnClickListener(this);
        mServiceParkingButton.setOnClickListener(this);
        mServicePointOfIssuingOrdersButton.setOnClickListener(this);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.filter)
                .setCancelable(true)
                .setView(rootView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        onFilterApplied();

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (buttonView == mSupermarketCheckBox)
        {
            mShopFilter.setIsSupermarket(isChecked);
        }
        else
        if (buttonView == mHypermarketCheckBox)
        {
            mShopFilter.setIsHypermarket(isChecked);
        }
        else
        if (buttonView == mAllDayCheckBox)
        {
            mShopFilter.setIsAllDay(isChecked);
        }
        else
        {
            AppLog.wtf(TAG, "Unknown button view");
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view == mServiceClearingSettlementButton)
        {
            toggleServiceFilterButton(mServiceClearingSettlementButton, MainDatabase.SERVICE_CLEARING_SETTLEMENT_MASK, R.drawable.service_clearing_settlement, R.drawable.service_clearing_settlement_disabled);
        }
        else
        if (view == mServiceCosmeticsButton)
        {
            toggleServiceFilterButton(mServiceCosmeticsButton, MainDatabase.SERVICE_COSMETICS_MASK, R.drawable.service_cosmetics, R.drawable.service_cosmetics_disabled);
        }
        else
        if (view == mServicePlaygroundButton)
        {
            toggleServiceFilterButton(mServicePlaygroundButton, MainDatabase.SERVICE_PLAYGROUND_MASK, R.drawable.service_playground, R.drawable.service_playground_disabled);
        }
        else
        if (view == mServiceFishIslandButton)
        {
            toggleServiceFilterButton(mServiceFishIslandButton, MainDatabase.SERVICE_FISH_ISLAND_MASK, R.drawable.service_fish_island, R.drawable.service_fish_island_disabled);
        }
        else
        if (view == mServiceBakeryButton)
        {
            toggleServiceFilterButton(mServiceBakeryButton, MainDatabase.SERVICE_BAKERY_MASK, R.drawable.service_bakery, R.drawable.service_bakery_disabled);
        }
        else
        if (view == mServiceCookeryButton)
        {
            toggleServiceFilterButton(mServiceCookeryButton, MainDatabase.SERVICE_COOKERY_MASK, R.drawable.service_cookery, R.drawable.service_cookery_disabled);
        }
        else
        if (view == mServiceTaxiOrderingButton)
        {
            toggleServiceFilterButton(mServiceTaxiOrderingButton, MainDatabase.SERVICE_TAXI_ORDERING_MASK, R.drawable.service_taxi_ordering, R.drawable.service_taxi_ordering_disabled);
        }
        else
        if (view == mServicePharmacyButton)
        {
            toggleServiceFilterButton(mServicePharmacyButton, MainDatabase.SERVICE_PHARMACY_MASK, R.drawable.service_pharmacy, R.drawable.service_pharmacy_disabled);
        }
        else
        if (view == mServiceDegustationButton)
        {
            toggleServiceFilterButton(mServiceDegustationButton, MainDatabase.SERVICE_DEGUSTATION_MASK, R.drawable.service_degustation, R.drawable.service_degustation_disabled);
        }
        else
        if (view == mServiceOrderingFoodButton)
        {
            toggleServiceFilterButton(mServiceOrderingFoodButton, MainDatabase.SERVICE_ORDERING_FOOD_MASK, R.drawable.service_ordering_food, R.drawable.service_ordering_food_disabled);
        }
        else
        if (view == mServiceCafeButton)
        {
            toggleServiceFilterButton(mServiceCafeButton, MainDatabase.SERVICE_CAFE_MASK, R.drawable.service_cafe, R.drawable.service_cafe_disabled);
        }
        else
        if (view == mServiceGiftCardsButton)
        {
            toggleServiceFilterButton(mServiceGiftCardsButton, MainDatabase.SERVICE_GIFT_CARDS_MASK, R.drawable.service_gift_cards, R.drawable.service_gift_cards_disabled);
        }
        else
        if (view == mServiceParkingButton)
        {
            toggleServiceFilterButton(mServiceParkingButton, MainDatabase.SERVICE_PARKING_MASK, R.drawable.service_parking, R.drawable.service_parking_disabled);
        }
        else
        if (view == mServicePointOfIssuingOrdersButton)
        {
            toggleServiceFilterButton(mServicePointOfIssuingOrdersButton, MainDatabase.SERVICE_POINT_OF_ISSUING_ORDERS_MASK, R.drawable.service_point_of_issuing_orders, R.drawable.service_point_of_issuing_orders_disabled);
        }
        else
        {
            AppLog.wtf(TAG, "Unknown view");
        }
    }

    private void updateUI()
    {
        mSupermarketCheckBox.setChecked(mShopFilter.isSupermarket());
        mHypermarketCheckBox.setChecked(mShopFilter.isHypermarket());
        mAllDayCheckBox.setChecked(mShopFilter.isAllDay());

        updateServiceFilterButton(mServiceClearingSettlementButton,   MainDatabase.SERVICE_CLEARING_SETTLEMENT_MASK,     R.drawable.service_clearing_settlement,     R.drawable.service_clearing_settlement_disabled);
        updateServiceFilterButton(mServiceCosmeticsButton,            MainDatabase.SERVICE_COSMETICS_MASK,               R.drawable.service_cosmetics,               R.drawable.service_cosmetics_disabled);
        updateServiceFilterButton(mServicePlaygroundButton,           MainDatabase.SERVICE_PLAYGROUND_MASK,              R.drawable.service_playground,              R.drawable.service_playground_disabled);
        updateServiceFilterButton(mServiceFishIslandButton,           MainDatabase.SERVICE_FISH_ISLAND_MASK,             R.drawable.service_fish_island,             R.drawable.service_fish_island_disabled);
        updateServiceFilterButton(mServiceBakeryButton,               MainDatabase.SERVICE_BAKERY_MASK,                  R.drawable.service_bakery,                  R.drawable.service_bakery_disabled);
        updateServiceFilterButton(mServiceCookeryButton,              MainDatabase.SERVICE_COOKERY_MASK,                 R.drawable.service_cookery,                 R.drawable.service_cookery_disabled);
        updateServiceFilterButton(mServiceTaxiOrderingButton,         MainDatabase.SERVICE_TAXI_ORDERING_MASK,           R.drawable.service_taxi_ordering,           R.drawable.service_taxi_ordering_disabled);
        updateServiceFilterButton(mServicePharmacyButton,             MainDatabase.SERVICE_PHARMACY_MASK,                R.drawable.service_pharmacy,                R.drawable.service_pharmacy_disabled);
        updateServiceFilterButton(mServiceDegustationButton,          MainDatabase.SERVICE_DEGUSTATION_MASK,             R.drawable.service_degustation,             R.drawable.service_degustation_disabled);
        updateServiceFilterButton(mServiceOrderingFoodButton,         MainDatabase.SERVICE_ORDERING_FOOD_MASK,           R.drawable.service_ordering_food,           R.drawable.service_ordering_food_disabled);
        updateServiceFilterButton(mServiceCafeButton,                 MainDatabase.SERVICE_CAFE_MASK,                    R.drawable.service_cafe,                    R.drawable.service_cafe_disabled);
        updateServiceFilterButton(mServiceGiftCardsButton,            MainDatabase.SERVICE_GIFT_CARDS_MASK,              R.drawable.service_gift_cards,              R.drawable.service_gift_cards_disabled);
        updateServiceFilterButton(mServiceParkingButton,              MainDatabase.SERVICE_PARKING_MASK,                 R.drawable.service_parking,                 R.drawable.service_parking_disabled);
        updateServiceFilterButton(mServicePointOfIssuingOrdersButton, MainDatabase.SERVICE_POINT_OF_ISSUING_ORDERS_MASK, R.drawable.service_point_of_issuing_orders, R.drawable.service_point_of_issuing_orders_disabled);
    }

    @SuppressWarnings("deprecation")
    private void updateServiceFilterButton(ImageButtonWithTooltip button, int mask, int activeDrawable, int disabledDrawable)
    {
        if ((mShopFilter.getServicesSet() & mask) != 0)
        {
            button.setImageDrawable(getActivity().getResources().getDrawable(activeDrawable));
        }
        else
        {
            button.setImageDrawable(getActivity().getResources().getDrawable(disabledDrawable));
        }
    }

    private void toggleServiceFilterButton(ImageButtonWithTooltip button, int mask, int activeDrawable, int disabledDrawable)
    {
        if ((mShopFilter.getServicesSet() & mask) != 0)
        {
            mShopFilter.setServicesSet(mShopFilter.getServicesSet() & ~mask);
        }
        else
        {
            mShopFilter.setServicesSet(mShopFilter.getServicesSet() | mask);
        }

        updateServiceFilterButton(button, mask, activeDrawable, disabledDrawable);
    }

    public void onFilterApplied()
    {
        if (mListener != null)
        {
            mListener.onShopFilterApplied(mShopFilter);
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
        void onShopFilterApplied(ShopFilter filter);
    }
}
