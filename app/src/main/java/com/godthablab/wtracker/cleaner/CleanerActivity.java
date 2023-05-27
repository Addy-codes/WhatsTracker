package com.godthablab.wtracker.cleaner;

import static com.godthablab.wtracker.BillingClient.Billing.MyPREFERENCES;
import static com.godthablab.wtracker.BillingClient.Billing.preminum;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.google.android.gms.ads.AdView;
import com.godthablab.wtracker.R;
//import com.qaapps.whatstracker.Ads;
import com.godthablab.wtracker.AssetType;
import com.godthablab.wtracker.C;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CleanerActivity extends AppCompatActivity {

        @BindView(R.id.toolbar_title)
        TextView toolbarTitle;

        @BindView(R.id.toolbar)
        Toolbar toolbar;

        @BindView(R.id.rvCleaner)
        RecyclerView rvCleaner;

        RelativeLayout backbutton;

        boolean onPause=false;

        private MoPubView moPubView;

        private List<CleanerButton> cleanerButtons = new ArrayList<>(10);

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_cleaner);

                checkpremium();

                final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
                MoPub.initializeSdk(this, configuration.build(),initSdkListener());

                ButterKnife.bind(this);

                Toolbar toolbar = findViewById(R.id.toolbar);
                toolbar.setTitle("Whats Cleaner");
                setSupportActionBar(toolbar);
                this.backbutton = findViewById(R.id.backbutton);

                //  getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                //   getSupportActionBar().setCustomView(R.layout.actionbar_cleaner);
                //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                populate();
                rvCleaner.setLayoutManager(new LinearLayoutManager(this));
                rvCleaner.setAdapter(new CleanerAdapter(this,cleanerButtons));


                backbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                onBackPressed();
                        }
                });


        }

        private void populate() {

                cleanerButtons.add(new CleanerButton("Images", C.WA_IMAGES, AssetType.TYPE_IMAGE,
                        0,0,R.drawable.btn_image));

                cleanerButtons.add(new CleanerButton("Videos", C.WA_VIDEOS, AssetType.TYPE_VIDEO,
                        0,0,R.drawable.btn_video));

                cleanerButtons.add(new CleanerButton("Audios", C.WA_AUDIO, AssetType.TYPE_AUDIO,
                        0,0,R.drawable.btn_audio));

                cleanerButtons.add(new CleanerButton("Voices", C.WA_VOICE_NOTE, AssetType.TYPE_VOICE_NOTE,
                        0,0,R.drawable.btn_voice));

                cleanerButtons.add(new CleanerButton("Gif", C.WA_GIF, AssetType.TYPE_GIF,
                        0,0,R.drawable.btn_gif));

                cleanerButtons.add(new CleanerButton("Document", C.WA_DOCUMENT, AssetType.TYPE_DOCUMENT,
                        0,0,R.drawable.btn_document));

                cleanerButtons.add(new CleanerButton("Profile Photos", C.WA_PROFILE_PHOTO, AssetType.TYPE_PROFILE_IMAGE,
                        0,0,R.drawable.btn_profile_photo));

                cleanerButtons.add(new CleanerButton("Databases", C.WA_DATABASES, AssetType.TYPE_DATABASE,
                        0,0,R.drawable.btn_database));

        }   // Setting Cleaner Button and Their Functionalities
        @Override
        protected void onResume() {
                super.onResume();
                if(onPause) {
                        onPause = false;
                        for(int i=0;i<cleanerButtons.size();i++) {
                                cleanerButtons.get(i).isCalculated = false;
                        }
                        Objects.requireNonNull(rvCleaner.getAdapter()).notifyDataSetChanged();
                }

        }


        @Override
        protected void onPause() {
                super.onPause();
                onPause = true;
        }



        @Override
        protected void onStop() {
                super.onStop();
                EventBus.getDefault().unregister(this);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                        case android.R.id.home:
                                // API 5+ solution
                                onBackPressed();
                                return true;

                        default:
                                return super.onOptionsItemSelected(item);
                }
        } // Return Home Button by pressing back

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
}
