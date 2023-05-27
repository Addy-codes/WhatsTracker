package com.godthablab.wtracker.gallery;

import android.content.Intent;
import android.os.Bundle;
import com.godthablab.wtracker.ClenerGallery.GalleryActivity;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.shared.AssetType;
import com.godthablab.wtracker.shared.C;
import com.godthablab.wtracker.shared.GallerySetting;
import androidx.appcompat.app.AppCompatActivity;

public class MainWhatsGalleryActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.gallery_activity);
        gallery();
    }

    public void gallery() {
        {
            GallerySetting gallerySetting = GallerySetting.GallerySettingBuilder.builder()
                    .assetType(AssetType.TYPE_IMAGE,AssetType.TYPE_VIDEO)
                    .titleStripsName("Image","Video")
                    .fileReadPaths(C.WA_IMAGES,C.WA_VIDEOS)
                    .fileSavePaths(C.WW_STATUS_IMAGES,C.WW_STATUS_VIDEOS)
                    .readOnly(true)
                    .emptyListMessage(getString(R.string.empty_msg_status))
                    .menuDownload(false)
                    .menuDelete(false)
                    .build();
            Intent intent = new Intent(this, GalleryActivity.class);
            intent.putExtra("gallerySetting",gallerySetting);
            intent.putExtra("title","Gallery");
            startActivity(intent);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
