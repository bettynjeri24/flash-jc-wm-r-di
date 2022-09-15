package com.ekenya.rnd.dashboard.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ekenya.rnd.onboarding.R;

public class AccountLookupDialog extends DialogFragment implements View.OnClickListener {
    private AlertDialog confirmationDialog;
    private ProcessPaymentListener processPaymentListener;

    public interface ProcessPaymentListener {
        /**
         * Process payment.
         */
        void processPayment();
    }

    public AccountLookupDialog() {
    }

    public void setProcessPaymentListener(ProcessPaymentListener processPaymentListener) {
        this.processPaymentListener = processPaymentListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogLayout = inflater.inflate(R.layout.dialog_account_lookup, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        confirmationDialog = builder.create();

        final TextView tvRegion = (TextView) dialogLayout.findViewById(R.id.tvRegion);
        final TextView tvListenceGrade = (TextView) dialogLayout.findViewById(R.id.tvListenceGrade);
        final TextView tvCharges = (TextView) dialogLayout.findViewById(R.id.tvCharges);
        final TextView tvLicenseNumber = (TextView) dialogLayout.findViewById(R.id.licenseNumber);
        final TextView tvDriverName = (TextView) dialogLayout.findViewById(R.id.tvDriverName);
        final TextView tvReceiptReference = (TextView) dialogLayout.findViewById(R.id.tvReceiptReference);

        String amount = "";
        String driverName = "";
        String licenseGrade = "";
        String licenseNumber = "";
        String offenseID = "";
        String receiptReference = "";
        String region = "";

        Bundle bundle = getArguments();

        if (bundle != null) {
            amount = bundle.getString("amount");
            driverName = bundle.getString("driverName");
            licenseGrade = bundle.getString("licenseGrade");
            licenseNumber = bundle.getString("licenseNumber");
            offenseID = bundle.getString("offenseID");
            receiptReference = bundle.getString("receiptReference");
            region = bundle.getString("region");
        }


        tvRegion.setText(region);
        tvListenceGrade.setText(licenseGrade);
        tvCharges.setText(amount);
        tvDriverName.setText(driverName);
        tvLicenseNumber.setText(licenseNumber);
        tvReceiptReference.setText(receiptReference);

        Button confirmButton = (Button) dialogLayout.findViewById(R.id.confirmButton);
        Button cancelButton = (Button) dialogLayout.findViewById(R.id.cancelButton);

        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        confirmationDialog.setView(dialogLayout);
        confirmationDialog.setCancelable(false);
        confirmationDialog.setCanceledOnTouchOutside(false);

        return confirmationDialog;
    }

    @Override
    public void onClick(View v) {

        final int viewId = v.getId();

        switch (viewId) {
            case R.id.confirmButton:
                confirmationDialog.dismiss();
                processPaymentListener.processPayment();

                break;

            case R.id.cancelButton:
                confirmationDialog.dismiss();
                break;
        }
    }
}
