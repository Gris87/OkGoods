package ru.okmarket.okgoods.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.fragments.HistoryDetailsFragment;
import ru.okmarket.okgoods.net.HttpClient;
import ru.okmarket.okgoods.other.Extras;
import ru.okmarket.okgoods.other.HistoryDetailsInfo;
import ru.okmarket.okgoods.util.AppLog;
import ru.okmarket.okgoods.widgets.CachedImageView;

public class HistoryDetailsActivity extends AppCompatActivity
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryDetailsActivity";



    private static final String SAVED_STATE_SELECTED_DETAILS = "SELECTED_DETAILS";



    private CachedImageView        mShopImageView          = null;
    private HistoryDetailsFragment mHistoryDetailsFragment = null;
    private HttpClient             mHttpClient             = null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);



        Toolbar toolbar         = (Toolbar)               findViewById(R.id.toolbar);
        mShopImageView          = (CachedImageView)       findViewById(R.id.shopImageView);
        mHistoryDetailsFragment = (HistoryDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.historyDetailsFragment);

        mHttpClient = HttpClient.getInstance(this);



        Intent intent = getIntent();

        int                           shopId  = intent.getIntExtra(                Extras.SHOP, 0);
        final String                  history = intent.getStringExtra(             Extras.HISTORY);
        ArrayList<HistoryDetailsInfo> details = intent.getParcelableArrayListExtra(Extras.HISTORY_DETAILS);
        double                        total   = intent.getDoubleExtra(             Extras.TOTAL, 0);



        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        setTitle(history);
        ((ImageView)mShopImageView.getContentView()).setScaleType(ImageView.ScaleType.CENTER_CROP);
        mHistoryDetailsFragment.setHistoryDetails(details);
        mHistoryDetailsFragment.setSelectedHistoryDetails(null);
        mHistoryDetailsFragment.setTotal(total);



        mHttpClient.getRequestQueue().cancelAll(TAG);

        StringRequest request = new StringRequest(Request.Method.GET, "http://okmarket.ru/stores/" + String.valueOf(shopId) + "/"
                , new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        int index = -1;

                        do
                        {
                            index = response.indexOf("<img src=\"", index + 1);

                            if (index < 0)
                            {
                                break;
                            }

                            int index2 = response.indexOf('>', index + 10);

                            if (index2 < 0)
                            {
                                break;
                            }

                            String imageTag = response.substring(index, index2 + 1);
                            index = index2 + 1;

                            if (imageTag.contains("id=\"sd-gallery"))
                            {
                                index2 = imageTag.indexOf("\"", 10);

                                if (index2 >= 0)
                                {
                                    mShopImageView.setImageUrl("http://okmarket.ru" + imageTag.substring(10, index2), mHttpClient.getImageLoader());

                                    break;
                                }
                            }
                        } while (true);
                    }
                }
                ,  new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        AppLog.w(TAG, "Failed to get photo for shop: " + history);
                    }
                }
        );

        request.setTag(TAG);

        mHttpClient.addToRequestQueue(request);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        mHttpClient.getRequestQueue().cancelAll(TAG);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVED_STATE_SELECTED_DETAILS, mHistoryDetailsFragment.getSelectedHistoryDetails());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        HistoryDetailsInfo selectedDetails = savedInstanceState.getParcelable(SAVED_STATE_SELECTED_DETAILS);

        mHistoryDetailsFragment.setSelectedHistoryDetails(selectedDetails);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            NavUtils.navigateUpTo(this, new Intent(this, HistoryActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
