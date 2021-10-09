package com.app.easymajdoor.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.easymajdoor.R;
import com.app.easymajdoor.activities.MainActivity;
import com.app.easymajdoor.databinding.FragmentGoogleAuthBinding;
import com.app.easymajdoor.model.AuthStatus;
import com.app.easymajdoor.auth.AuthRepository;
import com.app.easymajdoor.utils.Message;
import com.app.easymajdoor.utils.Resource;
import com.app.easymajdoor.utils.TextViewDottedLoading;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


public class GoogleAuthFragment extends Fragment {

    private FragmentGoogleAuthBinding binding;
    private static final int RC_SIGN_IN = 234;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private NavController navigator;
    private final AuthRepository authRepository = AuthRepository.getInstance();
    private GoogleSignInClient mGoogleSignInClient;
    private Message message;
    private GoogleSignInOptions signInOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = NavHostFragment.findNavController(this);
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), signInOptions);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGoogleAuthBinding.inflate(inflater, container, false);
        message = new Message(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signInBtn.setOnClickListener(v -> {
            setLoading(true);
            startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        checkAuthAndNavigate();
    }

    private void setLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        binding.signInBtn.setClickable(!isLoading);
    }

    private void checkAuthAndNavigate() {
        LiveData<Resource<AuthStatus, AuthRepository.Errors>> authStatus = authRepository.checkAuthStatus();
        authStatus.observe(getViewLifecycleOwner(), auth -> {
            if (auth.data == AuthStatus.AUTH_PH_NO) {

                startActivity(new Intent(requireContext(), MainActivity.class));
            } else if (auth.data == AuthStatus.AUTH) {
                navigator.navigate(GoogleAuthFragmentDirections.actionGoogleAuthFragmentToPhNoFragment());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException ignored) {
                setLoading(false);
                message.showSnackBar(getString(R.string.sign_in_failed));
            }
        }
    }

    private void firebaseAuthWithGoogle(@Nullable GoogleSignInAccount acct) {
        if (acct == null) {
            message.showSnackBar(getString(R.string.sign_in_failed));
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                checkAuthAndNavigate();
            } else {
                message.showSnackBar(getString(R.string.sign_in_failed));
            }
        });
    }

}