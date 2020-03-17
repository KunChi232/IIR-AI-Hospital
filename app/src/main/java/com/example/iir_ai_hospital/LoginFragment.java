package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.iir_ai_hospital.questionObject.Question;
import com.example.iir_ai_hospital.questionObject.User;
import com.example.iir_ai_hospital.server.HospitalQuestionServerRequest;
import com.example.iir_ai_hospital.server.HospitalServerClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

import static com.example.iir_ai_hospital.utils.Utils.PopBackFragment;
import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    public static String NAME = "珊迪";
    public static String ID = "A12345678";
    public static String BIRTH = "19850101";
    public static String UUID ;
    @BindView(R.id.et_userName) EditText userName;
    @BindView(R.id.et_userID) EditText userID;
    @BindView(R.id.et_userBirthYear) EditText userBirthYear;
    @BindView(R.id.et_userBirthMonth) EditText userBirthMonth;
    @BindView(R.id.et_userBirthDay) EditText userBirthDay;

    @OnClick(R.id.imgBtn_next) void onNextClick() {
//        signIn(
//                new HashMap<String, String>() {{
//                    put("id_number", userID.getText().toString());
//                    put("name", userName.getText().toString());
//                    put("birth", userBirthYear.getText().toString() + userBirthMonth.getText().toString() + userBirthDay.getText().toString());
//                }}
//        );

        startFirstQuestion(
//                new HashMap<String, String>() {{
//                    put("id_number", userID.getText().toString());
//                    put("name", userName.getText().toString());
//                    put("birth", userBirthYear.getText().toString() + userBirthMonth.getText().toString() + userBirthDay.getText().toString());
//                }}
                new HashMap<String, String>() {{
                    put("id_number", "A12345678");
                    put("name", "珊迪");
                    put("birth", "19850101");
                }}
        );
    }

    @OnClick(R.id.imgBtn_back) void onBackClick() {
        PopBackFragment();
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    private void signIn(Map<String, String>params) {
        Log.e("Login frag", "Sign In");
        HospitalServerClient hospitalServerClient = HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
        hospitalServerClient.signIn(params)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject responseObject = response.body();
                        if(response.isSuccessful() && responseObject != null) {
                            User user = new Gson().fromJson(responseObject, User.class);
                            UUID = user.getUuid();
                            Log.d("uuid", user.getUuid());
                            startFirstQuestion(
                                    new HashMap<String, String>(){{
                                        put("uuid", user.getUuid());
                                    }}
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("Login frag Failure", t.getMessage());
                        Log.e("Login frag Failure", t.toString());
                    }
                });
    }
    private void startFirstQuestion(Map<String, String>params) {
        Log.d("startQuestion", "send request");
        HospitalServerClient hospitalServerClient = HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
        hospitalServerClient.startQuestion(params)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject responseObject = response.body();
                        if(response.isSuccessful() && responseObject != null) {
                            Question question = new Gson().fromJson(responseObject, Question.class);
                            UUID = question.getUuid();
                            Log.d("startQuestion", question.getQuestion_type());
//                            Log.d("startQuestion", question.getQuestion());
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
