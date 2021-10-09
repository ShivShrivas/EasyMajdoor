package com.app.easymajdoor.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.easymajdoor.R;
import com.app.easymajdoor.activities.MainActivity;
import com.app.easymajdoor.databinding.FragmentPhNoBinding;
import com.app.easymajdoor.auth.AuthRepository;
import com.app.easymajdoor.utils.GeneralUtils;
import com.app.easymajdoor.utils.Message;
import com.app.easymajdoor.utils.SimpleTextWatcher;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class PhNoFragment extends Fragment {

    private FragmentPhNoBinding binding;
    private NavController navigator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPhNoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.phnoEt.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                binding.signInBtn.setEnabled(GeneralUtils.isValidPhNo(s.toString()));
            }
        });

        binding.signInBtn.setOnClickListener(v -> {
            String phno = "+91" + binding.phnoEt.getText().toString();
            Timber.d("PhNo: %s", phno);
            GeneralUtils.hideKeyboard(requireActivity());
            navigator.navigate(PhNoFragmentDirections.actionPhNoFragmentToOtpFragment(phno));
        });
    }

}