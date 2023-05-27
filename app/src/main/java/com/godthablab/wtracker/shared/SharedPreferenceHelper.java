package com.godthablab.wtracker.shared;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {

private static final String SP_NAME = "WhatsWebUtility";
public static final String SP_NAME_SET = "profileTrackerSet";
private static final String KEY_PURCHASE = "PURCHASE";
private static final String TERMS_ACCEPTED = "termsOfUse";
public static final String KEY_COINS = "coins";
public static final String MEMORY = "memory";
public static final String MEMORY2 = "memory2";
private static final String KEY_UNCONSUMED_TOKEN_100 = "token_100_coin";
private static final String KEY_UNCONSUMED_TOKEN_10K = "token_10K_coin";

public static SharedPreferences setSharedPreference(Context ctx) {
        return ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
}

public static void setPurchaseStatus(Context ctx, boolean purchaseStatus) {
        SharedPreferences sharedPreference = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean(KEY_PURCHASE,purchaseStatus);
        editor.apply();
}

public static boolean isPremium(Context ctx) {
        SharedPreferences sharedPreference = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getBoolean(KEY_PURCHASE,false);
}

public static void setTermsAccepted(Context ctx, boolean acceptStatus) {
        SharedPreferences sharedPreference = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putBoolean(TERMS_ACCEPTED,acceptStatus);
        editor.apply();
}

public static boolean isTermsAccepted(Context ctx) {
        SharedPreferences sharedPreference = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getBoolean(TERMS_ACCEPTED,false);
}

public static void updateCoins(Context ctx, long coins) {
        SharedPreferences sharedPreference = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putLong(KEY_COINS,coins);
        editor.apply();
}

public static long getCoins(Context ctx) {
        SharedPreferences sharedPreference = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getLong(KEY_COINS,0);
}


public static String get100CoinsToken(Context ctx) {
        SharedPreferences sharedPreference = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getString(KEY_UNCONSUMED_TOKEN_100,"");
}

public static void update100CoinsToken(Context ctx, String token) {
        SharedPreferences sharedPreference = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(KEY_UNCONSUMED_TOKEN_100,token);
        editor.apply();
}

public static String get10KCoinsToken(Context ctx) {
        SharedPreferences sharedPreference = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreference.getString(KEY_UNCONSUMED_TOKEN_10K,"");
}

public static void update10KCoinsToken(Context ctx, String token) {
        SharedPreferences sharedPreference = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(KEY_UNCONSUMED_TOKEN_10K,token);
        editor.apply();
}

}
