package com.godthablab.wtracker.FunnyMessages;

import static com.godthablab.wtracker.BillingClient.Billing.MyPREFERENCES;
import static com.godthablab.wtracker.BillingClient.Billing.preminum;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.godthablab.wtracker.R;
import com.godthablab.wtracker.asciiFaces.AsciiFacesMainActivity;
import com.godthablab.wtracker.emojiText.Texttoemoji;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubView;


public class FunnyMessagesActivity extends AppCompatActivity {


    String Maintext;
    String RepeatText;
    String no;
    int NoofRepeat;
    EditText etUsername;
    EditText mainOuput;
    EditText mainInput;
    EditText dialogInput;
    Button clearTxtBtn;
    Button convertButton;
    Button btnCopy;
    Button btnShare;
    ImageView imNewLine;
    boolean isNewLine = false;
    ProgressDialog pDialog;
    TextView txtNewLine;
    Button flipit;
    Button funnyText;
    LinearLayout backbutton;


    private MoPubView moPubView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funny_messages);

        checkpremium();

        final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
        MoPub.initializeSdk(this, configuration.build(),initSdkListener());

       // getSupportActionBar().setTitle("Funny Messages");
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*this.no =  etUsername.getText().toString();*/


        this.pDialog = new ProgressDialog(this);
        this.txtNewLine = findViewById(R.id.txtNewLine);
        this.imNewLine = findViewById(R.id.btnNewLine);
/*        if (this.isNewLine) {
            this.txtNewLine.setText("New Line On");
            this.imNewLine.setImageResource(R.drawable.ons);
        } else {
            this.txtNewLine.setText("New Line Off");
            this.imNewLine.setImageResource(R.drawable.offs);
        }*/
        this.mainInput = findViewById(R.id.mainInput);
        //   this.dialogInput = findViewById(R.id.dialog_input);
        this.mainOuput = findViewById(R.id.mainOutput);
        this.btnCopy = findViewById(R.id.copyTxtBtn);
        this.btnShare = findViewById(R.id.shareTxtBtn);
        this.clearTxtBtn = findViewById(R.id.clearTxtBtn);
        this.convertButton = findViewById(R.id.convertEmojeeBtn);
        this.flipit = findViewById(R.id.flipit);
        this.funnyText = findViewById(R.id.funnyText);
        this.dialogInput = findViewById(R.id.dialog_input);
        this.backbutton = findViewById(R.id.backbutton);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        findViewById(R.id.asciiFaces).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                startActivity(new Intent(FunnyMessagesActivity.this, AsciiFacesMainActivity.class));
            }
        });

        findViewById(R.id.emojiText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FunnyMessagesActivity.this, Texttoemoji.class));
            }
        });

        findViewById(R.id.send_empty_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FunnyMessagesActivity.this, SendEmptyMessage.class));
            }
        });


        this.btnCopy.setOnClickListener(new btnCopyListner());
        this.btnShare.setOnClickListener(new btnShareListner());


   /*     findViewById(R.id.repeater).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FunnyMessagesActivity.this,  FunnyMessagesActivity.class));
            }
        });*/


        flipit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldText = mainInput.getText().toString();
                String newText = convertString(oldText);
                mainOuput.setText(newText);


            }
        });

        funnyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldText = mainInput.getText().toString();
                String newText = convertFancyString(oldText);
                mainOuput.setText(newText);
            }
        });
    }

    /* My AlertDialog*/
    public void buttonClicked(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.repeatbox, null);


        AlertDialog.Builder dialogInput = new AlertDialog.Builder(this);
        dialogInput.setTitle("Repeat Message");
        dialogInput.setMessage("Repeat your Message for fun! Enter repeat count and send to surprise your friend");
        // this is set the view from XML inside AlertDialog
        dialogInput.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        dialogInput.setCancelable(false);


        EditText dialog_input = alertLayout.findViewById(R.id.dialog_input);


        dialogInput.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show());

        dialogInput.setPositiveButton("REPEAT", (dialog, which) -> {

            Log.d("test", dialog_input.getText().toString());
            onClick(dialog_input.getText().toString());

        });
        AlertDialog dialog = dialogInput.create();


        dialog.show();
    }


//    class btnConverListner  {


    public void onClick(String input) {
        FunnyMessagesActivity.this.mainOuput.setText("");
            FunnyMessagesActivity.this.RepeatText = FunnyMessagesActivity.this.mainInput.getText().toString();
            FunnyMessagesActivity.this.RepeatText = mainInput.getText().toString();
            no = input;
        Log.d("test3", input);
        try {
            FunnyMessagesActivity.this.NoofRepeat = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        mainInput = findViewById(R.id.mainInput);
        if (mainInput.getText().toString().isEmpty()) {
            Toast.makeText(FunnyMessagesActivity.this.getApplicationContext(), "Enter Repeat Text", Toast.LENGTH_SHORT).show();
        } else if (input.isEmpty()) {
            Toast.makeText(FunnyMessagesActivity.this.getApplicationContext(), "Enter Number of Repeat Text", Toast.LENGTH_SHORT).show();
        } else if (FunnyMessagesActivity.this.NoofRepeat <= 10000) {
            new FunnyMessagesActivity.CreateRepeateText().execute();
        } else {
            Toast.makeText(FunnyMessagesActivity.this.getApplicationContext(), "Number of Repeter Text Limited Please Enter Limited Number", Toast.LENGTH_SHORT).show();
        }
    }
//    }


    public void StringAndNoCheck() {
        FunnyMessagesActivity.this.mainOuput.setText("");
        FunnyMessagesActivity.this.RepeatText = FunnyMessagesActivity.this.mainInput.getText().toString();
        // this.no = FunnyMessagesActivity.this.dialogInput.getText().toString();

        try {
            FunnyMessagesActivity.this.NoofRepeat = Integer.parseInt(FunnyMessagesActivity.this.no);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        if (FunnyMessagesActivity.this.mainInput.getText().toString().isEmpty()) {
            Toast.makeText(FunnyMessagesActivity.this.getApplicationContext(), "Enter Repeat Text", Toast.LENGTH_SHORT).show();
        } else if (FunnyMessagesActivity.this.dialogInput.getText().toString().isEmpty()) {
            Toast.makeText(FunnyMessagesActivity.this.getApplicationContext(), "Enter Number of Repeat Text", Toast.LENGTH_SHORT).show();
        } else if (FunnyMessagesActivity.this.NoofRepeat <= 10000) {
            new FunnyMessagesActivity.CreateRepeateText().execute();
        } else {
            Toast.makeText(FunnyMessagesActivity.this.getApplicationContext(), "Number of Repeter Text Limited Please Enter Limited Number", Toast.LENGTH_SHORT).show();
        }
    }


    //Create repeat text in background
    private class CreateRepeateText extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            FunnyMessagesActivity.this.pDialog.setMessage("Please Wait...");
            FunnyMessagesActivity.this.pDialog.setProgressStyle(0);
            FunnyMessagesActivity.this.pDialog.setCancelable(false);
            FunnyMessagesActivity.this.pDialog.show();
        }

        public String doInBackground(String... strings) {
            int i;
            if (FunnyMessagesActivity.this.isNewLine) {
                for (i = 1; i <= FunnyMessagesActivity.this.NoofRepeat; i++) {
                    if (i == 1) {
                        FunnyMessagesActivity.this.Maintext = FunnyMessagesActivity.this.RepeatText;
                    } else {
                        FunnyMessagesActivity.this.Maintext += "\n" + FunnyMessagesActivity.this.RepeatText;
                    }
                }
            } else {
                for (i = 1; i <= FunnyMessagesActivity.this.NoofRepeat; i++) {
                    if (i == 1) {
                        FunnyMessagesActivity.this.Maintext = FunnyMessagesActivity.this.RepeatText;
                    } else {
                        FunnyMessagesActivity.this.Maintext += "\t" + FunnyMessagesActivity.this.RepeatText;
                    }
                }
            }
            return null;
        }

        @SuppressLint({"LongLogTag"})
        public void onPostExecute(String result) {
            FunnyMessagesActivity.this.pDialog.dismiss();
            FunnyMessagesActivity.this.mainOuput.setText(FunnyMessagesActivity.this.Maintext);
        }
    }


    private String convertString(String input) {

        String normal = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM123456789!@#$%^&*()_+,.';";
        String split = "bʍǝɹʇʎnᴉodɐspɟƃɥɾʞlzxɔʌquɯQMƎɹ┴⅄∩IOԀ∀SpℲפHſʞ˥ZXƆΛqNWƖᄅƐㄣϛ9ㄥ86¡@#$%^⅋*)(‾+'˙,;";

        /*String split = "bʍǝɹʇʎnᴉodɐspɟƃɥɾʞlzxɔʌquɯQMƎɹ┴⅄∩IOԀ∀SpℲפHſʞ˥ZXƆΛqNWƖᄅƐㄣϛ9ㄥ86¡@#$%^⅋*)(‾+'˙,;";*/
        String output = "";
        char letter;

        for (int i = 0; i < input.length(); i++) {
            letter = input.charAt(i);

            int a = normal.indexOf(letter);
            output += (a != -1) ? split.charAt(a) : letter;

        }

        return new StringBuilder(output).reverse().toString();

    }


    private String getCustomText() {
        return this.mainInput.getText().toString();
    }



    private String convertFancyString(String input) {

        String normal = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM123456789";
        String split = "ⓠⓦⓔⓡⓣⓨⓤⓘⓞⓟⓐⓢⓓⓕⓖⓗⓙⓚⓛⓩⓧⓒⓥⓑⓝⓜⓆⓌⒺⓇⓉⓎⓊⒾⓄⓅⒶⓈⒹⒻⒼⒽⒿⓀⓁⓏⓍⒸⓋⒷⓃⓂ①②③④⑤⑥⑦⑧⑨";
        String output = "";
        char letter;

        for (int i = 0; i < input.length(); i++) {
            letter = input.charAt(i);

            int a = normal.indexOf(letter);
            output += (a != -1) ? split.charAt(a) : letter;

        }

        return new StringBuilder(output).toString();

    }


    //Click event of Button Copy
    private class btnCopyListner implements View.OnClickListener {
        @SuppressLint({"WrongConstant"})
        public void onClick(View view) {
            if (FunnyMessagesActivity.this.mainOuput.getText().toString().isEmpty()) {
                Toast.makeText(FunnyMessagesActivity.this.getApplicationContext(), "Convert text before copy", Toast.LENGTH_SHORT).show();
                return;
            }
            ((ClipboardManager) FunnyMessagesActivity.this.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(FunnyMessagesActivity.this.mainInput.getText().toString(), FunnyMessagesActivity.this.mainOuput.getText().toString()));
            Toast.makeText(FunnyMessagesActivity.this.getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
        }
    }


    //Click event of Button Share
    private class btnShareListner implements View.OnClickListener {
        public void onClick(View view) {
            if (FunnyMessagesActivity.this.mainOuput.getText().toString().isEmpty()) {
                Toast.makeText(FunnyMessagesActivity.this.getApplicationContext(), "Please Convert text to share", Toast.LENGTH_LONG).show();
                return;
            }
            Intent shareIntent = new Intent();
            shareIntent.setAction("android.intent.action.SEND");
            shareIntent.setPackage("com.whatsapp");
            shareIntent.putExtra("android.intent.extra.TEXT", FunnyMessagesActivity.this.mainOuput.getText().toString());
            shareIntent.setType("text/plain");
            FunnyMessagesActivity.this.startActivity(Intent.createChooser(shareIntent, "Select an app to share"));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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