package com.godthablab.wtracker.fackChatf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.godthablab.wtracker.R;
import com.godthablab.wtracker.fackChatf.DataBaseDetailsf.DatabaseHelperf;
import com.godthablab.wtracker.fackChatf.DataBaseDetailsf.UserDetailsf;

public class StausFragmentf extends Fragment {
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_statusf, container, false);
//        if (!Internetconnection.checkConnection(getActivity())) {
//            Mrec banner = this.view.findViewById(R.id.startAppBanner);
//            banner.hideBanner();
//        }
        return this.view;
    }
}
