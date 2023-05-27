package com.godthablab.wtracker.NewSaverStatus;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.android.gms.ads.AdView;
import com.godthablab.wtracker.BillingClient.Billing;
import com.godthablab.wtracker.ClenerGallery.GalleryActivity2;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.ClenerGallery.GalleryActivity;
import com.godthablab.wtracker.shared.AssetType;
import com.godthablab.wtracker.shared.C;
import com.godthablab.wtracker.shared.FileOperation;
import com.godthablab.wtracker.shared.GallerySetting;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.godthablab.wtracker.BillingClient.Billing.preminum;
import static com.godthablab.wtracker.shared.GallerySetting.*;
import static com.godthablab.wtracker.whatsWebScan.WebActivity.MyPREFERENCES;

import java.io.File;
import java.util.ArrayList;

public class StatusSaverActivity extends AppCompatActivity implements View.OnClickListener {

        @BindView(R.id.rvStatus)
        RecyclerView rvStatus;

        @BindView(R.id.toolbar)
        Toolbar toolbar;

        @BindView(R.id.toolbar_title)
        TextView toolbarTitle;

        @BindView(R.id.flBtnRecentStories)
        FrameLayout flBtnRecentStories;

        @BindView(R.id.flBtnSavedStories)
        FrameLayout flBtnSavedStories;
        LinearLayout backbutton;


        private MoPubView moPubView;

        TextView emptyView;
        long statusFolder = 0;

/*
        @BindView(R.id.AdContainer)
        FrameLayout AdContainer;

        @BindView(R.id.adView)
        AdView adView;
*/


        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView( R.layout.frag_status_saver );

                checkpremium();

                final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
                MoPub.initializeSdk(this, configuration.build(),initSdkListener());


                ButterKnife.bind(this);

             //   Ads.adMobBannerRequest(this,adView);
                setSupportActionBar(toolbar);

                if(getSupportActionBar()!=null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        toolbarTitle.setText(R.string.title_status_saver);
                }


                this.backbutton = findViewById(R.id.backbutton);
                emptyView = (TextView) findViewById(R.id.empty_view);

                backbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                                onBackPressed();

                        }
                });


                rvStatus.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
                rvStatus.setAdapter(new StatusSaverAdapter(this, FileOperation.getFiles(C.WA_STATUS)));

                flBtnRecentStories.setOnClickListener(this);
                flBtnSavedStories.setOnClickListener(this);


                checkStatus();

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
                                .fileReadPaths(C.WA_STATUS,C.WA_STATUS)
                                .fileSavePaths(C.WW_STATUS_IMAGES,C.WW_STATUS_VIDEOS)
                                .readOnly(false)
                                .emptyListMessage(getString(R.string.empty_msg_status))
                                .menuDownload(true)
                                .menuDelete(false)
                                .build();
                        Intent intent = new Intent(this, GalleryActivity2.class);
                        intent.putExtra("gallerySetting",gallerySetting);
                        intent.putExtra("title","Recent Stories");
                        startActivity(intent);

                }

                if(v.equals(flBtnSavedStories)) {
                        GallerySetting  gallerySetting = GallerySettingBuilder.builder()
                                .assetType(AssetType.TYPE_IMAGE,AssetType.TYPE_VIDEO)
                                .titleStripsName("Image","Video")
                                .fileReadPaths(C.WW_STATUS_IMAGES,C.WW_STATUS_VIDEOS)
                                .fileSavePaths(C.WW_STATUS_IMAGES,C.WW_STATUS_VIDEOS)
                                .readOnly(false)
                                .menuDelete(true)
                                .menuDownload(false)
                                .build();
                        Intent intent = new Intent(this, GalleryActivity2.class);
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

        @Override
        protected void onStop() {
                super.onStop();
                EventBus.getDefault().unregister(this);
        }

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

        @Override
        public void onBackPressed() {
                super.onBackPressed();
                finish();
        }



        public long statusFolderSize(String path) {

                ArrayList<String> directory = new ArrayList<String>();
                File file = new File(path);

                if(file.isDirectory()) {
                        directory.add(file.getAbsolutePath());
                        while (directory.size() > 0) {
                                String folderPath = directory.get(0);
                                System.out.println("Size of this :"+folderPath);
                                directory.remove(0);
                                File folder = new File(folderPath);
                                File[] filesInFolder = folder.listFiles();
                                int noOfFiles = filesInFolder.length;

                                for(int i = 0 ; i < noOfFiles ; i++) {
                                        File f = filesInFolder[i];
                                        if(f.isDirectory()) {
                                                directory.add(f.getAbsolutePath());
                                        } else {
                                                this.statusFolder += f.length();
                                        }
                                }
                        }
                } else {
                        this.statusFolder = file.length();
                }
                return this.statusFolder;
        }

        public void checkStatus(){

                try {
                        statusFolderSize(com.godthablab.wtracker.C.WA_STATUS);
                        Log.d(TAG, "WA STATUS Size: " + statusFolderSize(com.godthablab.wtracker.C.WA_STATUS));

                        if(statusFolderSize(com.godthablab.wtracker.C.WA_STATUS) <= 10000){
                                // Toast.makeText(MainActivity.this, "Please View Status in WhatsApp and comeback", Toast.LENGTH_SHORT).show();
                                this.emptyView.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                                //  Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        }

                }catch (NullPointerException enull){

                }
        }

        private SdkInitializationListener initSdkListener(){

                return new SdkInitializationListener() {
                        @Override
                        public void onInitializationFinished() {
                                bannerAd();
                        }
                };

        }

        private void bannerAd() {

                moPubView = (MoPubView) findViewById(R.id.adview);
                moPubView.setAdUnitId(getString(R.string.mobanner));
                moPubView.loadAd();
        }

        protected void onDestroy(){
                moPubView.destroy();
                super.onDestroy();
        }


        public void checkpremium(){

                //Get Data From SharedPrefrences
                SharedPreferences channel = this.getSharedPreferences(Billing.MyPREFERENCES, Context.MODE_PRIVATE);
                String strChannel = channel.getString(preminum,"").toString();

                if(strChannel.equals("premium"))

                {

                        MoPubView adview;

                        adview = (MoPubView) findViewById(R.id.adview);

                        adview.setVisibility(View.GONE);


                }

                else

                {

                }

        }

}
