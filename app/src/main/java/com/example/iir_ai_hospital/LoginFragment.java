package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iir_ai_hospital.utils.Utils;

import static com.example.iir_ai_hospital.utils.Utils.PopBackFragment;
import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {
    @BindView(R.id.et_userName) EditText userName;
    @BindView(R.id.et_userID) EditText userID;
    @BindView(R.id.et_userBirthYear) EditText userBirthYear;
    @BindView(R.id.et_userBirthMonth) EditText userBirthMonth;
    @BindView(R.id.et_userBirthDay) EditText userBirthDay;

    @OnClick(R.id.imgBtn_next) void onNextClick() {
        JumpNextFragment(OptionFragment.newInstance(), "Question");
    }

    @OnClick(R.id.imgBtn_back) void onBackClick() {
        PopBackFragment();
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
