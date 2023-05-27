package com.godthablab.wtracker.tracker.visitsyou;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.godthablab.wtracker.R;


class VisitsYouHolder extends RecyclerView.ViewHolder{

TextView textViewSrNm;
TextView name;
TextView coins1;
TextView coins2;
TextView time;
LinearLayout relay_d;

VisitsYouHolder(View itemView) {
        super(itemView);
        textViewSrNm =  itemView.findViewById(R.id.textViewSrNm);
        name =  itemView.findViewById(R.id.name);
        coins1 =  itemView.findViewById(R.id.coins1);
        coins2 =  itemView.findViewById(R.id.coins2);
        time =  itemView.findViewById(R.id.time);
        relay_d =   itemView.findViewById(R.id.relay_d);
}

}