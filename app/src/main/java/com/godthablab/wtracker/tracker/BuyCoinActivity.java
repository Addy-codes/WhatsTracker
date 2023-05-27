package com.godthablab.wtracker.tracker;

import androidx.appcompat.app.AppCompatActivity;

public class BuyCoinActivity extends AppCompatActivity{/*} implements View.OnClickListener, BillingManager.BillingUpdatesListener {


@BindView(R.id.toolbar)
Toolbar toolbar;

@BindView(R.id.toolbar_title)
TextView toolbarTitle;

@BindView(R.id.flBtnBuy100)
FrameLayout flBtnBuy100;

@BindView(R.id.flBtnBuy10K)
FrameLayout flBtnBuy10K;

@BindView(R.id.tvCoinsEarned)
TextView tvCoinsEarned;

@BindView(R.id.progressBar)
ProgressBar progressBar;

BillingManager billingManager;
SkuDetails buy10KSkuDetails;
SkuDetails buy100SkuDetails;
long coins = 0;
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coins);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                toolbarTitle.setText(R.string.title_profile_tracker);
        }

        coins = SharedPreferenceHelper.getCoins(this);
        tvCoinsEarned.setText(String.valueOf(coins));

        flBtnBuy100.setOnClickListener(this);
        flBtnBuy10K.setOnClickListener(this);
        progressBar.setVisibility(View.INVISIBLE);
        billingManager= new BillingManager(this,this);

}



@Override
public void onClick(View v) {

        if(v.equals(flBtnBuy100)) {
                progressBar.setVisibility(View.VISIBLE);
                initiatePurchaseFlow(buy100SkuDetails);
        }

        if(v.equals(flBtnBuy10K)) {
                progressBar.setVisibility(View.VISIBLE);
                initiatePurchaseFlow(buy10KSkuDetails);
        }

}


@Override
public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
                finish();
        }

        return super.onOptionsItemSelected(item);
}

public void initiatePurchaseFlow(SkuDetails skuDetails) {

        if(skuDetails == null) {
                querySkuDetails();
                Toast.makeText(this, "Fetching Details please wait", Toast.LENGTH_SHORT).show();
                return;
        }

        billingManager.initiatePurchaseFlow(skuDetails);

}

public void querySkuDetails() {
         // Get SkuDetails
        billingManager.querySkuDetailsAsync( BillingClient.SkuType.INAPP,
                                             BillingConstants.getSkuListInApp(),
                (billingResult, skuDetailsList) -> {

                        for(SkuDetails skuDetails : skuDetailsList) {

                                if(skuDetails.getSku().equalsIgnoreCase(BillingConstants.SKU_100COINS)) {
                                        buy100SkuDetails = skuDetails;
                                }
                                if(skuDetails.getSku().equalsIgnoreCase(BillingConstants.SKU_10KCOINS)) {
                                        buy10KSkuDetails = skuDetails;
                                }
                        }

                        // Consume unfinished sku
                       *//* if(!"".equals(SharedPreferenceHelper.get100CoinsToken(BuyCoinActivity.this))
                            && buy100SkuDetails!=null) {
                                billingManager.consumeAsync(SharedPreferenceHelper.get100CoinsToken(BuyCoinActivity.this));
                        }*//*
                        // Consume unfinished sku
                        *//*if(!"".equals(SharedPreferenceHelper.get10KCoinsToken(BuyCoinActivity.this))
                            && buy10KSkuDetails!=null) {
                                billingManager.consumeAsync(SharedPreferenceHelper.get10KCoinsToken(BuyCoinActivity.this));
                        }*//*

                });
}

@Override
public void onBillingClientSetupFinished() {
        querySkuDetails();
}

String stoken;

@Override
public void onConsumeFinished(String token, int result) {
        progressBar.setVisibility(View.INVISIBLE);
        Log.e("onConsumeFinished","OK");

        if(result== BillingClient.BillingResponseCode.OK) {

                Log.e(""+token,"Coi s "+SharedPreferenceHelper.get10KCoinsToken(BuyCoinActivity.this));
                Log.e(""+token,"Coi s "+SharedPreferenceHelper.get100CoinsToken(BuyCoinActivity.this));

                if(token.equalsIgnoreCase(SharedPreferenceHelper.get100CoinsToken(BuyCoinActivity.this))) {
                        coins+=100;
                        SharedPreferenceHelper.update100CoinsToken(BuyCoinActivity.this,"");
                }

                if(token.equalsIgnoreCase(SharedPreferenceHelper.get10KCoinsToken(BuyCoinActivity.this))) {
                        coins+=10_000;
                        SharedPreferenceHelper.update10KCoinsToken(BuyCoinActivity.this,"");
                }

                tvCoinsEarned.setText(String.valueOf(coins));
                SharedPreferenceHelper.updateCoins(BuyCoinActivity.this,coins);
        }

}

@Override
public void onPurchasesUpdated(List<Purchase> purchases) {
        Log.e("onPurchasesUpdated","OK");
        for(Purchase p : purchases) {
                Log.e("onPurchasesUpdated","sku "+p.getSku());
                if(p.getSku().equalsIgnoreCase(BillingConstants.SKU_100COINS)) {
                        if(p.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                stoken = p.getPurchaseToken();
                                Log.e("What is this","token "+stoken);
                                SharedPreferenceHelper.update100CoinsToken(BuyCoinActivity.this,p.getPurchaseToken());
                                billingManager.consumeAsync(p.getPurchaseToken());
                        }
                }
                if(p.getSku().equalsIgnoreCase(BillingConstants.SKU_10KCOINS)) {
                        if(p.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                stoken = p.getPurchaseToken();
                                SharedPreferenceHelper.update10KCoinsToken(BuyCoinActivity.this,p.getPurchaseToken());
                                billingManager.consumeAsync(p.getPurchaseToken());
                        }
                }
        }
}


@Override
protected void onResume() {
        super.onResume();
}


@Override
protected void onDestroy() {

        if(billingManager!=null) {
                billingManager.destroy();
        }

        super.onDestroy();

}

*/
}
