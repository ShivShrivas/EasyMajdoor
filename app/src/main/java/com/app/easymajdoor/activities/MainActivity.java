package com.app.easymajdoor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.Bundle;

import com.app.easymajdoor.R;
import com.app.easymajdoor.model.AuthStatus;
import com.app.easymajdoor.auth.AuthRepository;
import com.app.easymajdoor.utils.Resource;

public class MainActivity extends AppCompatActivity {
    private final AuthRepository authRepository = AuthRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAuthAndNavigate();
    }

    private void checkAuthAndNavigate() {
        LiveData<Resource<AuthStatus, AuthRepository.Errors>> authStatus = authRepository.checkAuthStatus();
        authStatus.observe(this, auth -> {
            if (auth.data != AuthStatus.AUTH_PH_NO) {
                startActivity(new Intent(this, AuthActivity.class));
                finish();
            }
        });
    }

}




