package ru.okmarket.okgoods.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.fragments.HistoryDetailsFragment;
import ru.okmarket.okgoods.other.Extras;
import ru.okmarket.okgoods.other.HistoryDetailsInfo;

public class HistoryDetailsActivity extends AppCompatActivity
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryDetailsActivity";



    private HistoryDetailsFragment mHistoryDetailsFragment = null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);



        Toolbar toolbar         = (Toolbar)               findViewById(R.id.detailToolbar);
        mHistoryDetailsFragment = (HistoryDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.historyDetailsFragment);



        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        Intent intent = getIntent();
        ArrayList<HistoryDetailsInfo> details = intent.getParcelableArrayListExtra(Extras.HISTORY_DETAILS);
        mHistoryDetailsFragment.setHistoryDetails(details);
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
