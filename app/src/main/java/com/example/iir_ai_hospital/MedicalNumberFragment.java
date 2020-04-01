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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.getMedicalNumber;
import static com.example.iir_ai_hospital.utils.Utils.mainActivity;
import static com.example.iir_ai_hospital.utils.Utils.setLocale;

public class MedicalNumberFragment extends Fragment {

    public static String MEDICAL_NUMBER = "test_medical_number";

        @OnItemSelected(R.id.spinner_language) void onLanguageSelected() {

        pref.edit().putInt("lang", spinnerLang.getSelectedItemPosition()).apply();

        Log.d("Menu_onLanguageSelected", spinnerLang.getSelectedItem().toString());
        String lang = spinnerLang.getSelectedItem().toString();
        if(lang.equals("繁體中文")) {
            switchLanguage("ch");
        }
        else if(lang.equals("English")) {
            switchLanguage("en");
        }
    }
    @BindView(R.id.spinner_language) Spinner spinnerLang;
    private SharedPreferences pref;


    @OnClick(R.id.imgBtn_toMenu) void onToMenuClick() {
        if(medicalNumber.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "請輸入病人身分證字號", Toast.LENGTH_LONG).show();
        } else {
            getMedicalNumber(
                    new HashMap<String, String>() {{
                        put("id_number", MEDICAL_NUMBER = medicalNumber.getText().toString());
                    }}
            );
        }
    }
    @BindView(R.id.et_medical_number) EditText medicalNumber;
    public static MedicalNumberFragment newInstance() {
        return new MedicalNumberFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getContext().getSharedPreferences("language", Context.MODE_PRIVATE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_record, container, false);
        ButterKnife.bind(this, view);
        initSpinner();
        return view;
    }

        private void initSpinner() {

        String[] language = new String[] {"繁體中文" , "English"};
        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text_view, language);
        langAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerLang.setAdapter(langAdapter);

        spinnerLang.setSelection(pref.getInt("lang",0)); // 0:ch , 1:en

    }
    public void switchLanguage(String lang) {
        if(MainActivity.CURRENT_LANG.equals(lang)){
            return;
        }
        MainActivity.CURRENT_LANG = lang;
        setLocale(lang);
    }
}
