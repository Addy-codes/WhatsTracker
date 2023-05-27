package com.godthablab.wtracker;

import static android.content.ContentValues.TAG;

import static com.godthablab.wtracker.BillingClient.Billing.MyPREFERENCES;
import static com.godthablab.wtracker.BillingClient.Billing.preminum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AudienceNetworkAds;
import com.godthablab.wtracker.ClenerGallery.GalleryActivity;
import com.godthablab.wtracker.FunnyMessages.FunnyMessagesActivity;
import com.godthablab.wtracker.NewSaverStatus.StatusSaverActivity;
import com.godthablab.wtracker.ParentalControl.HomeActivity;
import com.godthablab.wtracker.ParentalControl.SPHelpher.SharedData;
import com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView.MainRestoreChat;
import com.godthablab.wtracker.RestoredDeletedMessages.RestoredMain;
import com.godthablab.wtracker.cleaner.CleanerActivity;
import com.godthablab.wtracker.fackChatf.MainFackChatf;
import com.godthablab.wtracker.shared.AssetType;
import com.godthablab.wtracker.shared.GallerySetting;
import com.godthablab.wtracker.tracker.TrackerActivity;
import com.godthablab.wtracker.webservice.PF300kfjs3;
import com.godthablab.wtracker.whatsWebScan.WebActivity;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;
import com.rilixtech.widget.countrycodepicker.BuildConfig;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MoPubInterstitial.InterstitialAdListener {

    private MoPubView moPubView;

    String pakageName = "com.whatsapp";
    ImageView remove_ads;
    RecyclerView rvStatus;
    private final int REQ_PERMISSIONS = 101;
    Button directChat;
    RadioButton whatsappRadio,whatsappbusinessRadio;
    EditText contactnumber, message;
    static CountryCodePicker ccp;
    RadioGroup directChatRadioGroup;
    String whatsAppORwhatsAppBusiness;
    LinearLayout whatsWeb, whatsgallery;
    RelativeLayout fakeChat, funnyMessages, parentalcontrol, whatscleaner, ProfileTracker, RestoreDeletedMessages;
    CardView statusSaver;
    TextView openStatus, emptyView,profile_visitor_premium,parental_control_premium;
    long statusFolder = 0;
    LinearLayout liveStatus;
    PF300kfjs3 profession;
    private MoPubInterstitial mInterstitial;

    EditText editTextMessage;
    EditText editPhno;
    CountryCodePicker countryCodePicke;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkpremium();

        final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
        MoPub.initializeSdk(this, configuration.build(), initSdkListener());

        mInterstitial = new MoPubInterstitial(this, getString(R.string.interstitial_id));
        mInterstitial.setInterstitialAdListener(this);

        liveStatus = (LinearLayout) findViewById(R.id.liveStatus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ccp = findViewById(R.id.ccp);
        profession=new PF300kfjs3(this);
        RestoreDeletedMessages = findViewById(R.id.RestoreDeletedMessages);
        rvStatus = findViewById(R.id.rvStatus);
        emptyView = (TextView) findViewById(R.id.empty_view);
        openStatus = (TextView) findViewById(R.id.open_status);
        ProfileTracker = findViewById(R.id.ProfileTracker);
        fakeChat = findViewById(R.id.fakeChat);
        parentalcontrol = findViewById(R.id.parentalcontrol);
        whatscleaner = findViewById(R.id.whatscleaner);
        message = findViewById(R.id.message);
        directChat = findViewById(R.id.directChat);
        whatsappbusinessRadio = findViewById(R.id.whatsappbusinessRadio);
        whatsappRadio = findViewById(R.id.whatsappRadio);
        directChatRadioGroup = findViewById(R.id.directChatRadioGroup);
        whatsWeb = findViewById(R.id.whatsWeb);
        whatsgallery = findViewById(R.id.whatsGallery);
        funnyMessages = findViewById(R.id.funnyMessages);
        remove_ads = findViewById(R.id.remove_ads);
        statusSaver = findViewById(R.id.statusSaver);
        rvStatus.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvStatus.setAdapter(new StatusSaverAdapter(this, FileOperation.getFiles(C.WA_STATUS)));
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        editPhno = (EditText) findViewById(R.id.editPhno);
        countryCodePicke =  findViewById(R.id.countryCodePicker);


        AudienceNetworkAds.initialize(this);
        final SdkConfiguration.Builder Sdkonfiguration = new SdkConfiguration.Builder("b195f8dd8ded45fe847ad89ed1d016da");
        MoPub.initializeSdk(this,Sdkonfiguration.build(),initSdkListner());

        remove_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                putpremium();
                Toast.makeText(MainActivity.this, "All Ads Removed.", Toast.LENGTH_LONG).show();
                checkpremium();
            }
        });

        openStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StatusSaverActivity.class));
            }
        });

        liveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StatusSaverActivity.class));
            }
        });

        parentalcontrol.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));

                if (checkPrem() != true){

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showInterstialAd();

                                }
                            });
                        }
                    }, 0);

                }




            }
        });

        RestoreDeletedMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNotificationServiceRunning() == true) {
                    startActivity(new Intent(MainActivity.this, MainRestoreChat.class));
                }

                if (isNotificationServiceRunning() == false) {
                    startActivity(new Intent(MainActivity.this, RestoredMain.class));
                    Toast.makeText(MainActivity.this, "Please Enable Notification Access", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ProfileTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TrackerActivity.class));
            }
        });

        whatscleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CleanerActivity.class));
            }
        });

        whatsWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WebActivity.class));

                if (checkPrem() != true){

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showInterstialAd();

                                }
                            });
                        }
                    }, 0);

                }

            }
        });

        whatsgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(MainActivity.this, MainWhatsGalleryActivity.class));
                gallery();
            }
        });

        fakeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainFackChatf.class));
            }
        });

        funnyMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FunnyMessagesActivity.class));

                if (checkPrem() != true){

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showInterstialAd();

                                }
                            });
                        }
                    }, 0);

                }


            }
        });

        statusSaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StatusSaverActivity.class));
            }
        });

      /*  directChatRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.whatsappRadio) {


                    pakageName = "com.whatsapp";


                }

                else if(checkedId == R.id.whatsappbusinessRadio) {


                    pakageName = "com.whatsapp.w4b";


                }
            }

        });*/

        directChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOnDirect();
            }
        });

        checkStatus();

//        String funnyBodyText="<ul><li >Stylish Messages</li><li>Flip It</li><li>Text Repeater</li><li>Send Empty Messages</li><li>Ascii Faces</li><li>Emoji Text</li></ul>";
//        ((TextView)findViewById(R.id.funnyMessagesBody)).setText(Html.fromHtml(funnyBodyText));
    }

    private SdkInitializationListener initSdkListener() {

        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                MoPubView moPunView=findViewById(R.id.adview);
                moPunView.setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da");
                moPunView.setAdSize(MoPubView.MoPubAdSize.HEIGHT_50);
                moPunView.loadAd();


            }
        };
    }


    private SdkInitializationListener initSdkListner() {

        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {

                MoPubView moPunView=findViewById(R.id.adview);
                moPunView.setAdUnitId("b195f8dd8ded45fe847ad89ed1d016da");
                moPunView.setAdSize(MoPubView.MoPubAdSize.HEIGHT_50);
                moPunView.loadAd();

                //mInterstitial.load();
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.GET_ACCOUNTS
                        },
                        REQ_PERMISSIONS);
            }

        }
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
//        if(menu instanceof MenuBuilder){
//            MenuBuilder m = (MenuBuilder) menu;
//            m.setOptionalIconsVisible(true);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_rate:
                RateApp();
                return true;

            case R.id.action_privacy:
                startActivity(new Intent(MainActivity.this, PrivacyPolicy.class));
                return true;

            case R.id.action_terms:
                startActivity(new Intent(MainActivity.this, TermsOfUse.class));
                return true;

            case R.id.action_share:
                sendOther();
                return true;

            case R.id.action_contact:

                String model = Build.MODEL;
                String manufacturer = Build.MANUFACTURER;
                String version = Build.VERSION.RELEASE;
                int versionCode = BuildConfig.VERSION_CODE;
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"godthablab@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Whats Tracker Support");
                intent.putExtra(Intent.EXTRA_TEXT, "--Support Info--\n\nUser ID: " + SharedData.getDeviceID(MainActivity.this) + "\nApp Version: " + versionCode + "\nPlan: " + SharedData.getPlanName(MainActivity.this) + "\nManufacturer: " + manufacturer + "\nModel: " + model + "\nOS: " + version);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean whatApp() {
        try {
            getPackageManager().getPackageInfo(whatsAppORwhatsAppBusiness = "com.whatsapp", 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }

    }

    private boolean whatsAppBussiness() {
        try {
            getPackageManager().getPackageInfo(whatsAppORwhatsAppBusiness = "com.whatsapp.w4b", 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public void gallery() {
        {
            GallerySetting gallerySetting = GallerySetting.GallerySettingBuilder.builder()
                    .assetType(com.godthablab.wtracker.shared.AssetType.TYPE_IMAGE, AssetType.TYPE_VIDEO)
                    .titleStripsName("Image", "Video")
                    .fileReadPaths(com.godthablab.wtracker.shared.C.WA_IMAGES, com.godthablab.wtracker.shared.C.WA_VIDEOS)
                    .fileSavePaths(com.godthablab.wtracker.shared.C.WW_STATUS_IMAGES, com.godthablab.wtracker.shared.C.WW_STATUS_VIDEOS)
                    .readOnly(true)
                    .emptyListMessage(getString(R.string.empty_msg_status))
                    .menuDownload(false)
                    .menuDelete(false)
                    .build();
            Intent intent = new Intent(this, GalleryActivity.class);
            intent.putExtra("gallerySetting", gallerySetting);
            intent.putExtra("title", "Gallery");
            startActivity(intent);
        }
    }

    public void checkNotifications() {

        if (Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners").contains(getApplicationContext().getPackageName())) {
            //service is enabled do something
        } else {
            //service is not enabled try to enabled by calling...
            getApplicationContext().startActivity(new Intent(
                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
    }

    public void sendOther() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", "Hey there! its nice app i am Using https://play.google.com/store/apps/details?id=com.godthablab.wtracker");
        startActivity(Intent.createChooser(intent, getString(R.string.sharewith)));
    }

    private boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }

    //Rate Us Method
    private void RateApp() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.main_rate_dialog, null);
        dialogBuilder.setView(dialogView);
        Button rate_us = dialogView.findViewById(R.id.btn_rate_us);
        Button cancle = dialogView.findViewById(R.id.btn_cancle);
        final AlertDialog b = dialogBuilder.create();
        rate_us.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
                } catch (ActivityNotFoundException e) {
                    MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
                }
                b.cancel();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                b.cancel();
            }
        });
        b.show();
    }

    public long statusFolderSize(String path) {

        ArrayList<String> directory = new ArrayList<String>();
        File file = new File(path);

        if (file.isDirectory()) {
            directory.add(file.getAbsolutePath());
            while (directory.size() > 0) {
                String folderPath = directory.get(0);
                System.out.println("Size of this :" + folderPath);
                directory.remove(0);
                File folder = new File(folderPath);
                File[] filesInFolder = folder.listFiles();
                int noOfFiles = filesInFolder.length;

                for (int i = 0; i < noOfFiles; i++) {
                    File f = filesInFolder[i];
                    if (f.isDirectory()) {
                        directory.add(f.getAbsolutePath());
                    } else {
                        this.statusFolder += f.length();
                    }
                }
            }
        } else {
            this.statusFolder = file.length();
        }
        return this.statusFolder;
    }

    public void checkStatus() {

        try {
            statusFolderSize(C.WA_STATUS);
            Log.d(TAG, "WA STATUS Size: " + statusFolderSize(C.WA_STATUS));

            if (statusFolderSize(C.WA_STATUS) <= 10000) {
                // Toast.makeText(MainActivity.this, "Please View Status in WhatsApp and comeback", Toast.LENGTH_SHORT).show();
                this.emptyView.setVisibility(View.VISIBLE);
            } else {
                //  Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
            }

        } catch (NullPointerException enull) {

        }
    }

  /*  private SdkInitializationListener initSdkListener() {

        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                bannerAd();
                mInterstitial.load();
            }
        };

    }*/

   /* private void bannerAd() {

        moPubView = (MoPubView) findViewById(R.id.adview);
        moPubView.setAdUnitId(getString(R.string.mobanner));
        moPubView.loadAd();
    }*/

    protected void onDestroy() {
        moPubView.destroy();
        super.onDestroy();
    }

    @Override
    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {

       // Toast.makeText(MainActivity.this, "Ad Loaded", Toast.LENGTH_SHORT).show();

        Log.d("Interstitial","Ad Loaded Congrates");

    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {

        Log.d("Interstitial"," Loading Ad failed");

    }

    @Override
    public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {

        moPubInterstitial.load();

        Log.d("Interstitial"," Loading Ad");

    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {

        Log.d("Interstitial","Ad Clicked");

    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
        moPubInterstitial.load();
        Log.d("Interstitial","Loading Ad");

    }

    public void showInterstialAd() {

        if (mInterstitial != null && mInterstitial.isReady()) {
            mInterstitial.show();
        } else {


            // Toast.makeText(getApplicationContext(), "Not available.", Toast.LENGTH_SHORT).show();
        }

    }

    public void putpremium() {

        String MyPREFERENCES = "premium";
        String preminum = "premium";
        SharedPreferences sharedpreferences;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String string = "premium";
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(preminum, string);
        editor.commit();
        //Toast.makeText(WebActivity.this,"first put",Toast.LENGTH_LONG).show();

    }

    public void checkpremium() {

        //Get Data From SharedPrefrences
        SharedPreferences channel = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String strChannel = channel.getString(preminum, "").toString();

        if (strChannel.equals("premium")) {

            MoPubView adview;

            adview = (MoPubView) findViewById(R.id.adview);

            adview.setVisibility(View.GONE);

            profile_visitor_premium = (TextView)findViewById(R.id.profile_visitor_premium);
            parental_control_premium = (TextView)findViewById(R.id.parental_control_premium);
            parental_control_premium.setVisibility(View.GONE);
            profile_visitor_premium.setVisibility(View.GONE);



        } else {

        }

    }

    public boolean checkPrem() {

        SharedPreferences channel = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String strChannel = channel.getString(preminum, "").toString();

        if (strChannel.equals("premium")) {

            MoPubView adview;



            adview = (MoPubView) findViewById(R.id.adview);

            adview.setVisibility(View.GONE);


            return true;

        }
        else

            return false;


    }

    private void launchWhatsAppBusiness() {

        String phoneno = editPhno.getText().toString();

        Intent intent;
        if (checkWhatApp()) {
            intent = new Intent("android.intent.action.MAIN");
            intent.setComponent(new ComponentName("com.whatsapp.w4b", "com.whatsapp.w4b.Conversation"));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(PhoneNumberUtils.stripSeparators(phoneno));
            stringBuilder.append("@s.whatsapp.net");
            intent.putExtra("jid", stringBuilder.toString());

            try {
                intent = new Intent("android.intent.action.MAIN");
                intent.setComponent(new ComponentName("com.whatsapp.w4b", "com.whatsapp.w4b.Conversation"));
                intent.setAction("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.TEXT", BuildConfig.FLAVOR);
                stringBuilder = new StringBuilder();
                stringBuilder.append(PhoneNumberUtils.stripSeparators(phoneno));
                stringBuilder.append("@s.whatsapp.net");
                intent.putExtra("jid", stringBuilder.toString());
                intent.setPackage("com.whatsapp.w4b");
                startActivity(intent);
                return;
            } catch (Exception unused) {
                unused.printStackTrace();
                Toast.makeText(this,"Some error occurred",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.whatsapp.w4b"));
        Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @SuppressLint("WrongConstant")
    private boolean checkWhatApp() {
        try {
            getPackageManager().getPackageInfo("com.whatsapp.w4b", 1);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }

    }

    public void sendOnDirect (){

      //  Log.d("test", "pakageName " + pakageName );
        if(whatsappRadio.isChecked()){
            boolean WhatsupInstalled=WhatsupInstalled("com.whatsapp");
            String mobilenum=editPhno.getText().toString();
            String message=editTextMessage.getText().toString();
            if(WhatsupInstalled){




                Uri uri = Uri.parse("whatsapp://send?phone=+91" + mobilenum + "&text=" + message);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                i.setPackage("com.whatsapp");
                startActivity(i);


            }else {


            }
        }else if(whatsappbusinessRadio.isChecked()) {

            if (WhatsupInstalled("com.whatsapp.w4b")) {

                Uri uri = Uri.parse("smsto:" + "9781767938");
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hai Good Morning");
                sendIntent.setPackage("com.whatsapp.w4b");
                startActivity(sendIntent);
            }
        }


        }

    private boolean isAppInstalled(String Packagename) {
        PackageManager pm = getPackageManager();
        boolean app_installed=false;
        try {
            pm.getPackageInfo(Packagename, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void sendOnWhatsApps (){
        String messageText = editTextMessage.getText().toString();
        String phoneno = editPhno.getText().toString();

        Log.d("test", "inside sendOnWhatsApps "  );


        if (TextUtils.isEmpty(editPhno.getText().toString()))
        {
            Toast.makeText(MainActivity.this, "Please Enter phone number!", Toast.LENGTH_SHORT).show();

            Log.d("test", "inside edittext check "  );

        }
        else
        {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            "https://api.whatsapp.com/send?phone=" + countryCodePicke.getSelectedCountryCode() + phoneno + "&text=" + messageText
                    )));

            Log.d("test", "pakageName " + pakageName );

            /*startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            "https://api.whatsapp.com/send?phone=" + countryCodePicke.getSelectedCountryCode() + phoneno + "&text=" + messageText
                    )));*/

        }

        countryCodePicke.getSelectedCountryCode();
        Log.d("test", "countryCodePicker " +countryCodePicke.getSelectedCountryCode());

    }

    public void sendOnWhatsAppsBusiness(){

        Uri uri=Uri.parse("smsto:" +"9781767938");
        Intent sendIntent=new Intent(Intent.ACTION_SENDTO,uri);
        sendIntent.putExtra(Intent.EXTRA_TEXT,"Hai Good Morning");
        sendIntent.setPackage("com.whatsapp.w4b");
        startActivity(sendIntent);

        try {



        }catch (Exception e){


        }


       /* boolean WhatsupInstalled=WhatsupInstalled("com.whatsapp.w4b");

        if(WhatsupInstalled){
            Uri uri=Uri.parse("smsto:" + "8112412854");
            Intent sendIntent=new Intent(Intent.ACTION_VIEW,uri);
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Hai Good Morning");
            sendIntent.setPackage("com.whatsapp.w4b");
            startActivity(sendIntent);

        }else {

            Toast.makeText(MainActivity.this,"App not installed",Toast.LENGTH_SHORT).show();
        }


*/

    }

    private boolean WhatsupInstalled(String s){

        PackageManager pm=getPackageManager();
        boolean app_installed=false;
        try {
            pm.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            app_installed=true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed=false;
        }
        return app_installed;
    }

}

