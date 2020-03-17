package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MultiChoiceFragment extends Fragment {


    @BindView(R.id.spinner_multichoice) Spinner multiChoice;
    @BindView(R.id.tv_question) TextView tv_question;


    public static MultiChoiceFragment newInstance() {
        return new MultiChoiceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_multichoice, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
