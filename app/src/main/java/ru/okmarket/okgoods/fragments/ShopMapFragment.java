package ru.okmarket.okgoods.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.okmarket.okgoods.R;

public class ShopMapFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "ShopMapFragment";



    private OnFragmentInteractionListener mListener             = null;
    private TextView                      mSelectedShopTextView = null;



    public ShopMapFragment()
    {
        // Nothing
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_shop_map, container, false);

        mSelectedShopTextView = (TextView)rootView.findViewById(R.id.selectShopTextView);

        init();

        return rootView;
    }

    private void init()
    {
        mSelectedShopTextView.setOnClickListener(this);

        resetSelectedShop();
        onFragmentCreated();
    }

    public void resetSelectedShop()
    {
        setSelectedShopText(getContext().getResources().getString(R.string.select_shop));
    }

    private void setSelectedShopText(String text)
    {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        mSelectedShopTextView.setText(content);
    }

    @Override
    public void onClick(View view)
    {
        if (view == mSelectedShopTextView)
        {
            onSelectShopClicked();
        }
    }

    public void onFragmentCreated()
    {
        if (mListener != null)
        {
            mListener.onShopMapFragmentCreated(this);
        }
    }

    public void onSelectShopClicked()
    {
        if (mListener != null)
        {
            mListener.onShopMapSelectShopClicked();
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
        void onShopMapFragmentCreated(ShopMapFragment fragment);
        void onShopMapSelectShopClicked();
    }
}
