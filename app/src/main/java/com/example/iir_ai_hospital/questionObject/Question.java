package com.example.iir_ai_hospital.questionObject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Question {

    @Expose
    @SerializedName("uuid")
    String uuid;
    @Expose
    @SerializedName("question_type")
    String question_type;

    @Expose
    @SerializedName("question")
    ArrayList<String> question;

    @Expose
    @SerializedName("question_en")
    ArrayList<String> question_en;

    @Expose
    @SerializedName("options")
    ArrayList<String> options;

    @Expose
    @SerializedName("options_en")
    ArrayList<String> options_en;

    @Expose
    @SerializedName("end")
    String end;

    public Question(String uuid,String question_type, ArrayList<String> question, ArrayList<String> question_en, ArrayList<String> options, ArrayList<String> options_en, String end) {
        this.uuid = uuid;
        this.question_type = question_type;
        this.question = question;
        this.question_en = question_en;
        this.options = options;
        this.options_en = options_en;
        this.end = end;

    }

    public  String getUuid() {
        return uuid;
    }

    public String getQuestion_type() {
        return question_type;
    }
    public ArrayList<String> getQuestion() {
        return question;
    }
    public ArrayList<String> getOptions() {
        return options;
    }
    public String getEnd() {
        return end;
    }
}
