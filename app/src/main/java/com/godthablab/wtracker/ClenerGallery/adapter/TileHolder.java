package com.godthablab.wtracker.ClenerGallery.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.godthablab.wtracker.R;
import com.godthablab.wtracker.shared.FileOperation;

import java.util.Locale;

public class TileHolder extends RecyclerView.ViewHolder {

public ImageView ivThumbnail;
public ImageView ivSelection;
public ImageView ivPlayButton;
public TextView tvDescriptionText;

public TileHolder(@NonNull View itemView) {
        super(itemView);
        ivSelection = itemView.findViewById(R.id.ivSelection);
        ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        ivPlayButton = itemView.findViewById(R.id.ivPlayButton);
        tvDescriptionText = itemView.findViewById(R.id.tvDescriptionText);
}

public void setFileSize(long fileSize) {
        tvDescriptionText.setVisibility(View.VISIBLE);
        tvDescriptionText.setText(String.format(Locale.getDefault(),"%s", FileOperation.humanReadableByteCount(fileSize,true)));
}

}
