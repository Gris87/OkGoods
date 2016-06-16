package ru.okmarket.okgoods.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import ru.okmarket.okgoods.R;

public class HistoryDetailsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);



        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);



        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }




        if (savedInstanceState == null)
        {
            HistoryDetailsFragment fragment = new HistoryDetailsFragment();

            Bundle arguments = new Bundle();
            arguments.putString(HistoryDetailsFragment.ARG_ITEM_ID, getIntent().getStringExtra(HistoryDetailsFragment.ARG_ITEM_ID));
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
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
