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
    private List<String> mPhotoLinks;

    public SimpleAdapter(Context context, List<String> photosList){
        this.mPhotoLinks = photosList;
        mContext = context;

    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {

    }

    public void addItem(int position){

    }

    public void removeItem(int position){

    }

    @Override
    public int getItemCount() {
        return mPhotoLinks.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder{
        public final TextView title;

        public SimpleViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }
}
