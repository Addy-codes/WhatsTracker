package com.godthablab.wtracker.tracker.wappcontact;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.godthablab.wtracker.R;


public class WappContactsHolder extends RecyclerView.ViewHolder {

public RoundedImageView contact_pic;
public TextView contact_name;

public WappContactsHolder(View itemView) {
        super(itemView);
        contact_pic = (RoundedImageView) itemView.findViewById(R.id.contact_pic);
        contact_name = (TextView) itemView.findViewById(R.id.contact_name);
}

}