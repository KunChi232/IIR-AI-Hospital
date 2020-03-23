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

import com.example.iir_ai_hospital.questionObject.Question;
import com.example.iir_ai_hospital.server.HospitalQuestionServerRequest;
import com.example.iir_ai_hospital.server.HospitalServerClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;
import static com.example.iir_ai_hospital.utils.Utils.robotAPI;

public class OptionFragment extends Fragment {
    private Bundle bundle;

    @BindView(R.id.tv_question) TextView questions;
    @BindView(R.id.tv_question_number) TextView tv_question_number;
    @OnClick(R.id.imgBtn_positive) void onPositiveClick() {
        Log.d("Positive", "click");
        Log.d("Positive", LoginFragment.UUID);
        nextQuestion(
                new HashMap<String, String>() {{
                    put("uuid", LoginFragment.UUID);
                    put("answer", "1");
                }}
        );
    }
    @OnClick(R.id.imgBtn_negative) void onNegativeClick() {
        nextQuestion(
                new HashMap<String, String>() {{
                    put("uuid", LoginFragment.UUID);
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
                        put("uuid", LoginFragment.UUID);
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

    private void nextQuestion(Map<String, String> params) {
        HospitalServerClient hospitalServerClient = HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
        hospitalServerClient.nextQuestion(params)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject responseObject = response.body();
                        if(response.isSuccessful() && responseObject != null) {
                            Question question = new Gson().fromJson(responseObject, Question.class);

                            if(LoginFragment.ISEND_FLAG){
                                Log.d("Jump", "Jump to sign fragment");
                                JumpNextFragment(SignatureFragment.newInstance(), "sign");
                            }
                            else {

                                LoginFragment.ISEND = question.getEnd();
                                LoginFragment.QUESTION_COUNTER ++;

                                if (LoginFragment.ISEND.equals("Y")) {
                                    LoginFragment.ISEND_FLAG = true;
                                }
                                if (question.getQuestion_type().equals("R")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
                                    bundle.putString("question_number", question.getQuestion_number());
                                    JumpNextFragment(OptionFragment.newInstance(bundle), "R");
                                } else if (question.getQuestion_type().equals("T")) {
                                    Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion());
                                    bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
                                    bundle.putString("question_number", question.getQuestion_number());
                                    JumpNextFragment(UserTypeFragment.newInstance(bundle), "T");
                                } else if (question.getQuestion_type().equals("RS")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
                                    bundle.putStringArrayList("option", question.getOptions(LoginFragment.CURRENT_LANG));
                                    bundle.putString("question_number", question.getQuestion_number());
                                    JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "RS");
                                }
                                else if(question.getQuestion_type().equals("D")) {
                                    Log.d("Date","jump to Date");
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArrayList("question", question.getQuestion(LoginFragment.CURRENT_LANG));
                                    bundle.putString("question_number", question.getQuestion_number());
                                    JumpNextFragment(DateFragment.newInstance(bundle), "D");
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }

    private void preQuestion(Map<String, String> params) {
        HospitalServerClient hospitalServerClient = HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
        hospitalServerClient.preQuestion(params)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject responseObject = response.body();
                        if(response.isSuccessful() && responseObject != null) {
                            Question question = new Gson().fromJson(responseObject, Question.class);

                            Log.d("startQuestion", question.getQuestion_type());
                            if(question.getQuestion_type().equals("R")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(OptionFragment.newInstance(bundle), "R");
                            }
                            else if(question.getQuestion_type().equals("T")) {
                                Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion().get(0));
                                bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(UserTypeFragment.newInstance(bundle), "T");
                            }
                            else if(question.getQuestion_type().equals("RS")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
                                bundle.putStringArrayList("option", question.getOptions(LoginFragment.CURRENT_LANG));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "RS");
                            }
                            else if(question.getQuestion_type().equals("D")) {
                                Bundle bundle = new Bundle();
                                bundle.putStringArrayList("question", question.getQuestion(LoginFragment.CURRENT_LANG));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(DateFragment.newInstance(bundle), "D");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }

}
