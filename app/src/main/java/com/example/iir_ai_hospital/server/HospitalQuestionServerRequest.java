package com.example.iir_ai_hospital.server;

import android.os.Bundle;
import android.util.Log;

import com.example.iir_ai_hospital.DateFragment;
import com.example.iir_ai_hospital.LoginFragment;
import com.example.iir_ai_hospital.MenuFragment;
import com.example.iir_ai_hospital.MultiChoiceFragment;
import com.example.iir_ai_hospital.OptionFragment;
import com.example.iir_ai_hospital.SignatureFragment;
import com.example.iir_ai_hospital.UserTypeFragment;
import com.example.iir_ai_hospital.questionObject.Question;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;

public class HospitalQuestionServerRequest {
    public static HospitalQuestionServerRequest mInstance = new HospitalQuestionServerRequest();
    HospitalServerClient retrofitInterface;

    public HospitalQuestionServerRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HospitalServerClient.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(HospitalServerClient.class);
    }

    public static HospitalQuestionServerRequest getInstance() {
        if (mInstance == null) {
            mInstance = new HospitalQuestionServerRequest();
        }
        return mInstance;
    }

    public HospitalServerClient getRetrofitInterface() {
        return retrofitInterface;
    }

    public static void nextQuestion(Map<String, Object> params) {
        Log.d("params", params.toString());
        HospitalServerClient hospitalServerClient = HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
        hospitalServerClient.nextQuestion(params)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject responseObject = response.body();
                        if(response.isSuccessful() && responseObject != null) {
                            Question question = new Gson().fromJson(responseObject, Question.class);

                            if(LoginFragment.ISEND_FLAG){
                                Log.d("Jump", "Jump to sign fragment");
                                JumpNextFragment(SignatureFragment.newInstance(), "sign");
                            }
                            else {

                                LoginFragment.ISEND = question.getEnd();
                                LoginFragment.QUESTION_COUNTER ++;

                                if (LoginFragment.ISEND.equals("Y")) {
                                    LoginFragment.ISEND_FLAG = true;
                                }
                                if (question.getQuestion_type().equals("R")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("question", question.getQuestion(MenuFragment.CURRENT_LANG).get(0));
                                    bundle.putString("question_number", question.getQuestion_number());
                                    JumpNextFragment(OptionFragment.newInstance(bundle), "R");
                                } else if (question.getQuestion_type().equals("T")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("question", question.getQuestion(MenuFragment.CURRENT_LANG).get(0));
                                    bundle.putString("question_number", question.getQuestion_number());
                                    JumpNextFragment(UserTypeFragment.newInstance(bundle), "T");
                                } else if (question.getQuestion_type().equals("RS")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("question", question.getQuestion(MenuFragment.CURRENT_LANG).get(0));
                                    bundle.putStringArrayList("option", question.getOptions(MenuFragment.CURRENT_LANG));
                                    bundle.putString("question_number", question.getQuestion_number());
                                    JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "RS");
                                }
                                else if(question.getQuestion_type().equals("D")) {
                                    Log.d("Date","jump to Date");
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArrayList("question", question.getQuestion(MenuFragment.CURRENT_LANG));
                                    bundle.putString("question_number", question.getQuestion_number());
                                    JumpNextFragment(DateFragment.newInstance(bundle), "D");
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }

    public static void preQuestion(Map<String, String> params) {
        HospitalServerClient hospitalServerClient = HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
        hospitalServerClient.preQuestion(params)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject responseObject = response.body();
                        if(response.isSuccessful() && responseObject != null) {
                            Question question = new Gson().fromJson(responseObject, Question.class);

                            Log.d("startQuestion", question.getQuestion_type());
                            if(question.getQuestion_type().equals("R")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion(MenuFragment.CURRENT_LANG).get(0));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(OptionFragment.newInstance(bundle), "R");
                            }
                            else if(question.getQuestion_type().equals("T")) {
                                Bundle bundle = new Bundle();
//                                bundle.putString("question", question.getQuestion().get(0));
                                bundle.putString("question", question.getQuestion(MenuFragment.CURRENT_LANG).get(0));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(UserTypeFragment.newInstance(bundle), "T");
                            }
                            else if(question.getQuestion_type().equals("RS")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("question", question.getQuestion(MenuFragment.CURRENT_LANG).get(0));
                                bundle.putStringArrayList("option", question.getOptions(MenuFragment.CURRENT_LANG));
                                bundle.putString("question_number", question.getQuestion_number());
                                JumpNextFragment(MultiChoiceFragment.newInstance(bundle), "RS");
                            }
                            else if(question.getQuestion_type().equals("D")) {
                                Bundle bundle = new Bundle();
                                bundle.putStringArrayList("question", question.getQuestion(MenuFragment.CURRENT_LANG));
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
    public static void endQuestion(Map<String, String> params) {
        HospitalServerClient hospitalServerClient =  HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
        hospitalServerClient.endQuestion(params)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JumpNextFragment(LoginFragment.newInstance(), "Login");
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("failed", t.getMessage());
                    }
                });
    }
    public static void sendUserProfile(Map<String, String> params) {
        HospitalServerClient hospitalServerClient = HospitalQuestionServerRequest.getInstance().getRetrofitInterface();
        hospitalServerClient.selfManage(params)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject responseObject = response.body();
                        if(response.isSuccessful() && responseObject != null) {
                            JumpNextFragment(MenuFragment.newInstance(), "Menu");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }
}
