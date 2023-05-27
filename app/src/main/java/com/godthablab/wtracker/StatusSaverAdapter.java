package com.godthablab.wtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.godthablab.wtracker.CleanerViewer.ImageViewer;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    public void onBindViewHolder(@NonNull StatusViewHolder holder, @SuppressLint("RecyclerView") int position) {

/*

        if (position != 0){

            Toast.makeText(context.getApplicationContext(), "FileList position less than", Toast.LENGTH_SHORT).show();

        }

        else {
            Toast.makeText(context.getApplicationContext(), "position are full", Toast.LENGTH_SHORT).show();

        }
*/


        if (position == 0) {
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
                    intent.putExtra("file", filesList[position].getAbsolutePath());
                    context.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return filesList.length;
    }


}
