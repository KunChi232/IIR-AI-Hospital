package com.example.iir_ai_hospital;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iir_ai_hospital.questionObject.Question;
import com.example.iir_ai_hospital.server.HospitalQuestionServerRequest;
import com.example.iir_ai_hospital.server.HospitalServerClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.preQuestion;
import static com.example.iir_ai_hospital.server.HospitalQuestionServerRequest.endQuestion;
import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;

public class SignatureFragment extends Fragment {

    @BindView(R.id.signatureView) SignatureView signatureView;

    @OnClick(R.id.imgBtn_clear) void onClearClick() {
        Log.d("clear", "canvas clear");
        signatureView.clearCanvas();
    }
    @OnClick(R.id.imgBtn_back) void onBackClick() {
        JumpNextFragment(MedicalNumberFragment.newInstance(), "Login", "rl");
    }
    @OnClick(R.id.imgBtn_previousP) void onPreQuestionClick() {
        if(LoginFragment.QUESTION_COUNTER == 1) {
            JumpNextFragment(LoginFragment.newInstance(null) ,"Login", "rl");
        }
        else {
            LoginFragment.QUESTION_COUNTER --;
            preQuestion(
                    new HashMap<String, String>() {{
                        put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                    }}
            );
        }

    }

    @OnClick(R.id.imgBtn_send) void onSignatureSendClick() {
        String encoded = signatureView.saveImageToBase64();
        final int chunkSize = 2048;
        for (int i = 0; i < encoded.length(); i += chunkSize) {
            Log.d("signature", encoded.substring(i, Math.min(encoded.length(), i + chunkSize)));
        }
        if(encoded.trim().length() > 0){
            endQuestion(
                    new HashMap<String, String>() {{
                        put("uuid", MedicalNumberFragment.MEDICAL_NUMBER);
                        put("signature", encoded);
                    }}
            );
        }
        else {
            Toast.makeText(getContext(), R.string.signature_empty, Toast.LENGTH_LONG).show();
        }

    }

    public static SignatureFragment newInstance() {
        return new SignatureFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signature, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

//    private void preQuestion(Map<String, String> params) {
//        HospitalServerClient hospitalServerClient = HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
//        hospitalServerClient.preQuestion(params)
//                .enqueue(new Callback<JsonObject>() {
//                    @Override
//                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                        JsonObject responseObject = response.body();
//                        if(response.isSuccessful() && responseObject != null) {
//                            Question question = new Gson().fromJson(responseObject, Question.class);
//
//                            Log.d("startQuestion", question.getQuestion_type());
//                            if(question.getQuestion_type().equals("R")) {
//                                Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
//                                bundle.putString("question_number", question.getQuestion_number());
//                                JumpNextFragment(OptionFragment.newInstance(bundle), "R");
//                            }
//                            else if(question.getQuestion_type().equals("date")) {
//                                Bundle bundle = new Bundle();
//                                bundle.putStringArrayList("question", question.getQuestion(LoginFragment.CURRENT_LANG));
//                                bundle.putString("question_number", question.getQuestion_number());
//                                JumpNextFragment(UserTypeFragment.newInstance(bundle), "userType");
//                            }
//                            else if(question.getQuestion_type().equals("RS")) {
//                                Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion(LoginFragment.CURRENT_LANG).get(0));
//                                bundle.putStringArrayList("option", question.getOptions(LoginFragment.CURRENT_LANG));
//                                bundle.putString("question_number", question.getQuestion_number());
//                                JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "RS");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                    }
//                });
//    }

//    private void endQuestion(Map<String, String> params) {
//        HospitalServerClient hospitalServerClient =  HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
//        hospitalServerClient.endQuestion(params)
//                .enqueue(new Callback<JsonObject>() {
//                    @Override
//                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                        JumpNextFragment(LoginFragment.newInstance(), "Login");
//                    }
//
//                    @Override
//                    public void onFailure(Call<JsonObject> call, Throwable t) {
//                        Log.d("failed", t.getMessage());
//                    }
//                });
//    }

}
