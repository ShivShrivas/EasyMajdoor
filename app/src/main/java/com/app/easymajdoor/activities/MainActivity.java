package com.app.easymajdoor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.easymajdoor.R;
import com.app.easymajdoor.databinding.ActivityMainBinding;
import com.app.easymajdoor.model.AuthStatus;
import com.app.easymajdoor.auth.AuthRepository;
import com.app.easymajdoor.utils.Resource;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private final AuthRepository authRepository = AuthRepository.getInstance();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logOutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut();
            startActivity(new Intent(this, AuthActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
            finish();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAuthAndNavigate();
    }

    private void checkAuthAndNavigate() {
        Timber.d("check auth");
        LiveData<Resource<AuthStatus, AuthRepository.Errors>> authStatus = authRepository.checkAuthStatus();
        authStatus.observe(this, auth -> {
            if (auth.data != AuthStatus.AUTH_PH_NO) {
                startActivity(new Intent(this, AuthActivity.class));
                finish();
            }
        });
    }

}




