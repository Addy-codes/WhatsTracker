package com.godthablab.wtracker;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.godthablab.wtracker.tracker.TrackerActivity;
import com.mopub.common.MoPub;
import com.mopub.common.MoPubReward;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideos;

import java.util.Set;

public class RewardVideo implements MoPubRewardedVideoListener {
    private Context mContext;
    private String sActivity;
    private AddCheck adCheck;

    private boolean loadingRewardVideoRequested = false;
    private boolean adsInitializationCompleted = false;
    private boolean rewardVideoLoaded = false;

    private boolean loadRewardVideoOnInitializationComplete = false;

    public RewardVideo(Context context, String requestingActivity) {
        mContext = context;
        sActivity = requestingActivity;
        adCheck = new AddCheck(mContext);
    }

    public void loadRewardVideo() {
        if (!rewardVideoLoaded && !loadingRewardVideoRequested && AddCheck.adEnabled) {

            Toast.makeText(mContext, "loading reward video, please wait, video will be displayed once loading is complete", Toast.LENGTH_LONG).show();
        }
    }

    public boolean showRewardVideo() {
        if (!rewardVideoLoaded && !loadingRewardVideoRequested && AddCheck.adEnabled) {

            Toast.makeText(mContext, "loading reward video, please wait, video will be displayed once loading is complete", Toast.LENGTH_LONG).show();
            return false;
        }
        if (sActivity.equals(AddCheck.MySourceActivity)) {
            if (rewardVideoLoaded && AddCheck.adEnabled) {
                // Display Ad
                MoPubRewardedVideos.showRewardedVideo(MyConstants.myRewardVideoID);
            }
        }
        return false;
    }

    public void destroyRequested() {
        MoPub.onDestroy((Activity) mContext);//for rewarded video ads only
    }

    public void backPressed() {
        MoPub.onBackPressed((Activity) mContext); // for rewarded video ads only
    }


    private void initializeMoPubSDK(String adUnit) {

        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(adUnit)

                .withLegitimateInterestAllowed(false)
                .build();
        MoPub.initializeSdk(mContext, sdkConfiguration, initSdkListener());
    }

    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {

                adsInitializationCompleted = true;
                Toast.makeText(mContext, "Initialization completed", Toast.LENGTH_SHORT).show();

                if (loadRewardVideoOnInitializationComplete) {
                    loadRewardVideoOnInitializationComplete = false;
                    MoPubRewardedVideos.loadRewardedVideo(MyConstants.myRewardVideoID);
                    loadingRewardVideoRequested = true;
                    loadRewardVideoOnInitializationComplete = false;
                }
            }
        };
    }

    @Override
    public void onRewardedVideoLoadSuccess(String adUnitId) {
        if (adUnitId.equals(MyConstants.myRewardVideoID)) {
            rewardVideoLoaded = true;
            loadingRewardVideoRequested = false;

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Reward video loaded successfully, show now ?")
                    .setTitle("Reward Video")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Display Ad
                            MoPubRewardedVideos.showRewardedVideo(MyConstants.myRewardVideoID);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User not in agreement, ignore.
                        }
                    });
            builder.create();
            builder.show();
        }
    }

    @Override
    public void onRewardedVideoLoadFailure(String adUnitId, MoPubErrorCode errorCode) {
        // Called when a video fails to load for the given adUnitId. The provided error code will provide more insight into the reason for the failure to load.
    }

    @Override
    public void onRewardedVideoStarted(String adUnitId) {
        // Called when a rewarded video starts playing.
    }

    @Override
    public void onRewardedVideoPlaybackError(String adUnitId, MoPubErrorCode errorCode) {
        //  Called when there is an error during video playback.
    }

    @Override
    public void onRewardedVideoClicked(@NonNull String adUnitId) {
        //  Called when a rewarded video is clicked.
    }

    @Override
    public void onRewardedVideoClosed(String adUnitId) {

        Log.d("TAG", "onRewardedVideoClosed");

    }

    @Override
    public void onRewardedVideoCompleted(Set<String> adUnitIds, MoPubReward reward) {
        // Called when a rewarded video is completed and the user should be rewarded.
        // You can query the reward object with boolean isSuccessful(), String getLabel(), and int getAmount().
        rewardVideoLoaded = false;
        AddCheck.adEnabled = false;
        double startPeriod = System.currentTimeMillis();
        double endPeriod = startPeriod + MyConstants.rewardedPeriod;


    }
    }


class AddCheck {

    private Context mContext ;
    public AddCheck(Context context) {
        mContext = context;
    }

    final static String MySourceActivity = "MainActivity";

    static boolean adEnabled = true; // Assumes that by default ad is required

    boolean bannerAdRequired(String activity){

        switch (activity){
            case MySourceActivity:
                if (adEnabled){
                    return true;
                }
                else {
                    return false;
                }
            default:
                return false;
        }
    }





}

class MyConstants {
    //Below are test IDs use only this when testing
    final static String myMainActivityBannerID = "b195f8dd8ded45fe847ad89ed1d016da";
    final static String myMainActivityInterstitialID = "24534e1901884e398f1253216226017e";
    final static String myRewardVideoID = "920b6145fb1546cf8b5cf2ac34638bb7";


    //below is for facebook ads testing, HashID of the device can be found in the run log when running an app with facebook SDK
    //look for something like I/AdInternalSettings: Test mode device hash: c9d1da04-c84c-4d36-a315-0aa7b6a8a7a8
    //final static String MyEmulatorHashID = "c9d1da04-c84c-4d36-a315-0aa7b6a8a7a8";

    final static boolean hasAd = true;

    final static String keyForAdCounter = "TotalAdsCount";
    final static String keyForAdFreeStartDate = "AdFreeStart";
    final static String keyForAdFreeEndDate = "AdFreeEnd";

    final static double rewardedPeriod = 60.0*1000.0*4.0;//for Four minutes (testing)
    //final static double rewardedPeriod = 60.0*1000.0*60.0*24.0*30.0; // 30 days equivalent in miliseconds
    final static String videoAdRewardedMessage = "Congratulations! \n Thank you for support, you can now enjoy Ad free App for 5 minutes";
    //final static String videoAdRewardedMessage = "Congratulations! \n Thank you for support, you can now enjoy Ad free App for 1 month";
}

