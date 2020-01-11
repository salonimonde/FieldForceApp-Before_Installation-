package com.fieldforce.ui.fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fieldforce.R;
import com.fieldforce.db.DatabaseManager;
import com.fieldforce.interfaces.ApiServiceCaller;
import com.fieldforce.models.HistoryModel;
import com.fieldforce.models.RegistrationModel;
import com.fieldforce.models.TodayModel;
import com.fieldforce.ui.activities.LocationActivity;
import com.fieldforce.ui.activities.MainActivity;
import com.fieldforce.ui.activities.RegistrationFormActivity;
import com.fieldforce.ui.adapters.OpenAdapter;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;
import com.fieldforce.webservices.ApiConstants;
import com.fieldforce.webservices.JsonResponse;
import com.fieldforce.webservices.WebRequest;

import static java.util.Comparator.comparing;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

public class TodayFragment extends ParentFragment implements ApiServiceCaller, View.OnClickListener {

    private static Context mContext;
    private static TodayFragment instance;
    private static String userId, userType;

    private LinearLayout linearEmptyMessage;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private String screenName = "";
    private TodayModel todayModel;
    private Boolean isOpen = false;
    private AlertDialog alertDialog;
    private Typeface mRegularBold, mRegularItalic, mRegular;
    private FloatingActionButton floatingActionButton;
    private static boolean setAllFlag = false;

    public TodayFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        mContext = getActivity();
        instance = this;

        mRegular = App.getMontserratRegularFont();

        floatingActionButton = rootView.findViewById(R.id.fab);


        screenName = AppPreferences.getInstance(mContext).getString(AppConstants.COMING_FROM, AppConstants.BLANK_STRING);

        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);
        userType = AppPreferences.getInstance(mContext).getString(AppConstants.USER_TYPE, AppConstants.BLANK_STRING);


        linearEmptyMessage = rootView.findViewById(R.id.linear_empty_message);
        recyclerView = rootView.findViewById(R.id.recycler_view_today);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        alertDialog = new AlertDialog.Builder(mContext).create();
//        deleteDataFromDB();


        getCardsFromDB();
        setFabColor();
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"ResourceAsColor", "RestrictedApi"})
    public void setFabColor() {

        if (screenName.equals(CommonUtility.getString(mContext, R.string.enquiry))) {
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorMenuCream)));
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_action_add));
            floatingActionButton.setOnClickListener(this);
        } else if (screenName.equals(getString(R.string.site_verification))) {
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorMenuBlue)));
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_action_maps));
            floatingActionButton.setOnClickListener(this);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.all))) {
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorMenuCream)));
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_action_add));
            floatingActionButton.setOnClickListener(this);
        }
    }


    @SuppressLint("NewApi")
    public void getCardsFromDB() {
        deleteDataFromDB();

        // Date filters applied by Saloni on 29/04/2019
        ArrayList<TodayModel> jobCards = new ArrayList<>();
        ArrayList<RegistrationModel> rejectedJobCards = new ArrayList<>();
        ArrayList<TodayModel> newJobCards = new ArrayList<>();


        if (screenName.equals(CommonUtility.getString(mContext, R.string.enquiry))) {
            if (MainActivity.filterType == 1) {
                jobCards = DatabaseManager.getEnquiryJobCardsToday(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 2) {
                jobCards = DatabaseManager.getEnquiryJobCardsWeek(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 3) {
                jobCards = DatabaseManager.getEnquiryJobCardsMonth(userId, AppConstants.CARD_STATUS_OPEN);
            } else {
                jobCards = DatabaseManager.getEnquiryJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            }
            OpenAdapter openAdapter = new OpenAdapter(mContext, screenName, jobCards);
            openAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(openAdapter);

        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.site_verification))) {

            if (MainActivity.filterType == 1) {
                jobCards = DatabaseManager.getSiteVerificationJobCardToday(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 2) {
                jobCards = DatabaseManager.getSiteVerificationJobCardWeek(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 3) {
                jobCards = DatabaseManager.getSiteVerificationJobCardMonth(userId, AppConstants.CARD_STATUS_OPEN);
            } else {
                jobCards = DatabaseManager.getSiteVerificationJobCard(userId, AppConstants.CARD_STATUS_OPEN);
            }
            OpenAdapter openAdapter = new OpenAdapter(mContext, screenName, jobCards);
            openAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(openAdapter);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.installation))) {
            if (MainActivity.filterType == 1) {
                jobCards = DatabaseManager.getMeterInstallJobCardsToday(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 2) {
                jobCards = DatabaseManager.getMeterInstallJobCardsWeek(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 3) {
                jobCards = DatabaseManager.getMeterInstallJobCardsMonth(userId, AppConstants.CARD_STATUS_OPEN);
            } else {
                jobCards = DatabaseManager.getMeterInstallJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            }
            OpenAdapter OpenAdapter = new OpenAdapter(mContext, screenName, jobCards);
            OpenAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(OpenAdapter);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.convert))) {
            if (MainActivity.filterType == 1) {
                jobCards = DatabaseManager.getConversionJobCardsToday(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 2) {
                jobCards = DatabaseManager.getConversionJobCardsWeek(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 3) {
                jobCards = DatabaseManager.getConversionJobCardsMonth(userId, AppConstants.CARD_STATUS_OPEN);
            } else {
                jobCards = DatabaseManager.getConversionJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            }
            OpenAdapter openAdapter = new OpenAdapter(mContext, screenName, jobCards);
            openAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(openAdapter);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.asset_indexing))) {
            jobCards = DatabaseManager.getAssetJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            OpenAdapter assetJobCardAdapter = new OpenAdapter(mContext, screenName, jobCards);
            recyclerView.setAdapter(assetJobCardAdapter);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.services))) {
            if (MainActivity.filterType == 1) {
                jobCards = DatabaseManager.getServiceJobCardsToday(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 2) {
                jobCards = DatabaseManager.getServiceJobCardsWeek(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 3) {
                jobCards = DatabaseManager.getServiceJobCardsMonth(userId, AppConstants.CARD_STATUS_OPEN);
            } else {
                jobCards = DatabaseManager.getServiceJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            }
            OpenAdapter openAdapter = new OpenAdapter(mContext, screenName, jobCards);
            openAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(openAdapter);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.complaint))) {
            if (MainActivity.filterType == 1) {
                jobCards = DatabaseManager.getComplaintJobCardsToday(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 2) {
                jobCards = DatabaseManager.getComplaintJobCardsWeek(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 3) {
                jobCards = DatabaseManager.getComplaintJobCardsMonth(userId, AppConstants.CARD_STATUS_OPEN);
            } else {
                jobCards = DatabaseManager.getComplaintJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            }
            OpenAdapter openAdapter = new OpenAdapter(mContext, screenName, jobCards);
            openAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(openAdapter);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.preventive))) {
            jobCards = DatabaseManager.getPreventiveJobCard(userId, AppConstants.CARD_STATUS_OPEN);
            OpenAdapter OpenAdapter = new OpenAdapter(mContext, screenName, jobCards);
            recyclerView.setAdapter(OpenAdapter);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.breakdown))) {
            jobCards = DatabaseManager.getBreakdownJobCard(userId, AppConstants.CARD_STATUS_OPEN);
            OpenAdapter OpenAdapter = new OpenAdapter(mContext, screenName, jobCards);
            recyclerView.setAdapter(OpenAdapter);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.commissioning))) {
            jobCards = DatabaseManager.getCommissionJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            OpenAdapter OpenAdapter = new OpenAdapter(mContext, screenName, jobCards);
            recyclerView.setAdapter(OpenAdapter);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.decommissioning))) {
            jobCards = DatabaseManager.getDISCJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            OpenAdapter OpenAdapter = new OpenAdapter(mContext, screenName, jobCards);
            recyclerView.setAdapter(OpenAdapter);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.recovery))) {
            /*jobCards = DatabaseManager.getMeterInstallJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            OpenAdapter OpenAdapter = new OpenAdapter(mContext, screenName, jobCards);
            recyclerView.setAdapter(OpenAdapter);*/
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.all))) {
            if (MainActivity.filterType == 1) {
                jobCards = DatabaseManager.getAllJobCardsToday(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 2) {
                jobCards = DatabaseManager.getAllJobCardsWeek(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 3) {
                jobCards = DatabaseManager.getAllJobCardsMonth(userId, AppConstants.CARD_STATUS_OPEN);
            } else if (MainActivity.filterType == 0) {
                jobCards = DatabaseManager.getAllJobCards(userId, AppConstants.CARD_STATUS_OPEN);
            }
            OpenAdapter openAdapter = new OpenAdapter(mContext, screenName, jobCards);
            openAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(openAdapter);
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.rejected_registrations))) {
            rejectedJobCards = DatabaseManager.getRejectedJobCards(userId, AppConstants.CARD_STATUS_OPEN);
//            Log.d("ssssssssss","  "+rejectedJobCards.size());
            OpenAdapter openAdapter = new OpenAdapter(mContext, rejectedJobCards, screenName);
            openAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(openAdapter);
        }


        if (jobCards == null)
            linearEmptyMessage.setVisibility(View.VISIBLE);
        else
            linearEmptyMessage.setVisibility(View.GONE);

    }

    private void deleteDataFromDB() {


        if (screenName.equals(CommonUtility.getString(mContext, R.string.enquiry))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getEnquiryHistoryJobCard(userId);
            if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataEnquiry(userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.site_verification))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getSiteVerificationHistoryJobCard(userId);
            if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataSiteVerification(mContext, userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.installation))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getMeterInstallHistoryJobCard(userId);
            if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataMeterInstall(mContext, userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.convert))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getSiteVerificationHistoryJobCard(userId);
            if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataConversion(userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.services))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getDSCHistoryJobCard(userId);
            if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataServices(mContext, userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.complaint))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getNSCHistoryJobCard(userId);
            if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataComplaints(mContext, userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.commissioning))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getNSCHistoryJobCard(userId);
            if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataNSC(mContext, userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.decommissioning))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getDSCHistoryJobCard(userId);
            if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataDSC(mContext, userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.preventive))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getPreventiveHistoryJobCard(userId);
            if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataPreventive(mContext, userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.breakdown))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getBreakdownHistoryJobCard(userId);
            if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataBreakdown(mContext, userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.asset_indexing))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getSiteVerificationHistoryJobCard(userId);
            /*if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataSiteVerification(mContext, userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }*/
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.recovery))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getSiteVerificationHistoryJobCard(userId);
            /*if (historyDateList != null) {
                for (int i = 0; i < historyDateList.size(); i++) {
                    DatabaseManager.deleteDataSiteVerification(mContext, userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }*/
        } else if (screenName.equals(getString(R.string.all))) {


            ArrayList<HistoryModel> allHistoryDateList = DatabaseManager.getAllHistoryJobCard(userId);
            if (allHistoryDateList != null) {
                for (int i = 0; i < allHistoryDateList.size(); i++) {
                    DatabaseManager.deleteDataAll(mContext, userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        } else {
            ArrayList<HistoryModel> allHistoryDateList = DatabaseManager.getHistoryRejectedJobCard(userId);
            if (allHistoryDateList != null) {
                for (int i = 0; i < allHistoryDateList.size(); i++) {
                    DatabaseManager.deleteDataRejected(userId, AppConstants.CARD_STATUS_CLOSED);
                }
            }
        }
    }


    public static void getTodayCards(String screenName) {
        showLoadingDialog(mContext);
        JSONObject jsonObject = new JSONObject();
        String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);


        if (screenName.equals(CommonUtility.getString(mContext, R.string.enquiry))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject, ApiConstants.GET_ENQUIRY_JOB_CARD,
                        instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_ENQUIRY_JOB_CARD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.site_verification))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_SITE_VERIFICATION_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_SITE_VERIFICATION_JOB_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.installation))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_INSTALLATION_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_INSTALLATION_JOB_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.convert))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_CONVERSION_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_CONVERSION_JOB_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.services))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_SERVICE_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_SERVICE_JOB_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.complaint))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_COMPLAINT_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_COMPLAINT_JOB_CARDS);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.asset_indexing))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.GET, ApiConstants.GET_ASSIGNED_ASSET_CARD_URL,
                        ApiConstants.GET_ASSIGNED_ASSET_CARD, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_ASSIGNED_ASSET_CARD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.preventive))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.GET_PREVENTIVE_JOB_CARDS_URL,
                        ApiConstants.GET_PREVENTIVE_JOB_CARDS, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_PREVENTIVE_JOB_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.breakdown))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.GET_BREAKDOWN_JOB_CARDS_URL,
                        ApiConstants.GET_BREAKDOWN_JOB_CARDS, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_BREAKDOWN_JOB_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.commissioning))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.GET_COMM_CARDS_URL,
                        ApiConstants.GET_COMM_CARDS, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_COMM_CARDS);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.decommissioning))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.GET_DECOM_CARDS_URL,
                        ApiConstants.DECOMM_TODAY_CARDS, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.DECOMM_TODAY_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.recovery))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                /*JsonObjectRequest request = WebRequest.callPostMethod(mContext, "", jsonObject, Request.Method.POST, ApiConstants.GET_RECOVERY_JOB_CARD_URL,
                        ApiConstants.GET_RECOVERY_JOB_CARD, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_RECOVERY_JOB_CARD);*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.all))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request1 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject, ApiConstants.GET_ENQUIRY_JOB_CARD,
                        instance, token);
                App.getInstance().addToRequestQueue(request1, ApiConstants.GET_ENQUIRY_JOB_CARD);

                JsonObjectRequest request2 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_SITE_VERIFICATION_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request2, ApiConstants.GET_SITE_VERIFICATION_JOB_CARDS);

                JsonObjectRequest request3 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_INSTALLATION_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request3, ApiConstants.GET_INSTALLATION_JOB_CARDS);

                JsonObjectRequest request4 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_CONVERSION_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request4, ApiConstants.GET_CONVERSION_JOB_CARDS);

                JsonObjectRequest request5 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_SERVICE_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request5, ApiConstants.GET_SERVICE_JOB_CARDS);

                JsonObjectRequest request6 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_COMPLAINT_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request6, ApiConstants.GET_COMPLAINT_JOB_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.rejected_registrations))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_REJECTED_REGISTRATIONS, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_REJECTED_REGISTRATIONS);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void deAssignTodayCards() {
        JSONObject jsonObject = new JSONObject();
        String token = AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, AppConstants.BLANK_STRING);


        if (screenName.equals(CommonUtility.getString(mContext, R.string.enquiry))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_ENQUIRY_DE_ASSIGN_JOB_CARD_URL, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_ENQUIRY_DE_ASSIGN_JOB_CARD_URL);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.site_verification))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_SITE_VERIFICATION_DE_ASSIGN_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_SITE_VERIFICATION_DE_ASSIGN_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.installation))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_INSTALLATION_DE_ASSIGN_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_INSTALLATION_DE_ASSIGN_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.convert))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_CONVERSION_DE_ASSIGN_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_CONVERSION_DE_ASSIGN_JOB_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.asset_indexing))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.GET_DE_ASSIGNED_ASSET_CARD_URL,
                        ApiConstants.GET_DE_ASSIGNED_ASSET_CARD, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_DE_ASSIGNED_ASSET_CARD);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.services))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_DE_ASSIGN_SERVICE_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_DE_ASSIGN_SERVICE_JOB_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.complaint))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_DE_ASSIGN_COMPLAINT_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_DE_ASSIGN_COMPLAINT_JOB_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.preventive))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.REASSIGN_DE_ASSIGN_PREVENTIVE_CARD_URL,
                        ApiConstants.REASSIGN_DEASSIGN_PREVENTIVE_CARD, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.REASSIGN_DEASSIGN_PREVENTIVE_CARD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.breakdown))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.REASSIGN_DE_ASSIGN_BREAKDOWN_CARD_URL,
                        ApiConstants.REASSIGN_DEASSIGN_BREAKDOWN_CARD, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.REASSIGN_DEASSIGN_BREAKDOWN_CARD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.commissioning))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.REASSIGN_DE_ASSIGN_COMMISSION_CARD_URL,
                        ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.decommissioning))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request = WebRequest.callPostMethod(jsonObject, Request.Method.POST, ApiConstants.REASSIGN_DE_ASSIGN_DECOMMISSION_CARD_URL,
                        ApiConstants.REASSIGN_DEASSIGN_DECOMMISSION_CARD, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.REASSIGN_DEASSIGN_DECOMMISSION_CARD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.recovery))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                /*JsonObjectRequest request = WebRequest.callPostMethod(mContext, "", jsonObject, Request.Method.POST, ApiConstants.GET_DE_ASSIGN_RECOVERY_JOB_CARD_URL,
                        ApiConstants.GET_DE_ASSIGN_RECOVERY_JOB_CARD, instance, AppPreferences.getInstance(mContext).getString(AppConstants.AUTH_TOKEN, ""));
                App.getInstance().addToRequestQueue(request, ApiConstants.GET_DE_ASSIGN_RECOVERY_JOB_CARD);*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.all))) {
            try {
                jsonObject.put(mContext.getString(R.string.user_id), userId);
                JsonObjectRequest request1 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_ENQUIRY_DE_ASSIGN_JOB_CARD_URL, instance, token);
                App.getInstance().addToRequestQueue(request1, ApiConstants.GET_ENQUIRY_DE_ASSIGN_JOB_CARD_URL);

                JsonObjectRequest request2 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_SITE_VERIFICATION_DE_ASSIGN_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request2, ApiConstants.GET_SITE_VERIFICATION_DE_ASSIGN_CARDS);

                JsonObjectRequest request3 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_INSTALLATION_DE_ASSIGN_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request3, ApiConstants.GET_INSTALLATION_DE_ASSIGN_CARDS);

                JsonObjectRequest request4 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_CONVERSION_DE_ASSIGN_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request4, ApiConstants.GET_CONVERSION_DE_ASSIGN_JOB_CARDS);

                JsonObjectRequest request5 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_DE_ASSIGN_SERVICE_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request5, ApiConstants.GET_DE_ASSIGN_SERVICE_JOB_CARDS);

                JsonObjectRequest request6 = WebRequest.callPostMethod1(Request.Method.POST, jsonObject,
                        ApiConstants.GET_DE_ASSIGN_COMPLAINT_JOB_CARDS, instance, token);
                App.getInstance().addToRequestQueue(request6, ApiConstants.GET_DE_ASSIGN_COMPLAINT_JOB_CARDS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAsyncSuccess(JsonResponse jsonResponse, String label) {
        switch (label) {
            case ApiConstants.GET_COMM_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.commissioning_cards.size() > 0) {
                            try {
                                if (jsonResponse.commissioning_cards.get(0).commission_id != null) {
                                    ArrayList<TodayModel> todayModelCardArrayList = new ArrayList<>();
                                    todayModelCardArrayList.addAll(jsonResponse.commissioning_cards);
                                    DatabaseManager.saveCommissionJobCards(mContext, todayModelCardArrayList);
                                    getCardsFromDB();
//                                    ((MainActivity) getActivity()).onResume();
                                    Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, mContext.getString(R.string.no_job_card_assign_to_you), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.DECOMM_TODAY_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.decommissioning_cards.size() > 0) {
                            dismissLoadingDialog();
                            try {
                                if (jsonResponse.decommissioning_cards.get(0).decommission_id != null) {
                                    ArrayList<TodayModel> discTodayCardArrayList = new ArrayList<>();
                                    discTodayCardArrayList.addAll(jsonResponse.decommissioning_cards);
                                    DatabaseManager.saveDecommJobCards(mContext, discTodayCardArrayList);
                                    getCardsFromDB();
//                                    ((MainActivity) getActivity()).onResume();
                                    Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, mContext.getString(R.string.no_job_card_assign_to_you), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_INSTALLATION_JOB_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.meter_install_cards.size() > 0) {
                            dismissLoadingDialog();


                            try {
                                if (jsonResponse.meter_install_cards.get(0).meterInstallationId != null) {
                                    /*todayModel.meterNo = jsonResponse.meter_install_cards.get(0).meterNo;
                                    todayModel.meterDigits = jsonResponse.meter_install_cards.get(0).meterDigits;
                                    todayModel.meterMake = jsonResponse.meter_install_cards.get(0).meterMake;
                                    todayModel.meterType = jsonResponse.meter_install_cards.get(0).meterType;
                                    todayModel.regulatorNo = jsonResponse.meter_install_cards.get(0).regulatorNo;
                                    todayModel.pipeLength = jsonResponse.meter_install_cards.get(0).pipeLength;
                                    todayModel.freeLength = jsonResponse.meter_install_cards.get(0).freeLength;
                                    todayModel.extraCharges = jsonResponse.meter_install_cards.get(0).extraCharges;
                                    todayModel.conversionDate = jsonResponse.meter_install_cards.get(0).conversionDate;
                                    todayModel.rfcVerified = jsonResponse.meter_install_cards.get(0).rfcVerified;
                                    todayModel.poNumber = jsonResponse.meter_install_cards.get(0).poNumber;*/
                                    ArrayList<TodayModel> todayModelCardArrayList = new ArrayList<>();
                                    todayModelCardArrayList.addAll(jsonResponse.meter_install_cards);
                                    for (int i = 0; i < todayModelCardArrayList.size(); i++) {
                                        todayModelCardArrayList.get(i).screen = getString(R.string.installation);
                                    }
                                    DatabaseManager.saveMeterInstallJobCards(mContext, todayModelCardArrayList);
                                    DatabaseManager.saveAllJobCards(mContext, todayModelCardArrayList);
                                    getCardsFromDB();
//                                    ((MainActivity) getActivity()).onResume();
                                    Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, mContext.getString(R.string.no_job_card_assign_to_you), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, getString(R.string.no_job_card_assign_to_you), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_COMPLAINT_JOB_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.complaint_cards.size() > 0) {
                            try {
                                if (jsonResponse.complaint_cards.get(0).complaintId != null) {
                                    ArrayList<TodayModel> todayCompServiceModelCardArrayList = new ArrayList<>();
                                    todayCompServiceModelCardArrayList.addAll(jsonResponse.complaint_cards);
                                    for (int i = 0; i < todayCompServiceModelCardArrayList.size(); i++) {
                                        todayCompServiceModelCardArrayList.get(i).screen = getString(R.string.complaint);
                                    }
                                    DatabaseManager.saveComplaintJobCards(mContext, todayCompServiceModelCardArrayList);
                                    DatabaseManager.saveAllJobCards(mContext, todayCompServiceModelCardArrayList);
                                    getCardsFromDB();
//                                    ((MainActivity) getActivity()).onResume();
                                    Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, getString(R.string.no_job_card_assign_to_you), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_SERVICE_JOB_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.service_jobs != null) {
                            try {
                                if (jsonResponse.service_jobs.size() > 0) {

                                    ArrayList<TodayModel> todayServiceModelCardArrayList = new ArrayList<>();
                                    todayServiceModelCardArrayList.addAll(jsonResponse.service_jobs);
                                    for (int i = 0; i < todayServiceModelCardArrayList.size(); i++) {
                                        todayServiceModelCardArrayList.get(i).screen = getString(R.string.services);
                                    }
                                    DatabaseManager.saveServiceJobCards(mContext, todayServiceModelCardArrayList);
                                    DatabaseManager.saveAllJobCards(mContext, todayServiceModelCardArrayList);
                                    getCardsFromDB();
//                                    ((MainActivity) getActivity()).onResume();
                                    Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, mContext.getString(R.string.no_job_card_assign_to_you), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_PREVENTIVE_JOB_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.preventive_maintenance_cards.size() > 0) {
                            try {
                                ArrayList<TodayModel> preventiveTodayCardArrayList = new ArrayList<>();
                                preventiveTodayCardArrayList.addAll(jsonResponse.preventive_maintenance_cards);
                                DatabaseManager.savePreventiveJobCards(mContext, preventiveTodayCardArrayList);
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_BREAKDOWN_JOB_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.breakdown_maintenance_cards.size() > 0) {
                            try {
                                ArrayList<TodayModel> breakdownTodayCardArrayList = new ArrayList<>();
                                breakdownTodayCardArrayList.addAll(jsonResponse.breakdown_maintenance_cards);
                                DatabaseManager.saveBreakdownJobCards(mContext, breakdownTodayCardArrayList);
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_ASSIGNED_ASSET_CARD: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.asset_cards.size() > 0) {
                            try {
                                ArrayList<TodayModel> assetCardArrayList = new ArrayList<>();
                                assetCardArrayList.addAll(jsonResponse.asset_cards);
                                DatabaseManager.saveAssetJobCards(mContext, assetCardArrayList);
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_CONVERSION_JOB_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.conversion_cards.size() > 0) {
                            try {
                                ArrayList<TodayModel> breakdownTodayCardArrayList = new ArrayList<>();
                                breakdownTodayCardArrayList.addAll(jsonResponse.conversion_cards);
                                for (int i = 0; i < breakdownTodayCardArrayList.size(); i++) {
                                    breakdownTodayCardArrayList.get(i).screen = getString(R.string.convert);
                                }
                                DatabaseManager.saveConversionJobCards(mContext, breakdownTodayCardArrayList);
                                DatabaseManager.saveAllJobCards(mContext, breakdownTodayCardArrayList);
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, getString(R.string.no_job_card_assign_to_you), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_RECOVERY_JOB_CARD: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.site_verification_cards.size() > 0) {
                            try {
                                /*ArrayList<TodayModel> breakdownTodayCardArrayList = new ArrayList<>();
                                breakdownTodayCardArrayList.addAll(jsonResponse.site_verification_cards);
                                DatabaseManager.saveSiteVerificationJobCards(mContext, breakdownTodayCardArrayList);
                                getCardsFromDB();
                                ((MainActivity) getActivity()).onResume();
*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_SITE_VERIFICATION_JOB_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.site_verification_cards.size() > 0) {
                            try {
                                ArrayList<TodayModel> breakdownTodayCardArrayList = new ArrayList<>();
                                breakdownTodayCardArrayList.addAll(jsonResponse.site_verification_cards);
                                for (int i = 0; i < breakdownTodayCardArrayList.size(); i++) {
                                    breakdownTodayCardArrayList.get(i).screen = getString(R.string.site_verification);
                                }
                                DatabaseManager.saveSiteVerificationJobCards(mContext, breakdownTodayCardArrayList);
                                DatabaseManager.saveAllJobCards(mContext, breakdownTodayCardArrayList);
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, getString(R.string.no_job_card_assign_to_you), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_request_list.size() > 0) {
                            try {
                                DatabaseManager.handleReassignDeassignNSC(mContext, jsonResponse.deassign_request_list, userId);
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_DECOMMISSION_CARD: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_request_list.size() > 0) {
                            try {
                                DatabaseManager.handleReassignDeassignDISC(mContext, jsonResponse.deassign_request_list, userId);
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_INSTALLATION_DE_ASSIGN_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_request_list.size() > 0) {
                            try {
                                DatabaseManager.handleReassignDeassignMeterInstall(mContext, jsonResponse.deassign_request_list, userId);
                                DatabaseManager.handleDeAssignAll(mContext, jsonResponse.deassign_request_list, userId, getString(R.string.installation));
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_DE_ASSIGN_SERVICE_JOB_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_service_list.size() > 0) {
                            try {
                                DatabaseManager.handleReassignDeassignService(mContext, jsonResponse.deassign_service_list, userId);
                                DatabaseManager.handleDeAssignAll(mContext, jsonResponse.deassign_service_list, userId, getString(R.string.services));
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_DE_ASSIGN_COMPLAINT_JOB_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_complaint_list.size() > 0) {
                            try {
                                MainActivity.bottomNavigationView.getMenu().getItem(2).setChecked(false);
                                DatabaseManager.handleReassignDeassignComplaint(mContext, jsonResponse.deassign_complaint_list, userId);
                                DatabaseManager.handleDeAssignAll(mContext, jsonResponse.deassign_complaint_list, userId, getString(R.string.complaint));
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_PREVENTIVE_CARD: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_request_list.size() > 0) {
                            try {
                                DatabaseManager.handleReassignDeassignPreventive(mContext, jsonResponse.deassign_request_list, userId);
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_BREAKDOWN_CARD: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_request_list.size() > 0) {
                            try {
                                DatabaseManager.handleReassignDeassignBreakdown(mContext, jsonResponse.deassign_request_list, userId);
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_SITE_VERIFICATION_DE_ASSIGN_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_request_list.size() > 0) {
                            try {
                                DatabaseManager.handleReassignDeassignSiteVerification(mContext, jsonResponse.deassign_request_list, userId);
                                DatabaseManager.handleDeAssignAll(mContext, jsonResponse.deassign_request_list, userId, getString(R.string.site_verification));
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_DE_ASSIGNED_ASSET_CARD: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_request_list.size() > 0) {
                            try {
                                DatabaseManager.handleDeAssignAssetJobCard(mContext, jsonResponse.deassign_request_list, userId);
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_CONVERSION_DE_ASSIGN_JOB_CARDS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_request_list.size() > 0) {
                            try {
                                DatabaseManager.handleDeAssignConversion(mContext, jsonResponse.deassign_request_list, userId);
                                DatabaseManager.handleDeAssignAll(mContext, jsonResponse.deassign_request_list, userId, getString(R.string.convert));
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_DE_ASSIGN_RECOVERY_JOB_CARD: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_request_list.size() > 0) {
                            try {
                                /*DatabaseManager.handleReassignDeassignSiteVerification(mContext, jsonResponse.deassign_request_list, userId);
                                getCardsFromDB();
                               ((MainActivity) getActivity()).onResume();*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_ENQUIRY_JOB_CARD: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.enquiry_cards.size() > 0) {
                            try {
                                ArrayList<TodayModel> todayModels = new ArrayList<>();
                                todayModels.addAll(jsonResponse.enquiry_cards);
                                for (int i = 0; i < todayModels.size(); i++) {
                                    todayModels.get(i).screen = getString(R.string.enquiry);
                                }
                                DatabaseManager.saveEnquiryJobCards(mContext, todayModels);
                                DatabaseManager.saveAllJobCards(mContext, todayModels);
                                getCardsFromDB();
//                                ((MainActivity) getActivity()).onResume();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, getString(R.string.no_job_card_assign_to_you), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (jsonResponse.result != null && jsonResponse.result.equals(JsonResponse.FAILURE)) {
                            Toast.makeText(mContext, jsonResponse.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_ENQUIRY_DE_ASSIGN_JOB_CARD_URL: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.deassign_request_list.size() > 0) {
                            try {
                                DatabaseManager.handleDeAssignEnquiry(mContext, jsonResponse.deassign_request_list, userId);
                                DatabaseManager.handleDeAssignAll(mContext, jsonResponse.deassign_request_list, userId, getString(R.string.enquiry));
//                                ((MainActivity) getActivity()).onResume();
                                getCardsFromDB();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            break;
            case ApiConstants.GET_REJECTED_REGISTRATIONS: {
                if (jsonResponse != null) {
                    if (jsonResponse.SUCCESS != null && jsonResponse.result.equals(jsonResponse.SUCCESS)) {
                        if (jsonResponse.data.size() > 0) {
                            ArrayList<RegistrationModel> registrationModels = new ArrayList<>();
                            registrationModels.addAll(jsonResponse.data);
                            DatabaseManager.saveRejectedJobCards(mContext, registrationModels);
                            DatabaseManager.saveRejectedRegistrationJobCards(mContext, registrationModels, AppConstants.CARD_STATUS_OPEN);
                            getCardsFromDB();
                        }
                    }
                }
                dismissLoadingDialog();
                deAssignTodayCards();
            }
            break;
        }
    }

    @Override
    public void onAsyncFail(String message, String label, NetworkResponse response) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.GET_COMM_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.DECOMM_TODAY_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_COMPLAINT_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_SERVICE_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            case ApiConstants.GET_SITE_VERIFICATION_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_INSTALLATION_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_PREVENTIVE_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_BREAKDOWN_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_ASSIGNED_ASSET_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_CONVERSION_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_RECOVERY_JOB_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_DECOMMISSION_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_DE_ASSIGN_RECOVERY_JOB_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_CONVERSION_DE_ASSIGN_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_SITE_VERIFICATION_DE_ASSIGN_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_BREAKDOWN_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_PREVENTIVE_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_DE_ASSIGN_COMPLAINT_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_DE_ASSIGN_SERVICE_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_INSTALLATION_DE_ASSIGN_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_DE_ASSIGNED_ASSET_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_REJECTED_REGISTRATIONS: {
                Log.d("aaaaaaaaaaaa","");
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void onAsyncCompletelyFail(String message, String label) {
        dismissLoadingDialog();
        switch (label) {
            case ApiConstants.GET_COMM_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.DECOMM_TODAY_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_COMPLAINT_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_SERVICE_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_SITE_VERIFICATION_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_INSTALLATION_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
                deAssignTodayCards();
            }
            break;
            case ApiConstants.GET_PREVENTIVE_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_BREAKDOWN_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_ASSIGNED_ASSET_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_CONVERSION_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_RECOVERY_JOB_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_COMMISSION_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_DECOMMISSION_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_DE_ASSIGN_RECOVERY_JOB_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_CONVERSION_DE_ASSIGN_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_SITE_VERIFICATION_DE_ASSIGN_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_BREAKDOWN_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.REASSIGN_DEASSIGN_PREVENTIVE_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_DE_ASSIGN_COMPLAINT_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_DE_ASSIGN_SERVICE_JOB_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_INSTALLATION_DE_ASSIGN_CARDS: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_DE_ASSIGNED_ASSET_CARD: {
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
            case ApiConstants.GET_REJECTED_REGISTRATIONS: {
                Log.d("bbbbbbbbbbbbbbbbb","");
                Toast.makeText(mContext, AppConstants.API_FAIL_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @SuppressLint("NewApi")
    public void onResume() {
        super.onResume();
        getCardsFromDB();
        setFabColor();
    }


    @Override

    public void onClick(View v) {

        if (v == floatingActionButton) {
            if (screenName.equals(getString(R.string.enquiry)) || screenName.equals(getString(R.string.all))) {
                Intent intent = new Intent(mContext, RegistrationFormActivity.class);
                intent.putExtra(AppConstants.COMING_FROM, mContext.getString(R.string.new_nsc));
                mContext.startActivity(intent);
            } else if (screenName.equals(getString(R.string.site_verification))) {
                Intent intent1 = new Intent(mContext, LocationActivity.class);
                mContext.startActivity(intent1);
            }
        }
    }


}
