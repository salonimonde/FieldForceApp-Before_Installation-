package com.fieldforce.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.fieldforce.R;
import com.fieldforce.services.ProcessMainClass;
import com.fieldforce.services.RestartServiceBroadcastReceiver;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;

public class SplashActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Context mContext = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String empId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);
                String userType = AppPreferences.getInstance(mContext).getString(AppConstants.USER_TYPE, AppConstants.BLANK_STRING);
                Intent intent;
                if (empId.equals("") || empId == null) {
                    intent = new Intent(mContext, LoginActivity.class);
                } else {
                    intent = new Intent(mContext, MainActivity.class);

                    AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.all));
                    AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "0");

                }

                startActivity(intent);
                finish();
            }
        }, 3000);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(getApplicationContext());
        }
    }
}