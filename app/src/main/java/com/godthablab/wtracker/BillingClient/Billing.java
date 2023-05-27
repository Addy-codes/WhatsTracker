package com.godthablab.wtracker.BillingClient;

/*

Banner Ads Activity

MainActivity
PrivacyPolicy
TermsOfUse
WebActivity
GalleryActivity
HomeActivity
SettingsActivity
TrackerActivity
FunnyMessagesActivity all
DisplayChat


*/



import static com.godthablab.wtracker.whatsWebScan.WebActivity.MyPREFERENCES;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.godthablab.wtracker.R;

public class Billing extends AppCompatActivity {

    public static final String MyPREFERENCES = "premium" ;
    public static final String preminum = "premium";
    SharedPreferences sharedpreferences;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.billing);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


    }


    public void Checkpremium(){

        //Get Data From SharedPrefrences
        SharedPreferences channel = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String strChannel = channel.getString(preminum,"").toString();

        if(strChannel.equals("premium"))

        {



        }

        else

            {

            }

    }


    public void putpremium(){

        String string = "premium";
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(preminum, string);
        editor.commit();
        //Toast.makeText(WebActivity.this,"first put",Toast.LENGTH_LONG).show();

    }


}