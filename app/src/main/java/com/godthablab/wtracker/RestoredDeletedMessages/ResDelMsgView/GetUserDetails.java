package com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.godthablab.wtracker.MainActivity;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView.DataBaseDetails.DatabaseRestoreMessage;
import com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView.DataBaseDetails.GetterSetterUserDetails;
import java.util.ArrayList;
import androidx.fragment.app.Fragment;

/*Chat User Titles*/

public class GetUserDetails extends Fragment implements OnItemClickListener {
    private static ArrayList<GetterSetterUserDetails> arrayList; // User name Titles
    private Cursor objCursor;
    private DatabaseRestoreMessage databaseRestoreMessage;
    private GetUserDetailsAdapter getUserDetailsAdapter;
    private ListView userList;
    View view;
    GetterSetterUserDetails user_id;
    EditText editText;
    Context mContext;
    LinearLayout backbutton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.get_user_details_activity, container, false);

        backbutton = (LinearLayout)view.findViewById(R.id.backbutton);

/*        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Toasting", Toast.LENGTH_SHORT).show();
            }
        });*/

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireFragmentManager().popBackStack();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        this.databaseRestoreMessage = new DatabaseRestoreMessage(getActivity());
        this.userList = this.view.findViewById(R.id.userlist);
        View emptyView = this.view.findViewById(R.id.empty_subtitle_text);

        userList.setEmptyView(emptyView);

        CallList();
        return this.view;

     /*   editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                Toast.makeText(getActivity(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                Toast.makeText(getActivity(),"after text change",Toast.LENGTH_LONG).show();
            }
        });*/

    }

    //Get recent call list methods
    private void CallList() {
        GetUserDetails();
       //lastMessage();
        this.userList.setOnItemClickListener(this);

    }

    @Override
    public void onResume(){
        super.onResume();
        /*getUserDetailsAdapter.changeSize();
        getUserDetailsAdapter.addToList(userdetails);*/
    }

    /*  public void  notifyDataChange(){

      //arrayList.clear();
      arrayList.addAll(GetUserDetailsAdapter.userdetails);
      getUserDetailsAdapter.notifyDataSetChanged();
      Log.d("Alert","DataSetHasChanged");

  }*/

    //Get user details method
    public void GetUserDetails() {
        this.databaseRestoreMessage.whatsAppdelete();
        this.objCursor = this.databaseRestoreMessage.ViewUserList();


        arrayList = new ArrayList();
        Log.d("Total Colounmn", this.objCursor.getCount() + "");
        this.objCursor.moveToFirst();


        for (int i = 0; i < this.objCursor.getCount(); i++) {
            int id = this.objCursor.getInt(this.objCursor.getColumnIndex("_id"));
            String name = this.objCursor.getString(this.objCursor.getColumnIndex("title"));
          //  String msg = objCursor.getString(objCursor.getColumnIndex(this.user_id + ""));
            String msg = objCursor.getString(objCursor.getColumnIndex( "text"));

            String time = objCursor.getString(objCursor.getColumnIndex("time"));

            GetterSetterUserDetails getterSetterUserDetails = new GetterSetterUserDetails();
            getterSetterUserDetails.setUid(id);
            getterSetterUserDetails.setUname(name);
            getterSetterUserDetails.setLastMessage(msg);
            getterSetterUserDetails.setTime(time);

            arrayList.add(getterSetterUserDetails);
            this.objCursor.moveToNext();
            this.getUserDetailsAdapter = new GetUserDetailsAdapter(getActivity(), arrayList);
            this.userList.setAdapter(this.getUserDetailsAdapter);
        }
        /*getUserDetailsAdapter.notifyDataSetChanged();*/
    }

   /* public void lastMessage() {
        Cursor c = this.databaseRestoreMessage.getLastMessageHistory(this.title + "");
        Log.d("Total Colounmn Last", c.getCount() + "");
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {

            String msg = c.getString(c.getColumnIndex("text"));


            GetterSetterUserDetails getterSetterUserDetails = new GetterSetterUserDetails();
            getterSetterUserDetails.setLastMessage(msg);
            arrayList.add(getterSetterUserDetails);
            this.objCursor.moveToNext();
            this.getUserDetailsAdapter = new GetUserDetailsAdapter(getActivity(), arrayList);
            this.userList.setAdapter(this.getUserDetailsAdapter);
            c.moveToNext();
        }
    }*/

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(getActivity(), DisplayChat.class);
        GetterSetterUserDetails userlist = arrayList.get(i);
        this.user_id = userlist;
        Log.i("ContentValues", "SEND ID: " + userlist.getUid());
        intent.putExtra("USER_ID", userlist.getUname());
        intent.putExtra("USER_NAME", userlist.getUname());
        intent.putExtra("USER_ONLINE", userlist.getUonline());
        intent.putExtra("USER_TYPING", userlist.getUtyping());
        intent.putExtra("USER_PROFILE", i);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    public void back(){
        getFragmentManager().popBackStack();

    }

}

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Db Updated.", Toast.LENGTH_LONG).show();
        }
    }







