package com.powerhouse.zeroone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class PollFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_poll, container, false);

        new Handler().postDelayed(()->{
            getActivity().startActivity(new Intent(getActivity(), PollMainActivity.class));
            getActivity().finish();
        }, 3000);

        return view;
    }
}