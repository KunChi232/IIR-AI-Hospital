package com.example.iir_ai_hospital;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iir_ai_hospital.questionObject.Question;
import com.example.iir_ai_hospital.server.HospitalQuestionServerRequest;
import com.example.iir_ai_hospital.server.HospitalServerClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;
import static com.example.iir_ai_hospital.utils.Utils.setLocale;

public class MenuFragment extends Fragment {

    @OnClick(R.id.imgBtn_back) void onBackClick() {
        JumpNextFragment(MedicalNumberFragment.newInstance(), "MedicalNumber", "rl");
    }

    @OnClick(R.id.btn_patient_fill) void onPatientFillClick() {
//        JumpNextFragment(LoginFragment.newInstance(getArguments()), "Login");
        startFirstQuestion(                new HashMap<String, String>() {{
            put("id_number",MedicalNumberFragment.MEDICAL_NUMBER);
            put("birth", LoginFragment.BIRTH);
            put("name", LoginFragment.NAME);
            put("Chart_No", LoginFragment.CHART_NO);
            put("sex", LoginFragment.SEX);
            put("topic_id", "1");
        }});
    }
    @OnClick(R.id.btn_self_management) void onSelfManagementClick() {
        Bundle bundle = new Bundle();
        bundle.putString("patient_type", "normal");
        JumpNextFragment(SelfManagementFragment.newInstance(bundle), "SelfManage", "lr");
    }
    @OnClick(R.id.btn_medicalstaff_self_management) void onMedStaffSelfManagementClick() {
        Bundle bundle = new Bundle();
        bundle.putString("patient_type", "medical_staff");
        JumpNextFragment(SelfManagementFragment.newInstance(bundle), "SelfManage", "lr");
    }
//    @OnItemSelected(R.id.spinner_language) void onLanguageSelected() {
//
//        pref.edit().putInt("lang", spinnerLang.getSelectedItemPosition()).apply();
//
//        Log.d("Menu_onLanguageSelected", spinnerLang.getSelectedItem().toString());
//        String lang = spinnerLang.getSelectedItem().toString();
//        if(lang.equals("繁體中文")) {
//            switchLanguage("ch");
//        }
//        else if(lang.equals("English")) {
//            switchLanguage("en");
//        }
//    }
//    public static String CURRENT_LANG = "ch";
//    @BindView(R.id.spinner_language) Spinner spinnerLang;
//    private SharedPreferences pref;
    public static MenuFragment newInstance(Bundle args) {
        MenuFragment f = new MenuFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        pref = getContext().getSharedPreferences("language", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);
//        initSpinner();
        return view;
    }

    private void startFirstQuestion(Map<String, String> params) {
        Log.d("startQuestion", "send request");
        HospitalServerClient hospitalServerClient = HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
        hospitalServerClient.startQuestion(params)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject responseObject = response.body();
                        if(response.isSuccessful() && responseObject != null) {
                            Question question = new Gson().fromJson(responseObject, Question.class);
                            LoginFragment.UUID = question.getUuid();
                            LoginFragment.ISEND = question.getEnd();
                            LoginFragment.QUESTION_COUNTER ++;
                            if(question.getQuestion_type().equals("R")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion(MainActivity.CURRENT_LANG).get(0));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(OptionFragment.newInstance(bundle), "R", "lr");
                            }
                            else if(question.getQuestion_type().equals("T")) {
                                Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion());
                                bundle.putString("question", question.getQuestion(MainActivity.CURRENT_LANG).get(0));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(UserTypeFragment.newInstance(bundle), "T", "lr");
                            }
                            else if(question.getQuestion_type().equals("RS")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion(MainActivity.CURRENT_LANG).get(0));
                                bundle.putStringArrayList("option", question.getOptions(MainActivity.CURRENT_LANG));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "RS", "lr");
                            }
                            else if(question.getQuestion_type().equals("D")) {
                                Bundle bundle = new Bundle();
                                bundle.putStringArrayList("question", question.getQuestion(MainActivity.CURRENT_LANG));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(DateFragment.newInstance(bundle), "D", "lr");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }

//
//    private void initSpinner() {
//
//        String[] language = new String[] {"繁體中文" , "English"};
//        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text_view, language);
//        langAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
//        spinnerLang.setAdapter(langAdapter);
//
//        spinnerLang.setSelection(pref.getInt("lang",0)); // 0:ch , 1:en
//
//    }
//    public void switchLanguage(String lang) {
//        if(this.CURRENT_LANG.equals(lang)){
//            return;
//        }
//        this.CURRENT_LANG = lang;
//        setLocale(lang);
//    }
}
