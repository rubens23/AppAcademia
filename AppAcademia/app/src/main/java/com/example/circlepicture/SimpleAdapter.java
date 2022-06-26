package com.example.circlepicture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
    private static final int COUNT = 50;//100

    private final Context mContext;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;

    public SimpleAdapter(Context context){
        mContext = context;
        mItems = new ArrayList<Integer>(COUNT);
        for(int i = 0; i < COUNT; i++){
            addItem(i);
        }
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        holder.title.setText(mItems.get(position).toString());

    }

    public void addItem(int position){
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position){
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder{
        public final TextView title;

        public SimpleViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }
}
