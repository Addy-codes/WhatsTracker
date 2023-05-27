package com.godthablab.wtracker.tracker.visitedbyyou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.godthablab.wtracker.R;

import java.util.ArrayList;
import java.util.List;

public class VisitedByYouAdapter extends RecyclerView.Adapter<VisitedByYouHolder> {

private int count;
private Context context;
private LayoutInflater layoutInflater;
private List<VisitedByUModel> visitedByUModels = new ArrayList<>();

public VisitedByYouAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        count = 0;
}

public void addToList(List<VisitedByUModel> list) {
        visitedByUModels.clear();
        visitedByUModels.addAll(list);
        count = list.size();
}

@NonNull
@Override
public VisitedByYouHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_visited_by_you, parent, false);
        VisitedByYouHolder viewHolder = new VisitedByYouHolder(view);
        return viewHolder;
}

@Override
public void onBindViewHolder(@NonNull VisitedByYouHolder holder, int position) {

        if (visitedByUModels.get(position).getImagePath().equals("")) {
                Glide.with(context)
                        .load(R.drawable.default_profile_image)
                        .into(holder.contact_pic);

        } else {
                Glide.with(context)
                        .load(visitedByUModels.get(position).getImageFile())
                        .into(holder.contact_pic);
        }

        holder.contact_name.setText(visitedByUModels.get(position).getName());
}

@Override
public int getItemCount() {
        return count;
}

}
