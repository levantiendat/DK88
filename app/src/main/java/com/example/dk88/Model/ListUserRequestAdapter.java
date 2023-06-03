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

public class ListUserRequestAdapter extends BaseAdapter {
    private Toast mToast;
    private Context context;
    private int layout;
    private List<StudentStateInfo> namelist;

    public ListUserRequestAdapter(Context context, int layout, List<StudentStateInfo> namelist) {
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
    class ViewHolder{
        TextView txtstudentid;
        TextView txtstatus;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ListUserRequestAdapter.ViewHolder holder;
        if(view==null){
            LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =inflater.inflate(layout, null);
            holder =new ListUserRequestAdapter.ViewHolder();

            //ánh xạ view
            holder.txtstudentid=(TextView) view.findViewById(R.id.name);
            holder.txtstatus=(TextView) view.findViewById(R.id.number);

            view.setTag(holder);
        }
        else{
            holder = (ListUserRequestAdapter.ViewHolder) view.getTag();
        }
        StudentStateInfo student = namelist.get(position);
        holder.txtstudentid.setText(student.getStudentID());

        holder.txtstatus.setText(student.getState());


        return view;
    }
}
