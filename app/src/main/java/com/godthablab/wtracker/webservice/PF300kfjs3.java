package com.godthablab.wtracker.webservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;




import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class PF300kfjs3 {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private String FACEBOOKTOKEN="facebookToken";

    private String FACEBOOKEXPIRE="facebookExpire";

    private String IS_GOOGLELOGIN="IS_GOOGLELOGIN";

    private static final String PREF_NAME = "Boosterprefrence_pharmacy";


    public static final String KEY_selected_massage        = "selected_massage";
    public static final String KEY_Selected_whatsupweb        = "Selected_whatsupweb";
    public static final String KEY_selected_message        = "selected_message";
    public static final String KEY_selected_language        = "selected_language";
    public static final String KEY_selected_coins        = "selected_coins";
    public static final String KEY_selected_memory        = "selected_memory";




    // Constructor
    public PF300kfjs3(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }



    public String GetSharedPreferences(String Keytag) {
        SharedPreferences SP = PreferenceManager
                .getDefaultSharedPreferences(_context);
        return SP.getString(Keytag, "");

    }

    public void SetSharedPreferences(String Keytag, String KeyValue) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(_context);
        Editor spe = prefs.edit();
        spe.putString(Keytag, KeyValue);
        spe.commit();
    }

   /* public String GC(String KeyValue) {
        SharedPreferences SP = PreferenceManager
                .getDefaultSharedPreferences(_context);
        return PC(SP.getString(KeyValue, "")) ;

    }*/
 /*   public String PC(String KeyValue) {

        return KeyValue+ PCC.JC37E();

    }*/



    public HashMap<String, String> loadMap(String key){
        HashMap<String, String> outputMap = new HashMap<String, String>();
        SharedPreferences pSharedPref = PreferenceManager
                .getDefaultSharedPreferences(_context);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString(key, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String k = keysItr.next();
                    String v = (String) jsonObject.get(k);
                    outputMap.put(k,v);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
    public boolean CheckInternet(Context con) {
        boolean flag = false;

        if (isNetworkAvailable()) {
            flag = true;

        } else {

            flag = false;
        }
        return flag;

    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }
    public void isGoogleLogin(boolean isGoogle){
        // Storing login value as TRUE
        editor.putBoolean(IS_GOOGLELOGIN, isGoogle);
    }

    public boolean isGoogleLogin()
    {
        return pref.getBoolean(IS_GOOGLELOGIN,false);
    }

    public String getFacebookToken() {
        // TODO Auto-generated method stub
        return pref.getString(FACEBOOKTOKEN, null);
    }

    public long getFacebookExpire() {
        // TODO Auto-generated method stub
        return pref.getLong(FACEBOOKEXPIRE, 0);
    }


    public void setFacebookToken(String token, long expire){
        // Storing login value as TRUE

        editor.putString(FACEBOOKTOKEN, token);
        editor.putLong(FACEBOOKEXPIRE, expire);
        // commit changes
        editor.commit();
    }
}
