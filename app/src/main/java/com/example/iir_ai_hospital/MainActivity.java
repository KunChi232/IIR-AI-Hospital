package com.example.iir_ai_hospital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import com.asus.robotframework.API.RobotAPI;

import static com.example.iir_ai_hospital.utils.Utils.robotAPI;
import static com.example.iir_ai_hospital.utils.Utils.mainActivity;
import static com.example.iir_ai_hospital.utils.Utils.JumpNextFragment;

public class MainActivity extends AppCompatActivity {

    public static String CURRENT_LANG = "ch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        /*
         * Hide title bar
         */
        getSupportActionBar().hide();

        initRobotApi();

        /*
         * Hide navigate bar
         * https://stackoverflow.com/questions/21724420/how-to-hide-navigation-bar-permanently-in-android-activity
         */
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        this.getWindow().getDecorView().setSystemUiVisibility(flags);


        JumpNextFragment(MedicalNumberFragment.newInstance(), "MedicalNumber","lr");
//        JumpNextFragment(MenuFragment.newInstance(), "MedicalNumber");
    }

    @Override
    protected void onStart() {
        super.onStart();
        int PERMISSIONS_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
        };
        if(!hasPermission(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_ALL);
        }
    }

    private void initRobotApi() {
        initUtils();
        /*
         * Avoid shy behavior
         */
        robotAPI.robot.setPressOnHeadAction(false);
        /*
         * Avoid screen jump back to junior default face
         */
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*
         * Avoid keyboard pop out in Fragment contain editText
         */
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /*
         * very important
         * https://zenbo.asus.com/developer/documents/Zenbo-SDK/DialogSystem
         * setTouchOnlySignal可以關掉語音hey zenbo觸發zenbo mode
         */
        robotAPI.robot.setTouchOnlySignal(true);
        robotAPI.robot.setVoiceTrigger(false);

    }

    private boolean hasPermission(Context context, String... permissions) {
        if(context != null && permissions != null) {
            for(String permission : permissions) {
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
             }
        }
        return true;
    }

    private void initUtils() {
        if(robotAPI == null)
        {
            robotAPI = new RobotAPI(this);
        }

        Log.d("MainActivity","initRobotApi");
        mainActivity = this;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(robotAPI != null) {
            robotAPI.release();
            robotAPI = null;
        }
    }

}
