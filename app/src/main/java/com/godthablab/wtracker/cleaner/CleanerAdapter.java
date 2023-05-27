package com.godthablab.wtracker.cleaner;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.godthablab.wtracker.ClenerGallery.GalleryActivity2;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.ClenerGallery.GalleryActivity;
import com.godthablab.wtracker.shared.AssetType;
import com.godthablab.wtracker.shared.C;
import com.godthablab.wtracker.shared.GallerySetting;
import com.godthablab.wtracker.shared.GallerySetting.GallerySettingBuilder;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

public class CleanerAdapter extends RecyclerView.Adapter<CleanerHolder> {

private LayoutInflater inflater;
private Context context;
private List<CleanerButton> cleanerButtonList;
CleanerAdapter(Context context, List<CleanerButton> cleanerButtonList) {
        this.context = context;
        this.cleanerButtonList = cleanerButtonList;
        inflater = LayoutInflater.from(context);
}  // Constructor

@NonNull
@Override
public CleanerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_button_layout, parent,false );
        return new CleanerHolder(view);
}  //Cleaner ViewHolder

@Override
public void onBindViewHolder(@NonNull CleanerHolder holder, int position) {
        CleanerButton button = cleanerButtonList.get(position);
        holder.setButton(button);
        if(!button.isCalculated)
                button.calculate(new WeakReference<>(holder));

        holder.rlBtnParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        handleOnClick(button.assetType,button.name);
                }
        });
}  // Binding data with Listview

@Override
public int getItemCount() {
        return cleanerButtonList.size();
}    // Counting items


private void handleOnClick(int type, String title) {

        GallerySetting gallerySetting = null;
        if(type == AssetType.TYPE_IMAGE) {
                gallerySetting = GallerySettingBuilder.builder()
                        .assetType(AssetType.TYPE_IMAGE,AssetType.TYPE_IMAGE)
                        .titleStripsName("Sent","Received")
                        .fileReadPaths(C.WA_IMAGES+"/Sent",C.WA_IMAGES)
                        .fileSavePaths("ignored value","ignored value")
                        .readOnly(false)
                        .menuDownload(false)
                        .menuDelete(true)
                        .showInterstitial(true)
                        .build();


        } else if(type == AssetType.TYPE_VIDEO){
                gallerySetting = GallerySettingBuilder.builder()
                        .assetType(AssetType.TYPE_VIDEO,AssetType.TYPE_VIDEO)
                        .titleStripsName("Sent","Received")
                        .fileReadPaths(C.WA_VIDEOS+"/Sent",C.WA_VIDEOS)
                        .fileSavePaths("ignored value","ignored value")
                        .readOnly(false)
                        .menuDownload(false)
                        .menuDelete(true)
                        .showInterstitial(true)
                        .build();


        }else if(type == AssetType.TYPE_AUDIO ) {
                gallerySetting = GallerySettingBuilder.builder()
                        .assetType(AssetType.TYPE_AUDIO,AssetType.TYPE_AUDIO)
                        .titleStripsName("Sent","Received")
                        .fileReadPaths(C.WA_AUDIO+"/Sent",C.WA_AUDIO)
                        .fileSavePaths("ignored value","ignored value")
                        .readOnly(false)
                        .menuDownload(false)
                        .menuDelete(true)
                        .showInterstitial(true)
                        .build();


        } else if(type == AssetType.TYPE_VOICE_NOTE ) {
                gallerySetting = GallerySettingBuilder.builder()
                        .assetType(AssetType.TYPE_VOICE_NOTE,AssetType.TYPE_VOICE_NOTE)
                        .titleStripsName("Sent","Received")
                        .fileReadPaths(C.WA_VOICE_NOTE+"/Sent",C.WA_VOICE_NOTE)
                        .fileSavePaths("ignored value","ignored value")
                        .readOnly(false)
                        .menuDownload(false)
                        .menuDelete(true)
                        .showInterstitial(true)
                        .build();


        } else if(type == AssetType.TYPE_GIF ) {
                gallerySetting = GallerySettingBuilder.builder()
                        .assetType(AssetType.TYPE_GIF,AssetType.TYPE_GIF)
                        .titleStripsName("Sent","Received")
                        .fileReadPaths(C.WA_GIF+"/Sent",C.WA_GIF)
                        .fileSavePaths("ignored value","ignored value")
                        .readOnly(false)
                        .menuDownload(false)
                        .menuDelete(true)
                        .showInterstitial(true)
                        .build();



        }else if(type == AssetType.TYPE_PROFILE_IMAGE ) {
                gallerySetting = GallerySettingBuilder.builder()
                        .assetType(AssetType.TYPE_PROFILE_IMAGE)
                        .titleStripsName("Profile")
                        .fileReadPaths(C.WA_PROFILE_PHOTO)
                        .fileSavePaths("ignored value")
                        .readOnly(false)
                        .menuDownload(false)
                        .menuDelete(true)
                        .hideTabLayout(true)
                        .showInterstitial(true)
                        .build();



        } else if(type == AssetType.TYPE_DOCUMENT ) {
                gallerySetting = GallerySettingBuilder.builder()
                        .assetType(AssetType.TYPE_DOCUMENT,AssetType.TYPE_DOCUMENT)
                        .titleStripsName("Sent","Received")
                        .fileReadPaths(C.WA_DOCUMENT+"/Sent",C.WA_DOCUMENT)
                        .fileSavePaths("ignored value","ignored value")
                        .readOnly(false)
                        .menuDownload(false)
                        .menuDelete(true)
                        .showInterstitial(true)
                        .build();


        } else {
                cleanDatabase();
                return;
        }



        Intent intent = new Intent(context, GalleryActivity2.class);
        intent.putExtra("gallerySetting",gallerySetting);
        intent.putExtra("title",title);
        context.startActivity(intent);

}



private void cleanDatabase() {

         new AlertDialog.Builder(context)
                        .setMessage(R.string.database_delete_message)
                        .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                                File databases = new File(C.WA_DATABASES);
                                if(databases.listFiles()==null )
                                        return;

                                for(File f : databases.listFiles()) {
                                        // Reserved file name we don't want to delete main backup file
                                        if(!"msgstore.db.crypt12".equals(f.getName())) {
                                                f.delete();
                                        } else {
                                                Toast.makeText(context,R.string.main_database_msg, Toast.LENGTH_SHORT).show();
                                        }
                                }
                                updateDbEntry();
                                notifyDataSetChanged();
                                Toast.makeText(context,R.string.database_deleted, Toast.LENGTH_SHORT).show();
                        })
                        .create()
                        .show();

}

// We don't have to compute all files again in case of DB delete
private void updateDbEntry() {
        for(int i=0;i<cleanerButtonList.size();i++) {
                if(cleanerButtonList.get(i).assetType == AssetType.TYPE_DATABASE) {
                        cleanerButtonList.get(i).isCalculated = false;
                }
        }
}


}
