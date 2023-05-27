package com.godthablab.wtracker.tracker.wappcontact;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by rao2cool on 16/4/17.
 */

public class Constants {

public static String UNLOCKED_SET = "unlockedContact123";
public static String TEN_RAND_NUMBER = "tenRandomNumber";
public static String S_PREFS_NAME = "profileTracker";

Context context;

public Constants(Context context) {
        this.context = context;
}

public Set<String> generateTenRandNumbers(int size) {
        Set<String> randNumbers = getTenFromPrefs();
        if (randNumbers == null) {
                randNumbers = new HashSet<>();
                if (size < 10) {
                        while (randNumbers.size() != size) {
                                randNumbers.add(generateRandNymber(size));
                                Log.d("randNumber", "size" + randNumbers.size());
                        }
                } else {
                        while (randNumbers.size() != 10) {
                                randNumbers.add(generateRandNymber(size));
                                Log.d("randNumber", "size" + randNumbers.size());
                        }
                }
                saveRandomNumber(randNumbers);
                return randNumbers;
        } else {
                return randNumbers;
        }
}

private void saveRandomNumber(Set<String> randNumbers) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.S_PREFS_NAME, 0);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(Constants.TEN_RAND_NUMBER, randNumbers);
        editor.apply();
}

private Set<String> getTenFromPrefs() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.S_PREFS_NAME, 0);
        return sharedPreferences.getStringSet(TEN_RAND_NUMBER, null);
}

public String generateRandNymber(int size) {
        Random random = new Random();
        return String.valueOf(random.nextInt(size));
}

public List<String> getWhatsAppContacts() {
        Cursor c = context.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY},
                ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?",
                new String[]{"com.whatsapp"},
                null);

        ArrayList<String> myWhatsappContacts = new ArrayList<String>();
        int contactNameColumn = c.getColumnIndex(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);
        while (c.moveToNext()) {
                // You can also read RawContacts.CONTACT_ID to read the
                // ContactsContract.Contacts table or any of the other related ones.
                String name = c.getString(contactNameColumn);
                myWhatsappContacts.add(name);
        }

        return myWhatsappContacts;
}


}

