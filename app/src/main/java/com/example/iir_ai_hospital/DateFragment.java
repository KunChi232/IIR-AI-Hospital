package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iir_ai_hospital.questionObject.DateAnswerRequestBody;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;
import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.nextQuestion;
import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.preQuestion;

public class DateFragment extends Fragment {

    @BindView(R.id.spinner_startday_year) Spinner startday_year;
    @BindView(R.id.spinner_startday_month) Spinner startday_month;
    @BindView(R.id.spinner_startday_day) Spinner startday_day;
    @BindView(R.id.spinner_endday_year) Spinner endday_year;
    @BindView(R.id.spinner_endday_month) Spinner endday_month;
    @BindView(R.id.spinner_endday_day) Spinner endday_day;
    @BindView(R.id.tv_question) TextView tv_startday;
    @BindView(R.id.tv_endday) TextView tv_enday;
    @BindView(R.id.textView8) TextView tv_enday_year;
    @BindView(R.id.textView9) TextView tv_endday_month;
    @BindView(R.id.textView10) TextView tv_endday_day;
    @BindView(R.id.tv_question_number) TextView tv_question_number;
    @BindView(R.id.imgBtn_previousP) ImageButton previousP;

    private Bundle bundle;
    private int QUESTUON_NUMBER = 1;
    @OnClick(R.id.imgBtn_nextP) void onNextProblemClick() {
        if(QUESTUON_NUMBER > 1){
            DateAnswerRequestBody answerRequestBody = new DateAnswerRequestBody(
                    startday_year.getSelectedItem().toString() + "/" + startday_month.getSelectedItem().toString() + "/" + startday_day.getSelectedItem().toString(),
                    endday_year.getSelectedItem().toString() + "/" + endday_month.getSelectedItem().toString() + "/" + endday_day.getSelectedItem().toString()
            );
            nextQuestion(
                    new HashMap<String, Object>(){{
                        put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                        put("Answer",new Gson().toJson(answerRequestBody)

                        );
                    }}
            );
        }
        else {
            DateAnswerRequestBody answerRequestBody = new DateAnswerRequestBody(
                    startday_year.getSelectedItem().toString() + "/" + startday_month.getSelectedItem().toString() + "/" + startday_day.getSelectedItem().toString()
            );
            nextQuestion(
                    new HashMap<String, Object>(){{
                        put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                        put("Answer",new Gson().toJson(answerRequestBody));
                    }}
            );
        }
    }
    @OnClick(R.id.imgBtn_previousP) void onPreQuestionClick() {

        LoginFragment.QUESTION_COUNTER --;
        LoginFragment.ISEND_FLAG = false;
        preQuestion(
                new HashMap<String, String>() {{
                    put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                }}
        );

    }
    @OnClick(R.id.imgBtn_back) void onBackClick() {
        JumpNextFragment(MedicalNumberFragment.newInstance(), "Login", "rl");
    }
    public static DateFragment newInstance(Bundle args) {
        DateFragment f = new DateFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        QUESTUON_NUMBER = bundle.getStringArrayList("question").size();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date, container, false);
        ButterKnife.bind(this, view);

        tv_question_number.setText(bundle.getString("question_number"));
        initQuestion(bundle.getStringArrayList("question"));

        if(LoginFragment.QUESTION_COUNTER == 1) {
            previousP.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private void initQuestion(ArrayList<String> question) {
        if(QUESTUON_NUMBER > 1) {
            tv_startday.setText(question.get(0));
            tv_enday.setText(question.get(1));

            initSpinner(1900);
        }
        else {
            tv_startday.setText(question.get(0));
            initSpinner(1900);
            endday_year.setVisibility(View.INVISIBLE);
            endday_month.setVisibility(View.INVISIBLE);
            endday_day.setVisibility(View.INVISIBLE);
            tv_enday.setVisibility(View.INVISIBLE);
            tv_enday_year.setVisibility(View.INVISIBLE);
            tv_endday_month.setVisibility(View.INVISIBLE);
            tv_endday_day.setVisibility(View.INVISIBLE);

        }
    }
    private void initSpinner(int minYear) {

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(new Date(startCalendar.getTimeInMillis() - 1209600000L));
        int currentYear = startCalendar.get(Calendar.YEAR);
        int currentMonth = startCalendar.get(Calendar.MONTH);
        int currentDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        int twoWeekAgoMonth = endCalendar.get(Calendar.MONTH);
        int twoWeekAgoDay = endCalendar.get(Calendar.DAY_OF_MONTH);
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
        startday_month.setSelection(twoWeekAgoMonth);
        endday_month.setAdapter(monthAdapter);
        endday_month.setSelection(currentMonth);

        String[] days = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_text_view, days);
        dayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        startday_day.setAdapter(dayAdapter);
        startday_day.setSelection(twoWeekAgoDay-1);
        endday_day.setAdapter(dayAdapter);
        endday_day.setSelection(currentDay-1);
    }
}
