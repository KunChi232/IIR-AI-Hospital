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

import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.sendUserProfile;
import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;
public class SelfManagementFragment extends Fragment {
    @OnClick(R.id.imgBtn_sendProfile) void onProfileSend() {
        if(userName.getText().toString().trim().length() == 0 || userId.getText().toString().trim().length() == 0 || userAddr.getText().toString().trim().length() == 0
                || userTelHome.getText().toString().trim().length() == 0 || userTelMobile.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), R.string.empty, Toast.LENGTH_LONG).show();
        } else {
            sendUserProfile(
                    new HashMap<String, String>() {{
                        put("name", userName.getText().toString());
                        put("id_number", userId.getText().toString());
                        put("address", userAddr.getText().toString());
                        put("tel_phone", userTelHome.getText().toString());
                        put("tel_mobile", userTelMobile.getText().toString());
                    }}
            );
        }
    }
    @OnClick(R.id.imgBtn_back) void onBackClick() {
        JumpNextFragment(MenuFragment.newInstance(), "Menu");
    }
    public static SelfManagementFragment newInstance() {
        return new SelfManagementFragment();
    }
    @BindView(R.id.et_userName) EditText userName;
    @BindView(R.id.et_userId) EditText userId;
    @BindView(R.id.et_userAddr) EditText userAddr;
    @BindView(R.id.et_userTelHome) EditText userTelHome;
    @BindView(R.id.et_userTelMobile) EditText userTelMobile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_management, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
