package com.example.iir_ai_hospital.utils;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.asus.robotframework.API.RobotAPI;
import com.example.iir_ai_hospital.MainActivity;
import com.example.iir_ai_hospital.R;

public class Utils {
    public static RobotAPI robotAPI;
    public static MainActivity mainActivity;

    public static void JumpNextFragment(Fragment f, String name) {
        if(mainActivity == null) {
            Log.e("utils","MainActivity is null");
        }
        else {
            if(name.equals("Login")) {
                clearFragmentPopStack();
            }
            mainActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, f)
                    .addToBackStack(name)
                    .commit();
        }
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
