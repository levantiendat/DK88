package com.example.dk88.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dk88.R;

import java.util.List;

public class PictureAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Picture> listPicture;

    public PictureAdapter(Context context, int layout, List<Picture> listPicture) {
        this.context = context;
        this.layout = layout;
        this.listPicture = listPicture;
    }

    @Override
    public int getCount() {
        return listPicture.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        ImageView image;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PictureAdapter.ViewHolder holder;
        if(view==null){
            LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =inflater.inflate(layout, null);
            holder =new PictureAdapter.ViewHolder();

            //ánh xạ view
            holder.image=(ImageView) view.findViewById(R.id.picture);
            view.setTag(holder);
        }
        else{
            holder = (PictureAdapter.ViewHolder) view.getTag();
        }
        Picture picture = listPicture.get(position);
        holder.image.setImageBitmap(picture.getBitmap());



        return view;
    }
}
