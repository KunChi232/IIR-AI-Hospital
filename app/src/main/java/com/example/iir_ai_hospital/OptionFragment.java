package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;

public class OptionFragment extends Fragment {
    @BindView(R.id.tv_questions) TextView questions;

    @OnClick(R.id.btn_positive) void onPositiveClick() {
        Log.d("positive", "click");
        JumpNextFragment(UserTypeFragment.newInstance(), "UserType");
    }
    @OnClick(R.id.btn_negative) void onNegativeClick() {
        Log.d("positive", "click");
        JumpNextFragment(UserTypeFragment.newInstance(), "UserType");
    }

    public static OptionFragment newInstance() {
        return new OptionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
