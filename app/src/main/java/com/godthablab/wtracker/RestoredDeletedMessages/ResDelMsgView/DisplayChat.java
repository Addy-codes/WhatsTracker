package com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.godthablab.wtracker.R;
import com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView.DataBaseDetails.DatabaseRestoreMessage;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubView;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class DisplayChat extends AppCompatActivity   {



    private static final String TAG = "USER Activity";
    public static ListView chatlist;
    public static String tokenname;
    LinearLayout Attach;
    ImageView Call;
    LinearLayout More;
    LinearLayout VideoCall;
    LinearLayout attach;
    LinearLayout backmenu;
    Bitmap bitmap;
    LinearLayout camera;
    RelativeLayout chatbackground;
    ArrayList<String> chatid = new ArrayList<>();
    DatabaseRestoreMessage databaseRestoreMessage;
    EmojIconActions emojIcon;
    String imagepath;
    ArrayList<String> imagepathList = new ArrayList<>();
    ArrayList<String> ismessagetype = new ArrayList<>();
    String istext;
    EmojiconEditText message;
    ArrayList<HashMap<String, String>> messageArray = new ArrayList<>();
    RelativeLayout messageLayout;
    ImageView more_chat;
    ArrayList<String> msgstatuslist = new ArrayList<>();
    ArrayList<String> msgtime = new ArrayList<>();
    TextView name;
    String online;
    byte[] profile;
    ImageView receive;
    RelativeLayout recordlayout;
    ImageView send;
    String sender;
    ArrayList<String> senderUser = new ArrayList<>();
    String typing;
    Uri uri;
    ImageView user_icon;
    String user_id;
    LinearLayout userdetails;
    String username;
    TextView visibilitystatus;
    LinearLayout backbutton;
    DisplayChatAdapter displayChatAdapter;

    private MoPubView moPubView;

    @Override
    public void onResume() {
        super.onResume();
      //  chatAdapterResdel.notifyDataSetChanged();
    }


    @SuppressLint({"WrongViewCast"})
    @RequiresApi(api = 16)


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_chat_activity);


        final SdkConfiguration.Builder configuration = new SdkConfiguration.Builder((getString(R.string.mobanner)));
        MoPub.initializeSdk(this, configuration.build(),initSdkListener());



        this.message = findViewById(R.id.message);
        this.backbutton = findViewById(R.id.backbutton);
        this.imagepath = tokenname;
        this.databaseRestoreMessage = new DatabaseRestoreMessage(this);
        this.user_id = getIntent().getExtras().getString("USER_ID");
        this.username = getIntent().getExtras().getString("USER_NAME");
        this.online = getIntent().getExtras().getString("USER_ONLINE");
        this.typing = getIntent().getExtras().getString("USER_TYPING");
        this.Call = findViewById(R.id.imCall);
        this.recordlayout = findViewById(R.id.recordLayout);
        this.messageLayout = findViewById(R.id.messageLayout);
        this.name = findViewById(R.id.username);
        this.name.setText(this.username + "");
        this.profile = GetUserDetailsAdapter.userdetails.get(getIntent().getExtras().getInt("USER_PROFILE")).getBytes();
        this.user_icon = findViewById(R.id.user_icon);
        this.visibilitystatus = findViewById(R.id.visibilitystatus);
        this.More = findViewById(R.id.more);
        this.VideoCall = findViewById(R.id.videocall);
        this.Attach = findViewById(R.id.attach);
        this.camera = findViewById(R.id.camera);
        this.attach = findViewById(R.id.attach);
        this.send = findViewById(R.id.send);
        this.receive = findViewById(R.id.receive);
        this.backmenu = findViewById(R.id.backmenu);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        chatlist = findViewById(R.id.chatlist);


        this.chatbackground = findViewById(R.id.chatbackground);
        if (this.imagepath != null) {
            try {
                this.bitmap = Media.getBitmap(getContentResolver(), FileProvider.getUriForFile(this, "android.arch.core.provider", new File(this.imagepath)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.chatbackground.setBackgroundDrawable(new BitmapDrawable(this.bitmap));
        }
        this.userdetails = findViewById(R.id.userdetails);
        this.more_chat = findViewById(R.id.background);

        this.messageLayout.setVisibility(View.INVISIBLE);
        DisplayChat();

        this.chatbackground = findViewById(R.id.chatbackground);
        ImageView emojiImageView = findViewById(R.id.emoji_btn);
        this.message = findViewById(R.id.message);
        this.emojIcon = new EmojIconActions(this, this.chatbackground, this.message, emojiImageView);
        this.emojIcon.ShowEmojIcon();
        this.emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);

    }

    //Display all your old chat method
    private void DisplayChat() {
        Cursor c = this.databaseRestoreMessage.getUserChatHistory(this.user_id + "");
        Log.d("Total Colounmn", c.getCount() + "");
        Log.e("Total Colounmn", c.getCount() + "");
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            String id = String.valueOf(c.getInt(c.getColumnIndex("title")));
            String msg = c.getString(c.getColumnIndex("text"));
            String time = c.getString(c.getColumnIndex("time"));

            HashMap<String, String> object = new HashMap<>();
                object.put("Message", msg);


            try {

                if (i > 0 && msg.toLowerCase().equals("this message was deleted")) {
                    Log.d("Deleted", "Deleted Message String Detected");
                    HashMap<String, String> lastObject = this.messageArray.get(i - 1);
                    lastObject.put("IsDeleted", "1");
                    this.messageArray.set(i - 1, lastObject);
                } else {
                    object.put("IsDeleted", "0");
                    this.messageArray.add(object);
                }
            }
            catch (IllegalThreadStateException ignored){}



            /*
            HashMap<String, String> object = new HashMap<>();
            object.put("Message", msg);
            if (i > 0 && msg.toLowerCase().equals("this message was deleted")) {
                Log.d("Deleted","Deleted Message String Detected");
                HashMap<String, String> lastObject = this.messageArray.get(i - 1);
                lastObject.put("IsDeleted", "1");
                addNotification();
                this.messageArray.set(i - 1, lastObject);
            } else {
                object.put("IsDeleted", "0");
                this.messageArray.add(object);

                }

            }*/



            this.msgtime.add(time);
            this.chatid.add(id + "");

            c.moveToNext();
            Log.i(TAG, "DisplayChat: " + this.chatid);

        }
        chatlist.setAdapter(new DisplayChatAdapter(this, this.messageArray, this.senderUser, this.msgtime, this.msgstatuslist, this.ismessagetype, this.imagepathList));
    }


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


}






