package com.ilicit.ewerdima;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ilicit.ewerdima.Models.MyUsers;

import java.util.ArrayList;

/**
 * Created by Shaffic on 5/13/15.
 */
public class FriendsListAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyUsers> friends;
    LayoutInflater inflater;


    public FriendsListAdapter(Context context, ArrayList<MyUsers> f){
        this.context = context;
        this.friends = f;

    }


    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public MyUsers getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent,
                    false);
        }

        TextView text = (TextView) convertView.findViewById(R.id.name);
        MyUsers users =friends.get(position);
        text.setText(users.getAlertedusers());





        return convertView;
    }
}
