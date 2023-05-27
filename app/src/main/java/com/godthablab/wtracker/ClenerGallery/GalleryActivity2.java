package com.godthablab.wtracker.ClenerGallery;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;
import static com.godthablab.wtracker.BillingClient.Billing.MyPREFERENCES;
import static com.godthablab.wtracker.BillingClient.Billing.preminum;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.godthablab.wtracker.ClenerGallery.adapter.GalleryPagerAdapter;
import com.godthablab.wtracker.FileOperation;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.shared.Ads;
import com.godthablab.wtracker.shared.FileSaveProgressDialog;
import com.godthablab.wtracker.shared.GallerySetting;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity2 extends AppCompatActivity implements View.OnClickListener {

@BindView(R.id.viewPager)
ViewPager viewPager;

@BindView(R.id.toolbar)
Toolbar toolbar;

@BindView(R.id.tab_layout)
TabLayout tabLayout;

@BindView(R.id.toolbar_title)
TextView toolbarTitle;

@BindView(R.id.menu_fab)
FloatingActionMenu menu_fab;

@BindView(R.id.download)
FloatingActionButton download;

@BindView(R.id.delete)
FloatingActionButton delete;

@BindView(R.id.share)
FloatingActionButton share;

/*@BindView(R.id.adView)
AdView adView;*/

@BindView(R.id.adLayout)
FrameLayout AdContainer;
        private MoPubView moPubView;
GallerySetting gallerySetting;

boolean deleteActionPerformed = false,downloadActionPerformed= false,shareActionPerformed= false;

Map<String,SelectedFile> selectedFiles = new HashMap<>();

@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activityw);
        ButterKnife.bind(this);
        checkpremium();

        final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
        MoPub.initializeSdk(this, configuration.build(),initSdkListener());

        setSupportActionBar(toolbar);
        gallerySetting = getIntent().getParcelableExtra("gallerySetting");
        String title = getIntent().getStringExtra("title");
        if(getSupportActionBar()!=null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                if(title == null)
                        toolbarTitle.setText(R.string.title_gallery);
                else
                        toolbarTitle.setText(title);
        }
        loading();

        applySettings();
}

private void applySettings() {
        // Nothing will apply
        if(gallerySetting == null) {
                menu_fab.setVisibility(View.GONE);
                return;
        }

        menu_fab.setVisibility(View.VISIBLE);
        if(gallerySetting.isReadOnly()) {
                menu_fab.setVisibility(View.GONE);
        } else {
                if (gallerySetting.isMenuDelete()) {
                        delete.setVisibility(View.VISIBLE);
                        delete.setOnClickListener(this);
                } else {
                        delete.setVisibility(View.GONE);
                }

                if (gallerySetting.isMenuDownload()) {
                        download.setVisibility(View.VISIBLE);
                        download.setOnClickListener(this);
                } else {
                        download.setVisibility(View.GONE);
                }
        }

        share.setOnClickListener(this);

        tabLayout.setupWithViewPager(viewPager);

        if(gallerySetting.isHideTabLayout())
                tabLayout.setVisibility(View.GONE);

        viewPager.setAdapter(new GalleryPagerAdapter(getSupportFragmentManager(),gallerySetting));

}


        public void loading() {
                try {

                        final ProgressDialog progDailog = ProgressDialog.show(this,
                                "Loading...",
                                "Please wait.....", true);
                        new Thread() {
                                public void run() {
                                        try {
                                                // sleep the thread, whatever time you want.
                                                sleep(5000);
                                        } catch (Exception e) {
                                        }
                                        progDailog.dismiss();
                                }
                        }.start();

                        //getting data code here
                        //getting data code here
                        //getting data code here
                        //getting data code here
                        //getting data code here

                } catch (Exception e) {
                        Log.e(LOG_TAG, e.getMessage());
                        //   PopIt("CheckAccountError", e.getMessage(), "Denied");
                }
        }

@Override
public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
              //  showInterstitialAd();
                finish();
        }

        return super.onOptionsItemSelected(item);
}

@Override
public void onBackPressed() {
        super.onBackPressed();
        //showInterstitialAd();
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
                SharedPreferences channel = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
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
/*public void showInterstitialAd() {

        if(gallerySetting == null || !gallerySetting.isShowInterstitial())
                return;

        if ((downloadActionPerformed || deleteActionPerformed || shareActionPerformed))
                Ads.getInstance(this).showInterstitialAd();
}*/

@Override
public void onClick(View v) {

        if(v.equals(download)) {
                ArrayList<SelectedFile> list = new ArrayList<>(selectedFiles.values());
                if(list.size() == 0) {
                        Toast.makeText(this, R.string.no_item, Toast.LENGTH_SHORT).show();
                        return;
                }
                FileSaveProgressDialog saveProgressDialog = FileSaveProgressDialog.getInstance(list);
                saveProgressDialog.setCancelable(false);
                saveProgressDialog.show(getSupportFragmentManager(),"Dialog");
                downloadActionPerformed = true;
        }
        if(v.equals(delete)) {
                ArrayList<SelectedFile> list = new ArrayList<>(selectedFiles.values());

                if(list.size() == 0) {
                        Toast.makeText(this, R.string.no_item, Toast.LENGTH_SHORT).show();
                        return;
                }

                FileSaveProgressDialog saveProgressDialog = FileSaveProgressDialog.getInstance(list);
                saveProgressDialog.setCancelable(false);
                saveProgressDialog.show(getSupportFragmentManager(),"Dialog");
                deleteActionPerformed = true;
        }
        if(v.equals(share)) {
                List<File> list=  new ArrayList<>();
                for(SelectedFile sf : selectedFiles.values() ) {
                        File f = new File(sf.src);
                        list.add(f);
                }
                if(list.size() == 0) {
                        Toast.makeText(this, R.string.no_item, Toast.LENGTH_SHORT).show();
                        return;
                }
                FileOperation.shareMultiple(list,this);
                shareActionPerformed = true;
        }

}


@Override
public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
}

@Override
public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
}

@Subscribe(threadMode = ThreadMode.MAIN)
public void onMessageEvent(SelectedFile event) {
        if(SelectedFile.ADD.equals(event.action)) {
                selectedFiles.put(event.src,event);

                if(!menu_fab.isOpened())
                        menu_fab.open(true);

        } else if(SelectedFile.DELETE.equals(event.action)) {
                selectedFiles.put(event.src, event);

                if(!menu_fab.isOpened())
                        menu_fab.open(true);

        } else if(SelectedFile.REMOVE_FROM_LIST.equals(event.action)) {
                selectedFiles.remove(event.src);

               if( selectedFiles.size() <= 0 && menu_fab.isOpened() )
                        menu_fab.close(true);
        }
}

@Subscribe(threadMode = ThreadMode.MAIN)
public void onMessageEvent(String message) {
        if(message.equals("clearList")) {
                selectedFiles.clear();
        }

        if(message.equals("operationDone")) {
                int deletedFiles=0,downloadedFiles=0;
                for(SelectedFile selectedFile: selectedFiles.values()) {
                        if(selectedFile.action.equalsIgnoreCase(SelectedFile.DELETE)) {
                                deletedFiles++;
                        }
                        if(selectedFile.action.equalsIgnoreCase(SelectedFile.ADD)) {
                                downloadedFiles++;
                        }
                }
                if(deletedFiles>0) {
                        Toast.makeText(this,deletedFiles+" item(s) deleted",Toast.LENGTH_SHORT).show();
                }
                if(downloadedFiles>0) {
                        Toast.makeText(this,downloadedFiles+" item(s) downloaded",Toast.LENGTH_SHORT).show();
                }

                menu_fab.close(true);
                selectedFiles.clear();
                EventBus.getDefault().post(new GalleryFragment.Reload());
        }

        if(message.equals("operationFailed")) {
                menu_fab.close(true);
                selectedFiles.clear();
                EventBus.getDefault().post(new GalleryFragment.Reload());
                Toast.makeText(this,"Operation Failed",Toast.LENGTH_SHORT).show();
        }

        if(menu_fab.isOpened())
                menu_fab.close(true);

}


/*Ignore message data*/
/*@Subscribe(threadMode = ThreadMode.MAIN)
public void onMessageEvent(Ads.AdsPurchaseMessage adsPurchaseMessage) {
        if(adsPurchaseMessage.isAdsPurchased()) {
                AdContainer.setVisibility(View.GONE);
        } else {
                AdContainer.setVisibility(View.VISIBLE);
        }
}*/

}
