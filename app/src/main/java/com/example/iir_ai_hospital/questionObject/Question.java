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
    @SerializedName("question_number")
    String question_number;

    @Expose
    @SerializedName("end")
    String end;

    public Question(String uuid,String question_type, ArrayList<String> question, ArrayList<String> question_en, ArrayList<String> options, ArrayList<String> options_en, String question_number,String end) {
        this.uuid = uuid;
        this.question_type = question_type;
        this.question = question;
        this.question_en = question_en;
        this.options = options;
        this.options_en = options_en;
        this.question_number = question_number;
        this.end = end;

    }

    public  String getUuid() {
        return uuid;
    }

    public String getQuestion_type() {
        return question_type;
    }
    public ArrayList<String> getQuestion(String lang) {
        if(lang.equals("ch"))
            return question;
        else
            return question_en;
    }
    public ArrayList<String> getOptions(String lang) {
        if(lang.equals("ch"))
            return options;
        else
            return options_en;
    }
    public String getEnd() {
        return end;
    }
    public String getQuestion_number() {
        return question_number;
    }
}
