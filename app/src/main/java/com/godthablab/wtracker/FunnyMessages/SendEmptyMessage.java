package com.godthablab.wtracker.FunnyMessages;

import static com.godthablab.wtracker.BillingClient.Billing.MyPREFERENCES;
import static com.godthablab.wtracker.BillingClient.Billing.preminum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.godthablab.wtracker.R;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubView;


public class SendEmptyMessage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] numbers = { "1","5", "10", "50", "100","250","500","1000","2500","5000","10000"};

    String text;
    Spinner spin;
    int no = 1;

    CheckBox checkboxCustomText;
    boolean customText = false;
    EditText editCustomText;
    String message;
    CheckBox SupportUs;
    RadioGroup radioButtons;
    boolean withParagraph = true;
    Button button,buttonCopy;

    private MoPubView moPubView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_empty_message);

        checkpremium();

        final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
        MoPub.initializeSdk(this, configuration.build(),initSdkListener());


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Send Empty Message");
        setSupportActionBar(toolbar);

        this.spin = (Spinner) findViewById(R.id.spinner);
        this.SupportUs = (CheckBox) findViewById(R.id.checkBox);
        this.radioButtons = (RadioGroup) findViewById(R.id.radioGroup);
        this.editCustomText = (EditText) findViewById(R.id.et_custom_text);
        this.checkboxCustomText = (CheckBox) findViewById(R.id.cb_custom_text);
        this.editCustomText.setVisibility(View.INVISIBLE);


        spin.setOnItemSelectedListener(this);

        button = findViewById(R.id.button);

        buttonCopy = findViewById(R.id.buttonCopy);

        this.checkboxCustomText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {

                if (z) {
                    SendEmptyMessage.this.editCustomText.setVisibility(View.VISIBLE);
                    SendEmptyMessage.this.customText = true;
                    return;
                }
                SendEmptyMessage.this.editCustomText.setVisibility(View.INVISIBLE);
                SendEmptyMessage.this.customText = false;
            }
        });

        radioButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.rows) {

                    withParagraph = true;

                }

                else if(checkedId == R.id.single) {

                    withParagraph = false;

                }
            }

        });


        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, numbers);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendWhatsApp();
            }
        });


        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                copy();

            }
        });

    }

    //Performing action onItemSelected and onNothing selected

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
       // Toast.makeText(getApplicationContext(), numbers[position] , Toast.LENGTH_LONG).show();
        this.no = Integer.parseInt(this.spin.getSelectedItem().toString());
        this.text = this.spin.getSelectedItem().toString();
    }

    private void generateText() {
        String str = this.withParagraph ? "\n" : "";
        String customText2 = this.customText ? getCustomText() : " ";
        this.message = "";
        for (int i = 0; i < this.no && this.message.length() <= 25000; i++) {
            this.message += str + customText2;
        }
        if (this.SupportUs.isChecked()) {
            this.message += "\n" + getString(R.string.backlink);
        }
    }

    private String getCustomText() {
        return this.editCustomText.getText().toString();
    }

    public void sendWhatsApp() {
        generateText();
        PackageManager packageManager = getPackageManager();
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            packageManager.getPackageInfo("com.whatsapp", 0);
            intent.setPackage("com.whatsapp");
            intent.putExtra("android.intent.extra.TEXT", this.message);
            startActivity(Intent.createChooser(intent, getString(R.string.sharewith)));
        } catch (PackageManager.NameNotFoundException unused) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void copy() {
        generateText();
        ((ClipboardManager) getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Blank", this.message));
        Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_LONG).show();

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