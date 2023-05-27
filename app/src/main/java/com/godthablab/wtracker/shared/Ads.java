package com.godthablab.wtracker.shared;

public class Ads {/*

private InterstitialAd mInterstitialAd;
private static Ads ads = null;
private Context ctx;

public static Ads getInstance(Context ct) {
        if(ads==null)
                ads = new Ads(ct);
        return ads;
}

private Ads(Context ctx) {
        this.ctx = ctx;
        mInterstitialAd = new InterstitialAd(ctx);
        mInterstitialAd.setAdUnitId(ctx.getString(R.string.admob_interstial_ad));
        mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                }


                @Override
                public void onAdClosed() {
                        // Code to be executed when the interstitial ad is closed.
                        // Load immediately after it close
                        loadInterstitialAd();
                }
        });
}

public void loadInterstitialAd() {
        if(!mInterstitialAd.isLoaded())
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
}

public void showInterstitialAd()  {

        // Don't show user ad
        if(SharedPreferenceHelper.isPremium(this.ctx))
                return;

        if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
        }
}


public static void adMobBannerRequest(Context ctx, AdView mAdView) {

        if(SharedPreferenceHelper.isPremium(ctx))
                return;

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                        super.onAdLoaded();
                        mAdView.setVisibility(View.VISIBLE);
                }
        });
}


public static class AdsPurchaseMessage {

private boolean adsPurchased;
public final static AdsPurchaseMessage PURCHASED = new AdsPurchaseMessage(true);
public final static AdsPurchaseMessage SHOW_ADS = new AdsPurchaseMessage(false);

AdsPurchaseMessage(boolean adsPurchased) {
        this.adsPurchased = adsPurchased;
}

public boolean isAdsPurchased() {
        return adsPurchased;
}

}


*/
}
