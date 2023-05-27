package com.godthablab.wtracker.ParentalControl;

import static com.godthablab.wtracker.BillingClient.Billing.MyPREFERENCES;
import static com.godthablab.wtracker.BillingClient.Billing.preminum;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.godthablab.wtracker.ParentalControl.SPHelpher.SharedData;
import com.godthablab.wtracker.ParentalControl.server.ApiClient;
import com.godthablab.wtracker.ParentalControl.server.ApiInterface;
import com.godthablab.wtracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends FragmentActivity {

    // private InterstitialAd interstitialAd;

    private MoPubView moPubView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private AppBarConfiguration mAppBarConfiguration;
    String token, deviceId, android_id;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ImageView imgback;
    ProgressDialog progressDialog;
    ImageView settingsIcon, premium_member;
    private BillingClient billingClient;
    CountryCodePicker ccp;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    final Calendar myCalendar = Calendar.getInstance();

    String planStartDate, planEndDate;
    String sku;
    public PurchasesUpdatedListener purchaseUpdateListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {

            String s =  billingResult.getDebugMessage();
        }
    };

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkpremium();

        //Toolbar toolbar = findViewById(R.id.toolbar);
        // Obtain the FirebaseAnalytics instance.`
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        viewPager = findViewById(R.id.viewPager);
        imgback = findViewById(R.id.imgback);
        tabLayout = findViewById(R.id.tabLayout);
        settingsIcon = findViewById(R.id.settings);
        premium_member = findViewById(R.id.premium_member);
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,   SettingsActivity.class));
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
        MoPub.initializeSdk(this, configuration.build(),initSdkListener());


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        // deviceId = task.getResult().getId();

                        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);

                        RegisterUser();

                    }
                });

        billingClient = BillingClient.newBuilder(this)
                .setListener(purchaseUpdateListener)
                .enablePendingPurchases()
                .build();



        if (!SharedData.getIsSubscribed(this)) {

            try {

                final int[] flag = {0};
                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {

                        String AddShow;

                        if (position==0 ){

                            if (flag[0] ==0) {
                                AddShow = getIntent().getStringExtra("NoAddSplash");
                                flag[0] = 1;
                            } else  {
                                AddShow="ShowAdd";
                            }

                            /*if (!AddShow.equalsIgnoreCase("NoAddSplash")) {
                                try {
                                    interstitialAd.show();
                                } catch (Exception e) {
                                }
                            }*/
                        }
                        super.onPageSelected(position);
                    }
                });


                //Start service and notify when trial expires as user is not premium
                startService(new Intent(getBaseContext(),   TrialExpService.class));
            }
            catch (Exception e){}

        } else {
            //stop service as soon as user becomes premium
            stopService(new Intent(getBaseContext(),   TrialExpService.class));
        }
    }


    public void RegisterUser() {
        // if (Utility.isNetworkAvailable(getActivity())) {
        progressDialog =   Utility.showProgress(HomeActivity.this);
        ApiInterface apiService = ApiClient.getClient(HomeActivity.this).create(ApiInterface.class);
        // JSONObject model = new JSONObject();


        try {

            HashMap<String, String> request = new HashMap<>();
            request.put("device_id", android_id);
            request.put("token", token);
            request.put("device_type", "1");
            request.put("key", "AAAAeEntGRo:APA91bHtYBRllTO6AiflISRZEN-ceeVuEDi0lRLVCjDgSeUcy6RSwYQ0dW9VVbMkJ7BR17_FwfO0Hx4jDGYT7y2KsqZFUGVb_6KDtvGKOYabOtvIE39Q5j9l0bihnk1_HyA-sHOiFyoT");

            Call<JSONObject> call = apiService.CreateUser(request);

            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                    try {
                        Utility.hideProgress(progressDialog);
                        if (response.code() == 200 && response.body() != null && response.body().getString("status").equals("1")) {
                            JSONObject result = response.body();
                            JSONObject data = result.getJSONArray("result").getJSONObject(0);
                            SharedData.setUserID(HomeActivity.this, data.getString("id"));
                            SharedData.setDeviceID(HomeActivity.this, android_id);
                            SharedData.setToken(HomeActivity.this, token);
                            if (result.has("subscriber") && result.getJSONArray("subscriber").length() > 0 && SharedData.getTrackingNumber(HomeActivity.this).length() == 0) {
                                SharedData.setTrackingName(HomeActivity.this, result.getJSONArray("subscriber").getJSONObject(0).getString("name"));
                                String phoneNuber = result.getJSONArray("subscriber").getJSONObject(0).getString("mobile_number");
                                ccp = new CountryCodePicker(HomeActivity.this);
                                //if (phoneNuber.length()>=10)
                                phoneNuber = phoneNuber.substring(ccp.getSelectedCountryCode().length());
                                Log.v("fdlkhfdlkjfd", phoneNuber.substring(ccp.getSelectedCountryCode().length()));
                                // phoneNuber.substring(phoneNuber.length() - 10);
                                SharedData.setTrackingNumber(HomeActivity.this, phoneNuber);
                            }
                            if (data.getInt("plan_id") == 0) {
                                try {

                                    if (  Utility.parseDate(data.getString("start")) > 0 &&   Utility.parseDate(data.getString("end")) == 0 && SharedData.getTrackerStoppedAt(HomeActivity.this) == 0) {
                                        SharedData.setIsTracking(HomeActivity.this, true);
                                    } else
                                        SharedData.setIsTracking(HomeActivity.this, false);
                                    if (SharedData.getTrackerStartedAt(HomeActivity.this) == 0) {
                                        SharedData.setTrackerStartedAt(HomeActivity.this,   Utility.parseDate(data.getString("start")));
                                    }
                                    if (SharedData.getTrackerStartedAt(HomeActivity.this) > 0) {
                                        long timer_ms = SharedData.GetTrialHours(SharedData.getTrackerStartedAt(HomeActivity.this));
                                        long remainingTime = timer_ms - System.currentTimeMillis();
                                        if (remainingTime <= 0) {
                                            SharedData.setIsTracking(HomeActivity.this, false);
                                            SharedData.setTrialExpired(HomeActivity.this, "true");
                                        }
                                    }
                                } catch (Exception ex) {

                                }
                            } else {
                                SharedData.setPlanId(HomeActivity.this, data.getString("plan_id"));
                                if (data.getString("IsTrackingStopped") == "0")
                                    SharedData.setIsTracking(HomeActivity.this, true);
                                SharedData.setPlanStartDate(HomeActivity.this, (data.getString("start")));
                                SharedData.setPlanEndDate(HomeActivity.this, (data.getString("end")));
                                long timer_ms =   Utility.convertDateToMillis(SharedData.getPlanEndDate(HomeActivity.this));
                                long remainingTime = timer_ms - System.currentTimeMillis();
                                if (remainingTime <= 0) {
                                    SharedData.setIsTracking(HomeActivity.this, false);
                                    SharedData.setPlanExpired(HomeActivity.this,"true");
                                }
                            }
                            try {
                                // recreate();

                                List<String> skuList = new ArrayList<>();
                                skuList.add("com.godthablab.wtracker.yearly");
                                skuList.add("com.godthablab.wtracker.monthly");
                                skuList.add("com.godthablab.wtracker.weekly");

                                final SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                                params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                                billingClient.startConnection(new BillingClientStateListener() {
                                    @Override
                                    public void onBillingSetupFinished(BillingResult billingResult) {
                                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                            billingClient.querySkuDetailsAsync(params.build(),
                                                    new SkuDetailsResponseListener() {
                                                        @Override
                                                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                                            Utility.skuDetails = skuDetailsList;
                                                            if (getSubscriptionEndDate() != null && getSubscriptionEndDate().after(new Date())) {
                                                                SharedData.setIsSubscribed(HomeActivity.this, "true");
                                                                SharedData.setPlanStartDate(HomeActivity.this, planStartDate);
                                                                SharedData.setPlanEndDate(HomeActivity.this, planEndDate);
                                                                Utility.SavePayment(sku, HomeActivity.this);
                                                                AddNumberFragment.manageSubscriptionPanel();
                                                            }
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onBillingServiceDisconnected() {
                                        // Try to restart the connection on the next request to
                                        // Google Play by calling the startConnection() method.
                                    }
                                });


                                TrackingAdapter adapter = new TrackingAdapter(HomeActivity.this, 2);
                                viewPager.setAdapter(adapter);
                                new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                                    @Override
                                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                        switch (position) {
                                            case 0:
                                                //tab.setIcon(R.drawable.addaumber_icon);
                                                tab.setText("Add Number");
                                                break;
                                            case 1:
                                                //tab.setIcon(R.drawable.tracknumber_icon);
                                                tab.setText("Track Number");
                                                break;
                                            default:
                                                break;
                                        }

                                    }
                                }).attach();
                            } catch (Exception ex) {

                            }

                        } else if (response.code() == 500) {
                            if (response.errorBody() != null) {
                                Toast.makeText(HomeActivity.this, R.string.internal_server_error, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this, R.string.internal_server_error, Toast.LENGTH_SHORT).show();
                            }

                        } else {

                        }
                    } catch (Exception ex) {
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, R.string.internal_server_error, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    // hideProgress();
                    try{
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, R.string.internal_server_error, Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }

            });
        } catch (Exception ex) {
            Utility.hideProgress(progressDialog);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (SharedData.getIsSubscribed(this)) {
            premium_member.setVisibility(View.VISIBLE);
        } else {
            premium_member.setVisibility(View.GONE);
        }
    }

    @Nullable
    public Date getSubscriptionEndDate() {
        try {
            // Get the Purchase object:
            Purchase purchase = null;
            Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);

            if (purchasesResult!=null && purchasesResult.getPurchasesList() != null) {
                for (Purchase p : purchasesResult.getPurchasesList()) {
                    if (p.getPurchaseState() == Purchase.PurchaseState.PURCHASED && p.isAutoRenewing()) {
                        purchase = p;
                        //sku = p.getSku();
                        break;
                    }
                }
            }

            // Get the SkuDetails object:
            SkuDetails skuDetail = null;
            for (SkuDetails s :   Utility.skuDetails) { // _skuDetails is an array of SkuDetails retrieved with querySkuDetailsAsync
                if (s.getSku().equals(sku)) {
                    skuDetail = s;
                    break;
                }
            }

            if (purchase != null && skuDetail != null) {

                Date purchaseDate = new Date(purchase.getPurchaseTime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(purchaseDate);
                planStartDate =   Utility.ConvertDateToString(purchaseDate);
                Date now = new Date();
                while (calendar.getTime().before(now)) {
                    switch (skuDetail.getSubscriptionPeriod()) {
                        case "P1W":
                            calendar.add(Calendar.HOUR, 7 * 24);
                            break;
                        case "P1M":
                            calendar.add(Calendar.MONTH, 1);
                            break;
                        case "P3M":
                            calendar.add(Calendar.MONTH, 3);
                            break;
                        case "P6M":
                            calendar.add(Calendar.MONTH, 6);
                            break;
                        case "P1Y":
                            calendar.add(Calendar.YEAR, 1);
                            break;
                    }
                }
                planEndDate =   Utility.ConvertDateToString(calendar.getTime());
                return calendar.getTime();
            }
        } catch(Exception e){
            e.printStackTrace();
        }


        return null;
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
