package com.godthablab.wtracker.shared;

import android.os.Parcel;
import android.os.Parcelable;

public class GallerySetting implements Parcelable {



String titleStripsName[];
String filePaths[];
int assetType[];
String fileReadPaths[];
String fileSavePaths[];
boolean readOnly;
boolean menuDelete;
boolean menuDownload;
boolean hideTabLayout;
boolean showInterstitial;
String emptyListMessage;
/*FirebaseInstance issue Resolved waiting for testing */
public GallerySetting() {
        /*Default Values*/
        hideTabLayout = false;
        showInterstitial = false;
        emptyListMessage="";
}
/*Status Saver UI updated */
protected GallerySetting(Parcel in) {
        titleStripsName = in.createStringArray();
        filePaths = in.createStringArray();
        fileReadPaths = in.createStringArray();
        fileSavePaths = in.createStringArray();
        assetType = in.createIntArray();
        readOnly = in.readByte() != 0;
        menuDelete = in.readByte() != 0;
        menuDownload = in.readByte() != 0;
        hideTabLayout = in.readByte() != 0;
        showInterstitial = in.readByte() != 0;
        emptyListMessage = in.readString();
}

public static final Creator<GallerySetting> CREATOR = new Creator<GallerySetting>() {
        @Override
        public GallerySetting createFromParcel(Parcel in) {
                return new GallerySetting(in);
        }
/* working on gallery crash issue*/
        @Override
        public GallerySetting[] newArray(int size) {
                return new GallerySetting[size];
        }
};

public String[] getTitleStripsName() {
        return titleStripsName;
}

public String[] getFilePaths() {
        return filePaths;
}

public String[] getFileReadPaths() {
        return fileReadPaths;
}

public String[] getFileSavePaths() {
        return fileSavePaths;
}

public boolean isReadOnly() {
        return readOnly;
}

public boolean isMenuDelete() {
        return menuDelete;
}

public boolean isMenuDownload() {
        return menuDownload;
}

public int[] getAssetType() {
        return assetType;
}

public boolean isHideTabLayout() {
        return hideTabLayout;
}

public boolean isShowInterstitial() {
        return showInterstitial;
}

public String getEmptyListMessage() {
        return emptyListMessage;
}

@Override
public int describeContents() {
        return 0;
}

@Override
public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(titleStripsName);
        dest.writeStringArray(filePaths);
        dest.writeStringArray(fileReadPaths);
        dest.writeStringArray(fileSavePaths);
        dest.writeIntArray(assetType);
        dest.writeByte((byte) (readOnly ? 1 : 0));
        dest.writeByte((byte) (menuDelete ? 1 : 0));
        dest.writeByte((byte) (menuDownload ? 1 : 0));
        dest.writeByte((byte) (hideTabLayout ? 1 : 0));
        dest.writeByte((byte) (showInterstitial ? 1 : 0));
        dest.writeString(emptyListMessage);
}


public static class GallerySettingBuilder {

private GallerySetting gallerySetting;

GallerySettingBuilder() {
        this.gallerySetting = new GallerySetting();
}

public static GallerySettingBuilder builder() {
        return new GallerySettingBuilder();
}

public GallerySettingBuilder readOnly(boolean value) {
        gallerySetting.readOnly = value;
        return this;
}


public GallerySettingBuilder menuDelete(boolean value) {
        gallerySetting.menuDelete = value;
        return this;

}
public GallerySettingBuilder menuDownload(boolean value) {
        gallerySetting.menuDownload = value;
        return this;

}

public GallerySettingBuilder titleStripsName(String... value) {
        gallerySetting.titleStripsName = value;
        return this;
}


public GallerySettingBuilder filePaths(String... value) {
        gallerySetting.filePaths = value;
        return this;
}


public GallerySettingBuilder fileReadPaths(String... value) {
        gallerySetting.fileReadPaths = value;
        return this;
}


public GallerySettingBuilder fileSavePaths(String... value) {
        gallerySetting.fileSavePaths = value;
        return this;
}

public GallerySettingBuilder assetType(int... value) {
        gallerySetting.assetType = value;
        return this;
}

public GallerySettingBuilder hideTabLayout(boolean hideTabLayout) {
        gallerySetting.hideTabLayout = hideTabLayout;
        return this;
}

public GallerySettingBuilder showInterstitial(boolean showInterstitial) {
        gallerySetting.showInterstitial = showInterstitial;
        return this;
}

public GallerySettingBuilder emptyListMessage(String emptyListMessage) {
        gallerySetting.emptyListMessage = emptyListMessage;
        return this;
}


public GallerySetting build() {
        return gallerySetting;
}


} //end of GallerySettingBuilder

}
