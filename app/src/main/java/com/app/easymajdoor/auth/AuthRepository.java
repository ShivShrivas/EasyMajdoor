package com.app.easymajdoor.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.easymajdoor.model.AuthStatus;
import com.app.easymajdoor.model.UserDetails;
import com.app.easymajdoor.utils.Resource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;

public class AuthRepository {
    public enum Errors {
        NOT_AUTHENTICATED,
        UNKNOWN_ERROR,
        PHONE_NO_ALREADY_EXISTS
    }

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference dbRoot = database.getReference();

    public static AuthRepository instance;

    public static AuthRepository getInstance() {
        if (instance == null) {
            synchronized (AuthRepository.class) {
                if (instance == null) {
                    instance = new AuthRepository();
                }
            }
        }
        return instance;
    }

    private AuthRepository() {
    }

    public LiveData<Resource<AuthStatus, Errors>> checkAuthStatus() {
        FirebaseUser user = auth.getCurrentUser();
        MutableLiveData<Resource<AuthStatus, Errors>> authStatus = new MutableLiveData<>();

        if (user != null) {
            dbRoot.child("users/" + user.getUid() + "/phNo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        authStatus.setValue(Resource.success(AuthStatus.AUTH_PH_NO));
                    } else {
                        authStatus.setValue(Resource.success(AuthStatus.AUTH));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    authStatus.setValue(Resource.error(Errors.UNKNOWN_ERROR));

                }
            });
        } else {
            authStatus.setValue(Resource.success(AuthStatus.LOGGED_OUT));
        }

        return authStatus;
    }

    public LiveData<Resource<Void, Errors>> addDetails(String phno) {
        FirebaseUser user = auth.getCurrentUser();
        MutableLiveData<Resource<Void, Errors>> status = new MutableLiveData<>();

        if (user != null) {
            UserDetails det = new UserDetails(user.getDisplayName(), phno, user.getEmail());

            dbRoot.child("users").orderByChild("phNo").equalTo(phno).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        status.setValue(Resource.error(Errors.PHONE_NO_ALREADY_EXISTS));
                    } else {
                        dbRoot.child("users/" + user.getUid()).setValue(det).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                status.setValue(Resource.success());
                            } else {
                                status.setValue(Resource.error(Errors.UNKNOWN_ERROR));
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    status.setValue(Resource.error(Errors.UNKNOWN_ERROR));
                }
            });

        } else {
            status.setValue(Resource.error(Errors.NOT_AUTHENTICATED));
        }

        return status;
    }

    public boolean isGoogleAuthCompleted() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }

}
