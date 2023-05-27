package com.godthablab.wtracker.shared;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.godthablab.wtracker.R;
import com.godthablab.wtracker.ClenerGallery.SelectedFile;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileSaveProgressDialog extends DialogFragment {

public static FileSaveProgressDialog getInstance(ArrayList<SelectedFile> files) {
        FileSaveProgressDialog fileSaveProgressDialog = new FileSaveProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("files",files);
        fileSaveProgressDialog.setArguments(bundle);
        return fileSaveProgressDialog;
}

private ArrayList<SelectedFile> files;

@Override
public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE,0);
        if(getArguments()!=null)
                files = getArguments().getParcelableArrayList("files");

}


@BindView(R.id.filePending)
TextView filePending;
@BindView(R.id.totalFiles)
TextView totalFiles;
@BindView(R.id.tvTitle)
TextView tvTitle;
@BindView(R.id.operationProgress)
ProgressBar operationProgress;

@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.dialog_save_progress,  container,false);
        ButterKnife.bind(this,view);
        if(files == null)
                dismiss();

        totalFiles.setText(String.format(Locale.US,"%d",/*files.size()*/files.size()));
        operationProgress.setMax(files.size());
        new Thread(runnable).start();

        return view;
}

private Runnable runnable = () -> {
        for(int i=0;i<files.size();i++) {
                SelectedFile sf = files.get(i);
                if(SelectedFile.ADD.equals(files.get(i).action)) {
                        File src =  new File(sf.src);
                        Log.e(src.getAbsolutePath(),new File(sf.dst,src.getName()).getAbsolutePath());
                        try {

                                FileOperation.copy(src,new File(sf.dst,src.getName()));
                        } catch (IOException e) {
                                e.printStackTrace();
                        }

                } else if(SelectedFile.DELETE.equals(files.get(i).action)) {
                        FileOperation.delete(new File(sf.src));
                }
                updateProgressUi(i,sf.action);
        }

        complete();

};

private void updateProgressUi(int progress, String action) {
        if(getActivity()!=null)
        getActivity().runOnUiThread(() -> {
                tvTitle.setText(String.format(Locale.US,"Action : %s", action));
                operationProgress.setProgress(progress);
                filePending.setText(String.format(Locale.US,"%d",progress));
        });

}

private void complete() {
        if(getActivity()!=null)
                getActivity().runOnUiThread(() -> {
                        EventBus.getDefault().post("operationDone");
                        dismiss();
                });
}



}

