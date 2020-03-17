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

import com.example.iir_ai_hospital.questionObject.Question;
import com.example.iir_ai_hospital.server.HospitalQuestionServerRequest;
import com.example.iir_ai_hospital.server.HospitalServerClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;

public class UserTypeFragment extends Fragment {

    private Bundle bundle;

    @BindView(R.id.et_startday_year) EditText startday_year;
    @BindView(R.id.et_startday_month) EditText startday_month;
    @BindView(R.id.et_startday_day) EditText startday_day;
    @BindView(R.id.et_endday_year) EditText endday_year;
    @BindView(R.id.et_endday_month) EditText endday_month;
    @BindView(R.id.et_endday_day) EditText endday_day;
    @BindView(R.id.tv_startday) TextView tv_startday;
    @BindView(R.id.tv_endday) TextView tv_enday;

    @OnClick(R.id.imgBtn_nextP) void onNextProblemClick() {
        nextQuestion(
                new HashMap<String, String>(){{
                    put("uuid", LoginFragment.UUID);
                    put("Answer",
                            startday_year.getText().toString() + startday_month.getText().toString() + startday_day.getText().toString() + "/" +
                                    endday_year.getText().toString() + endday_month.getText().toString() + endday_day.getText().toString()
                            );
                }}
        );
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

        tv_startday.setText(Objects.requireNonNull(bundle.getStringArrayList("question")).get(0));
        tv_enday.setText(Objects.requireNonNull(bundle.getStringArrayList("question")).get(1));
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
                            if(question.getEnd().equals("Y")){
                                JumpNextFragment(LoginFragment.newInstance(), "Login");
                            }
                            Log.d("startQuestion", question.getQuestion_type());
                            if(question.getQuestion_type().equals("options") ){
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion().get(0));
                                JumpNextFragment(OptionFragment.newInstance(bundle), "optionFrag");
                            }
                            else if(question.getQuestion_type().equals("date")) {
                                Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion().get(0));
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
