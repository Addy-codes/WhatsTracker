package com.godthablab.wtracker.tracker.wappcontact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.godthablab.wtracker.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class WappContactsAdapter extends RecyclerView.Adapter<WappContactsHolder> {

private int count;
private Context context;
private LayoutInflater layoutInflater;
private List<String> visitedByUModels = new ArrayList<>();

WappContactsAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        count = 0;
}

void addToList(List<String> list) {
        visitedByUModels.clear();
        visitedByUModels.addAll(list);
        Collections.sort(visitedByUModels, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                }
        });
        count = list.size();
}

@NonNull
@Override
public WappContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_wapp_contacts, parent, false);
        return new WappContactsHolder(view);
}

@Override
public void onBindViewHolder(@NonNull WappContactsHolder holder, int position) {

        Glide.with(context)
                .load(R.drawable.default_profile_image)
                .into(holder.contact_pic);

        holder.contact_name.setText(visitedByUModels.get(position));
}

@Override
public int getItemCount() {
        return count;
}


}
