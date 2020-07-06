package com.szchoiceway.aios.bridge;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.clearLogData(getContext());
                ((MainActivity)getActivity()).updateTV();
            }
        });

        Button launchButton = view.findViewById(R.id.launch);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.addLogData(getContext(), "Launch button clicked.");
                Intent in = new Intent(view.getContext(), DaemonService.class);
                in.putExtra(DaemonService.LAUNCH_APPS, true);
                in.putExtra(DaemonService.SCREEN_TURN_ON, false);
                view.getContext().startForegroundService(in);
            }
        });

        view.findViewById(R.id.rel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).updateTV();
            }
        });
    }
}
