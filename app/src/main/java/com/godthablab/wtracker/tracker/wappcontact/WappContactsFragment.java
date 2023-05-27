package com.godthablab.wtracker.tracker.wappcontact;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.godthablab.wtracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WappContactsFragment extends Fragment {

@BindView(R.id.rcList)
RecyclerView rcList;

@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_list_view,
                                     container,
                                      false);
        ButterKnife.bind(this,view);

        init(view);
        return view ;
}

private void init(View rootView) {
        if(getActivity()==null)
                return;

        rcList = rootView.findViewById(R.id.rcList);
        WappContactsAdapter visitedByYouAdapter = new WappContactsAdapter(getActivity());

        visitedByYouAdapter.addToList(new Constants(getActivity()).getWhatsAppContacts());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rcList.setLayoutManager(layoutManager);
        rcList.setAdapter(visitedByYouAdapter);
}

}
