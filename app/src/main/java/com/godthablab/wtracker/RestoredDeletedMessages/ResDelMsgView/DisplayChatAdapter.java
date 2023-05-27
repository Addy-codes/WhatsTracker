package com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.godthablab.wtracker.R;

import java.util.ArrayList;
import java.util.HashMap;

class DisplayChatAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Context context;
    private ArrayList<String> imagpath;
    private ArrayList<String> ismessagetype;
    private ArrayList<HashMap<String, String>> messageArray;
    private ArrayList<String> msgstatuslist;
    private ArrayList<String> sendUser;
    private ArrayList<String> time;

    public DisplayChatAdapter(DisplayChat context, ArrayList<HashMap<String, String>> messageArray, ArrayList<String> senderUser, ArrayList<String> msgtime, ArrayList<String> msgstatuslist, ArrayList<String> ismessagetype) {

    }

    private class Holder {
        LinearLayout imageLayoutreceiver;
        LinearLayout imageLayoutsender;
        ImageView msgread;
        ImageView msgreceive;
        ImageView msgsend;
        TextView receive;
        LinearLayout receiverLayout;
        TextView receivetime;
        TextView receivetimeimage;
        ImageView receverimage;
        TextView send;
        LinearLayout senderLayout;
        ImageView senderimage;
        TextView sendertimeimage;
        TextView sendtime;
        TextView isDeleted;

    }


    public DisplayChatAdapter(Context context, ArrayList<HashMap<String, String>> messageArray, ArrayList<String> senderUser, ArrayList<String> time, ArrayList<String> msgstatuslist, ArrayList<String> ismessagetype, ArrayList<String> imagpath) {
        this.context = context;
        this.messageArray = messageArray;
        this.sendUser = senderUser;
        this.time = time;
        this.msgstatuslist = msgstatuslist;
        this.ismessagetype = ismessagetype;
        this.imagpath = imagpath;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.messageArray.size();
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder = new Holder();
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.display_chat_list, null);
        holder.send = rowView.findViewById(R.id.send);
        holder.receivetime = rowView.findViewById(R.id.receivetime);
        holder.senderLayout = rowView.findViewById(R.id.senderLayout);

        holder.isDeleted = rowView.findViewById(R.id.isDeleted);
        holder.receivetime.setText(this.time.get(i));

            HashMap<String, String> object = this.messageArray.get(i);
            if (object.get("IsDeleted").equals("1"))

                holder.isDeleted.setVisibility(View.VISIBLE);
            else
                holder.isDeleted.setVisibility(View.GONE);
            {
                Log.i("Is Deleted ","Message Is Deleted ");
            }

            holder.send.setText(object.get("Message"));



/*
HashMap<String, String> object = this.messageArray.get(i);
        if (object.get("IsDeleted").equals("1"))

            holder.isDeleted.setVisibility(View.VISIBLE);
        else
            holder.isDeleted.setVisibility(View.GONE);
        {
            Log.i("Is Deleted ","Message Is Deleted ");
        }

        holder.send.setText(object.get("Message"));
*/




/*        holder.msgsend.setVisibility(View.INVISIBLE);
        holder.msgreceive.setVisibility(View.INVISIBLE);
        holder.msgread.setVisibility(View.VISIBLE);*/

        return rowView;
    }

}
