package com.app.easymajdoor.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.easymajdoor.activities.MainActivity;
import com.app.easymajdoor.custom.boxed_edit_text.BoxETListener;
import com.app.easymajdoor.custom.boxed_edit_text.BoxedEditText;
import com.app.easymajdoor.databinding.FragmentOtpBinding;
import com.app.easymajdoor.utils.GeneralUtils;

import timber.log.Timber;

public class OtpFragment extends Fragment {
    private FragmentOtpBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.boxedET.setBoxETListener(new BoxETListener() {
            @Override
            public void onReachedEnd() {
                GeneralUtils.hideKeyboard(requireActivity());
            }

            @Override
            public void onChange(boolean isFilled) {
                Timber.d("filled: %b", isFilled);
            }
        });
    }
}