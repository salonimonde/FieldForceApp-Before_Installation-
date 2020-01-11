package com.fieldforce.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldforce.R;

import org.w3c.dom.Text;

public class DialogCreator {
    private static Typeface mRegularBold;
    private static Typeface mRegularItalic;
    private static Typeface mRegular;

    public static void showMessageDialog(final Context context, String message)
    {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }




    public static void showExitDialog(final Activity activity, String title, String message, final String screenName) {
        mRegularBold = App.getMontserratMediumFont();
        mRegularItalic = App.getMontserratMediumFont();
        mRegular = App.getMontserratRegularFont();

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.dialog_with_title, null);
        final AlertDialog alert = new AlertDialog.Builder(activity).create();
        TextView t = promptView.findViewById(R.id.tv_title);
        t.setTypeface(mRegular);
        t.setText(title);
        TextView msg = promptView.findViewById(R.id.tv_msg);
        msg.setTypeface(mRegular);
        msg.setText(message);
        ImageView imgDialog = promptView.findViewById(R.id.img_dialog);
        imgDialog.setImageResource(R.drawable.logo);
        Button yes = promptView.findViewById(R.id.btn_yes);
        yes.setTypeface(mRegular);
        yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activity.finish();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                activity.startActivity(a);
                AppPreferences.getInstance(activity).putString(AppConstants.SCREEN_FROM_EXIT, screenName);
                alert.dismiss();
            }
        });

        Button no = promptView.findViewById(R.id.btn_no);
        no.setTypeface(mRegular);
        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.setView(promptView);
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.show();
    }

    public static void showFilterDialog(final Activity activity, String title, String message, final String screenName) {
        mRegularBold = App.getMontserratMediumFont();
        mRegularItalic = App.getMontserratMediumFont();
        mRegular = App.getMontserratRegularFont();

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.dialog_for_module_filter, null);
        final AlertDialog alert = new AlertDialog.Builder(activity).create();
        LinearLayout linearLayout = promptView.findViewById(R.id.fragment_history_menu_bottom);
        linearLayout.setBackground(ContextCompat.getDrawable(activity,R.drawable.layout_border));
        /*TextView t = promptView.findViewById(R.id.tv_title);
        t.setTypeface(mRegular);
        t.setText(title);
        TextView msg = promptView.findViewById(R.id.tv_msg);
        msg.setTypeface(mRegular);
        msg.setText(message);
        ImageView imgDialog = promptView.findViewById(R.id.img_dialog);
        imgDialog.setImageResource(R.drawable.logo);
        Button yes = promptView.findViewById(R.id.btn_yes);
        yes.setTypeface(mRegular);
        yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activity.finish();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                activity.startActivity(a);
                AppPreferences.getInstance(activity).putString(AppConstants.SCREEN_FROM_EXIT, screenName);
                alert.dismiss();
            }
        });

        Button no = promptView.findViewById(R.id.btn_no);
        no.setTypeface(mRegular);
        no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert.dismiss();
            }
        });*/
        alert.setView(promptView);
//        alert.getWindow().getAttributes().windowAnimations = R.style.DialogThemeModule;
        alert.show();
    }

    public static void showAlertDialog(String address, final Activity activity) {
        mRegularBold = App.getMontserratMediumFont();
        mRegularItalic = App.getMontserratMediumFont();
        mRegular = App.getMontserratRegularFont();

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.dialog_with_title, null);
        final AlertDialog alert = new AlertDialog.Builder(activity).create();


        ImageView iv = promptView.findViewById(R.id.img_dialog);
        iv.setImageResource(R.drawable.correct);
        TextView t = promptView.findViewById(R.id.tv_title);
        t.setTypeface(mRegular);
        t.setVisibility(View.GONE);

        TextView msg = promptView.findViewById(R.id.tv_msg);
        msg.setTypeface(mRegular);
        msg.setText(address);


        Button ok = promptView.findViewById(R.id.btn_yes);
        ok.setText("Ok");
        ok.setTypeface(mRegular);


        Button no = promptView.findViewById(R.id.btn_no);
        no.setTypeface(mRegular);
        no.setVisibility(View.GONE);

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert.dismiss();
                activity.finish();
            }
        });

        alert.setView(promptView);
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.setCancelable(false);
        alert.show();
    }

    public static void showBackPressDialog(final Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.dialog_back, null);
        final AlertDialog alert = new AlertDialog.Builder(context).create();
        TextView txtAlert;
        TextView txtAlertMsg;
        Button ok = promptView.findViewById(R.id.btn_ok);
        ok.setTypeface(mRegular);

        txtAlert = promptView.findViewById(R.id.lbl_alert);
        txtAlertMsg = promptView.findViewById(R.id.txt_alert_msg);

        txtAlert.setVisibility(View.GONE);
        txtAlert.setTypeface(mRegularBold);
        txtAlertMsg.setTypeface(mRegular);

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        alert.setView(promptView);
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.setCancelable(false);
        alert.show();
    }
     public static void showInstallationDetailsDialog(final Context context, String regulatorNo,
                                                      String meterNo, String installedOn, String rfcVerifiedOn){

         LayoutInflater layoutInflater = LayoutInflater.from(context);
         View promptView = layoutInflater.inflate(R.layout.dialog_installation_details, null);
         final AlertDialog alert = new AlertDialog.Builder(context).create();

         TextView lblInstallationDetails, lblRegulatorNo, lblMeterNo, lblInstalledOn, lblRfcVerifiedOn;
         TextView txtRegulatorNo, txtMeterNo, txtInstalledOn, txtRfcVerifiedOn;

         Button ok = promptView.findViewById(R.id.btn_ok);
         ok.setText("Ok");
         ok.setTypeface(mRegular);

         lblInstallationDetails = promptView.findViewById(R.id.lbl_installation_details);
         lblInstallationDetails.setTypeface(mRegularBold);
         lblRegulatorNo = promptView.findViewById(R.id.lbl_regulator_no);
         lblRegulatorNo.setTypeface(mRegularBold);
         lblMeterNo = promptView.findViewById(R.id.lbl_meter_no);
         lblMeterNo.setTypeface(mRegularBold);
         lblInstalledOn = promptView.findViewById(R.id.lbl_installed_on);
         lblInstalledOn.setTypeface(mRegularBold);
         lblRfcVerifiedOn = promptView.findViewById(R.id.lbl_rfc_verified_on);
         lblRfcVerifiedOn.setTypeface(mRegularBold);


         txtRegulatorNo = promptView.findViewById(R.id.txt_regulator_no);
         txtRegulatorNo.setTypeface(mRegular);
         txtMeterNo = promptView.findViewById(R.id.txt_meter_no);
         txtMeterNo.setTypeface(mRegular);
         txtInstalledOn = promptView.findViewById(R.id.txt_installed_on);
         txtInstalledOn.setTypeface(mRegular);
         txtRfcVerifiedOn = promptView.findViewById(R.id.txt_rfc_verified_on);
         txtRfcVerifiedOn.setTypeface(mRegular);

         txtRegulatorNo.setText(regulatorNo);
         txtMeterNo.setText(meterNo);
         txtInstalledOn.setText(installedOn);
         txtRfcVerifiedOn.setText(rfcVerifiedOn);

         ok.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 alert.dismiss();
             }
         });

         alert.setView(promptView);
         alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
         alert.setCancelable(false);
         alert.show();

     }
}
