package ru.okmarket.okgoods.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.MainDatabase;

@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
public class SelectCityDialog extends DialogFragment
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "SelectCityDialog";
    // endregion
    // endregion



    // region Attributes
    private OnFragmentInteractionListener mListener = null;
    // endregion



    @Override
    public String toString()
    {
        return "SelectCityDialog{" +
                "mListener=" + mListener +
                '}';
    }

    public static SelectCityDialog newInstance()
    {
        return new SelectCityDialog();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        MainDatabase   mainDatabase = MainDatabase.newInstance(getActivity());
        SQLiteDatabase db           = mainDatabase.getReadableDatabase();

        String[] cities = MainDatabase.getCities(db);

        db.close();



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_select_city_title)
                .setCancelable(true)
                .setItems(cities, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        onCitySelected(MainDatabase.CITIES[which]);

                        dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        onCitySelected(MainDatabase.CITIES[0]);
    }

    public void onCitySelected(String cityId)
    {
        if (mListener != null)
        {
            mListener.onCitySelected(cityId);
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
        void onCitySelected(String cityId);
    }
}
