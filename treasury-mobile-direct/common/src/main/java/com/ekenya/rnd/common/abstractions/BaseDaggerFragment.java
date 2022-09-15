package com.ekenya.rnd.common.abstractions;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCommon;
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks;
import com.ekenya.rnd.common.dialogs.dialog_progress.ProgressDialogCommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dagger.android.support.DaggerFragment;

public abstract class BaseDaggerFragment extends DaggerFragment {

    AlertDialog progressAlertDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void showHideProgress(String message){
        // init dialog if empty
        if (progressAlertDialog == null){
            ProgressDialogCommon progressDialog = new ProgressDialogCommon(getContext());
            progressDialog.setloadingMessage(message);
            progressAlertDialog = progressDialog.create();
        }

        if (!progressAlertDialog.isShowing()){
            progressAlertDialog.show();
        }else {
            progressAlertDialog.dismiss();
        }
    }

}
