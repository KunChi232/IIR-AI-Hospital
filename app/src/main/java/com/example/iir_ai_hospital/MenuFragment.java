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

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;
import static com.example.iir_ai_hospital.utils.Utils.setLocale;

public class MenuFragment extends Fragment {

    @OnClick(R.id.btn_patient_fill) void onPatientFillClick() {
        JumpNextFragment(LoginFragment.newInstance(), "Login");
    }
    @OnClick(R.id.btn_self_management) void onSelfManagementClick() {
        JumpNextFragment(SelfManagementFragment.newInstance(), "SelfManage");
    }
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
    public static String CURRENT_LANG = "ch";
    @BindView(R.id.spinner_language) Spinner spinnerLang;
    private SharedPreferences pref;
    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getContext().getSharedPreferences("language", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);
        initSpinner();
        return view;
    }
//
    private void initSpinner() {

        String[] language = new String[] {"繁體中文" , "English"};
        ArrayAdapter<String> langAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text_view, language);
        langAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerLang.setAdapter(langAdapter);

        spinnerLang.setSelection(pref.getInt("lang",0)); // 0:ch , 1:en

    }
    public void switchLanguage(String lang) {
        if(this.CURRENT_LANG.equals(lang)){
            return;
        }
        this.CURRENT_LANG = lang;
        setLocale(lang);
    }
}
