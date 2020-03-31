package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.medicalNumberToProfile;
public class MedicalNumberFragment extends Fragment {
    public static String MEDICAL_NUMBER = "test_medical_number";
    @OnClick(R.id.imgBtn_toMenu) void onToMenuClick() {
        if(medicalNumber.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "請輸入病人身分證字號", Toast.LENGTH_LONG).show();
        } else {
            medicalNumberToProfile(
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_record, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
