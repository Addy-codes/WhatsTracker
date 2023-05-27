package com.godthablab.wtracker.fackChatf;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.fackChatf.DataBaseDetailsf.DatabaseHelperf;
import com.godthablab.wtracker.fackChatf.DataBaseDetailsf.UserDetailsf;



import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class ChatsFragmentf extends Fragment implements OnItemClickListener, OnItemLongClickListener {
    private static ArrayList<UserDetailsf> arrayList;
    private Cursor objCursor;
    private DatabaseHelperf databaseHelperf;
    private FloatingActionButton materialDesignFAM;
    private UserAdapterf userAdapterf;
    private ListView userList;
    View view;


    //Fab button click listener for new chat
    private class btnFamListner implements OnClickListener {
        public void onClick(View view) {
            ChatsFragmentf.this.startActivity(new Intent(ChatsFragmentf.this.getActivity(), ChatProfilef.class));
            ChatsFragmentf.this.LoadAd();
        }
    }

    //Button dialog no listener
    private class btnDialogNoListner implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
        }
    }

    //Rate us Dialog method
    private class btnDialogRateUsListner implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            ChatsFragmentf.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + ChatsFragmentf.this.getActivity().getPackageName())));
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_chatf, container, false);
        this.databaseHelperf = new DatabaseHelperf(getActivity());
        this.materialDesignFAM = this.view.findViewById(R.id.material_design_android_floating_action_menu);
        this.userList = this.view.findViewById(R.id.userlist);
        CallList();
        this.materialDesignFAM.setOnClickListener(new btnFamListner());
        return this.view;
    }

    //Get recent call list method
    private void CallList() {
        GetUserDetails();
        this.userList.setOnItemClickListener(this);
        this.userList.setOnItemLongClickListener(this);
    }

    //Get user details method
    private void GetUserDetails() {
        this.objCursor = this.databaseHelperf.ViewUserList();
        arrayList = new ArrayList();
        Log.d("Total Colounmn", this.objCursor.getCount() + "");
        this.objCursor.moveToFirst();
        for (int i = 0; i < this.objCursor.getCount(); i++) {
            int id = this.objCursor.getInt(this.objCursor.getColumnIndex("uid"));
            String name = this.objCursor.getString(this.objCursor.getColumnIndex("uname"));
            String status = this.objCursor.getString(this.objCursor.getColumnIndex("ustatus"));
            String typing = this.objCursor.getString(this.objCursor.getColumnIndex("utyping"));
            String online = this.objCursor.getString(this.objCursor.getColumnIndex("uonline"));
            byte[] blob = this.objCursor.getBlob(this.objCursor.getColumnIndex("uprofile"));
            UserDetailsf userDetailsf = new UserDetailsf();
            userDetailsf.setUid(id);
            userDetailsf.setUname(name);
            userDetailsf.setUstatus(status);
            userDetailsf.setUonline(online);
            userDetailsf.setUtyping(typing);
            userDetailsf.setBytes(blob);
            arrayList.add(userDetailsf);
            this.objCursor.moveToNext();
            this.userAdapterf = new UserAdapterf(getActivity(), arrayList);
            this.userList.setAdapter(this.userAdapterf);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), UserChatf.class);
        UserDetailsf userlist = arrayList.get(i);
        Log.i("ContentValues", "SEND ID: " + userlist.getUid());
        intent.putExtra("USER_ID", userlist.getUid());
        intent.putExtra("USER_NAME", userlist.getUname());
        intent.putExtra("USER_ONLINE", userlist.getUonline());
        intent.putExtra("USER_TYPING", userlist.getUtyping());
        intent.putExtra("USER_PROFILE", i);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        CallDialog(i);
        return true;
    }

    //Call dialog method
    private void CallDialog(final int i) {
        final UserDetailsf userDetailsf = arrayList.get(i);
        Log.i("ContentValues", "SEND ID: " + userDetailsf.getUid());
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Delete Conversation.");
        alertDialog.setMessage("Are you sure you want to delete this conversation?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ChatsFragmentf.this.databaseHelperf.DeleteUserProfile(userDetailsf.getUid());
                ChatsFragmentf.arrayList.remove(i);
                ChatsFragmentf.this.userAdapterf.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("NO", new btnDialogNoListner());
        alertDialog.setNeutralButton("Rate Us", new btnDialogRateUsListner());
        alertDialog.show();
    }

    private void LoadAd() {
        //  StartAppAd.showAd(getActivity());
    }
}
