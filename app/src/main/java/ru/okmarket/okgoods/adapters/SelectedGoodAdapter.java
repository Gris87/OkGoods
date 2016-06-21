package ru.okmarket.okgoods.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.okmarket.okgoods.R;
import ru.okmarket.okgoods.db.MainDatabase;
import ru.okmarket.okgoods.other.SelectedGoodInfo;

public class SelectedGoodAdapter extends RecyclerView.Adapter<SelectedGoodAdapter.ViewHolder>
{
    @SuppressWarnings("unused")
    private static final String TAG = "SelectedGoodAdapter";



    private Context                     mContext      = null;
    private MainDatabase                mMainDatabase = null;
    private SQLiteDatabase              mDB           = null;
    private ArrayList<SelectedGoodInfo> mItems        = null;



    public SelectedGoodAdapter(Context context, MainDatabase mainDatabase, SQLiteDatabase db)
    {
        mContext      = context;
        mMainDatabase = mainDatabase;
        mDB           = db;
        mItems        = new ArrayList<>();

        updateFromDatabase();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_selected_good, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final SelectedGoodInfo item = mItems.get(position);

        holder.mGoodNameTextView.setText(item.getName());

        if (item.getGoodId() > 0 && item.getCost() > 0 && item.getCount() > 0)
        {
            holder.mCostTextView.setVisibility(View.VISIBLE);
            holder.mCostTextView.setText(mContext.getString(R.string.rub_currency_count, item.getCost(), item.getCount()));
        }
        else
        {
            holder.mCostTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return mItems.size();
    }

    public void updateFromDatabase()
    {
        mItems = mMainDatabase.getSelectedGoods(mDB);

        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mGoodNameTextView;
        public TextView mCostTextView;



        public ViewHolder(View view)
        {
            super(view);

            mGoodNameTextView = (TextView)view.findViewById(R.id.goodNameTextView);
            mCostTextView     = (TextView)view.findViewById(R.id.costTextView);
        }
    }
}
