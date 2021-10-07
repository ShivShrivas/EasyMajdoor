package com.app.easymajdoor.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.easymajdoor.databinding.FragmentGoogleAuthBinding;
import com.app.easymajdoor.databinding.FragmentOtpBinding;


public class GoogleAuthFragment extends Fragment {

    private FragmentGoogleAuthBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGoogleAuthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}