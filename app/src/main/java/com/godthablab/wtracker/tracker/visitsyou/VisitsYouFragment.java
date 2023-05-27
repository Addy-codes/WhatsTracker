package com.godthablab.wtracker.tracker.visitsyou;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.godthablab.wtracker.R;
import com.godthablab.wtracker.tracker.wappcontact.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class VisitsYouFragment extends Fragment implements View.OnClickListener {

RecyclerView recycleViewVisitsYou;
RelativeLayout relativeLayoutLoadMore;
View rootView;
VisitsYouAdapter visitedByYouAdapter;


@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater,
                         @Nullable ViewGroup container,
                         @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_visits_you, container, false);
        init();
        return rootView;
}

private void init() {
        recycleViewVisitsYou =  rootView.findViewById(R.id.recycleViewVisitsYou);
        relativeLayoutLoadMore =  rootView.findViewById(R.id.relativeLayoutLoadMore);
        relativeLayoutLoadMore.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        visitedByYouAdapter = new VisitsYouAdapter(getContext());

        try {
                visitedByYouAdapter.addToList(getWhatsAppContacts());
        } catch (NullPointerException | IndexOutOfBoundsException e) {
                e.printStackTrace();
        }

        recycleViewVisitsYou.setLayoutManager(layoutManager);
        recycleViewVisitsYou.setAdapter(visitedByYouAdapter);

}

private List<String> getWhatsAppContacts() throws NullPointerException {
        Cursor c = getContext().getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY},
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[]{"com.whatsapp"},
                null);

        ArrayList<String> myWhatsappContacts = new ArrayList<String>();
        int contactNameColumn = c.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
        while (c.moveToNext()) {
                // You can also read RawContacts.CONTACT_ID to read the.
                // ContactsContract.Contacts table or any of the other related ones.
                String name = c.getString(contactNameColumn);
                myWhatsappContacts.add(name);
        }

        Set<String> randomNumbers = new Constants(getContext()).generateTenRandNumbers(myWhatsappContacts.size());

        return getFilteredList(myWhatsappContacts, randomNumbers);
}

private ArrayList<String> getFilteredList(ArrayList<String> myWhatsappContacts, Set<String> randomNumbers) {
        ArrayList<String> contacts = new ArrayList<>();
        for (String s : randomNumbers) {
                int i = Integer.parseInt(s);
                contacts.add(myWhatsappContacts.get(i));
        }

        return contacts;
}


@Override
public void onClick(View view) {
        switch (view.getId()) {
                case R.id.relativeLayoutLoadMore:
                        visitedByYouAdapter.changeSize();
                        relativeLayoutLoadMore.setVisibility(View.GONE);
                        break;
        }
}
}