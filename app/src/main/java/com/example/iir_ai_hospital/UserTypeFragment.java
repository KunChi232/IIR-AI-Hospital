package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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
    @OnClick(R.id.imgBtn_nextP) void onNextProblemClick() {
        nextQuestion(
                new HashMap<String, String>(){{
                    put("uuid", LoginFragment.UUID);
                    put("Answer",userType.getText().toString()
                            );
                }}
        );
    }
    @OnClick(R.id.imgBtn_previousP) void onPreQuestionClick() {
        if(LoginFragment.QUESTION_COUNTER == 1) {
            JumpNextFragment(LoginFragment.newInstance() ,"Login");
        }
        else{
            LoginFragment.QUESTION_COUNTER --;
            LoginFragment.ISEND_FLAG = false;
            preQuestion(
                    new HashMap<String, String>() {{
                        put("uuid", LoginFragment.UUID);
                    }}
            );
        }
    }
    @OnClick(R.id.imgBtn_back) void onBackClick() {
        JumpNextFragment(LoginFragment.newInstance() ,"Login");
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
        return view;
    }

//

}
