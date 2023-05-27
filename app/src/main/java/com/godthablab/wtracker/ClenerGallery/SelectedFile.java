package com.godthablab.wtracker.ClenerGallery;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectedFile implements Parcelable {

public String src;
public String dst;
public String action;

public static final String ADD = "add";
public static final String REMOVE_FROM_LIST = "removeFromList";
public static final String NONE = "none";
public static final String DELETE = "delete";

public SelectedFile(String src, String dst) {
        this.src = src;
        this.dst = dst;
        this.action = ADD;
}

public SelectedFile(String src, String dst, String action) {
        this.src = src;
        this.dst = dst;
        this.action = action;
}

protected SelectedFile(Parcel in) {
        src = in.readString();
        dst = in.readString();
        action = in.readString();
}

public static final Creator<SelectedFile> CREATOR = new Creator<SelectedFile>() {
        @Override
        public SelectedFile createFromParcel(Parcel in) {
                return new SelectedFile(in);
        }

        @Override
        public SelectedFile[] newArray(int size) {
                return new SelectedFile[size];
        }
};

@Override
public int describeContents() {
        return 0;
}

@Override
public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(src);
        dest.writeString(dst);
        dest.writeString(action);
}

}
