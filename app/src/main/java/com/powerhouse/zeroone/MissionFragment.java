package com.powerhouse.zeroone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class MissionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mission, container, false);

        new Handler().postDelayed(()->{
            getActivity().startActivity(new Intent(getActivity(), MissionMainActivity.class));
            getActivity().finish();
        }, 3000);
        return view;
    }

    public boolean handleBackPress() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        return true;
    }
}
