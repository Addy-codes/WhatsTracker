package com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView.DataBaseDetails.GetterSetterUserDetails;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public  class GetUserDetailsAdapter extends BaseAdapter {

    static ArrayList<GetterSetterUserDetails> userdetails;
    private Context context;
    private LayoutInflater inflater;
    private int count = 5;


    public class Holder {
        CircleImageView img;
        TextView username;
        TextView visibility;
        TextView lastMessage;
        TextView time;
    }


  public void addToList(ArrayList<GetterSetterUserDetails> userdetails) {
        userdetails.clear();
        userdetails.addAll(userdetails);
        count = userdetails.size() - 1;
        notifyDataSetChanged();
    }

    public void changeSize() {
        Log.d("adapter", "changeSize");
        count = userdetails.size();
        notifyDataSetChanged();
    }


    GetUserDetailsAdapter(Context context, ArrayList<GetterSetterUserDetails> userdetailsses) {
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
            GetterSetterUserDetails userlist = userdetails.get(i);
            Holder holder = new Holder();
            @SuppressLint("ViewHolder") View rowView = this.inflater.inflate(R.layout.get_user_listview, null);
            holder.username = rowView.findViewById(R.id.username);
            holder.visibility = rowView.findViewById(R.id.visibilitystatus);
            holder.img = rowView.findViewById(R.id.user_icon);
            holder.lastMessage = rowView.findViewById(R.id.last_message);
            holder.time = rowView.findViewById(R.id.msg_time);
            holder.username.setText(userlist.getUname());
            holder.lastMessage.setText(userlist.getLastMessage());
            holder.time.setText(userlist.getTime());

         // UpDateData(userdetails);
        return rowView;

    }


    public void notifyDataSetChanged () {

    }



}
