package com.example.iir_ai_hospital.utils;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.asus.robotframework.API.RobotAPI;
import com.example.iir_ai_hospital.LoginFragment;
import com.example.iir_ai_hospital.MainActivity;
import com.example.iir_ai_hospital.R;

import java.util.Locale;

public class Utils {
    public static RobotAPI robotAPI;
    public static MainActivity mainActivity;

    public static void JumpNextFragment(Fragment f, String name, String animDirection) {
        if(robotAPI == null) {
            robotAPI = new RobotAPI(mainActivity);
        }
        if(mainActivity == null) {
            Log.e("utils","MainActivity is null");
        }
        else {
            if(name.equals("Login")) {
//                clearFragmentPopStack();
                LoginFragment.QUESTION_COUNTER = 0;
                LoginFragment.ISEND_FLAG = false;
                LoginFragment.ISEND = "N";
            }
            robotAPI.robot.stopSpeak();

            FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
            if(animDirection.equals("lr"))
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            else
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);

            transaction.replace(R.id.main_container, f)
            .commit();
        }
    }

    public static void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = mainActivity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = mainActivity.getIntent();
        mainActivity.finish();
        mainActivity.startActivity(refresh);
    }

    public static void PopBackFragment() {
        if(mainActivity == null) {
            Log.e("utils","MainActivity is null");
        } else {
            mainActivity.getSupportFragmentManager()
                    .popBackStack();
        }
    }
    private static void clearFragmentPopStack() {
        int count = mainActivity.getSupportFragmentManager().getBackStackEntryCount();
        for(int i = 0; i < count; ++i) {
            mainActivity.getSupportFragmentManager().popBackStack();
        }
    }
}
