package com.app.easymajdoor.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.easymajdoor.R;

public class PhNoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_ph_no, container, false);
    }
}