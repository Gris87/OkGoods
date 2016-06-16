package ru.okmarket.okgoods.activities;

import android.content.Context;
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
import ru.okmarket.okgoods.other.HistoryInfo;

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



        Toolbar toolbar         = (Toolbar)               findViewById(R.id.toolbar);
        mHistoryDetailsFragment = (HistoryDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.historyDetailsFragment);



        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.historyRecyclerView);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView)
    {
        MainDatabase   mainDatabase = new MainDatabase(this);
        SQLiteDatabase db           = mainDatabase.getReadableDatabase();

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(mainDatabase.getHistory(db)));

        db.close();
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
            holder.mItem = mItems.get(position);

            holder.mShopNameTextView.setText(holder.mItem.getShopName());
            holder.mDateTextView.setText(holder.mItem.getDate());
            holder.mDurationTextView.setText(String.valueOf(holder.mItem.getDuration()));
            holder.mTotalTextView.setText(String.valueOf(holder.mItem.getTotal()));

            holder.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    /*
                    if (mTwoPane)
                    {
                        Bundle arguments = new Bundle();
                        arguments.putString(HistoryDetailsFragment.ARG_ITEM_ID, holder.mItem.id);
                        HistoryDetailsFragment fragment = new HistoryDetailsFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    }
                    else
                    {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, HistoryDetailsActivity.class);
                        intent.putExtra(HistoryDetailsFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                    */
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
            public HistoryInfo mItem;
            public View        mView;
            public TextView    mShopNameTextView;
            public TextView    mDateTextView;
            public TextView    mDurationTextView;
            public TextView    mTotalTextView;


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
