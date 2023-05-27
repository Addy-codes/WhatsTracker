package com.godthablab.wtracker.ParentalControl;

import static com.godthablab.wtracker.BillingClient.Billing.MyPREFERENCES;
import static com.godthablab.wtracker.BillingClient.Billing.preminum;
import static com.godthablab.wtracker.ParentalControl.Utility.ShowAlert;
import static com.godthablab.wtracker.ParentalControl.Utility.formatDate;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.godthablab.wtracker.BuildConfig;
import com.godthablab.wtracker.ParentalControl.SPHelpher.SharedData;
import com.godthablab.wtracker.R;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    Switch onlineNotification, offlineNotification,vibrateNotification;
    ImageButton notificationSound;
    ImageView copyClipboard;
    Button contact, thanks ,chooseAnotherButton ;
    LinearLayout getPremium, subscribedPanel;
    CardView subcribePanel,chooseAnotherCard;
    TextView planName, renewDate;
    private MoPubView moPubView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        checkpremium();

        final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
        MoPub.initializeSdk(this, configuration.build(),initSdkListener());

        if (!SharedData.getIsSubscribed(this)) {
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
        //Another plan add card
        chooseAnotherButton = findViewById(R.id.chooseAnotherPlan);
        chooseAnotherCard = findViewById(R.id.chooseAnotherPlan_card);
        //
        planName = findViewById(R.id.planName);
        renewDate = findViewById(R.id.renewDate);
        getPremium = findViewById(R.id.getPremium);
        subscribedPanel = findViewById(R.id.subscribedPanel);
        subcribePanel = findViewById(R.id.subcribePanel);
        onlineNotification = findViewById(R.id.onlineNotification);
        offlineNotification = findViewById(R.id.offlineNotification);
        notificationSound = findViewById(R.id.notificationSound);
        vibrateNotification=findViewById(R.id.vibrateNotification);
        onlineNotification.setChecked(SharedData.getOnlineNotification(SettingsActivity.this));
        offlineNotification.setChecked(SharedData.getOfflineNotification(SettingsActivity.this));
        vibrateNotification.setChecked(SharedData.getNotificationVibrate(SettingsActivity.this));

        onlineNotification.setOnCheckedChangeListener(this);
        offlineNotification.setOnCheckedChangeListener(this);
        vibrateNotification.setOnCheckedChangeListener(this);
        notificationSound.setOnClickListener(this);

        copyClipboard = findViewById(R.id.copyClipboard);
        copyClipboard.setOnClickListener(this);

        thanks = findViewById(R.id.thanks);
        contact = findViewById(R.id.contact);

        contact.setOnClickListener(this);
        thanks.setOnClickListener(this);

        getPremium.setOnClickListener(this);
        chooseAnotherButton.setOnClickListener(this);

        TextView userId = findViewById(R.id.userId);
        userId.setText(SharedData.getDeviceID(SettingsActivity.this));


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == onlineNotification.getId()) {
            SharedData.setOnlineNotification(SettingsActivity.this, String.valueOf(isChecked));
        } else if (buttonView.getId() == offlineNotification.getId()) {
            SharedData.setOfflineNotification(SettingsActivity.this, String.valueOf(isChecked));
        }
        else if(buttonView.getId()==vibrateNotification.getId())
        {
            SharedData.setNotificationVibrate(SettingsActivity.this, String.valueOf(isChecked));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.copyClipboard) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("ID copied to clipboard", SharedData.getDeviceID(SettingsActivity.this));
            clipboard.setPrimaryClip(clip);
              ShowAlert(SettingsActivity.this, "ID Copied To Clipboard");
        } else if (v.getId() == R.id.contact) {
            String model = Build.MODEL;
            String manufacturer = Build.MANUFACTURER;
            String version = Build.VERSION.RELEASE;
            int versionCode = BuildConfig.VERSION_CODE;
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tbsdevelopersteam@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "WhatEye Problem");
            intent.putExtra(Intent.EXTRA_TEXT, "--Support Info--\n\nUser ID: " + SharedData.getDeviceID(SettingsActivity.this) + "\nApp Version: " + versionCode + "\nPlan: " + SharedData.getPlanName(SettingsActivity.this) + "\nManufacturer: " + manufacturer + "\nModel: " + model + "\nOS: " + version);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        } else if (v.getId() == R.id.thanks) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "")));
        } else if (v.getId() == R.id.getPremium) {

            startActivity(new Intent(SettingsActivity.this, PremiumPlansctivity.class));

        } else if(v.getId()==R.id.notificationSound) {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
            } else {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
            }
            startActivity(intent);
        }else if(v.getId() == R.id.chooseAnotherPlan){
            startActivity(new Intent(SettingsActivity.this, PremiumPlansctivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedData.getIsSubscribed(this)) {
            subcribePanel.setVisibility(View.GONE);
           // subscribedPanel.setVisibility(View.VISIBLE);
           // chooseAnotherCard.setVisibility(View.VISIBLE);

            try {

                switch (Integer.parseInt(SharedData.getPlanId(this))) {

                    case 1:
                        planName.setText("1 Week Premium Plus Member");
                        renewDate.setText("Renews on " +   formatDate(SharedData.getPlanEndDate(this)));
                        break;

                    case 2:
                        planName.setText("1 Month Premium Plus Member");
                        renewDate.setText("Renews on " +   formatDate(SharedData.getPlanEndDate(this)));
                        break;

                    case 3:
                        planName.setText("3 Month Premium Plus Member");
                        renewDate.setText("Renews on " +   formatDate(SharedData.getPlanEndDate(this)));
                        break;
                }
            }

            catch (Exception ex)

            {

            }

        } else {

          //  subcribePanel.setVisibility(View.VISIBLE);
            subscribedPanel.setVisibility(View.GONE);
            chooseAnotherCard.setVisibility(View.GONE);

        }
    }




    @Override
    public void onBackPressed() {
        if (!SharedData.getIsSubscribed(this)) {

        }
        super.onBackPressed();
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
          //  Toast.makeText(SettingsActivity.this, "checkpremium()", Toast.LENGTH_SHORT).show();
            Log.d("strChannel","strChannel" +strChannel);
            MoPubView adview;
            adview = (MoPubView) findViewById(R.id.adview);
            adview.setVisibility(View.GONE);
        }

        else

        {
           // Toast.makeText(SettingsActivity.this, "NO premium", Toast.LENGTH_SHORT).show();
        }
    }






}
