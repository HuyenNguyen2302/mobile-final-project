package com.wpi.cs4518.werideshare.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wpi.cs4518.werideshare.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicleDetailsFragment extends Fragment {


    public VehicleDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_details, container, false);
    }
}
