package com.godthablab.wtracker;

import android.content.Context;
import android.content.SharedPreferences;

public class MySession {

    Context context;
    private String IsPurchase = "Ispurchese";

    public MySession(Context context) {
        this.context = context;


    }

    public void setUserPurchesed(boolean IsPurchase) {

        if (context != null) {
            SharedPreferences Loginpref = context.getSharedPreferences("inAppprefs", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = Loginpref.edit();
            editor.putBoolean(String.valueOf(IsPurchase), IsPurchase);
            editor.apply();
        }
    }

    public boolean isUserPurched() {

        if (context != null) {
            SharedPreferences LoginPref = context.getSharedPreferences("inAppPrefs", Context.MODE_PRIVATE);
            return LoginPref.getBoolean(IsPurchase, false);
        } else {

            return false;
        }
    }

}
