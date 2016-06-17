package ru.okmarket.okgoods.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;

import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.fragments.HistoryDetailsFragment;
import ru.okmarket.okgoods.other.Extras;
import ru.okmarket.okgoods.other.HistoryDetailsInfo;
import ru.okmarket.okgoods.other.HistoryInfo;
import ru.okmarket.okgoods.widgets.DividerItemDecoration;

public class HistoryActivity extends AppCompatActivity
{
    @SuppressWarnings("unused")
    private static final String TAG = "HistoryActivity";



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

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(mainDatabase.getHistory(db)));

        db.close();
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



    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
    {
        private final ArrayList<HistoryInfo> mItems;



        public SimpleItemRecyclerViewAdapter(ArrayList<HistoryInfo> items)
        {
            mItems = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position)
        {
            final HistoryInfo item = mItems.get(position);

            holder.mShopNameTextView.setText(item.getShopName());
            holder.mDateTextView.setText(item.getDate());
            holder.mDurationTextView.setText(getString(R.string.time, item.getDurationString()));
            holder.mTotalTextView.setText(getString(R.string.rub_currency, item.getTotal()));

            holder.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    MainDatabase   mainDatabase = new MainDatabase(HistoryActivity.this);
                    SQLiteDatabase db           = mainDatabase.getReadableDatabase();

                    ArrayList<HistoryDetailsInfo> details = mainDatabase.getHistoryDetails(db, item.getId());

                    db.close();

                    if (mHistoryDetailsFragment != null)
                    {
                        mHistoryDetailsFragment.setHistoryDetails(details);
                    }
                    else
                    {
                        Intent intent = new Intent(HistoryActivity.this, HistoryDetailsActivity.class);
                        intent.putExtra(Extras.HISTORY_DETAILS, details);

                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public View     mView;
            public TextView mShopNameTextView;
            public TextView mDateTextView;
            public TextView mDurationTextView;
            public TextView mTotalTextView;


            public ViewHolder(View view)
            {
                super(view);

                mView             = view;
                mShopNameTextView = (TextView)view.findViewById(R.id.shopNameTextView);
                mDateTextView     = (TextView)view.findViewById(R.id.dateTextView);
                mDurationTextView = (TextView)view.findViewById(R.id.durationTextView);
                mTotalTextView    = (TextView)view.findViewById(R.id.totalTextView);
            }
        }
    }
}
