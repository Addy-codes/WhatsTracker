package com.godthablab.wtracker.gallery;

import static com.godthablab.wtracker.shared.GallerySetting.GallerySettingBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.godthablab.wtracker.ClenerGallery.GalleryActivity;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.shared.AssetType;
import com.godthablab.wtracker.shared.C;
import com.godthablab.wtracker.shared.FileOperation;
import com.godthablab.wtracker.shared.GallerySetting;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusSaverActivity extends AppCompatActivity implements View.OnClickListener {

      /*  @BindView(R.id.rvStatus)
        RecyclerView rvStatus;*/

       /* @BindView(R.id.toolbar)
        Toolbar toolbar;*/

   /*     @BindView(R.id.toolbar_title)
        TextView toolbarTitle;*/

        @BindView(R.id.flBtnRecentStories)
        FrameLayout flBtnRecentStories;

        @BindView(R.id.flBtnSavedStories)
        FrameLayout flBtnSavedStories;



/*

        @BindView(R.id.AdContainer)
        FrameLayout AdContainer;

        @BindView(R.id.adView)
        AdView adView;
*/


        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView( R.layout.gallery_from_status );
               ButterKnife.bind(this);




             //   Ads.adMobBannerRequest(this,adView);
             //   setSupportActionBar(toolbar);

                if(getSupportActionBar()!=null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                 //       toolbarTitle.setText(R.string.title_status_saver);
                }


            /*    rvStatus.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
                rvStatus.setAdapter(new StatusSaverAdapter(this, FileOperation.getFiles(C.WA_STATUS)));
*/
                flBtnRecentStories.setOnClickListener(this);
                flBtnSavedStories.setOnClickListener(this);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

                if(item.getItemId() == android.R.id.home) {
                        finish();
                }

                return super.onOptionsItemSelected(item);
        }



        @Override
        public void onClick(View v) {

                if(v.equals(flBtnRecentStories)) {
                        GallerySetting  gallerySetting = GallerySettingBuilder.builder()
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


                if(v.equals(flBtnSavedStories)) {
                        GallerySetting  gallerySetting = GallerySettingBuilder.builder()
                                .assetType(AssetType.TYPE_IMAGE,AssetType.TYPE_VIDEO)
                                .titleStripsName("Image","Video")
                                .fileReadPaths(C.WW_STATUS_IMAGES,C.WW_STATUS_VIDEOS)
                                .fileSavePaths(C.WW_STATUS_IMAGES,C.WW_STATUS_VIDEOS)
                                .readOnly(false)
                                .menuDownload(false)
                                .menuDelete(true)
                                .build();
                        Intent intent = new Intent(this, GalleryActivity.class);
                        intent.putExtra("gallerySetting",gallerySetting);
                        intent.putExtra("title","Saved Stories");
                        startActivity(intent);
                }
        }



   /*     @Override
        protected void onStart() {
                super.onStart();
                EventBus.getDefault().register(this);
        }*/

/*        @Override
        protected void onStop() {
                super.onStop();
                EventBus.getDefault().unregister(this);
        }*/

    /*    *//*Ignore message data*//*
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onMessageEvent(Ads.AdsPurchaseMessage adsPurchaseMessage) {
                if(adsPurchaseMessage.isAdsPurchased()) {
                        AdContainer.setVisibility(View.GONE);
                } else {
                        AdContainer.setVisibility(View.VISIBLE);
                }
        }
*/

}
