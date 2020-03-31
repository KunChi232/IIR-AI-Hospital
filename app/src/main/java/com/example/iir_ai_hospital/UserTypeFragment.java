package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;
import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.nextQuestion;
import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.preQuestion;

public class UserTypeFragment extends Fragment {

    private Bundle bundle;

    @BindView(R.id.et_userType) EditText userType;
    @BindView(R.id.tv_question_number) TextView tv_question_number;
    @BindView(R.id.tv_question) TextView tv_question;
    @BindView(R.id.imgBtn_previousP)
    ImageButton previousP;
    @OnClick(R.id.imgBtn_nextP) void onNextProblemClick() {

        if(userType.getText().toString().trim().length()>0){
            nextQuestion(
                    new HashMap<String, Object>(){{
                        put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                        put("Answer",userType.getText().toString()
                        );
                    }}
            );
        }
        else {
            Toast.makeText(getContext(), R.string.empty, Toast.LENGTH_LONG).show();
        }


    }
    @OnClick(R.id.imgBtn_previousP) void onPreQuestionClick() {

        LoginFragment.QUESTION_COUNTER --;
        LoginFragment.ISEND_FLAG = false;
        preQuestion(
                new HashMap<String, String>() {{
                    put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                }}
        );

    }
    @OnClick(R.id.imgBtn_back) void onBackClick() {
        JumpNextFragment(MedicalNumberFragment.newInstance() ,"Login");
    }
    public static UserTypeFragment newInstance(Bundle args) {
        UserTypeFragment fragment = new UserTypeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_type, container, false);
        ButterKnife.bind(this,view);
        tv_question.setText(bundle.getString("question"));
        tv_question_number.setText(bundle.getString("question_number"));

        if(LoginFragment.QUESTION_COUNTER == 1) {
            previousP.setVisibility(View.INVISIBLE);
        }

        return view;
    }

//

}
