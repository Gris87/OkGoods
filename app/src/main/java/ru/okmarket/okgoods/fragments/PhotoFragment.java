package ru.okmarket.okgoods.fragments;


import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.net.HttpClient;
import uk.co.senab.photoview.PhotoView;

public class PhotoFragment extends Fragment
{
    @SuppressWarnings("unused")
    private static final String TAG = "PhotoFragment";



    private static final String ARG_URL = "URL";



    private String mUrl;



    public PhotoFragment()
    {
        // Nothing
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_photo, container, false);

        PhotoView photoView = (PhotoView)rootView.findViewById(R.id.photoView);
        ImageLoader imageLoader = HttpClient.getInstance(getActivity()).getImageLoader();

        imageLoader.get(mUrl, ImageLoader.getImageListener(photoView, R.drawable.ymk_ya_logo, R.drawable.delete));

        return rootView;
    }

}
