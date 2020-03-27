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


import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;
import static com.example.iir_ai_hospital.utils.Utils.robotAPI;
import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.nextQuestion;
import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.preQuestion;
public class OptionFragment extends Fragment {
    private Bundle bundle;

    @BindView(R.id.tv_question) TextView questions;
    @BindView(R.id.tv_question_number) TextView tv_question_number;
    @OnClick(R.id.imgBtn_positive) void onPositiveClick() {
        Log.d("Positive", "click");
        Log.d("Positive", MedicalNumberFragment.MEDICAL_NUMBER);
        nextQuestion(
                new HashMap<String, Object>() {{
                    put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                    put("answer", "1");
                }}
        );
    }
    @OnClick(R.id.imgBtn_negative) void onNegativeClick() {
        nextQuestion(
                new HashMap<String, Object>() {{
                    put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                    put("answer", "0");
                }}
        );
    }

    @OnClick(R.id.imgBtn_back) void onBackClick() {
        JumpNextFragment(LoginFragment.newInstance() ,"Login");
    }

    @OnClick(R.id.imgBtn_previousP) void onPreQuestionClick() {
        if(LoginFragment.QUESTION_COUNTER == 1) {
            JumpNextFragment(LoginFragment.newInstance() ,"Login");
        }
        else {
            LoginFragment.QUESTION_COUNTER --;
            LoginFragment.ISEND_FLAG = false;
            preQuestion(
                    new HashMap<String, String>() {{
                        put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                    }}
            );
        }

    }
    public static OptionFragment newInstance(Bundle args) {
        OptionFragment fragment = new OptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bundle = getArguments();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_option, container, false);
        ButterKnife.bind(this, view);
        Log.d("question", bundle.getString("question"));
        questions.setText(bundle.getString("question"));
        tv_question_number.setText(bundle.getString("question_number"));
        robotAPI.robot.speak(bundle.getString("question"));
        return view;
    }


}
