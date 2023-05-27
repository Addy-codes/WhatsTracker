package com.godthablab.wtracker;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

class StatusViewHolder extends RecyclerView.ViewHolder {

public CircleImageView status;

StatusViewHolder(@NonNull View itemView) {
        super(itemView);
        status = itemView.findViewById(R.id.status);
}

}
