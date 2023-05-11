package com.example.dk88;

import android.content.Context;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class ImageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Bitmap> bitmaps;

    public ImageAdapter(Context context, ArrayList<Bitmap> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new AbsListView.LayoutParams(350, 350)); // Thiết lập kích thước của ImageView
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); // Thiết lập ScaleType để ảnh không bị kéo ra hoặc bị cắt bớt
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(bitmaps.get(position)); // Thiết lập Bitmap cho ImageView

        return imageView;
    }
}
