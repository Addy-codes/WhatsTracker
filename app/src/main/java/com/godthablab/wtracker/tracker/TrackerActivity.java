package com.godthablab.wtracker.tracker;

import static android.content.ContentValues.TAG;
import static com.godthablab.wtracker.BillingClient.Billing.MyPREFERENCES;
import static com.godthablab.wtracker.BillingClient.Billing.preminum;
import static com.godthablab.wtracker.shared.SharedPreferenceHelper.KEY_COINS;
import static com.godthablab.wtracker.shared.SharedPreferenceHelper.MEMORY;
import static com.godthablab.wtracker.shared.SharedPreferenceHelper.MEMORY2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.viewpager.widget.ViewPager;


import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.godthablab.wtracker.MainActivity;
import com.google.android.material.tabs.TabLayout;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.shared.SharedPreferenceHelper;
import com.godthablab.wtracker.tracker.visitsyou.VisitsYouFragment;
import com.godthablab.wtracker.tracker.wappcontact.WappContactsFragment;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubRewardedVideos;
import com.mopub.mobileads.MoPubView;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackerActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler{

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    SharedPreferences sharedpreferences;

    private long totalCoins;
    private BillingProcessor bp;

    String rewardUnit = "920b6145fb1546cf8b5cf2ac34638bb7";

    private static final String SP_NAME = "WhatsWebUtility";
    private MoPubView moPubView;


    String memory = "0";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String count = "memory";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);


       loadRewardVedioAds();

        checkpremium();


        bp = new BillingProcessor(TrackerActivity.this, getResources().getString(R.string.play_console_license), this);
        bp.initialize();

        final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
        MoPub.initializeSdk(this, configuration.build(), initSdkListener());

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        //setting action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbarTitle.setText(R.string.title_profile_tracker);

        }

        /*setting up tablayout with viewPager*/
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager();




        SharedPreferences sharedPreference = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        int dummyCoin = 0;
        Long oldCoin = sharedPreference.getLong(KEY_COINS,dummyCoin);
       //Toast.makeText(TrackerActivity.this, "" + oldCoin , Toast.LENGTH_SHORT).show();
        Log.d(TAG, "showCoinStatus: " + oldCoin);
        this.totalCoins = oldCoin;
        Log.d("TAG", "onCreate end: " +this.totalCoins);



        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        memory = sharedpreferences.getString(count,memory);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(count,memory);
        editor.commit();




    }

    /*Saving key_Coins data*/
    SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(KEY_COINS)) {
                updateMenuTitles();
            }
        }
    };

    /*Setting Tab in TabLayout*/
    private void setupViewPager() {

        TrackerPagerAdapter trackerPagerAdapter = new TrackerPagerAdapter(getSupportFragmentManager());

        trackerPagerAdapter.addFragment(new /*VisitsYou*/VisitsYouFragment(), "Visitors"); /*adding first fragment*/
        //    trackerPagerAdapter.addFragment(new /*VisitedByYou*/VisitedByYouFragment(), "Visited"); Removed Third Tab
        trackerPagerAdapter.addFragment(new /*WappContacts*/WappContactsFragment(), "Contacts");

        viewPager.setAdapter(trackerPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

    }

    /*  Setting Earned Coin Upadtes in SharedPrefrence*/
    private void updateMenuTitles() {
        if (tvCoinsEarned == null)
            return;
        tvCoinsEarned.setText(String.format(Locale.getDefault(), "COINS : %d", SharedPreferenceHelper.getCoins(this)));
    }

    @Override
    /*Every Time On Resume Update Coin Value in menu*/
    protected void onResume() {
        super.onResume();
        updateMenuTitles();
        SharedPreferenceHelper.setSharedPreference(this).registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferenceHelper.setSharedPreference(this).unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tracker_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    TextView tvCoinsEarned ;

    LinearLayout rootView;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.coins_menu);
        rootView = (LinearLayout) item.getActionView();
        rootView.setOnClickListener(menuClick);
        tvCoinsEarned = rootView.findViewById(R.id.tvCoinsEarned);
        updateMenuTitles();
        return true;

    }

    View.OnClickListener menuClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            memory = sharedpreferences.getString(count,memory);
            Log.d("TAG", "menu click memory " +memory );

            if (memory.equals("2")) {

                buyCoins();

                // Toast.makeText(TrackerActivity.this, "Ads Count Finished", Toast.LENGTH_SHORT).show();
            }
            else

            {
                watchAnAd();

            }

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.e("Menu Clicek0", "AD " + item.getItemId());

        if (item.getItemId() == android.R.id.home) {
            finish();
        }


        if (item.getItemId() == R.id.coins_menu) {





            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    public void buyCoins() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dialogView = inflater.inflate(R.layout.buy_coins, null);
        dialogBuilder.setView(dialogView);
        Log.d("buyCoins", "Hello");
        LinearLayout buy100coin = dialogView.findViewById(R.id.buy100coin);
        LinearLayout buy10k_coins = dialogView.findViewById(R.id.buy10k_coins);

        final AlertDialog b = dialogBuilder.create();

        buy100coin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                bp.subscribe(TrackerActivity.this, "subscription_test001");

            }
        });

        buy10k_coins.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                bp.subscribe(TrackerActivity.this, "subscription_test001");

                Log.d("TAG", "updateCoins watch ads: " +totalCoins);
            }
        });

        b.show();
    }

    public void watchAnAd() {

        Log.d("buyCoins1", "Hello");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.watch_ads, null);
        dialogBuilder.setView(dialogView);
        Button buy_coins = dialogView.findViewById(R.id.buy_coins);
        Button watchad = dialogView.findViewById(R.id.watchad);
        final AlertDialog b = dialogBuilder.create();



        buy_coins.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               buyCoins();

            }
        });

        watchad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                condition();

                Log.d("TAG", "updateCoins watch ads: " +totalCoins);

                b.cancel();

            }
        });

        b.show();
    }

    private SdkInitializationListener initSdkListener() {

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

    protected void onDestroy() {
        moPubView.destroy();
        super.onDestroy();
    }

    public void checkpremium() {

        //Get Data From SharedPrefrences
        SharedPreferences channel = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String strChannel = channel.getString(preminum, "").toString();

        if (strChannel.equals("premium")) {

            MoPubView adview;

            adview = (MoPubView) findViewById(R.id.adview);

            adview.setVisibility(View.GONE);


        } else {

        }

    }

    public void updateCoins(long coins) {

        Log.d("TAG", "Coins start: " + coins);

        SharedPreferences sharedPreference = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        coins = totalCoins += 100;

        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putLong(KEY_COINS,coins);
        editor.apply();

        Log.d("TAG", "Coins end: " + coins);



    }

    public void showCoinStatus(){
        SharedPreferences sharedPreference = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        int dummyCoin = 0;
        Long oldCoin = sharedPreference.getLong(KEY_COINS,dummyCoin);
        Toast.makeText(TrackerActivity.this, "" + oldCoin , Toast.LENGTH_SHORT).show();
        Log.d(TAG, "showCoinStatus: " +oldCoin);

    }

    public void loadRewardVedioAds(){

        final SdkConfiguration.Builder cofigBulider = new SdkConfiguration.Builder("920b6145fb1546cf8b5cf2ac34638bb7");
        MoPub.initializeSdk(this, cofigBulider.build(), initSdkListener());
        MoPubRewardedVideos.loadRewardedVideo("920b6145fb1546cf8b5cf2ac34638bb7");

    }

    public void condition() {

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        memory = sharedpreferences.getString(count,memory);

        Log.d(TAG, "memory " +memory );

        if (memory.equals("0")) {

            updateCoins(totalCoins);
            MoPubRewardedVideos.showRewardedVideo(rewardUnit);
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(count,"1");
            editor.commit();

         //   Toast.makeText(TrackerActivity.this, "Ads Played", Toast.LENGTH_SHORT).show();

                            loadRewardVedioAds();

        } else if (memory.equals("1")) {

            updateCoins(totalCoins);
            MoPubRewardedVideos.showRewardedVideo(rewardUnit);
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(count,"2");
            editor.commit();

         //   Toast.makeText(TrackerActivity.this, "Ads Played 2nd time", Toast.LENGTH_SHORT).show();

            loadRewardVedioAds();


        } else if (memory.equals("2")) {

            buyCoins();
            // Toast.makeText(TrackerActivity.this, "Ads Count Finished", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {



    }

    @Override
    public void onPurchaseHistoryRestored() {



    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        loadRewardVedioAds();



    }
}


