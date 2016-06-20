package ru.okmarket.okgoods.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;

import ru.okmarket.okgoods.adapters.HistoryAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.fragments.HistoryDetailsFragment;
import ru.okmarket.okgoods.other.Extras;
import ru.okmarket.okgoods.other.HistoryDetailsInfo;
import ru.okmarket.okgoods.other.HistoryInfo;
import ru.okmarket.okgoods.widgets.DividerItemDecoration;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.OnItemClickListener
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryActivity";



    private static final String SAVED_STATE_HISTORY_DETAILS = "HISTORY_DETAILS";



    private HistoryDetailsFragment mHistoryDetailsFragment = null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        Toolbar toolbar           = (Toolbar)               findViewById(R.id.toolbar);
        RecyclerView recyclerView = (RecyclerView)          findViewById(R.id.historyRecyclerView);
        mHistoryDetailsFragment   = (HistoryDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.historyDetailsFragment);



        assert recyclerView != null;



        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        MainDatabase   mainDatabase = new MainDatabase(this);
        SQLiteDatabase db           = mainDatabase.getReadableDatabase();

        HistoryAdapter adapter = new HistoryAdapter(this, mainDatabase.getHistory(db));
        adapter.setOnItemClickListener(this);

        db.close();



        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        if (mHistoryDetailsFragment != null)
        {
            outState.putParcelableArrayList(SAVED_STATE_HISTORY_DETAILS, mHistoryDetailsFragment.getHistoryDetails());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        if (mHistoryDetailsFragment != null)
        {
            ArrayList<HistoryDetailsInfo> details = savedInstanceState.getParcelableArrayList(SAVED_STATE_HISTORY_DETAILS);
            mHistoryDetailsFragment.setHistoryDetails(details);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHistoryClicked(HistoryInfo history)
    {
        MainDatabase   mainDatabase = new MainDatabase(HistoryActivity.this);
        SQLiteDatabase db           = mainDatabase.getReadableDatabase();

        ArrayList<HistoryDetailsInfo> details = mainDatabase.getHistoryDetails(db, history.getId());

        db.close();



        if (mHistoryDetailsFragment != null)
        {
            mHistoryDetailsFragment.setHistoryDetails(details);
        }
        else
        {
            Intent intent = new Intent(HistoryActivity.this, HistoryDetailsActivity.class);

            intent.putExtra(Extras.SHOP,            history.getShopId());
            intent.putExtra(Extras.HISTORY,         history.getDate() + ". " + history.getShopName());
            intent.putExtra(Extras.HISTORY_DETAILS, details);

            startActivity(intent);
        }
    }
}
