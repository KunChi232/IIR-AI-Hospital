package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.util.Log;
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

//    private void nextQuestion(Map<String, String> params) {
//        HospitalServerClient hospitalServerClient = HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
//        hospitalServerClient.nextQuestion(params)
//                .enqueue(new Callback<JsonObject>() {
//                    @Override
//                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                        JsonObject responseObject = response.body();
//                        if(response.isSuccessful() && responseObject != null) {
//                            Question question = new Gson().fromJson(responseObject, Question.class);
//
//                            if(LoginFragment.ISEND_FLAG){
//                                Log.d("Jump", "Jump to sign fragment");
//                                JumpNextFragment(SignatureFragment.newInstance(), "sign");
//                            }
//                            else {
//
//                                LoginFragment.ISEND = question.getEnd();
//                                LoginFragment.QUESTION_COUNTER ++;
//
//                                if (LoginFragment.ISEND.equals("Y")) {
//                                    LoginFragment.ISEND_FLAG = true;
//                                }
//                                if (question.getQuestion_type().equals("R")) {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
//                                    bundle.putString("question_number", question.getQuestion_number());
//                                    JumpNextFragment(OptionFragment.newInstance(bundle), "R");
//                                } else if (question.getQuestion_type().equals("T")) {
//                                    Bundle bundle = new Bundle();
////                                bundle.putString("question", question.getQuestion());
//                                    bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
//                                    bundle.putString("question_number", question.getQuestion_number());
//                                    JumpNextFragment(UserTypeFragment.newInstance(bundle), "T");
//                                } else if (question.getQuestion_type().equals("RS")) {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
//                                    bundle.putStringArrayList("option", question.getOptions(LoginFragment.CURRENT_LANG));
//                                    bundle.putString("question_number", question.getQuestion_number());
//                                    JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "RS");
//                                }
//                                else if(question.getQuestion_type().equals("D")) {
//                                    Log.d("Date","jump to Date");
//                                    Bundle bundle = new Bundle();
//                                    bundle.putStringArrayList("question", question.getQuestion(LoginFragment.CURRENT_LANG));
//                                    bundle.putString("question_number", question.getQuestion_number());
//                                    JumpNextFragment(DateFragment.newInstance(bundle), "D");
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                    }
//                });
//    }
//
//    private void preQuestion(Map<String, String> params) {
//        HospitalServerClient hospitalServerClient = HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
//        hospitalServerClient.preQuestion(params)
//                .enqueue(new Callback<JsonObject>() {
//                    @Override
//                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                        JsonObject responseObject = response.body();
//                        if(response.isSuccessful() && responseObject != null) {
//                            Question question = new Gson().fromJson(responseObject, Question.class);
//
//                            Log.d("startQuestion", question.getQuestion_type());
//                            if(question.getQuestion_type().equals("R")) {
//                                Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
//                                bundle.putString("question_number", question.getQuestion_number());
//                                JumpNextFragment(OptionFragment.newInstance(bundle), "R");
//                            }
//                            else if(question.getQuestion_type().equals("T")) {
//                                Bundle bundle = new Bundle();
////                                bundle.putString("question", question.getQuestion().get(0));
//                                bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
//                                bundle.putString("question_number", question.getQuestion_number());
//                                JumpNextFragment(UserTypeFragment.newInstance(bundle), "T");
//                            }
//                            else if(question.getQuestion_type().equals("RS")) {
//                                Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
//                                bundle.putStringArrayList("option", question.getOptions(LoginFragment.CURRENT_LANG));
//                                bundle.putString("question_number", question.getQuestion_number());
//                                JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "RS");
//                            }
//                            else if(question.getQuestion_type().equals("D")) {
//                                Bundle bundle = new Bundle();
//                                bundle.putStringArrayList("question", question.getQuestion(LoginFragment.CURRENT_LANG));
//                                bundle.putString("question_number", question.getQuestion_number());
//                                JumpNextFragment(DateFragment.newInstance(bundle), "D");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                    }
//                });
//    }

}
