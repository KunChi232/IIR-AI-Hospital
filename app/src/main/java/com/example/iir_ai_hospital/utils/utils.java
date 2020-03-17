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
        } else {
            mainActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, f)
                    .addToBackStack(name)
                    .commit();
        }
    }
}
