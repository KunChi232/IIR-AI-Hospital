package com.example.iir_ai_hospital;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.iir_ai_hospital.utils.Utils.PopBackFragment;
import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;
import static com.example.iir_ai_hospital.utils.Utils.setLocale;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    public static String NAME = "珊迪";
    public static String ID = "A12345678";
    public static String BIRTH = "19850101";
    public static String CHART_NO;
    public static String SEX;
    public static String UUID ;
    public static String ISEND = "N";
//    public static String CURRENT_LANG = "ch";
    public static int QUESTION_COUNTER = 0;
    public static boolean ISEND_FLAG = false;
    private SharedPreferences pref;
    @BindView(R.id.tv_login_name) TextView userName;
    @BindView(R.id.tv_login_id) TextView userID;
    @BindView(R.id.tv_login_birth) TextView userBirth;
//    @BindView(R.id.spinner_language) Spinner spinnerLanguage;

    @OnClick(R.id.imgBtn_next) void onNextClick() {
//        signIn(
//                new HashMap<String, String>() {{
//                    put("id_number", userID.getText().toString());
//                    put("name", userName.getText().toString());
//                    put("birth", userBirthYear.getText().toString() + userBirthMonth.getText().toString() + userBirthDay.getText().toString());
//                }}
//        );
//            JumpNextFragment(SignatureFragment.newInstance(), "Signature");
//        startFirstQuestion(
////                new HashMap<String, String>() {{
////                    put("id_number", userID.getText().toString());
////                    put("name", userName.getText().toString());
////                    put("birth", userBirthYear.getSelectedItem().toString() + userBirthMonth.getSelectedItem().toString() + userBirthDay.getSelectedItem().toString());
////                }}
//                new HashMap<String, String>() {{
//                    put("id_number",MedicalNumberFragment.MEDICAL_NUMBER);
//                    put("birth", BIRTH);
//                    put("name", NAME);
//                    put("birth", BIRTH);
//                    put("Chart_No", CHART_NO);
//                    put("sex", SEX);
//                    put("topic_id", "1");
//                }}
//        );

        JumpNextFragment(MenuFragment.newInstance(null), "Menu");
    }

    @OnClick(R.id.imgBtn_back) void onBackClick() {
        JumpNextFragment(MedicalNumberFragment.newInstance(), "MedicalNumber");
    }

//    @OnItemSelected(R.id.spinner_language) void onLanguageSelected() {
//
//        pref.edit().putInt("lang", spinnerLanguage.getSelectedItemPosition()).apply();
//
//        Log.d("onLanguageSelected", spinnerLanguage.getSelectedItem().toString());
//        String lang = spinnerLanguage.getSelectedItem().toString();
//        if(lang.equals("繁體中文")) {
//            switchLanguage("ch");
//        }
//        else if(lang.equals("English")) {
//            switchLanguage("en");
//        }
//    }


    public static LoginFragment newInstance(Bundle args) {
        LoginFragment f = new LoginFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        NAME = getArguments().getString("Patient_Name");
        ID = getArguments().getString("Patient_Id");
        BIRTH = getArguments().getString("Patient_Birth").replace("-", "/");
        CHART_NO = getArguments().getString("Chart_No");
        SEX = getArguments().getString("sex");
//        pref = getContext().getSharedPreferences("language", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        try{
            userName.setText(NAME);
            userBirth.setText(BIRTH);
            userID.setText(ID);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }


        return view;
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
                            ISEND = question.getEnd();
                            QUESTION_COUNTER ++;
                            if(question.getQuestion_type().equals("R")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion(MainActivity.CURRENT_LANG).get(0));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(OptionFragment.newInstance(bundle), "R");
                            }
                            else if(question.getQuestion_type().equals("T")) {
                                Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion());
                                bundle.putString("question", question.getQuestion(MainActivity.CURRENT_LANG).get(0));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(UserTypeFragment.newInstance(bundle), "T");
                            }
                            else if(question.getQuestion_type().equals("RS")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion(MainActivity.CURRENT_LANG).get(0));
                                bundle.putStringArrayList("option", question.getOptions(MainActivity.CURRENT_LANG));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "RS");
                            }
                            else if(question.getQuestion_type().equals("D")) {
                                Bundle bundle = new Bundle();
                                bundle.putStringArrayList("question", question.getQuestion(MainActivity.CURRENT_LANG));
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

//    private void initSpinner(int minYear) {
//
//        Calendar calendar = Calendar.getInstance();
//        int currentYear = calendar.get(Calendar.YEAR);
//        int currentMonth = calendar.get(Calendar.MONTH);
//        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//        Log.d("month", String.valueOf(currentMonth));
//        ArrayList<String> years = new ArrayList<String>();
//        for (int i = currentYear; i >= minYear; i--) {
//            years.add(Integer.toString(i));
//        }
//        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text_view, years);
//        yearAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
//        userBirthYear.setAdapter(yearAdapter);
//        userBirthYear.setSelection(0);
//
//        String[] months = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12"};
//
//        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text_view, months);
//        monthAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
//        userBirthMonth.setAdapter(monthAdapter);
//        userBirthMonth.setSelection(currentMonth);
//
//        String[] days = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
//        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text_view, days);
//        dayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
//        userBirthDay.setAdapter(dayAdapter);
//        userBirthDay.setSelection(currentDay-1);
//
////        String[] language = new String[] {"繁體中文" , "English"};
////        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text_view, language);
////        langAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
////        spinnerLanguage.setAdapter(langAdapter);
////
////        spinnerLanguage.setSelection(pref.getInt("lang",0)); // 0:ch , 1:en
//
//    }
//
////    public void switchLanguage(String lang) {
////        if(CURRENT_LANG.equals(lang)){
////            return;
////        }
////        CURRENT_LANG = lang;
////        setLocale(lang);
////    }
}
