package com.fieldforce.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.ui.activities.LoginActivity;
import com.fieldforce.ui.activities.MainActivity;
import com.fieldforce.ui.activities.RegistrationFormActivity;

import io.fabric.sdk.android.services.common.CommonUtils;

public class CustomDialog extends Dialog implements View.OnClickListener {

    private Activity mActivity;
    private Button btnYes, btnNo;
    private TextView txtTitle;
    private String title, comingFrom;
    private boolean isAadhar = false;

    public CustomDialog(Activity activity, String title, String comingFrom, Boolean isAadhar) {
        super(activity);
        this.mActivity = activity;
        this.title = title;
        this.comingFrom = comingFrom;
        this.isAadhar = isAadhar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        txtTitle = findViewById(R.id.txt_title);
        txtTitle.setText(title);

        btnYes = findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(this);
        btnNo = findViewById(R.id.btn_no);
        btnNo.setOnClickListener(this);

        ImageView imgModule = findViewById(R.id.img_module);

        if (comingFrom.equals(mActivity.getString(R.string.nsc))) {
            if (isAadhar) {
                btnNo.setVisibility(View.VISIBLE);
            } else {
                btnNo.setVisibility(View.GONE);
            }
            imgModule.setImageResource(R.drawable.ic_action_accepted);
        } else if (comingFrom.equals(mActivity.getString(R.string.verify_otp))) {
            imgModule.setImageResource(R.drawable.ic_action_accepted);
            btnNo.setVisibility(View.GONE);
            btnYes.setText("OK");
        } else if (comingFrom.equals(mActivity.getString(R.string.verify_otp_fail))) {
            imgModule.setImageResource(R.drawable.ic_action_cross);
            btnNo.setVisibility(View.GONE);
            btnYes.setText("OK");
        } else if (comingFrom.equals(mActivity.getString(R.string.otp_already_verifed))) {
            btnNo.setVisibility(View.GONE);
            btnYes.setText("OK");
        } else if (comingFrom.equals(mActivity.getString(R.string.sign_out))) {
            imgModule.setImageResource(R.drawable.ic_action_logout);
        } else if (comingFrom.equals(mActivity.getString(R.string.main_activity))) {
            imgModule.setImageResource(R.drawable.ic_action_exit);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == btnYes) {
            if (comingFrom.equals(mActivity.getString(R.string.nsc))) {
                dismiss();
                mActivity.finish();
//                System.exit(0);
            } else if (comingFrom.equals(mActivity.getString(R.string.verify_otp)) || (comingFrom.equals(mActivity.getString(R.string.verify_otp_fail)))
                    || (comingFrom.equals(mActivity.getString(R.string.otp_already_verifed)))) {
                dismiss();
            } else if (comingFrom.equals(mActivity.getString(R.string.sign_out))) {
                AppPreferences.getInstance(mActivity).putString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.USER_NAME, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.USER_CITY, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.MOBILE_NO, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.PROFILE_IMAGE_URL, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.STATE, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.CITY, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.STATE_ID, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.CITY_ID, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.VENDOR_ID, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.FIELD_FORCE_ID, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.USER_TYPE, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.SELECTED_MODULE, AppConstants.BLANK_STRING);
                AppPreferences.getInstance(mActivity).putString(AppConstants.SCREEN_NO, AppConstants.BLANK_STRING);
                Intent intent = new Intent(mActivity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(intent);
                MainActivity.activity.finish();
                mActivity.finish();
            } else if (comingFrom.equals(mActivity.getString(R.string.main_activity))) {
                dismiss();

                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
                mActivity.startActivity(a);
                System.exit(0);
                MainActivity.activity.finish();

                mActivity.finish();
            }
        } else if (v == btnNo) {
            dismiss();
        }
    }
}
