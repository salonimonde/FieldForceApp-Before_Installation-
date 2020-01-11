package com.fieldforce.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.ui.activities.MainActivity;
import com.fieldforce.utility.App;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.CommonUtility;

import java.util.ArrayList;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private Spinner spinnerModule, spinnerFilter;
    private Context mContext;
    private Typeface mRegularBold, mRegularItalic, mRegular;
    private String screenName = "", userType = "";
    private String screenNo = "0";
    private Button btnApply, btnReset;
    private ImageView imgCancel;
    private LinearLayout linearSpinnerDate;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRegularBold = App.getMontserratMediumFont();
        mRegularItalic = App.getMontserratBoldFont();
        mRegular = App.getMontserratRegularFont();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.dialog_for_module_filter, container, false);

        mContext = getActivity().getApplicationContext();

        userType = AppPreferences.getInstance(mContext).getString(AppConstants.USER_TYPE, AppConstants.BLANK_STRING);

        spinnerFilter = rootView.findViewById(R.id.spinner_date);
        spinnerModule = rootView.findViewById(R.id.spinner_module_filter);

        btnApply = rootView.findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(this);
        btnReset = rootView.findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(this);

        imgCancel = rootView.findViewById(R.id.img_cross);
        imgCancel.setOnClickListener(this);

        linearSpinnerDate = rootView.findViewById(R.id.linear_module_date);
        Bundle bundle = this.getArguments();
        screenName = bundle.getString(AppConstants.SELECTED_MODULE);

        int[] menuNames = new int[]{R.string.all, R.string.enquiry, R.string.site_verification, R.string.installation,
                R.string.convert, R.string.services, R.string.complaint, R.string.rejected_registrations};

        final int[] menuColor = new int[]{R.color.colorMenuOrange, R.color.colorMenuCream, R.color.colorMenuBlue, R.color.colorMenuGrey,
                R.color.colorMenuOrange, R.color.colorMenuCream, R.color.colorMenuBlue,R.color.colorMenuGrey};

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < menuNames.length; i++) {
            arrayList.add(getString(menuNames[i]));
        }

        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, arrayList) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, menuColor[position]));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, menuColor[position]));
                return v;
            }
        };
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModule.setAdapter(dataAdapter3);
        spinnerModule.setSelection(Integer.parseInt(screenName));
        spinnerModule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.all));
                    AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "0");
                    MainActivity.filterType = 0;
                }else if (position == 1) {
                    AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.enquiry));
                    AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "1");
                    MainActivity.filterType = 0;
                } else if (position == 2) {
                    AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.site_verification));
                    AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "2");
                    MainActivity.filterType = 0;
                } else if (position == 3) {
                    AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.installation));
                    AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "3");
                    MainActivity.filterType = 0;
                } else if (position == 4) {
                    AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.convert));
                    AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "4");
                    MainActivity.filterType = 0;
                } else if (position == 5) {
                    AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.services));
                    AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "5");
                    MainActivity.filterType = 0;
                } else if (position == 6) {
                    AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.complaint));
                    AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "6");
                    MainActivity.filterType = 0;
                }else if (position == 7) {
                    AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.rejected_registrations));
                    AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "7");
                    MainActivity.filterType = 0;
                    spinnerFilter.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.filter_array)) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, menuColor[position]));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(mRegular);
                ((TextView) v).setTextColor(CommonUtility.getColor(mContext, menuColor[position]));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(dataAdapter);
        spinnerFilter.setSelection(MainActivity.filterType);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    MainActivity.filterType = 0;
                } else if (position == 1) {
                    MainActivity.filterType = 1;
                } else if (position == 2) {
                    MainActivity.filterType = 2;
                } else {
                    MainActivity.filterType = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return rootView;
    }
    @Override
    public void onClick(View v) {
        if (v == btnApply) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("screen_no", AppPreferences.getInstance(mContext).getString(AppConstants.SCREEN_NO, AppConstants.BLANK_STRING));
            startActivity(intent);
        }
        if (v == btnReset) {
            AppPreferences.getInstance(mContext).putString(AppConstants.SCREEN_NO, "0");
            AppPreferences.getInstance(mContext).putString(AppConstants.COMING_FROM, getString(R.string.all));
            String screenNo = AppPreferences.getInstance(mContext).getString(AppConstants.SCREEN_NO, AppConstants.BLANK_STRING);
            MainActivity.filterType = 0;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("screen_no", screenNo);
            startActivity(intent);
        }
        if (v == imgCancel) {
            dismiss();
        }
    }
}