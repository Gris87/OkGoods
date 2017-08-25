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

@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
public class ShopMapFragment extends Fragment implements View.OnClickListener
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "ShopMapFragment";
    // endregion
    // endregion



    // region Attributes
    private OnFragmentInteractionListener mListener             = null;
    private TextView                      mSelectedShopTextView = null;
    // endregion



    @Override
    public String toString()
    {
        return "ShopMapFragment{" +
                "mListener="               + mListener             +
                ", mSelectedShopTextView=" + mSelectedShopTextView +
                '}';
    }

    @SuppressWarnings("RedundantCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_shop_map, container, false);



        mSelectedShopTextView = (TextView)rootView.findViewById(R.id.selectShopTextView);



        mSelectedShopTextView.setOnClickListener(this);

        resetSelectedShop();



        return rootView;
    }

    public void resetSelectedShop()
    {
        setSelectedShopText(getContext().getResources().getString(R.string.shop_map_select_shop));
    }

    public void setSelectedShopText(String text)
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
        void onShopMapSelectShopClicked();
    }
}
