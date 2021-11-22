package com.app.easymajdoor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.app.easymajdoor.MapsActivity;
import com.app.easymajdoor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity implements TextWatcher {


    EditText ed1, ed2, ed3, ed4, ed5, ed6;
    public String verificationIdOtp;
    Button verifyButton;
    FirebaseAuth mAuth;
    public PhoneAuthProvider.ForceResendingToken tokenOtp;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack1 = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationIdOtp = s;
            tokenOtp = forceResendingToken;
            // Toast.makeText(PhoneNumber.this, "" + s, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);


        }
    };
    String getOtpBackend, phone;
    TextView some_id, resend_code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);


        getOtpBackend = getIntent().getStringExtra("backentOtp");
        phone = getIntent().getStringExtra("phone");
        verifyButton = findViewById(R.id.otpVerify);
        resend_code = findViewById(R.id.resend_code);
        some_id = findViewById(R.id.some_id);
        some_id.setText(phone);

        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        ed3 = findViewById(R.id.ed3);
        ed4 = findViewById(R.id.ed4);
        ed5 = findViewById(R.id.ed5);
        ed6 = findViewById(R.id.ed6);
        ed1.addTextChangedListener(this);
        ed2.addTextChangedListener(this);
        ed3.addTextChangedListener(this);
        ed4.addTextChangedListener(this);
        ed5.addTextChangedListener(this);
        ed6.addTextChangedListener(this);

        resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phone, getOtpBackend
                );
            }
        });


        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ed1.getText().toString().trim().isEmpty() &&
                        !ed2.getText().toString().trim().isEmpty() &&
                        !ed3.getText().toString().trim().isEmpty() &&
                        !ed4.getText().toString().trim().isEmpty() &&
                        !ed5.getText().toString().trim().isEmpty() &&
                        !ed6.getText().toString().trim().isEmpty()) {
                    String codeOTP = ed1.getText().toString() +
                            ed2.getText().toString() +
                            ed3.getText().toString() +
                            ed4.getText().toString() +
                            ed5.getText().toString() +
                            ed6.getText().toString();


                    if (getOtpBackend != null) {
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                getOtpBackend, codeOTP
                        );
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signInWithCredential(phoneAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(OtpVerification.this, MapsActivity.class));

                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(OtpVerification.this, "check internet connection",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //startActivity(new Intent(OtpVerification.this,HomePage.class));
            }
        });

    }

    private void resendVerificationCode(String phone, String getOtpBackend) {
        PhoneNumberAuth ph = new PhoneNumberAuth();
        Toast.makeText(OtpVerification.this, "Please wait...", Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBack1,     // OnVerificationStateChangedCallbacks
                tokenOtp);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 1) {
            if (ed1.length() == 1) {
                ed2.requestFocus();
            }
            if (ed2.length() == 1) {
                ed3.requestFocus();
            }
            if (ed3.length() == 1) {
                ed4.requestFocus();
            }
            if (ed4.length() == 1) {
                ed5.requestFocus();
            }
            if (ed5.length() == 1) {
                ed6.requestFocus();
            }
        } else if (s.length() == 0) {
            if (ed6.length() == 0) {
                ed5.requestFocus();
            }
            if (ed5.length() == 0) {
                ed4.requestFocus();
            }
            if (ed4.length() == 0) {
                ed3.requestFocus();
            }
            if (ed3.length() == 0) {
                ed2.requestFocus();
            }
            if (ed2.length() == 0) {
                ed1.requestFocus();
            }
        }

    }

    public void authenticateUser(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(new PhoneNumberAuth().verificationId, codeByUser);
        signInTheUserByCredentials(credential);
//        mAuth=FirebaseAuth.getInstance();
//        mAuth.signInWithCredential(phoneAuthCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                startActivity(new Intent(getApplicationContext(),HomePage.class));
//                finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}