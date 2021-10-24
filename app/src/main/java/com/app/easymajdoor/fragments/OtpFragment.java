package com.app.easymajdoor.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.easymajdoor.MapsActivity;
import com.app.easymajdoor.R;
import com.app.easymajdoor.activities.MainActivity;
import com.app.easymajdoor.auth.AuthRepository;
import com.app.easymajdoor.custom.boxed_edit_text.BoxETListener;
import com.app.easymajdoor.custom.boxed_edit_text.BoxedEditText;
import com.app.easymajdoor.databinding.FragmentOtpBinding;
import com.app.easymajdoor.utils.GeneralUtils;
import com.app.easymajdoor.utils.Message;
import com.app.easymajdoor.utils.Resource;
import com.app.easymajdoor.utils.Status;
import com.app.easymajdoor.utils.TextViewDottedLoading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class OtpFragment extends Fragment {
    private final long OTP_TIMEOUT = 20000;
    private FragmentOtpBinding binding;
    private FirebaseAuth mAuth;
    private Message message;
    private NavController navigator;
    private CountDownTimer countDownTimer;
    private String phNo;
    private final AuthRepository authRepository = AuthRepository.getInstance();
    private String otpVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private TextViewDottedLoading pleaseEnterAnim;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = NavHostFragment.findNavController(this);
        mAuth = FirebaseAuth.getInstance();
        phNo = OtpFragmentArgs.fromBundle(getArguments()).getNumber();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtpBinding.inflate(inflater, container, false);
        message = new Message(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pleaseEnterAnim = new TextViewDottedLoading(binding.pleaseEnter,500);

        countDownTimer = new CountDownTimer(OTP_TIMEOUT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.resendCodeBtn.setClickable(false);
                binding.resendCodeBtn.setText(getString(R.string.resend_otp_in, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                binding.resendCodeBtn.setClickable(true);
                binding.resendCodeBtn.setText(R.string.resend);
            }
        };

        binding.boxedET.setBoxETListener(new BoxETListener() {
            @Override
            public void onReachedEnd() {
                GeneralUtils.hideKeyboard(requireActivity());
            }

            @Override
            public void onChange(boolean isFilled) {
                binding.verifyBtn.setEnabled(isFilled);
            }
        });

        binding.verifyBtn.setOnClickListener(v -> verifyOtp(binding.boxedET.getText()));
        binding.resendCodeBtn.setOnClickListener(v -> sendOtp());
        binding.backBtn.setOnClickListener(v -> navigator.popBackStack());
        sendOtp();
    }

    private void sendOtp() {
        binding.resendCodeBtn.setText(getString(R.string.resend_otp_in, 20));
        binding.resendCodeBtn.setClickable(false);
        setLoading(true);
        pleaseEnterAnim.setText(getString(R.string.sending_otp), true);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phNo)
                        .setTimeout(OTP_TIMEOUT, TimeUnit.MILLISECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(new Callbacks())
                        .setForceResendingToken(mResendToken)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    void verifyOtp(String otp) {
        setLoading(true);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpVerificationId, otp);
        Objects.requireNonNull(mAuth.getCurrentUser()).updatePhoneNumber(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Timber.d("signInWithCredential:success");
                        addDetailsToDb();
                    } else {
                        setLoading(false);
                        Timber.d(task.getException(), "signInWithCredential:failure");
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message.showSnackBar(R.string.invalid_code);
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            message.showSnackBar(R.string.number_already_taken);
                        } else {
                            message.showSnackBar(R.string.sign_in_error);
                        }
                    }
                });

    }

    private void addDetailsToDb() {
        pleaseEnterAnim.setText(getString(R.string.creating_account), true);
        authRepository.addDetails(phNo).observe(getViewLifecycleOwner(), status -> {
            if (status.status == Status.SUCCESS) {
                Timber.d("wtf");
                startActivity(new Intent(requireContext(), MapsActivity.class));
                requireActivity().finish();
            } else if (status.error == AuthRepository.Errors.PHONE_NO_ALREADY_EXISTS) {
                message.showSnackBar(R.string.number_already_taken);
                navigator.popBackStack();
            } else {
                message.showSnackBar(R.string.unknown_error);
                navigator.popBackStack();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        binding.boxedET.setEnabled(!isLoading);
        binding.verifyBtn.setClickable(!isLoading);
        binding.backBtn.setClickable(!isLoading);
    }

    public class Callbacks extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            setLoading(false);

            String code = credential.getSmsCode();
            if (code != null) {
                binding.boxedET.setText(code);
            }
            Timber.d("onVerificationCompleted:%s", credential);
            addDetailsToDb();
            countDownTimer.cancel();
            countDownTimer.onFinish();
            message.showSnackBar(R.string.instant_verification);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Timber.d(e, "onVerificationFailed");
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                message.showSnackBar(R.string.invalid_request);
            } else if (e instanceof FirebaseTooManyRequestsException) {
                message.showSnackBar(R.string.sms_quota_exceeded);
            } else {
                message.showSnackBar(R.string.number_blocked);
            }

            navigator.popBackStack();
            setLoading(false);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            otpVerificationId = verificationId;
            mResendToken = token;
            setLoading(true);
            pleaseEnterAnim.setText(getString(R.string.auto_retrieve), true);
            countDownTimer.start();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            setLoading(false);
            pleaseEnterAnim.setText(getString(R.string.please_enter_the_otp_that_send_on, phNo), false);
            countDownTimer.cancel();
            countDownTimer.onFinish();
            message.showSnackBar(R.string.enter_code_manually);
        }

    }

}