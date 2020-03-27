package com.example.iir_ai_hospital.server;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HospitalServerClient {
    public String base_url = "http://140.116.247.163:8000";

    @POST("covid/signIn")
    Call<JsonObject> signIn(@Body Map<String, String> params);
    @POST("covid/new_startQuestion")
    Call<JsonObject> startQuestion(@Body Map<String, String> params);
    @POST("covid/nextQuestion")
    Call<JsonObject> nextQuestion(@Body Map<String, Object> params);
    @POST("covid/preQuestion")
    Call<JsonObject> preQuestion(@Body Map<String, String> params);
    @POST("covid/endQuestion")
    Call<JsonObject> endQuestion(@Body Map<String, String> params);
    @POST("covid/selfManage")
    Call<JsonObject> selfManage(@Body Map<String,String> params);
    @POST("covid/patientProfile")
    Call<JsonObject> patientProfile(@Body Map<String, String> params);
}
