package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserTypeFragment extends Fragment {

    @BindView(R.id.et_startday_year) EditText startday_year;
    @BindView(R.id.et_startday_month) EditText startday_month;
    @BindView(R.id.et_startday_day) EditText startday_day;
    @BindView(R.id.et_endday_year) EditText endday__year;
    @BindView(R.id.et_endday_month) EditText endday_month;
    @BindView(R.id.et_endday_day) EditText endday_day;


    public static UserTypeFragment newInstance() {
        return new UserTypeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_type, container, false);
        ButterKnife.bind(this,view);
        return view;
    }
}
