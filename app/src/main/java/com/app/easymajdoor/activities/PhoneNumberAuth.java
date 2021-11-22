package com.app.easymajdoor.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.easymajdoor.MapsActivity;
import com.app.easymajdoor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneNumberAuth extends AppCompatActivity {
    private final String TAG = "PhoneNumber";
    public String verificationId;
    Button send_otp;
    EditText phoneNumber;
    public PhoneAuthProvider.ForceResendingToken token;
    FirebaseAuth mAuth;
    Dialog dailog;
    FirebaseUser firebaseUser;
    FirebaseDatabase databaseReference;
    String phone;
    ProgressDialog progressDialog;
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            progressDialog.dismiss();
            Toast.makeText(PhoneNumberAuth.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            token = forceResendingToken;
            // Toast.makeText(PhoneNumber.this, "" + s, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(PhoneNumberAuth.this, OtpVerification.class);
            i.putExtra("backentOtp", verificationId);
            i.putExtra("phone", phone);
            progressDialog.dismiss();
            startActivity(i);
            finish();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);


        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStart() {
        super.onStart();
        ProgressDialog progressDialog = new ProgressDialog(PhoneNumberAuth.this);
        progressDialog.setMessage("Checking your Details...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(PhoneNumberAuth.this, MapsActivity.class));
                        progressDialog.dismiss();
                        finish();

                    } else {
            progressDialog.dismiss();


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_auth);
        send_otp = findViewById(R.id.verify);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("please wait...");


        mAuth = FirebaseAuth.getInstance();
//        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        phoneNumber = findViewById(R.id.searchBar);

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phoneNumber.getText().toString().trim().isEmpty()) {
                    if (phoneNumber.getText().toString().length() == 10) {

                        phone = "+91" + phoneNumber.getText().toString();
                        progressDialog.show();
                        sendVerificationCode(phone);   // OnVerificationStateChangedCallbacks


                    } else {
                        Toast.makeText(PhoneNumberAuth.this, "enter a valid 10 digit mobile number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PhoneNumberAuth.this, "enter a mobile number", Toast.LENGTH_SHORT).show();
                }

            }
            //   startActivity(new Intent(PhoneNumber.this,OtpVerification.class));
            //}

        });
    }

    private void sendVerificationCode(String number) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                number,
//                10,
//                TimeUnit.SECONDS,
//                (Activity) TaskExecutors.MAIN_THREAD,
//                mCallBack
//        );
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    // initializing our callbacks for on
    // verification callback method.
//            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        // below method is used when
//        // OTP is sent from Firebase
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            // when we receive the OTP it
//            // contains a unique id which
//            // we are storing in our string
//            // which we have already created.
//            verificationId = s;
//        }
//
//        // this method is called when user
//        // receive OTP from Firebase.
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//            // below line is used for getting OTP code
//            // which is sent in phone auth credentials.
//            final String code = phoneAuthCredential.getSmsCode();
//
//            // checking if the code
//            // is null or not.
//            if (code != null) {
//                // if the code is not null then
//                // we are setting that code to
//                // our OTP edittext field.
//              //  edtOTP.setText(code);
//
//                // after setting this code
//                // to OTP edittext field we
//                // are calling our verifycode method.
//               // verifyCode(code);
//            }
//        }
//
//        // this method is called when firebase doesn't
//        // sends our OTP code due to any error or issue.
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//            // displaying error message with firebase exception.
//            Toast.makeText(PhoneNumber.this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Intent i = new Intent(PhoneNumberAuth.this, OtpVerification.class);

                            startActivity(i);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(PhoneNumberAuth.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}