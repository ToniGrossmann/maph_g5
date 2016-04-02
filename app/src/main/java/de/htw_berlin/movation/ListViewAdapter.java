package de.htw_berlin.movation;

/**
 * Created by Telan on 02.04.2016.
 */
import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private static ArrayList<String> listContact;
    private LayoutInflater mInflater;

    public LayoutInflater inflater;
    public Activity activity;

    public ListViewAdapter(Context context, ArrayList<String> results){
        listContact = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listContact.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return listContact.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_adapter, null);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.textView1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.txtname.setText(listContact.get(position));


        return convertView;
    }

    static class ViewHolder{
        TextView txtname, txtphone;
    }
}