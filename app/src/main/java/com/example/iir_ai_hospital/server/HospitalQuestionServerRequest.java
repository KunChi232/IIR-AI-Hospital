package com.example.iir_ai_hospital.server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
}
