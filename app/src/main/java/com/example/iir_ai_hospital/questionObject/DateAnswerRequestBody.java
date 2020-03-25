package com.example.iir_ai_hospital.questionObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateAnswerRequestBody {
    @SerializedName("0")
    @Expose
    String member1;
    @SerializedName("1")
    @Expose
    String member2;

    public DateAnswerRequestBody(String member1, String member2) {
        this.member1 = member1;
        this.member2 = member2;
    }
    public DateAnswerRequestBody(String member1) {
        this.member1 = member1;
    }
}
