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

    @OnClick(R.id.btn_positive) void onPositiveClick() {
        Log.d("Positive", "click");
        Log.d("Positive", LoginFragment.UUID);
        robotAPI.robot.stopSpeak();
        nextQuestion(
                new HashMap<String, String>() {{
                    put("uuid", LoginFragment.UUID);
                    put("answer", "1");
                }}
        );
    }
    @OnClick(R.id.btn_negative) void onNegativeClick() {
        robotAPI.robot.stopSpeak();
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
                            Log.d("startQuestion", question.getQuestion_type());
                            if(question.getQuestion_type().equals("options")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion().get(0));
                                JumpNextFragment(OptionFragment.newInstance(bundle), "optionFrag");
                            }
                            else if(question.getQuestion_type().equals("date")) {
                                Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion());
                                bundle.putStringArrayList("question", question.getQuestion());
                                JumpNextFragment(UserTypeFragment.newInstance(bundle), "userType");
                            }
                            else if(question.getQuestion_type().equals("multi-options")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion().get(0));
                                bundle.putStringArrayList("option", question.getOptions());
                                JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "multiChoice");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }
}
