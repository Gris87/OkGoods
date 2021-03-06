package ru.okmarket.okgoods.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.adapters.HistoryAdapter;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.db.entities.HistoryDetailsEntity;
import ru.okmarket.okgoods.db.entities.HistoryEntity;
import ru.okmarket.okgoods.fragments.HistoryDetailsFragment;
import ru.okmarket.okgoods.other.ApplicationExtras;

@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor"})
public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.OnItemClickListener
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryActivity";
    // endregion



    // region Save state constants
    private static final String SAVED_STATE_HISTORY_DETAILS  = "HISTORY_DETAILS";
    private static final String SAVED_STATE_SELECTED_DETAILS = "SELECTED_DETAILS";
    private static final String SAVED_STATE_TOTAL            = "TOTAL";
    // endregion
    // endregion



    // region Attributes
    private HistoryDetailsFragment mHistoryDetailsFragment = null;
    // endregion



    @Override
    public String toString()
    {
        return "HistoryActivity{" +
                "mHistoryDetailsFragment=" + mHistoryDetailsFragment +
                '}';
    }

    @SuppressWarnings("RedundantCast")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        Toolbar toolbar           = (Toolbar)               findViewById(R.id.toolbar);
        RecyclerView recyclerView = (RecyclerView)          findViewById(R.id.historyRecyclerView);
        mHistoryDetailsFragment   = (HistoryDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.historyDetailsFragment);



        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        MainDatabase   mainDatabase = MainDatabase.newInstance(this);
        SQLiteDatabase db           = mainDatabase.getReadableDatabase();

        HistoryAdapter adapter = HistoryAdapter.newInstance(this, MainDatabase.getHistory(db, MainDatabase.LIMIT_UNLIMITED));
        adapter.setOnItemClickListener(this);

        db.close();



        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        if (mHistoryDetailsFragment != null)
        {
            outState.putParcelableArrayList(SAVED_STATE_HISTORY_DETAILS,  mHistoryDetailsFragment.getHistoryDetails());
            outState.putParcelable(         SAVED_STATE_SELECTED_DETAILS, mHistoryDetailsFragment.getSelectedHistoryDetails());
            outState.putDouble(             SAVED_STATE_TOTAL,            mHistoryDetailsFragment.getTotal());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        if (mHistoryDetailsFragment != null)
        {
            ArrayList<HistoryDetailsEntity> details         = savedInstanceState.getParcelableArrayList(SAVED_STATE_HISTORY_DETAILS);
            HistoryDetailsEntity            selectedDetails = savedInstanceState.getParcelable(         SAVED_STATE_SELECTED_DETAILS);
            double                          total           = savedInstanceState.getDouble(             SAVED_STATE_TOTAL);

            mHistoryDetailsFragment.setHistoryDetails(details);
            mHistoryDetailsFragment.setSelectedHistoryDetails(selectedDetails);
            mHistoryDetailsFragment.setTotal(total);
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
    public void onHistoryClicked(HistoryAdapter.HistoryViewHolder holder, HistoryEntity history)
    {
        MainDatabase   mainDatabase = MainDatabase.newInstance(this);
        SQLiteDatabase db           = mainDatabase.getReadableDatabase();

        ArrayList<HistoryDetailsEntity> details = MainDatabase.getHistoryDetails(db, history.getId(), MainDatabase.LIMIT_UNLIMITED);

        db.close();



        if (mHistoryDetailsFragment != null)
        {
            mHistoryDetailsFragment.setHistoryDetails(details);
            mHistoryDetailsFragment.setSelectedHistoryDetails(null);
            mHistoryDetailsFragment.setTotal(history.getTotal());
        }
        else
        {
            Intent intent = new Intent(this, HistoryDetailsActivity.class);

            intent.putExtra(ApplicationExtras.SHOP,            history.getShopId());
            intent.putExtra(ApplicationExtras.HISTORY,         history.getDate() + ". " + history.getShopName());
            intent.putExtra(ApplicationExtras.HISTORY_DETAILS, details);
            intent.putExtra(ApplicationExtras.TOTAL,           history.getTotal());

            startActivity(intent);
        }
    }
}
