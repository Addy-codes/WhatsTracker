package com.godthablab.wtracker.NewSaverStatus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.CleanerViewer.ImageViewer;

import java.io.File;

public class StatusSaverAdapter extends RecyclerView.Adapter<StatusViewHolder> {

        private Context context;
        private File[] filesList;
        private LayoutInflater inflater;

        public StatusSaverAdapter(Context context, File[] filesList) {
                this.context =  context;
                this.filesList = filesList;
                inflater = LayoutInflater.from(context);
        }


        @NonNull
        @Override
        public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.row_status_saver, parent,false );
                return new StatusViewHolder(view );
        }

        @Override
        public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {

                if(position == 0) {
                        // Load default image
                        holder.status.setBorderWidth(2);
                        holder.status.setBorderColor(Color.WHITE);
                        Glide.with(context)
                                .load(R.drawable.circle_cropped)
                                .dontAnimate()
                                .into(holder.status);
                } else {
                        holder.status.setBorderWidth(0);
                        Glide.with(context)
                                .load(filesList[position])
                                .dontAnimate()
                                .thumbnail(0.1f)
                                .into(holder.status);

                        holder.status.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        Intent intent = new Intent(context, ImageViewer.class);
                                        intent.putExtra("file",filesList[position].getAbsolutePath());
                                        context.startActivity(intent);
                                }
                        });
                }


        }

        @Override
        public int getItemViewType(int position) {
                return position==0?0:1;
        }

        @Override
        public int getItemCount() {
                return filesList.length;
        }

}
