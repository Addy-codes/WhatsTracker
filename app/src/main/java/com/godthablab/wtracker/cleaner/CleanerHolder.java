package com.godthablab.wtracker.cleaner;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.godthablab.wtracker.R;
import com.godthablab.wtracker.FileOperation;

import java.util.Locale;

public class CleanerHolder extends RecyclerView.ViewHolder {

public RelativeLayout rlBtnParent;
public TextView tvTitle;
public ImageView ivBackground;
public TextView tvFileSize;
public TextView tvFileCount;

CleanerHolder(@NonNull View itemView) {
        super(itemView);
        rlBtnParent = itemView.findViewById(R.id.rlBtnParent);
        ivBackground = itemView.findViewById(R.id.ivBackground);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvFileSize = itemView.findViewById(R.id.tvFileSize);
        tvFileCount = itemView.findViewById(R.id.tvFileCount);
}

void setButton(CleanerButton cleanerButton)  {
        ivBackground.setImageResource(cleanerButton.res_drawable);
        tvTitle.setText(cleanerButton.name);
        setFileSize(cleanerButton.fileSize);
        setFileCount(cleanerButton.fileCount);
}

public void setFileSize(long fileSize) {
        tvFileSize.setText(String.format(Locale.getDefault(),"%s", FileOperation.humanReadableByteCount(fileSize,true)));
}

public void setFileCount(int fileCount) {
        if(fileCount>1)
                tvFileCount.setText(String.format(Locale.getDefault(),"%d Files",fileCount));
        else
                tvFileCount.setText(String.format(Locale.getDefault(),"%d File",fileCount));
}


}
