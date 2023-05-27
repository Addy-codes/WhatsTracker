package com.godthablab.wtracker.tracker.visitedbyyou;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.godthablab.wtracker.R;


public class VisitedByYouHolder extends RecyclerView.ViewHolder {

public RoundedImageView contact_pic;
public TextView contact_name;
public VisitedByYouHolder(View itemView) {
        super(itemView);
        contact_pic = (RoundedImageView) itemView.findViewById(R.id.contact_pic);
        contact_name = (TextView) itemView.findViewById(R.id.contact_name);
}

}
