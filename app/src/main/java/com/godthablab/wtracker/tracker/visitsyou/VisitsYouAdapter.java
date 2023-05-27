package com.godthablab.wtracker.tracker.visitsyou;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.godthablab.wtracker.R;
import com.godthablab.wtracker.shared.SharedPreferenceHelper;
import com.godthablab.wtracker.tracker.BuyCoinActivity;
import com.godthablab.wtracker.tracker.TrackerActivity;
import com.godthablab.wtracker.tracker.wappcontact.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.godthablab.wtracker.shared.SharedPreferenceHelper.SP_NAME_SET;

/**
 * Created by rao2cool on 19/4/17.
 */

public class VisitsYouAdapter extends RecyclerView.Adapter<VisitsYouHolder> {

    private int count = 5;
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> visitedByUModels = new ArrayList<>();
    private Set<String> unlockedContact;

    public VisitsYouAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        unlockedContact = getUnlockedContacts();
        count = 0;
    }


    private Set<String> getUnlockedContacts() {
        SharedPreferences sharedPref = context.getSharedPreferences(SP_NAME_SET, 0);
        Set<String> unlockedContact1 = sharedPref.getStringSet(Constants.UNLOCKED_SET, null);
        return unlockedContact1;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    void addToList(List<String> list) {
        visitedByUModels.clear();
        visitedByUModels.addAll(list);
        count = list.size() - 1;
        notifyDataSetChanged();
    }

    void changeSize() {
        Log.d("adapter", "changeSize");
        count = visitedByUModels.size();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VisitsYouHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_item_visits_u, parent, false);
        return new VisitsYouHolder(view);
    }

    int[] showNamesPos = new int[]{3, 4, 8, 9};

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final VisitsYouHolder holder, int position) {
        holder.textViewSrNm.setText((position + 1) + ")");
        if (checkForShowName(position)) {
            holder.name.setSelected(true);
            holder.name.setVisibility(View.VISIBLE);
            holder.coins1.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.coins2.setVisibility(View.VISIBLE);

            holder.name.setText(visitedByUModels.get(position));
            holder.time.setText(generateRandomNumber() + " hours ago");
        } else {
            holder.coins1.setVisibility(View.VISIBLE);
            holder.name.setVisibility(View.GONE);
            holder.coins2.setVisibility(View.GONE);
            holder.time.setVisibility(View.VISIBLE);
            holder.name.setText(visitedByUModels.get(position));
            holder.time.setText(generateRandomNumber() + " hours ago");
        }

        if (unlockedContact != null) {
            if (unloackedContact(position)) {
                holder.coins1.setVisibility(View.GONE);
                holder.name.setSelected(true);
                holder.name.setVisibility(View.VISIBLE);
                holder.coins2.setVisibility(View.GONE);
                holder.time.setVisibility(View.VISIBLE);
            }
        }


        holder.relay_d.setOnClickListener(view -> {

            Log.d("checkingvalue", checkForValidWalletMoney() + "");
            if (checkForValidWalletMoney()) {
                openConfirmationDialog(holder);
            } else {
                watchAnAd();

            }
        });
    }


    private void saveValueForUnlocked(int position) {

        if (unlockedContact != null) {
            unlockedContact.add(String.valueOf(position));
        } else {
            unlockedContact = new HashSet<>();
            unlockedContact.add(String.valueOf(position));
        }

        SharedPreferences sharedPref = context.getSharedPreferences(SP_NAME_SET, 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.clear();
        editor.putStringSet(Constants.UNLOCKED_SET, unlockedContact);
        editor.apply();
    }

    private void openConfirmationDialog(final VisitsYouHolder holder) {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_box);

        TextView agree_btn = dialog1.findViewById(R.id.agree_btn);
        TextView disagree_btn = dialog1.findViewById(R.id.disagree_btn);

        agree_btn.setOnClickListener(v -> {

            dialog1.dismiss();
            holder.coins1.setVisibility(View.GONE);
            holder.name.setVisibility(View.VISIBLE);
            holder.coins2.setVisibility(View.GONE);
            holder.time.setVisibility(View.VISIBLE);

            saveValueForUnlocked(holder.getAdapterPosition());
            totalCoins -= 100;
            SharedPreferenceHelper.updateCoins(context, totalCoins);
        });

        disagree_btn.setOnClickListener(v -> dialog1.dismiss());

        dialog1.show();

    }

    private boolean unloackedContact(int position) {
        for (String s : unlockedContact) {
            Log.d("adapter", "s : " + s);
            if (s.equalsIgnoreCase(String.valueOf(position))) {
                return true;
            }
        }
        return false;
    }

    private long totalCoins = 0;

    private boolean checkForValidWalletMoney() {
        totalCoins = SharedPreferenceHelper.getCoins(context);
        Log.d("adapter", "TotalCoins" + totalCoins);
        if (totalCoins >= 100) {
            return true;
        }
        return false;
    }

    private boolean checkForShowName(int position) {
        for (int i : showNamesPos) {
            if (i == position) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public int generateRandomNumber() {
        Random random = new Random();
        int rand = random.nextInt(10);
        if (rand == 0) {
            generateRandomNumber();
        } else {
            return rand;
        }
        return 5;
    }

    public void buyCoins() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dialogView = inflater.inflate(R.layout.buy_coins, null);
        dialogBuilder.setView(dialogView);
        Log.d("buyCoins", "Hello");

        final AlertDialog b = dialogBuilder.create();

        b.show();
    }

    public void watchAnAd () {
        Log.d("buyCoins1", "Hello");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.watch_ads, null);
        dialogBuilder.setView(dialogView);
        Button buy_coins = dialogView.findViewById(R.id.buy_coins);
        Button watchad = dialogView.findViewById(R.id.watchad);
        final AlertDialog b = dialogBuilder.create();
        buy_coins.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buyCoins();
            }
        });
        watchad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                b.cancel();
            }
        });
        b.show();
    }

}
