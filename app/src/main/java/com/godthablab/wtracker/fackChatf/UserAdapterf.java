package com.godthablab.wtracker.fackChatf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.godthablab.wtracker.R;
import com.godthablab.wtracker.fackChatf.DataBaseDetailsf.UserDetailsf;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class UserAdapterf extends BaseAdapter {
    static ArrayList<UserDetailsf> userdetails;
    private Context context;
    private LayoutInflater inflater;

    public class Holder {
        CircleImageView img;
        TextView username;
        TextView visibility;
    }

    UserAdapterf(Context context, ArrayList<UserDetailsf> userdetailsses) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userdetails = userdetailsses;
    }

    public int getCount() {
        return userdetails.size();
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        UserDetailsf userlist = userdetails.get(i);
        Holder holder = new Holder();
        @SuppressLint("ViewHolder") View rowView = this.inflater.inflate(R.layout.fake_chat_activity, null);
        holder.username = rowView.findViewById(R.id.username);
        holder.visibility = rowView.findViewById(R.id.visibilitystatus);
        holder.img = rowView.findViewById(R.id.user_icon);
        holder.username.setText(userlist.getUname());
        if (userlist.getUtyping().equals("typing")) {
            holder.visibility.setText("typing...");
        } else {
            holder.visibility.setText("");
        }
        holder.img.setImageBitmap(getImagefromdatabase(userlist.getBytes()));
        return rowView;
    }

    private Bitmap getImagefromdatabase(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
