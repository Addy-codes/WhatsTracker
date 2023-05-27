package com.godthablab.wtracker.whatsWebScan;

import static com.godthablab.wtracker.BillingClient.Billing.MyPREFERENCES;
import static com.godthablab.wtracker.BillingClient.Billing.preminum;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


import com.godthablab.wtracker.MainActivity;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.RestoredDeletedMessages.NotificationsContract;
import com.godthablab.wtracker.SplashActivity;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class WebActivity extends AppCompatActivity implements MoPubInterstitial.InterstitialAdListener {
    public static Handler handler;
    private static ValueCallback<Uri[]> mUploadMessageArr;
    String TAG = WebActivity.class.getSimpleName();
    boolean doubleBackToExitPressedOnce = false;
    ProgressBar progressBar;
    private WebView webViewscanner;
    LinearLayout backbutton;
    ImageView UnblockedKeyboard, Blockedkeyboard;
    TextView how_it_works;


    public static final String MyPREFERENCES = "first_time" ;
    public static final String data = "key";
    public static final String MyPREFERENCE = "premium" ;
    public static final String preminum = "premium";

    SharedPreferences sharedpreferences;

    private MoPubView moPubView;
    private MoPubInterstitial mInterstitial;

    private static boolean RUN_ONCE = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsweb);

        checkpremium();


        final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
        MoPub.initializeSdk(this, configuration.build(),initSdkListener());

        mInterstitial = new MoPubInterstitial(this, getString(R.string.interstitial_id));
        mInterstitial.setInterstitialAdListener(this);

        InitHandler();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // showInterstialAd();
                        checkIfFirstTime();
                    }
                });
            }
        }, 2500);

        this.backbutton = findViewById(R.id.backbutton);
        UnblockedKeyboard = (ImageView) findViewById(R.id.UnblockedKeyboard);
        Blockedkeyboard = (ImageView) findViewById(R.id.Blockedkeyboard);
        UnblockedKeyboard.setOnClickListener(imgButtonHandler);
        Blockedkeyboard.setOnClickListener(imgButtonHandler1);
        this.how_it_works = findViewById(R.id.how_it_works);

        how_it_works.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WebActivity.this,HowItWorks.class));
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

//        getSupportActionBar().setTitle("Whats Web");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        if (!Internetconnection.checkConnection(this)) {
//            Banner banner = findViewById(R.id.startAppBanner);
//            banner.hideBanner();
//        }
        this.progressBar = findViewById(R.id.progressBar);
        if (VERSION.SDK_INT >= 24) {
            onstart();
            this.webViewscanner = findViewById(R.id.webViewscan);
            this.webViewscanner.clearFormData();
            this.webViewscanner.getSettings().setSaveFormData(true);
            this.webViewscanner.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
            this.webViewscanner.setLayoutParams(new LayoutParams(-1, -1));
            this.webViewscanner.setWebChromeClient(new webChromeClients());
            this.webViewscanner.setWebViewClient(new MyBrowser());
            this.webViewscanner.getSettings().setAppCacheMaxSize(5242880);
            this.webViewscanner.getSettings().setAllowFileAccess(true);
            this.webViewscanner.getSettings().setAppCacheEnabled(true);
            this.webViewscanner.getSettings().setJavaScriptEnabled(true);
            this.webViewscanner.getSettings().setDefaultTextEncodingName( "UTF-8");
            this.webViewscanner.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            this.webViewscanner.getSettings().setDatabaseEnabled(true);
            this.webViewscanner.getSettings().setBuiltInZoomControls(false);
            this.webViewscanner.getSettings().setSupportZoom(false);
            this.webViewscanner.getSettings().setUseWideViewPort(true);
            this.webViewscanner.getSettings().setDomStorageEnabled(true);
            this.webViewscanner.getSettings().setAllowFileAccess(true);
            this.webViewscanner.getSettings().setLoadWithOverviewMode(true);
            this.webViewscanner.getSettings().setLoadsImagesAutomatically(true);
            this.webViewscanner.getSettings().setBlockNetworkImage(false);
            this.webViewscanner.getSettings().setBlockNetworkLoads(false);
            this.webViewscanner.getSettings().setLoadWithOverviewMode(true);
            this.webViewscanner.loadUrl("https://web.whatsapp.com/%F0%9F%8C%90/en");
        } else {
            onstart();
            this.webViewscanner = findViewById(R.id.webViewscan);
            this.webViewscanner.clearFormData();
            this.webViewscanner.getSettings().setSaveFormData(true);
            this.webViewscanner.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
            this.webViewscanner.setLayoutParams(new LayoutParams(-1, -1));
            this.webViewscanner.setWebChromeClient(new webChromeClients());
            this.webViewscanner.setWebViewClient(new MyBrowser());
            this.webViewscanner.getSettings().setAppCacheMaxSize(5242880);
            this.webViewscanner.getSettings().setAllowFileAccess(true);
            this.webViewscanner.getSettings().setAppCacheEnabled(true);
            this.webViewscanner.getSettings().setJavaScriptEnabled(true);
            this.webViewscanner.getSettings().setDefaultTextEncodingName( "UTF-8");
            this.webViewscanner.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            this.webViewscanner.getSettings().setDatabaseEnabled(true);
            this.webViewscanner.getSettings().setBuiltInZoomControls(false);
            this.webViewscanner.getSettings().setSupportZoom(false);
            this.webViewscanner.getSettings().setUseWideViewPort(true);
            this.webViewscanner.getSettings().setDomStorageEnabled(true);
            this.webViewscanner.getSettings().setAllowFileAccess(true);
            this.webViewscanner.getSettings().setLoadWithOverviewMode(true);
            this.webViewscanner.getSettings().setLoadsImagesAutomatically(true);
            this.webViewscanner.getSettings().setBlockNetworkImage(false);
            this.webViewscanner.getSettings().setBlockNetworkLoads(false);
            this.webViewscanner.getSettings().setLoadWithOverviewMode(true);
            this.webViewscanner.loadUrl("https://web.whatsapp.com/%F0%9F%8C%90/en");
        }
    }


    @SuppressLint("HandlerLeak")
    private class btnInitHandlerListner extends Handler {
        @SuppressLint({"SetTextI18n"})
        public void handleMessage(Message msg) {
        }
    }

    //Webview Client Methods
    private class webChromeClients extends WebChromeClient {
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.e("CustomClient", consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }
    }


    //Webview Client Methods
    private class MyBrowser extends WebViewClient {
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            WebActivity.this.progressBar.setVisibility(View.VISIBLE);
            Log.e(WebActivity.this.TAG, "progressbar");
            super.onPageStarted(view, url, favicon);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(WebActivity.this.TAG, "progressbar GONE");
            WebActivity.this.progressBar.setVisibility(View.GONE);
        }
    }

    //Initialisation Method
    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = 17)




    View.OnClickListener imgButtonHandler = new View.OnClickListener() {

        public void onClick(View v) {
            //imgButton.setBackgroundResource(R.drawable.block_keyboard);
            Toast.makeText(WebActivity.this,"Keyborad is Blocked",Toast.LENGTH_SHORT).show();


           /* InputMethodManager inputManager = (InputMethodManager) TeleWeb.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(TeleWeb.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);*/


            UnblockedKeyboard.setVisibility(View.GONE);
            Blockedkeyboard.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener imgButtonHandler1 = new View.OnClickListener() {

        public void onClick(View v) {
            //imgButton.setBackgroundResource(R.drawable.block_keyboard);
            Toast.makeText(WebActivity.this,"Keyborad is Unblocked",Toast.LENGTH_SHORT).show();



            Blockedkeyboard.setVisibility(View.GONE);
            UnblockedKeyboard.setVisibility(View.VISIBLE);

        }
    };

    public void onstart() {
        if (VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_COARSE_LOCATION"}, 123);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1001 && VERSION.SDK_INT >= 21) {
            mUploadMessageArr.onReceiveValue(FileChooserParams.parseResult(i2, intent));
            mUploadMessageArr = null;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean z = true;
        if (keyCode == 4) {
            try {
                if (this.webViewscanner.canGoBack()) {
                    this.webViewscanner.goBack();
                    return z;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
        z = super.onKeyDown(keyCode, event);
        return z;
    }

    protected void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
        this.webViewscanner.clearCache(true);
    }

    public void onDestroy() {
        super.onDestroy();
        moPubView.destroy();
        mInterstitial.destroy();
        this.webViewscanner.clearCache(true);
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        this.webViewscanner.clearCache(true);
        super.onStop();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @SuppressLint({"HandlerLeak"})
    private void InitHandler() {
        handler = new btnInitHandlerListner();
    }


    //It's Method of More App
    private void more() {
        String appPackageName = getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction("android.intent.action.SEND");
        sendIntent.putExtra("android.intent.extra.TEXT", "Whats Web Scan\n  https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    @Override

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void runOnce() {
        if (RUN_ONCE) {
            RUN_ONCE = false;

            Toast.makeText(WebActivity.this,"hello",Toast.LENGTH_LONG).show();
        }
    }

    public void checkIfFirstTime(){


        //Get Data From SharedPrefrences
        SharedPreferences channel = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String strChannel = channel.getString(data,"").toString();


        // Toast.makeText(getApplicationContext(), strChannel, Toast.LENGTH_LONG).show();
        Log.d("hello","" + strChannel);
        if(strChannel.equals("first")){

           // Toast.makeText(WebActivity.this, "NO nonnononononon", Toast.LENGTH_LONG).show();
        }

        else {

            firstTimeDialog();
           // Toast.makeText(WebActivity.this, "You are new to this activity", Toast.LENGTH_LONG).show();

        }

    }

    public void putFirst(){

        String string = "first";
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(data, string);
        editor.commit();
        //Toast.makeText(WebActivity.this,"first put",Toast.LENGTH_LONG).show();

    }

    private void firstTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.first_time)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        putFirst();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



    private SdkInitializationListener initSdkListener(){

        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                bannerAd();
                mInterstitial.load();
            }
        };

    }

    private void bannerAd() {

        moPubView = (MoPubView) findViewById(R.id.adview);
        moPubView.setAdUnitId(getString(R.string.mobanner));
        moPubView.loadAd();
    }


    @Override
    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {



    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {

    }

    @Override
    public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {

    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {

    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
        moPubInterstitial.load();
    }

    public void showInterstialAd(){

        if (mInterstitial != null && mInterstitial.isReady()) {
            mInterstitial.show();
        } else {
            Toast.makeText(getApplicationContext(), "Not available.", Toast.LENGTH_SHORT).show();
        }

    }

    public void checkpremium(){

        //Get Data From SharedPrefrences
        SharedPreferences channel = this.getSharedPreferences(MyPREFERENCE, Context.MODE_PRIVATE);
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
