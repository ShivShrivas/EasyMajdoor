package com.app.easymajdoor.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.easymajdoor.R;
import com.app.easymajdoor.custom.boxed_edit_text.BoxETListener;
import com.app.easymajdoor.custom.boxed_edit_text.BoxedEditText;
import com.app.easymajdoor.utils.GeneralUtils;
import com.google.firebase.auth.FirebaseAuth;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //handleAuth();
        setContentView(R.layout.activity_main);

        ((BoxedEditText)findViewById(R.id.boxedET)).setBoxETListener(new BoxETListener() {
            @Override
            public void onReachedEnd() {
                GeneralUtils.hideKeyboard(MainActivity.this);
            }

            @Override
            public void onChange(boolean isFilled) {
                Timber.d("filled: %b", isFilled);
            }
        });

    }

    private void handleAuth() {
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(this, AuthActivity.class));
            }
        });
    }


}




