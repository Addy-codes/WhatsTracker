package com.godthablab.wtracker.tracker.visitedbyyou;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.godthablab.wtracker.C;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.tracker.wappcontact.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VisitedByYouFragment extends Fragment {

@BindView(R.id.rcList)
RecyclerView rcList;

@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_list_view, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
}

private void init(View rootView) {
        VisitedByYouAdapter visitedByYouAdapter = new VisitedByYouAdapter(requireContext());

        List<VisitedByUModel> list = getListOfVByU();
        if (list != null && !list.isEmpty()) {
                visitedByYouAdapter.addToList(list);
        } else {
                visitedByYouAdapter.addToList(getWappDummyContacts());
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        rcList.setLayoutManager(layoutManager);
        rcList.setAdapter(visitedByYouAdapter);

}

private List<VisitedByUModel> getWappDummyContacts() {
        Constants constants = new Constants(getContext());
        List<String> stringList = constants.getWhatsAppContacts();
        List<VisitedByUModel> visitedByUModels = new ArrayList<>();
        if (stringList.isEmpty()) {
                Toast.makeText(getContext(), "Sorry We coudn't find any whatsapp contact on your phone", Toast.LENGTH_LONG).show();
                return visitedByUModels;
        }
        for (int i = 0; i < 10; i++) {
                VisitedByUModel visitedByUModel = new VisitedByUModel();
                visitedByUModel.setImagePath("");
                visitedByUModel.setName(stringList.get(Integer.parseInt(constants.generateRandNymber(stringList.size()))));
                visitedByUModels.add(visitedByUModel);
        }

        return visitedByUModels;
}

private List<VisitedByUModel> getListOfVByU() {

        try {
                String dirPath = C.WA_PROFILE_PHOTO/*Environment.getExternalStorageDirectory() + "/WhatsApp" + "/" + "Media" + "/" + "WhatsApp Profile Photos"*/;
                File file = new File(dirPath);
                List<VisitedByUModel> visitedByUModels = new ArrayList<>();
                if (file.exists()) {
                        File[] dirFiles = file.listFiles();
                        if (dirFiles.length != 0) {
                                for (int ii = 0; ii < dirFiles.length; ii++) {
                                        File selectedFile = dirFiles[ii];
                                        VisitedByUModel visitedByUModel = new VisitedByUModel();
                                        visitedByUModel.setName(getContactName(selectedFile.getName()));
                                        visitedByUModel.setImagePath(selectedFile.getAbsolutePath());
                                        visitedByUModel.setImageFile(selectedFile);
                                        visitedByUModels.add(visitedByUModel);
                                }
                        }
                }
                return visitedByUModels;
        } catch (Exception e) {
                return null;
        }
}

public String getContactName(String imgName) {
        int length = imgName.length();
        int newLength = length - 19;
        return imgName.substring(0, newLength);
}

}
