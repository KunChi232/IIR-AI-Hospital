package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;
import static com.example.iir_ai_hospital.utils.Utils.robotAPI;
import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.nextQuestion;
import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.preQuestion;

public class MultiChoiceFragment extends Fragment {

    private Bundle bundle;

    @BindView(R.id.tv_question) TextView tv_question;
    @BindView(R.id.tv_question_number) TextView tv_question_number;
    @BindView(R.id.dynamicLayoutContainer) LinearLayout dynamicLayout;
    @BindView(R.id.imgBtn_previousP) ImageButton previousP;

    @OnClick(R.id.imgBtn_back) void onBackClick() {
        JumpNextFragment(MedicalNumberFragment.newInstance() ,"Login");
    }
    @OnClick(R.id.imgBtn_previousP) void onPreQuestionClick() {

        LoginFragment.ISEND_FLAG = false;
        LoginFragment.QUESTION_COUNTER --;
        preQuestion(
                new HashMap<String, String>() {{
                    put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                }}
        );


    }
    public static MultiChoiceFragment newInstance(Bundle args) {
        MultiChoiceFragment fragment = new MultiChoiceFragment();
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
        View view  = inflater.inflate(R.layout.fragment_multichoice, container, false);
        ButterKnife.bind(this, view);

        tv_question.setText(bundle.getString("question"));
        tv_question_number.setText(bundle.getString("question_number"));
        robotAPI.robot.speak(bundle.getString("question"));
        dynamicButton(Objects.requireNonNull(bundle.getStringArrayList("option")));

        if(LoginFragment.QUESTION_COUNTER == 1) {
            previousP.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private void dynamicButton(ArrayList<String> multiChoice) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20,20,20,20);
        for(int i = 0; i < multiChoice.size(); i++) {
            final int j = i;
            Button btn = new Button(getContext());
            btn.setText(multiChoice.get(j));
            btn.setTextSize(30);
            btn.setOnClickListener(view -> nextQuestion(
                    new HashMap<String, Object>() {{
                        put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                        put("answer", String.valueOf(j));
                    }}
            ));

            dynamicLayout.addView(btn, lp);
        }
    }
}
