package com.example.dk88.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dk88.R;

import java.util.List;

public class ListStudentIDAdapter extends BaseAdapter {
    private Toast mToast;
    private Context context;
    private int layout;
    private List<String> namelist;

    public ListStudentIDAdapter(Context context, int layout, List<String> namelist) {
        this.context = context;
        this.layout = layout;
        this.namelist = namelist;
    }

    @Override
    public int getCount() {
        return namelist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        TextView txtstudentid;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ListStudentIDAdapter.ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder = new ListStudentIDAdapter.ViewHolder();

            //ánh xạ view
            holder.txtstudentid = (TextView) view.findViewById(R.id.name);


            view.setTag(holder);
        } else {
            holder = (ListStudentIDAdapter.ViewHolder) view.getTag();
        }
        String studentID = namelist.get(position);
        holder.txtstudentid.setText(studentID);
        return view;
    }
}
