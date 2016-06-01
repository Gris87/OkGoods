package ru.okmarket.okgoods.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.other.ShopFilter;
import ru.okmarket.okgoods.widgets.ImageButtonWithTooltip;

public class ShopFilterDialog extends DialogFragment
{
    @SuppressWarnings("unused")
    private static final String TAG = "ShopFilterDialog";



    private OnFragmentInteractionListener mListener = null;
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



    @Override
    @NonNull
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

    public void onFilterApplied()
    {
        if (mListener != null)
        {
            ShopFilter filter = new ShopFilter();

            mListener.onShopFilterApplied(filter);
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
