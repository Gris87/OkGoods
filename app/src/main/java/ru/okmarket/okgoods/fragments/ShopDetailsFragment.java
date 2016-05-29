package ru.okmarket.okgoods.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.other.ShopInfo;

public class ShopDetailsFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "ShopDetailsFragment";



    private OnFragmentInteractionListener mListener     = null;
    private Button                        mCancelButton = null;
    private Button                        mOkButton     = null;




    public ShopDetailsFragment()
    {
        // Nothing
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_shop_details, container, false);

        mCancelButton = (Button)rootView.findViewById(R.id.cancelButton);
        mOkButton     = (Button)rootView.findViewById(R.id.okButton);

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
            mOkButton.setEnabled(true);
        }
        else
        {
            mOkButton.setEnabled(false);
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
