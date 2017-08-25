package ru.okmarket.okgoods.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.net.HttpClient;
import ru.okmarket.okgoods.widgets.CachedPhotoView;

@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
public class PhotoFragment extends Fragment
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "PhotoFragment";
    // endregion



    // region Arguments
    private static final String ARG_URL = "URL";
    // endregion
    // endregion



    // region Attributes
    private String mUrl = null;
    // endregion



    @Override
    public String toString()
    {
        return "PhotoFragment{" +
                "mUrl='" + mUrl + '\'' +
                '}';
    }

    public static PhotoFragment newInstance(String url)
    {
        PhotoFragment fragment = new PhotoFragment();

        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mUrl = getArguments().getString(ARG_URL);
        }
    }

    @SuppressWarnings("RedundantCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_photo, container, false);



        CachedPhotoView photoView = (CachedPhotoView)rootView.findViewById(R.id.photoView);



        ImageLoader imageLoader = HttpClient.getInstance(getActivity()).getImageLoader();
        photoView.setImageUrl(mUrl, imageLoader);



        return rootView;
    }
}
