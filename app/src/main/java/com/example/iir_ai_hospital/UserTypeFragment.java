package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
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

    @BindView(R.id.spinner_startday_year) Spinner startday_year;
    @BindView(R.id.spinner_startday_month) Spinner startday_month;
    @BindView(R.id.spinner_startday_day) Spinner startday_day;
    @BindView(R.id.spinner_endday_year) Spinner endday_year;
    @BindView(R.id.spinner_endday_month) Spinner endday_month;
    @BindView(R.id.spinner_endday_day) Spinner endday_day;
    @BindView(R.id.tv_startday) TextView tv_startday;
    @BindView(R.id.tv_endday) TextView tv_enday;
    @BindView(R.id.tv_question_number) TextView tv_question_number;

    @OnClick(R.id.imgBtn_nextP) void onNextProblemClick() {
        nextQuestion(
                new HashMap<String, String>(){{
                    put("uuid", LoginFragment.UUID);
                    put("Answer",
                            startday_year.getSelectedItem().toString() + startday_month.getSelectedItem().toString() + startday_day.getSelectedItem().toString() + "/" +
                                    endday_year.getSelectedItem().toString() + endday_month.getSelectedItem().toString() + endday_day.getSelectedItem().toString()
                            );
                }}
        );
    }
    @OnClick(R.id.imgBtn_previousP) void onPreQuestionClick() {
        if(LoginFragment.QUESTION_COUNTER == 1) {
            JumpNextFragment(LoginFragment.newInstance() ,"Login");
        }
        preQuestion(
                new HashMap<String, String>() {{
                    put("uuid", LoginFragment.UUID);
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
        initSpinner(1900);
        tv_startday.setText(Objects.requireNonNull(bundle.getStringArrayList("question")).get(0));
        tv_enday.setText(Objects.requireNonNull(bundle.getStringArrayList("question")).get(1));
        tv_question_number.setText(bundle.getString("question_number"));
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

                            LoginFragment.ISEND = question.getEnd();
                            if(LoginFragment.ISEND.equals("Y")) {
                                JumpNextFragment(LoginFragment.newInstance(), "Login");
                            }
                            else {
                                if (question.getQuestion_type().equals("options")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
                                    bundle.putString("question_number", question.getQuestion_number());
                                    JumpNextFragment(OptionFragment.newInstance(bundle), "optionFrag");
                                } else if (question.getQuestion_type().equals("date")) {
                                    Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion());
                                    bundle.putStringArrayList("question", question.getQuestion(LoginFragment.CURRENT_LANG));
                                    bundle.putString("question_number", question.getQuestion_number());
                                    JumpNextFragment(UserTypeFragment.newInstance(bundle), "userType");
                                } else if (question.getQuestion_type().equals("multi-options")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
                                    bundle.putStringArrayList("option", question.getOptions(LoginFragment.CURRENT_LANG));
                                    bundle.putString("question_number", question.getQuestion_number());
                                    JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "multiChoice");
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
                            if(question.getQuestion_type().equals("options")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(OptionFragment.newInstance(bundle), "optionFrag");
                            }
                            else if(question.getQuestion_type().equals("date")) {
                                Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion().get(0));
                                bundle.putStringArrayList("question", question.getQuestion(LoginFragment.CURRENT_LANG));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(UserTypeFragment.newInstance(bundle), "userType");
                            }
                            else if(question.getQuestion_type().equals("multi-options")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
                                bundle.putStringArrayList("option", question.getOptions(LoginFragment.CURRENT_LANG));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "multiChoice");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }

    private void initSpinner(int minYear) {

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("month", String.valueOf(currentMonth));
        ArrayList<String> years = new ArrayList<String>();
        for (int i = currentYear; i >= minYear; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text_view, years);
        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        startday_year.setAdapter(yearAdapter);
        startday_year.setSelection(0);
        endday_year.setAdapter(yearAdapter);
        endday_year.setSelection(0);

        String[] months = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12"};

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text_view, months);
        monthAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        startday_month.setAdapter(monthAdapter);
        startday_month.setSelection(currentMonth);
        endday_month.setAdapter(monthAdapter);
        endday_month.setSelection(currentMonth);

        String[] days = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text_view, days);
        dayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        startday_day.setAdapter(dayAdapter);
        startday_day.setSelection(currentDay-1);
        endday_day.setAdapter(dayAdapter);
        endday_day.setSelection(currentDay-1);
    }
}
