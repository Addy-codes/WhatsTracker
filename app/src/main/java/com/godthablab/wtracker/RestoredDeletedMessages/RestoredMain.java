package com.godthablab.wtracker.RestoredDeletedMessages;

import static com.godthablab.wtracker.BillingClient.Billing.MyPREFERENCES;
import static com.godthablab.wtracker.BillingClient.Billing.preminum;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.godthablab.wtracker.FunnyMessages.FunnyMessagesActivity;
import com.godthablab.wtracker.MainActivity;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView.MainRestoreChat;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubView;

public class RestoredMain extends AppCompatActivity {

    LinearLayout StartRestoringMessage;

    private MoPubView moPubView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restoremessage);

        checkpremium();

        final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
        MoPub.initializeSdk(this, configuration.build(),initSdkListener());


        StartRestoringMessage = findViewById(R.id.start_restoring_message);
        //  Intent for main Deleted msg Activity
        StartRestoringMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestoredMain.this, MainRestoreChat.class));
            }
        });

   /*     if (Settings.Secure.getString(this.getContentResolver(),"enabled_notification_listeners").contains(getApplicationContext().getPackageName()))
        {
            startActivity(new Intent(  RestoredMain.this, RestoredMain.class));
        } else {
            //service is not enabled try to enabled by calling...

            startActivity(new Intent(  RestoredMain.this, MainFackChatResDel.class));
        }*/
    }

    public boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNotificationServiceRunning() == true) {
            //startActivity(new Intent(RestoredMain.this, MainActivity.class));
        finish();
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

