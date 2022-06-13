package com.example.circlepicture;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends BaseAdapter {

    private List<String> photosToGallery;
    private Context context;

    public GalleryAdapter(List<String> photosList, Context ctx){
        this.photosToGallery = photosList;
        this.context = ctx;
    }

    @Override
    public int getCount() {
        return photosToGallery.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(photosToGallery.get(position));
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = (ImageView) view;

        if (imageView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(120, 200));//450h
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Picasso.get()
                .load(photosToGallery.get(i))
                .into(imageView);
        return imageView;
    }
}
