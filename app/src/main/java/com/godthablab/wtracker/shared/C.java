package com.godthablab.wtracker.shared;

import android.os.Environment;

public class C {

public static final String MY_APP_FOLDER;
public static final String ROOT;
public static final String ROOT_MEDIA;

// Whats App folders
public static final String WA_IMAGES;
public static final String WA_VIDEOS;
public static final String WA_VOICE_NOTE;
public static final String WA_DOCUMENT;
public static final String WA_STICKERS;
public static final String WA_GIF;
public static final String WA_AUDIO;
public static final String WA_STATUS;
public static final String WA_PROFILE_PHOTO;
public static final String WA_DATABASES;

public static final String WA_SENT;
public static final String WA_PRIVATE;

// Application private folder
public static final String WW_IMAGES;
public static final String WW_VIDEOS;
public static final String WW_GIF;
public static final String WW_STATUS_IMAGES;
public static final String WW_STATUS_VIDEOS;

static {
        MY_APP_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsWebData";
        ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp";
        ROOT_MEDIA = ROOT + "/Media";

        WA_IMAGES = ROOT_MEDIA+"/WhatsApp Images";
        WA_VIDEOS = ROOT_MEDIA + "/WhatsApp Video";
        WA_VOICE_NOTE = ROOT_MEDIA + "/WhatsApp Voice Notes";
        WA_DOCUMENT = ROOT_MEDIA + "/WhatsApp Documents";
        WA_STICKERS = ROOT_MEDIA + "/WhatsApp Stickers";
        WA_GIF = ROOT_MEDIA + "/WhatsApp Animated Gifs";
        WA_AUDIO = ROOT_MEDIA + "/WhatsApp Audio";
        WA_PROFILE_PHOTO = ROOT_MEDIA + "/WhatsApp Profile Photos";
        WA_DATABASES = ROOT + "/Databases";

        WA_STATUS = ROOT_MEDIA + "/.Statuses";
        WA_SENT =  "/Sent";
        WA_PRIVATE = "/Private";

        WW_IMAGES = MY_APP_FOLDER +"/Images";
        WW_VIDEOS = MY_APP_FOLDER +"/Videos";
        WW_GIF = MY_APP_FOLDER +"/Gif";
        WW_STATUS_IMAGES = WW_IMAGES +"/Status";
        WW_STATUS_VIDEOS = WW_VIDEOS +"/Status";

}

}
