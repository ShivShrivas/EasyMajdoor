package com.app.easymajdoor.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.easymajdoor.R;
import com.app.easymajdoor.activities.MainActivity;
import com.app.easymajdoor.databinding.FragmentPhNoBinding;
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

    private FirebaseAuth mAuth;
    private Message message;
    private FragmentPhNoBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPhNoBinding.inflate(inflater, container, false);
        message = new Message(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.phnoEt.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                binding.nextBtn.setEnabled(GeneralUtils.isValidPhNo(s.toString()));
            }
        });

        binding.nextBtn.setOnClickListener(v -> {
            String phno = "+91" + binding.phnoEt.getText().toString();
            Timber.d("PhNo: %s", phno);
            startPhoneNumberVerification(phno);
            GeneralUtils.hideKeyboard(requireActivity());
            setLoading(true);
        });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(new Callbacks())
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void setLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        binding.nextBtn.setClickable(!isLoading);
    }

    public class Callbacks extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            setLoading(false);
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(requireContext(), MainActivity.class));
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            message.showSnackBar(R.string.verification_failed);
            setLoading(false);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            setLoading(false);
            Timber.d("Verification ID: %s, Resend Token: %s", verificationId, token);
            // TODO: Navigate to CODE FRAGMENT
        }
    }

}