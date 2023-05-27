package com.godthablab.wtracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class InterstialAds extends AppCompatActivity {

    //creating Object of InterstitialAd
    private InterstitialAd interstitialAd;

    //creating Object of Buttons
    private RelativeLayout loadAdBtn;
    private RelativeLayout showAdBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        //initializing the Google Admob SDK

        MobileAds.initialize (this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

                //Showing a simple Toast Message to the user when The Google AdMob Sdk Initialization is Completed

                Toast.makeText (InterstialAds.this, "AdMob Sdk Initialize "+ initializationStatus.toString(), Toast.LENGTH_LONG).show();

            }
        });

        //Initializing the InterstitialAd  objects
        interstitialAd = new InterstitialAd (InterstialAds.this) ;

        // Initializing the Button  objects to their respective views from activity_main.xml file
        loadAdBtn = (RelativeLayout) findViewById(R.id.parentalcontrol);
        showAdBtn = (RelativeLayout) findViewById(R.id.whatscleaner);

        //setting Ad Unit Id to the interstitialAd
        interstitialAd.setAdUnitId ( "ca-app-pub-3940256099942544~3347511713" ) ;



        //click listeners for buttons
        loadAdBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                //calling the loadInterstitialAd method to load Interstitial Ad
                loadInterstitialAd() ;
            }
        });

        showAdBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                //calling the showInterstitialAd method to show Interstitial Ad
                showInterstitialAd() ;
            }
        });

        // creating different AdListener for Interstitial Ad with some Override methods


        interstitialAd.setAdListener( new AdListener() {
            @Override
            public void onAdLoaded() {

                // Showing a simple Toast message to user when an ad is loaded
                Toast.makeText ( InterstialAds.this, "Interstitial Ad is Loaded", Toast.LENGTH_LONG).show() ;

            }

            @Override
            public void onAdFailedToLoad( LoadAdError adError) {

                // Showing a simple Toast message to user when and ad is failed to load
                Toast.makeText ( InterstialAds.this, "Interstitial Ad Failed to Load ", Toast.LENGTH_LONG).show() ;

            }

            @Override
            public void onAdOpened() {

                // Showing a simple Toast message to user when an ad opens and overlay and covers the device screen
                Toast.makeText ( InterstialAds.this, "Interstitial Ad Opened", Toast.LENGTH_LONG).show() ;

            }

            @Override
            public void onAdClicked() {

                // Showing a simple Toast message to user when a user clicked the ad
                Toast.makeText ( InterstialAds.this, "Interstitial Ad Clicked", Toast.LENGTH_LONG).show() ;

            }

            @Override
            public void onAdLeftApplication() {

                // Showing a simple Toast message to user when the user left the application
                Toast.makeText ( InterstialAds.this, "Interstitial Ad Left the Application", Toast.LENGTH_LONG).show() ;

            }

            @Override
            public void onAdClosed() {

                //loading new interstitialAd when the ad is closed
                loadInterstitialAd() ;

                // Showing a simple Toast message to user when the user interacted with ad and got the other app and then return to the app again
                Toast.makeText ( InterstialAds.this, "Interstitial Ad is Closed", Toast.LENGTH_LONG).show() ;

            }
        });
    }

    @SuppressLint("MissingPermission")
    public  void loadInterstitialAd()
    {
        // Creating  a Ad Request
        AdRequest adRequest = new AdRequest.Builder().build() ;

        // load Ad with the Request
        interstitialAd.loadAd(adRequest) ;

        // Showing a simple Toast message to user when an ad is Loading
        Toast.makeText ( InterstialAds.this, "Interstitial Ad is loading ", Toast.LENGTH_LONG).show() ;

    }

    public void showInterstitialAd()
    {
        if ( interstitialAd.isLoaded() )
        {
            //showing the ad Interstitial Ad if it is loaded
            interstitialAd.show() ;

            // Showing a simple Toast message to user when an Interstitial ad is shown to the user
            Toast.makeText ( InterstialAds.this, "Interstitial is loaded and showing ad  ", Toast.LENGTH_LONG).show() ;

        }
        else
        {
            //Load the Interstitial ad if it is not loaded
            loadInterstitialAd() ;

            // Showing a simple Toast message to user when an ad is not loaded
            Toast.makeText ( InterstialAds.this, "Interstitial Ad is not Loaded ", Toast.LENGTH_LONG).show() ;

        }

    }



}