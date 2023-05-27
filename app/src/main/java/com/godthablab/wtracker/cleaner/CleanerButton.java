package com.godthablab.wtracker.cleaner;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import com.godthablab.wtracker.AssetType;
import com.godthablab.wtracker.FileOperation;

import java.io.File;
import java.lang.ref.WeakReference;

public class CleanerButton {

public String name;
public String readFolderPath;
public int assetType;
public int fileCount;
public long fileSize;
public int res_drawable;
public boolean isCalculated;
public AsyncTask<String, String, String> asyncTask;

public CleanerButton(String name, String readFolderPath, int assetType,
                     int fileCount, long fileSize, int res_drawable) {
        this.name = name;
        this.readFolderPath = readFolderPath;
        this.assetType = assetType;
        this.fileCount = fileCount;
        this.fileSize = fileSize;
        this.res_drawable = res_drawable;
        isCalculated = false;
}


@SuppressLint("StaticFieldLeak")
void calculate(WeakReference<CleanerHolder> cleanerHolder) {
        asyncTask = new MyAsyncTask(cleanerHolder,new WeakReference<>(this));
        asyncTask.execute("start");

}

public void cancel() {
        if(asyncTask!=null)
                asyncTask.cancel(true);
}

static class MyAsyncTask extends AsyncTask<String, String, String> {
        WeakReference<CleanerHolder> cleanerHolder;

        private WeakReference<CleanerButton> cleanerButton;

        MyAsyncTask(WeakReference<CleanerHolder> cleanerHolder,
                    WeakReference<CleanerButton> cleanerButton) {
                this.cleanerHolder = cleanerHolder;
                this.cleanerButton = cleanerButton;
        }

        @Override
        protected String doInBackground(String... strings) {
                File folder = new File(cleanerButton.get().readFolderPath);
                File folderSent = new File(cleanerButton.get().readFolderPath,"/Sent");
                File[] files;

                if(this.cleanerButton.get().assetType == AssetType.TYPE_VOICE_NOTE) {
                        files = FileOperation.findFileRecursive(folder);
                } else
                        files = folder.listFiles();

                if(files==null) {
                        cleanerButton.get().fileCount = 0;
                        cleanerButton.get().fileSize = 0;
                        return "ok";
                }
                cleanerButton.get().fileCount = 0;
                cleanerButton.get().fileSize = 0;
                int count = 0;


                for(File f :files ) {
                        if(!f.isDirectory() && isValid(f.getName())  ) {
                                cleanerButton.get().fileSize += f.length();
                                count++;

                                if(this.cleanerButton.get().assetType == AssetType.TYPE_GIF)

                                        Log.e("TYPE_GIF",count+ " "+ f.getName());
                        }
                }
                // For Sent folder inside readFolderPath
                if(this.cleanerButton.get().assetType == AssetType.TYPE_VOICE_NOTE)
                        files = FileOperation.findFileRecursive(folderSent);
                else
                        files = folderSent.listFiles();


                if(files!=null) {
                        for(File f :files ) {
                                if(!f.isDirectory() && isValid(f.getName()) ) {
                                        cleanerButton.get().fileSize += f.length();
                                        count++;

                                        if(this.cleanerButton.get().assetType == AssetType.TYPE_GIF)

                                                Log.e("TYPE_GIF",count+ " "+ f.getName());
                                }
                        }
                }

                cleanerButton.get().fileCount = count;
                return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
                super.onPostExecute(s);
                cleanerButton.get().isCalculated = true;
                cleanerHolder.get().setFileCount(cleanerButton.get().fileCount);
                cleanerHolder.get().setFileSize(cleanerButton.get().fileSize);
        }


private boolean isValid(String fName) {
        switch (this.cleanerButton.get().assetType) {

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
                        .TYPE_DOCUMENT: return !".nomedia".equalsIgnoreCase(fName); /* Document can be anything */

                case AssetType
                        .TYPE_DATABASE:  return fName.endsWith(".crypt12"); /* Database can be anything */

        }
        return false;
}

}// end of async task


}
