package com.example.iir_ai_hospital;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
                        put("Chart_No", LoginFragment.CHART_NO);
                    }}
            );
        }
    }
    @OnClick(R.id.imgBtn_back) void onBackClick() {
        JumpNextFragment(MedicalNumberFragment.newInstance(), "Menu", "rl");
    }
    public static SelfManagementFragment newInstance(Bundle args) {
        SelfManagementFragment f = new SelfManagementFragment();
        f.setArguments(args);
        return f;
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

        initAlertDialog();
        return view;
    }

    private void initAlertDialog() {
        Bundle bundle = getArguments();
        int confrim;
        if(bundle.get("patient_type").equals("normal")) {
            confrim = R.string.patient_rule;
        } else {
            confrim = R.string.meidcal_staff_rule;
        }

        ViewDialog alert = new ViewDialog();
        alert.showDialog(confrim);
    }

    private class ViewDialog {

        public void showDialog(int stringResource){
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.rule_alert_layout);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            ImageButton dialogButton = dialog.findViewById(R.id.imgBtn_confrim);
            TextView rule = dialog.findViewById(R.id.tv_rule);
            rule.setText(stringResource);
            rule.setMovementMethod(new ScrollingMovementMethod());
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
