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

public class SelectCityDialog extends DialogFragment
{
    @SuppressWarnings("unused")
    private static final String TAG = "SelectCityDialog";



    private OnFragmentInteractionListener mListener = null;



    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        MainDatabase mainDatabase = new MainDatabase(getActivity());
        SQLiteDatabase db = mainDatabase.getReadableDatabase();
        String[] cities = mainDatabase.getCities(db);
        db.close();



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.pref_description_general_city)
                .setCancelable(true)
                .setItems(cities, new DialogInterface.OnClickListener()
                {
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
        void onCitySelected(String cityId);
    }
}
