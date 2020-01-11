package com.fieldforce.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fieldforce.R;
import com.fieldforce.db.DatabaseManager;
import com.fieldforce.interfaces.ApiServiceCaller;
import com.fieldforce.models.UserProfileModel;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;
import com.fieldforce.utility.CustomDialog;
import com.fieldforce.webservices.ApiConstants;
import com.fieldforce.webservices.JsonResponse;
import com.fieldforce.webservices.WebRequest;

import org.json.JSONObject;

public class LoginActivity extends ParentActivity implements View.OnClickListener, ApiServiceCaller {

    private Context mContext;
    private EditText edtID, edtPassword;
    private Button btnLogin;
    private String userId, userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        edtID = findViewById(R.id.edt_id);
        edtID.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CommonUtility.askForPermissions(mContext, App.getInstance().permissions);
        }
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//            Log.d("xxxxxxxxxxxxxx", " " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Log.d("ooooooooo", "" + telephonyManager.getSimSerialNumber());
            isValidate();
        }
    }

    public void doLogin() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String imeiNumber = telephonyManager.getDeviceId();
//        String imeiNumber = "355463061207453";
        if (CommonUtility.getInstance(this).checkConnectivity(mContext)) {
            showLoadingDialog();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", userId);
                jsonObject.put("password", userPass);
                jsonObject.put("imei_no", imeiNumber);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.LOGIN_URL, this, "");
                App.getInstance().addToRequestQueue(request, ApiConstants.LOGIN_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, getString(R.string.error_internet_not_connected), Toast.LENGTH_LONG).show();
        }
    }

    public void isValidate() {
        userId = edtID.getText().toString().trim();
        userPass = edtPassword.getText().toString();
        if (!TextUtils.isEmpty(userId)) {
            if (!TextUtils.isEmpty(userPass)) {
                if (userPass.length() >= 6)
                    doLogin();
                else
                    edtPassword.setError(getString(R.string.password_should_have_at_least_characters));
            } else
                edtPassword.setError(getString(R.string.please_enter_password));
        } else {
            edtID.setError(getString(R.string.please_enter_user_id));
        }
    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {
        switch (label) {
            case ApiConstants.LOGIN_URL: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.responsedata != null) {
                            try {
                                dismissLoadingDialog();
                                UserProfileModel userProfileModel = new UserProfileModel();
                                userProfileModel.user_id = jsonResponse.responsedata.getUser_id();
                                userProfileModel.emp_id = jsonResponse.responsedata.getEmp_id();
                                userProfileModel.user_name = jsonResponse.responsedata.getUser_name();
                                userProfileModel.city = jsonResponse.responsedata.getCity();
                                userProfileModel.contact_no = jsonResponse.responsedata.getContact_no();
                                userProfileModel.address = jsonResponse.responsedata.getAddress();
//                                userProfileModel.emp_type = jsonResponse.responsedata.getEmp_type();
                                userProfileModel.email_id = jsonResponse.responsedata.getEmail_id();
                                userProfileModel.name = jsonResponse.responsedata.getName();
                                userProfileModel.stateId = jsonResponse.responsedata.getStateId();
                                userProfileModel.state = jsonResponse.responsedata.getState();
                                userProfileModel.cityId = jsonResponse.responsedata.getCityId();
                                userProfileModel.vendorId = jsonResponse.responsedata.getVendorId();
                                userProfileModel.fieldForceId = jsonResponse.responsedata.getFieldForceId();
                                userProfileModel.userType = jsonResponse.responsedata.getUserType();

                                DatabaseManager.saveLoginDetails(mContext, userProfileModel);

                                AppPreferences.getInstance(mContext).putString(AppConstants.AUTH_TOKEN, jsonResponse.responsedata.getToken());
                                AppPreferences.getInstance(mContext).putString(AppConstants.USER_NAME, jsonResponse.responsedata.getUser_name());
                                AppPreferences.getInstance(mContext).putString(AppConstants.USER_CITY, jsonResponse.responsedata.getCity());
                                AppPreferences.getInstance(mContext).putString(AppConstants.EMP_ID, jsonResponse.responsedata.getEmp_id());
                                AppPreferences.getInstance(mContext).putString(AppConstants.MOBILE_NO, jsonResponse.responsedata.getContact_no());
                                AppPreferences.getInstance(mContext).putString(AppConstants.NAME, jsonResponse.responsedata.getName());
                                AppPreferences.getInstance(mContext).putString(AppConstants.STATE, jsonResponse.responsedata.getState());
                                AppPreferences.getInstance(mContext).putString(AppConstants.CITY, jsonResponse.responsedata.getCity());
                                AppPreferences.getInstance(mContext).putString(AppConstants.STATE_ID, jsonResponse.responsedata.getStateId());
                                AppPreferences.getInstance(mContext).putString(AppConstants.CITY_ID, jsonResponse.responsedata.getCityId());
                                AppPreferences.getInstance(mContext).putString(AppConstants.VENDOR_ID, jsonResponse.responsedata.getVendorId());
                                AppPreferences.getInstance(mContext).putString(AppConstants.FIELD_FORCE_ID, jsonResponse.responsedata.getFieldForceId());
                                AppPreferences.getInstance(mContext).putString(AppConstants.USER_TYPE, jsonResponse.responsedata.getUserType());
//                                AppPreferences.getInstance(mContext).putString(AppConstants.USER_TYPE, AppConstants.BLANK_STRING);
//                                AppPreferences.getInstance(mContext).putString(AppConstants.PROFILE_IMAGE_URL, jsonResponse.responsedata.getProfile_image());


                                AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.all));
                                AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "0");


                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            dismissLoadingDialog();
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {
        switch (label) {
            case ApiConstants.LOGIN_URL: {
                dismissLoadingDialog();
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        switch (label) {
            case ApiConstants.LOGIN_URL: {
                dismissLoadingDialog();
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();

            }
            break;
        }
    }

    public void onBackPressed() {
        CustomDialog customDialog = new CustomDialog((Activity) mContext, getString(R.string.do_you_want_to_close_exit_app_now),
                getString(R.string.main_activity), false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();
        customDialog.setCancelable(false);
    }
}
