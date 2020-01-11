package com.fieldforce.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fieldforce.R;
import com.fieldforce.db.DatabaseManager;
import com.fieldforce.models.HistoryModel;
import com.fieldforce.ui.adapters.HistoryAdapter;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class HistoryFragment extends Fragment {

    private Context mContext;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private String screenName = "", userId = "", userType;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        mContext = getActivity().getApplicationContext();
        screenName = AppPreferences.getInstance(mContext).getString(AppConstants.COMING_FROM, AppConstants.BLANK_STRING);
        recyclerView = rootView.findViewById(R.id.recycler_view_history);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        userId = AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING);
        userType = AppPreferences.getInstance(mContext).getString(AppConstants.USER_TYPE, AppConstants.BLANK_STRING);

        try {
            getHistoryCards();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void getHistoryCards() throws ParseException {


        if (screenName.equals(CommonUtility.getString(mContext, R.string.enquiry))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getEnquiryHistoryJobCard(userId);
            if (historyDateList != null) {
                ArrayList<HistoryModel> historyList = new ArrayList<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(historyDateList.get(i).date);
                    historyModel.setName(historyDateList.get(i).name);
                    historyModel.setscreen(historyDateList.get(i).screen);
                    historyModel.setNscId(historyDateList.get(i).nscId);
                    historyModel.setArea(historyDateList.get(i).area);
                    historyModel.setStatus(historyDateList.get(i).status);
                    historyList.add(historyModel);
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);

            } else {
                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            }
        }

        if (screenName.equals(CommonUtility.getString(mContext, R.string.site_verification))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getSiteVerificationHistoryJobCard(userId);
            if (historyDateList != null) {
                ArrayList<HistoryModel> historyList = new ArrayList<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(historyDateList.get(i).date);
                    historyModel.setName(historyDateList.get(i).name);
                    historyModel.setscreen(getString(R.string.verification));
                    historyModel.setNscId(historyDateList.get(i).nscId);
                    historyModel.setArea(historyDateList.get(i).area);
                    historyModel.setStatus(historyDateList.get(i).status);
                    historyList.add(historyModel);
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);

            } else {
                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.installation))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getMeterInstallHistoryJobCard(userId);

            if (historyDateList != null) {
                ArrayList<HistoryModel> historyList = new ArrayList<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(historyDateList.get(i).date);
                    historyModel.setName(historyDateList.get(i).name);
                    historyModel.setscreen(historyDateList.get(i).screen);
                    historyModel.setNscId(historyDateList.get(i).nscId);
                    historyModel.setArea(historyDateList.get(i).area);
                    historyModel.setStatus(historyDateList.get(i).status);
                    historyList.add(historyModel);
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);

            } else {

                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.convert))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getConversionHistoryJobCard(userId);
            if (historyDateList != null) {
                ArrayList<HistoryModel> historyList = new ArrayList<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(historyDateList.get(i).date);
                    Log.d("aaaaaaaaaaaaaaaaaa",""+historyDateList.get(i).date);
                    historyModel.setName(historyDateList.get(i).name);
                    historyModel.setscreen(historyDateList.get(i).screen);
                    historyModel.setNscId(historyDateList.get(i).nscId);
                    historyModel.setArea(historyDateList.get(i).area);
                    historyModel.setStatus(historyDateList.get(i).status);
                    historyList.add(historyModel);
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);

            } else {
                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.asset_indexing))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getAssetIndexingHistoryJobCard(userId);

            if (historyDateList != null) {
                int countOpen = 0, countClosed = 0, total = 0, totalCompleted = 0;
                LinkedHashMap<String, Integer> hashMapOpen = new LinkedHashMap<>();
                LinkedHashMap<String, Integer> hashMapClosed = new LinkedHashMap<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    countOpen = DatabaseManager.getAssetJobCardCount(userId, AppConstants.CARD_STATUS_OPEN);
                    countClosed = DatabaseManager.getAssetJobCardCount(userId, AppConstants.CARD_STATUS_CLOSED);
                    hashMapOpen.put(historyDateList.get(i).date, countOpen);
                    hashMapClosed.put(historyDateList.get(i).date, countClosed);
                    total = total + countOpen + countClosed;
                    totalCompleted = totalCompleted + countClosed;
                }

                ArrayList<HistoryModel> historyList = new ArrayList<>();

                List<String> dateList = new ArrayList<>(hashMapOpen.keySet());
                List<Integer> openList = new ArrayList<>(hashMapOpen.values());
                List<Integer> closedList = new ArrayList<>(hashMapClosed.values());

                for (int i = 0; i < dateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(dateList.get(i));
                    historyModel.setOpen(String.valueOf(openList.get(i)));
                    historyModel.setCompleted(String.valueOf(closedList.get(i)));

                    historyList.add(historyModel);
                }

                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            } else {
                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.services))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getServiceHistoryJobCard(userId);
            if (historyDateList != null) {
                ArrayList<HistoryModel> historyList = new ArrayList<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(historyDateList.get(i).date);
                    historyModel.setName(historyDateList.get(i).name);
                    historyModel.setscreen(historyDateList.get(i).screen);
                    historyModel.setNscId(historyDateList.get(i).nscId);
                    historyModel.setArea(historyDateList.get(i).area);
                    historyModel.setStatus(historyDateList.get(i).status);
                    historyList.add(historyModel);
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);

            } else {
                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.complaint))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getComplaintHistoryJobCard(userId);
            if (historyDateList != null) {
                ArrayList<HistoryModel> historyList = new ArrayList<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(historyDateList.get(i).date);
                    historyModel.setName(historyDateList.get(i).name);
                    historyModel.setscreen(historyDateList.get(i).screen);
                    historyModel.setNscId(historyDateList.get(i).nscId);
                    historyModel.setArea(historyDateList.get(i).area);
                    historyModel.setStatus(historyDateList.get(i).status);
                    historyList.add(historyModel);
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);

            } else {
                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.preventive))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getPreventiveHistoryJobCard(userId);
            if (historyDateList != null) {
                int countOpen = 0, countClosed = 0, total = 0, totalCompleted = 0;
                LinkedHashMap<String, Integer> hashMapOpen = new LinkedHashMap<>();
                LinkedHashMap<String, Integer> hashMapClosed = new LinkedHashMap<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    countOpen = DatabaseManager.getPreventiveTodayCount(userId, AppConstants.CARD_STATUS_OPEN, historyDateList.get(i).date);
                    countClosed = DatabaseManager.getPreventiveTodayCount(userId, AppConstants.CARD_STATUS_CLOSED, historyDateList.get(i).date);
                    hashMapOpen.put(historyDateList.get(i).date, countOpen);
                    hashMapClosed.put(historyDateList.get(i).date, countClosed);
                    total = total + countOpen + countClosed;
                    totalCompleted = totalCompleted + countClosed;
                }

                ArrayList<HistoryModel> historyList = new ArrayList<>();

                List<String> dateList = new ArrayList<>(hashMapOpen.keySet());
                List<Integer> openList = new ArrayList<>(hashMapOpen.values());
                List<Integer> closedList = new ArrayList<>(hashMapClosed.values());

                for (int i = 0; i < dateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(dateList.get(i));
                    historyModel.setOpen(String.valueOf(openList.get(i)));
                    historyModel.setCompleted(String.valueOf(closedList.get(i)));

                    historyList.add(historyModel);
                }

                HistoryAdapter historyMaintenanceAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyMaintenanceAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyMaintenanceAdapter);
            } else {
                HistoryAdapter historyMaintenanceAdapter = new HistoryAdapter();
                historyMaintenanceAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyMaintenanceAdapter);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.breakdown))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getBreakdownHistoryJobCard(userId);
            if (historyDateList != null) {
                int countOpen = 0, countClosed = 0, total = 0, totalCompleted = 0;
                LinkedHashMap<String, Integer> hashMapOpen = new LinkedHashMap<>();
                LinkedHashMap<String, Integer> hashMapClosed = new LinkedHashMap<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    countOpen = DatabaseManager.getBreakdownTodayCount(userId, AppConstants.CARD_STATUS_OPEN, historyDateList.get(i).date);
                    countClosed = DatabaseManager.getBreakdownTodayCount(userId, AppConstants.CARD_STATUS_CLOSED, historyDateList.get(i).date);
                    hashMapOpen.put(historyDateList.get(i).date, countOpen);
                    hashMapClosed.put(historyDateList.get(i).date, countClosed);
                    total = total + countOpen + countClosed;
                    totalCompleted = totalCompleted + countClosed;
                }

                ArrayList<HistoryModel> historyList = new ArrayList<>();

                List<String> dateList = new ArrayList<>(hashMapOpen.keySet());
                List<Integer> openList = new ArrayList<>(hashMapOpen.values());
                List<Integer> closedList = new ArrayList<>(hashMapClosed.values());

                for (int i = 0; i < dateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(dateList.get(i));
                    historyModel.setOpen(String.valueOf(openList.get(i)));
                    historyModel.setCompleted(String.valueOf(closedList.get(i)));

                    historyList.add(historyModel);
                }

                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);

            } else {
                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.commissioning))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getNSCHistoryJobCard(userId);

            if (historyDateList != null) {
                int countOpen = 0, countClosed = 0, total = 0, totalCompleted = 0;
                LinkedHashMap<String, Integer> hashMapOpen = new LinkedHashMap<>();
                LinkedHashMap<String, Integer> hashMapClosed = new LinkedHashMap<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    countOpen = DatabaseManager.getCommissionTodayCount(userId, AppConstants.CARD_STATUS_OPEN, historyDateList.get(i).date);
                    countClosed = DatabaseManager.getCommissionTodayCount(userId, AppConstants.CARD_STATUS_CLOSED, historyDateList.get(i).date);
                    hashMapOpen.put(historyDateList.get(i).date, countOpen);
                    hashMapClosed.put(historyDateList.get(i).date, countClosed);
                    total = total + countOpen + countClosed;
                    totalCompleted = totalCompleted + countClosed;
                }

                ArrayList<HistoryModel> historyList = new ArrayList<>();

                List<String> dateList = new ArrayList<>(hashMapOpen.keySet());
                List<Integer> openList = new ArrayList<>(hashMapOpen.values());
                List<Integer> closedList = new ArrayList<>(hashMapClosed.values());

                for (int i = 0; i < dateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(dateList.get(i));
                    historyModel.setOpen(String.valueOf(openList.get(i)));
                    historyModel.setCompleted(String.valueOf(closedList.get(i)));

                    historyList.add(historyModel);
                }

                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);

            } else {
                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.decommissioning))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getDSCHistoryJobCard(userId);

            if (historyDateList != null) {
                int countOpen = 0, countClosed = 0, total = 0, totalCompleted = 0;
                LinkedHashMap<String, Integer> hashMapOpen = new LinkedHashMap<>();
                LinkedHashMap<String, Integer> hashMapClosed = new LinkedHashMap<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    countOpen = DatabaseManager.getDISCTodayCount(userId, AppConstants.CARD_STATUS_OPEN, historyDateList.get(i).date);
                    countClosed = DatabaseManager.getDISCTodayCount(userId, AppConstants.CARD_STATUS_CLOSED, historyDateList.get(i).date);


                    hashMapOpen.put(historyDateList.get(i).date, countOpen);
                    hashMapClosed.put(historyDateList.get(i).date, countClosed);

                    total = total + countOpen + countClosed;
                    totalCompleted = totalCompleted + countClosed;
                }

                ArrayList<HistoryModel> historyList = new ArrayList<>();

                List<String> dateList = new ArrayList<>(hashMapOpen.keySet());
                List<Integer> openList = new ArrayList<>(hashMapOpen.values());
                List<Integer> closedList = new ArrayList<>(hashMapClosed.values());

                for (int i = 0; i < dateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(dateList.get(i));
                    historyModel.setOpen(String.valueOf(openList.get(i)));
                    historyModel.setCompleted(String.valueOf(closedList.get(i)));

                    historyList.add(historyModel);
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            } else {

                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.recovery))) {
            /*ArrayList<HistoryModel> historyDateList = DatabaseManager.getMeterInstallHistoryJobCard(userId);

            if (historyDateList != null) {
                int countOpen = 0, countClosed = 0, total = 0, totalCompleted = 0;
                HashMap<String, Integer> hashMapOpen = new HashMap<>();
                HashMap<String, Integer> hashMapClosed = new HashMap<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    countOpen = DatabaseManager.getMeterInstallTodayCount(userId, AppConstants.CARD_STATUS_OPEN, historyDateList.get(i).date);
                    countClosed = DatabaseManager.getMeterInstallTodayCount(userId, AppConstants.CARD_STATUS_CLOSED, historyDateList.get(i).date);
                    hashMapOpen.put(historyDateList.get(i).date, countOpen);
                    hashMapClosed.put(historyDateList.get(i).date, countClosed);
                    total = total + countOpen + countClosed;
                    totalCompleted = totalCompleted + countClosed;
                }

                ArrayList<HistoryModel> historyList = new ArrayList<>();

                List<String> dateList = new ArrayList<>(hashMapOpen.keySet());
                List<Integer> openList = new ArrayList<>(hashMapOpen.values());
                List<Integer> closedList = new ArrayList<>(hashMapClosed.values());

                for (int i = 0; i < dateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(dateList.get(i));
                    historyModel.setOpen(String.valueOf(openList.get(i)));
                    historyModel.setCompleted(String.valueOf(closedList.get(i)));

                    historyList.add(historyModel);
                }

                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                recyclerView.setAdapter(historyAdapter);
            } else {*/

            HistoryAdapter historyAdapter = new HistoryAdapter();
            historyAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(historyAdapter);
//            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.all))) {

            ArrayList<HistoryModel> historyDateList = DatabaseManager.getAllHistoryJobCard(userId);
            if (historyDateList != null) {
                ArrayList<HistoryModel> historyList = new ArrayList<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(historyDateList.get(i).date);
                    historyModel.setName(historyDateList.get(i).name);
                    historyModel.setNscId(historyDateList.get(i).nscId);
                    historyModel.setArea(historyDateList.get(i).area);
                    historyModel.setStatus(historyDateList.get(i).status);

                    if (historyDateList.get(i).screen.equals(getString(R.string.site_verification))){
                        historyModel.setscreen(getString(R.string.verification));
                    } else {
                        historyModel.setscreen(historyDateList.get(i).screen);
                    }
                    historyList.add(historyModel);
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);

            } else {
                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
            }
        } else if (screenName.equals(CommonUtility.getString(mContext, R.string.rejected_registrations))) {
            ArrayList<HistoryModel> historyDateList = DatabaseManager.getHistoryRejectedJobCard(userId);
            if (historyDateList != null) {
                ArrayList<HistoryModel> historyList = new ArrayList<>();
                for (int i = 0; i < historyDateList.size(); i++) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setDate(historyDateList.get(i).date);
                    historyModel.setName(historyDateList.get(i).name);
                    historyModel.setscreen(historyDateList.get(i).screen);
                    historyModel.setNscId(historyDateList.get(i).nscId);
                    historyModel.setArea(historyDateList.get(i).area);
                    historyModel.setStatus(historyDateList.get(i).status);
                    historyList.add(historyModel);
                }
                HistoryAdapter historyAdapter = new HistoryAdapter(mContext, screenName, historyList);
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);

            } else {
                HistoryAdapter historyAdapter = new HistoryAdapter();
                historyAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(historyAdapter);
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


}
