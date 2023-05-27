package com.godthablab.wtracker.ClenerGallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.godthablab.wtracker.R;
import com.godthablab.wtracker.ClenerGallery.adapter.TileAdapter;
import com.godthablab.wtracker.shared.AssetType;
import com.godthablab.wtracker.shared.FileOperation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryFragment extends Fragment {

@Deprecated
public static GalleryFragment getInstance(int type, boolean isReadOnly, boolean menuDownload, boolean menuDelete,
                                          String fileReadPath, String fileSavePath) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        bundle.putBoolean("readOnly",isReadOnly);
        bundle.putBoolean("menuDownload",menuDownload);
        bundle.putBoolean("menuDelete",menuDelete);
        bundle.putString("fileReadPath",fileReadPath);
        bundle.putString("fileSavePath",fileSavePath);
        fragment.setArguments(bundle);
        return fragment;
}


public static GalleryFragment getInstance(int type, boolean isReadOnly, boolean menuDownload, boolean menuDelete,
                                          String fileReadPath, String fileSavePath, String emptyListMessage) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        bundle.putBoolean("readOnly",isReadOnly);
        bundle.putBoolean("menuDownload",menuDownload);
        bundle.putBoolean("menuDelete",menuDelete);
        bundle.putString("fileReadPath",fileReadPath);
        bundle.putString("fileSavePath",fileSavePath);
        bundle.putString("emptyListMessage",emptyListMessage);
        fragment.setArguments(bundle);
        return fragment;
}

private int type = 0;
private boolean readOnly;
private boolean menuDelete;
private boolean menuDownload;
private String fileReadPath;
private String fileSavePath;
private String emptyListMessage = "";

@Override
public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
                type = getArguments().getInt("type",-1);
                readOnly = getArguments().getBoolean("readOnly",true);
                menuDownload = getArguments().getBoolean("menuDownload",false);
                menuDelete = getArguments().getBoolean("menuDelete",false);
                fileReadPath = getArguments().getString("fileReadPath");
                fileSavePath = getArguments().getString("fileSavePath");
                emptyListMessage = getArguments().getString("emptyListMessage");
        }
}

@BindView(R.id.rvGallery)
RecyclerView rvGallery;
@BindView(R.id.tvEmptyListMessage)
TextView tvEmptyListMessage;
TileAdapter tileAdapter;
List<File> fileArrayList = new ArrayList<>();
BackgroundSync backgroundSync;

@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                         @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.frag_gallery_frag,  container,false);
        ButterKnife.bind(this,view);
        // fileArrayList = getFiles();
        tileAdapter = new TileAdapter(getActivity(),type,fileArrayList,readOnly,menuDelete,menuDownload,fileSavePath);
        rvGallery.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rvGallery.setAdapter(tileAdapter);
        backgroundSync = new BackgroundSync(new WeakReference<>(this));
        backgroundSync.execute();
        return view;
}


private List<File> getFiles() {
        ArrayList<File> files = new ArrayList<>();

        File mediaArray[];

        if(type==AssetType.TYPE_VOICE_NOTE) {
                mediaArray = FileOperation.findFileRecursive(new File(fileReadPath));
        } else {
                mediaArray = FileOperation.getFiles(fileReadPath);
        }

        for(File f: mediaArray ){
                if( !f.isDirectory() && toAdd(f.getName()))  {
                        files.add(f);
                }
        }

        return files;
}


private boolean toAdd(String fName) {
        switch (type) {

                case AssetType
                        .TYPE_PROFILE_IMAGE :

                case AssetType
                      .TYPE_IMAGE : return fName.endsWith(".jpg")||fName.endsWith(".png")||
                                           fName.endsWith(".jpeg");
                case AssetType
                      .TYPE_VIDEO : return fName.endsWith(".mp4")||fName.endsWith(".m4a")||
                                           fName.endsWith(".mkv");
                case AssetType
                      .TYPE_GIF : return fName.endsWith(".gif") || fName.endsWith(".mp4");

                case AssetType
                      .TYPE_AUDIO :

                case AssetType
                        .TYPE_VOICE_NOTE : return fName.endsWith(".mp3")||fName.endsWith(".wav")||fName.endsWith(".opus");

                case AssetType
                        .TYPE_DOCUMENT: return true; /* Document can be anything */

        }
        return false;
}


@Override
public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
}

@Override
public void onStop() {
        super.onStop();
        if(backgroundSync!=null && !backgroundSync.isCancelled()) {
                backgroundSync.cancel(true);
        }
        EventBus.getDefault().unregister(this);
}

/*Ignore message data*/
@Subscribe(threadMode = ThreadMode.MAIN)
public void onMessageEvent(Reload reload) {
        fileArrayList.clear();
        fileArrayList.addAll(getFiles());
        tileAdapter.setSelectionOn(false);
        tileAdapter.notifyDataSetChanged();
}


/* Class object to reload data*/
public static class Reload {}


public static class BackgroundSync extends AsyncTask<Void, String, String> {
        WeakReference<GalleryFragment> ref;
        public BackgroundSync(WeakReference<GalleryFragment> ref) {
                this.ref = ref;
        }

        @Override
        protected String doInBackground(Void... voids) {
                ref.get().fileArrayList.clear();
                ref.get().fileArrayList.addAll(ref.get().getFiles());
                return "done";
        }

        @Override
        protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if( !isCancelled() && ref.get()!=null && !ref.get().isDetached() ) {
                        if(ref.get().fileArrayList.size()==0 && ref.get().emptyListMessage!=null) {
                                ref.get().tvEmptyListMessage.setVisibility(View.VISIBLE);
                                ref.get().tvEmptyListMessage.setText(ref.get().emptyListMessage);
                        }

                        ref.get().tileAdapter.notifyDataSetChanged();
                }


        }
}


}
