package com.godthablab.wtracker.ClenerGallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.CleanerViewer.GifViewer;
import com.godthablab.wtracker.CleanerViewer.ImageViewer;
import com.godthablab.wtracker.CleanerViewer.MusicViewer;
import com.godthablab.wtracker.CleanerViewer.VideoViewer;
import com.godthablab.wtracker.ClenerGallery.SelectedFile;
import com.godthablab.wtracker.AssetType;
import com.godthablab.wtracker.shared.FileOperation;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileAdapter extends RecyclerView.Adapter<TileHolder> {

private Context context;
private List<File> filesList;
private boolean isSelectionOn = false;
private LayoutInflater inflater;
private Set<Integer> selectedFileIndex;
private int type;
private boolean readOnly;
private boolean menuDelete;
private boolean menuDownload;
private String dstPath;
public TileAdapter(Context context, int type , List<File> filesList, boolean readOnly,
                   boolean menuDelete, boolean menuDownload, String dstPath) {
        this.context =  context;
        this.filesList = filesList;
        this.menuDownload = menuDownload;
        this.menuDelete = menuDelete;
        this.type=type;
        inflater = LayoutInflater.from(context);
        selectedFileIndex = new HashSet<>();
        this.readOnly = readOnly;
        this.dstPath = dstPath;
}

@NonNull
@Override
public TileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_tile, parent,false );
        return new TileHolder(view );
}

@Override
public void onBindViewHolder(@NonNull TileHolder holder,
                             int position) {
        if (type == AssetType.TYPE_IMAGE || type == AssetType.TYPE_PROFILE_IMAGE) {
                Glide.with(context)
                        .load(filesList.get(position))
                        .thumbnail(0.1f)
                        .into(holder.ivThumbnail);
        } else if (type == AssetType.TYPE_GIF) { // Gif is in mp4
                if (filesList.get(position).getName().endsWith(".mp4")) {
                        Glide.with(context)
                                .load(ThumbnailUtils.createVideoThumbnail(filesList.get(position).getAbsolutePath(),
                                        MediaStore.Images.Thumbnails.MINI_KIND))
                                .into(holder.ivThumbnail);
                } else {
                        Glide.with(context)
                                .load(filesList.get(position))
                                .thumbnail(0.1f)
                                .into(holder.ivThumbnail);

                }
        } else if (type == AssetType.TYPE_VIDEO) {
                holder.ivPlayButton.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(ThumbnailUtils.createVideoThumbnail(filesList.get(position).getAbsolutePath(),
                                MediaStore.Images.Thumbnails.MINI_KIND))
                        .into(holder.ivThumbnail);
        } else if (type == AssetType.TYPE_AUDIO) {
                holder.setFileSize(filesList.get(position).length());
                Glide.with(context)
                        .load(R.drawable.thumbnail_audio)
                        .into(holder.ivThumbnail);
        }else if(type== AssetType.TYPE_VOICE_NOTE) {
                holder.setFileSize(filesList.get(position).length());
                Glide.with(context)
                        .load(R.drawable.thumbnail_voice)
                        .into(holder.ivThumbnail);
        } else if(type== AssetType.TYPE_DOCUMENT) {
                holder.setFileSize(filesList.get(position).length());
                Glide.with(context)
                        .load(R.drawable.thumbnail_document)
                        .into(holder.ivThumbnail);
        }

        if(isSelectionOn) {
                holder.ivSelection.setVisibility(View.VISIBLE);

                if(selectedFileIndex.contains(position)) {
                        holder.ivSelection.setImageResource(R.drawable.selection_checked);
                } else {
                        holder.ivSelection.setImageResource(R.drawable.selection_unchecked);
                }


        } else if(holder.ivSelection.getVisibility()== View.VISIBLE) {
                holder.ivSelection.setVisibility(View.INVISIBLE);
        }

        holder.ivThumbnail.setOnClickListener(v -> {


                if(isSelectionOn) {
                        if(selectedFileIndex.contains(position)) {
                                EventBus.getDefault().post(new SelectedFile(filesList.get(position).getAbsolutePath(),
                                                                            dstPath,
                                                           SelectedFile.REMOVE_FROM_LIST));
                                holder.ivSelection.setImageResource(R.drawable.selection_unchecked);
                                selectedFileIndex.remove(position);
                        } else {
                                // Check for OnLongClickListener also.
                                EventBus.getDefault().post(new SelectedFile(filesList.get(position).getAbsolutePath(),
                                                                dstPath,
                                                            menuDownload?SelectedFile.ADD:
                                                            menuDelete?SelectedFile.DELETE:SelectedFile.NONE));
                                holder.ivSelection.setImageResource(R.drawable.selection_checked);
                                selectedFileIndex.add(position);
                        }
                } else {
                        Intent intent = null;
                        if(type== AssetType.TYPE_IMAGE || type== AssetType.TYPE_PROFILE_IMAGE) {
                                intent = new Intent(context, ImageViewer.class);
                        } else if(type== AssetType.TYPE_VIDEO) {
                                intent = new Intent(context, VideoViewer.class);
                         }else if(type== AssetType.TYPE_AUDIO )  {
                                intent = new Intent(context, MusicViewer.class);
                        } else if(type== AssetType.TYPE_GIF) {
                                if(filesList.get(position).getName().endsWith("mp4"))
                                        intent = new Intent(context, VideoViewer.class);
                                else
                                        intent = new Intent(context, GifViewer.class);

                        } else if(type== AssetType.TYPE_DOCUMENT) {
                               // No need to open extra stream
                                FileOperation.openFile(filesList.get(position),context);
                                return;
                        } else if(type== AssetType.TYPE_VOICE_NOTE )  {
                                Toast.makeText(context, "Operation not supported", Toast.LENGTH_SHORT).show();
                                return;
                        }  else {
                                return;
                        }

                        intent.putExtra("file", filesList.get(position).getAbsolutePath());
                        intent.putExtra("readOnly", readOnly);
                        intent.putExtra("menuDelete", menuDelete);
                        intent.putExtra("menuDownload", menuDownload);
                        context.startActivity(intent);
                }


        });

        if(!readOnly) {
                // No selection for read only
                holder.ivThumbnail.setOnLongClickListener(v -> {
                        isSelectionOn = !isSelectionOn;
                        if(isSelectionOn) {
                                EventBus.getDefault().post(new SelectedFile(filesList.get(position).getAbsolutePath(),
                                        dstPath,
                                        menuDownload?SelectedFile.ADD:
                                                menuDelete?SelectedFile.DELETE:SelectedFile.NONE));
                                holder.ivSelection.setImageResource(R.drawable.selection_checked);
                                selectedFileIndex.add(position);
                        } else {
                                EventBus.getDefault().post("clearList");
                        }
                        notifyDataSetChanged();
                        return true;
                });
        }


}
/*

worked on parental control issue Resolved
 parental control ui half Completed
 Status Saver UI Improved
 WhatsCleaner UI Improved Gallery remained
Working on Gallery Crash issue

* */

public void setSelectionOn(boolean selectionOn) {
        // clear selected indices after selection turned off
        if(!selectionOn) {
                selectedFileIndex.clear();
        }
        isSelectionOn = selectionOn;
}

@Override
public int getItemCount() {
        return filesList.size();
}


}
